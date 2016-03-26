package com.zhidian3g.common.redisClient;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.zhidian3g.common.mail.MailService;
import com.zhidian3g.common.util.CommonLoggerUtil;

/**
 * Redis工具类,用于获取JedisPool.
 * 参考官网说明如下：
 */
public class JedisPools {
    private  JedisPool jedisPool = null;
	/**
	 * 私有构造器.
	 */
	private JedisPools() {
		initJedisPool();
	}
	
    /**
     * 获取连接池.
     * @return 连接池实例
     */
    private void initJedisPool() {
		try{  
        	if(jedisPool == null) {
        		JedisPoolConfig config = JedisComMessage.getJedisPoolConfig();
                
                /**
                 *如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息
                 *请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒)
                 */
                if(StringUtils.isBlank(JedisComMessage.JEDIS_CLIENT_PASSWORD)) {
                	jedisPool = new JedisPool(config, JedisComMessage.JEDIS_IP, JedisComMessage.JEDIS_PORT, 
                			JedisComMessage.JEDIS_CONNECTION_TIMEOUT);
                } else {
                	jedisPool = new JedisPool(config, JedisComMessage.JEDIS_IP, JedisComMessage.JEDIS_PORT, 
                			JedisComMessage.JEDIS_CONNECTION_TIMEOUT, JedisComMessage.JEDIS_CLIENT_PASSWORD);
                }
        	}
        	
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static class RedisUtilHolder{
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static JedisPools instance = new JedisPools();
    }

	public static JedisPools getInstance() {
		return RedisUtilHolder.instance;
	}
	
	/**
	 * 释放redis实例到连接池.
     * @param jedis redis实例
     */
	public void closeJedis(Jedis jedis) {
		if(jedisPool != null && jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 当出现异常时 销毁对象
	 * 
	 * @param jedis
	 */
	public void exceptionBroken(Jedis jedis) {
		if(jedisPool != null && jedis != null)	jedisPool.returnBrokenResource(jedis);
	}
	
	private int reConnectionTime = 0;
	public int getErrTime() {
		return reConnectionTime;
	}
	
	/**
	 * 
	 * 获取对应数据库的jedis连接
	 * @param dbNum redis数据库编号 
	 * @return
	 */
	public Jedis getJedis(){
		Jedis jedis = null;
		// 捕捉异常
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
			exceptionBroken(jedis);
			closeJedis(jedis);
			reConnectionTime++;
			System.out.println("获取Jedis连接失败,开始尝试重置连接池!:" + reConnectionTime);
			if (reConnectionTime % 10 == 0) {
				MailService.send(getLocalIP() + " Next_AdReceiveServer广告接收系统redis连接池连接出现问题", "尝试重置连接池3次了,邮件通知! <br>" + CommonLoggerUtil.getExceptionString(e));
				// 产生异常,连接池重置.
				jedisPoolReset();
				return null;
			} else {
				/**
				 * 继续尝试获取jedis连接
				 */
				jedis = getJedis();
				reConnectionTime = 0;
			}
		}
		return jedis;
	}
	
	/**
	 * jedis连接池重置
	 */
	private void jedisPoolReset() {
		System.out.println("============================重置redis连接池=================");
		try {
			if(jedisPool != null) {
				jedisPool.destroy();
				jedisPool = null;
			}
			
			//重置连接池
			this.initJedisPool();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getLocalIP() {
        String ip = "";
        try {
            Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e1.nextElement();
                if (!ni.getName().equals("eth0")) {
                    continue;
                } else {
                    Enumeration<?> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements()) {
                        InetAddress ia = (InetAddress) e2.nextElement();
                        if (ia instanceof Inet6Address)
                            continue;
                        ip = ia.getHostAddress();
                    }
                    break;
                }
            }
        } catch (SocketException e) {
        	e.printStackTrace();
        }
        return ip;
    }
}
