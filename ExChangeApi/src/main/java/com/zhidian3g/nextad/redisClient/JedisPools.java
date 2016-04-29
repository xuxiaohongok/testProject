package com.zhidian3g.nextad.redisClient;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.zhidian3g.common.utils.LoggerUtil;

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
        		jedisPool = new JedisPool(config, JedisComMessage.JEDIS_IP, JedisComMessage.JEDIS_PORT, 
        				JedisComMessage.JEDIS_CONNECTION_TIMEOUT);
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
			LoggerUtil.addExceptionLog(e);
			exceptionBroken(jedis);
			closeJedis(jedis);
			reConnectionTime++;
			LoggerUtil.addExceptionLog("获取Jedis连接失败,开始尝试重置连接池!:" + reConnectionTime);
			if (reConnectionTime % 30 == 0) {
//				MailService.send(getLocalIP() + " 媒体对接 redis连接池连接出现问题", "尝试重置连接池3次了,邮件通知! <br>" + LoggerUtil.getExceptionString(e));
				// 产生异常,连接池重置.
				jedisPoolReset();
				return null;
			} 
		}
		return jedis;
	}
	
	/**
	 * 
	 * 获取对应数据库的jedis连接
	 * @param dbNum redis数据库编号 
	 * @return
	 */

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
}
