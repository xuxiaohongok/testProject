package com.zhidian3g.dsp.redisClient;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.zhidian3g.dsp.mail.MailService;
import com.zhidian3g.dsp.util.LoggerUtil;
import com.zhidian3g.dsp.util.PropertiesUtil;

/**
 * Redis工具类,用于获取JedisPool.
 * 参考官网说明如下：
 */
public class JedisPools  {
	
	/**
	 * redis 客户端连接配置类
	 * @author Administrator
	 *
	 */
	private  final String SERVER_IP_ADDRESS = PropertiesUtil.getInstance().getValue("server.ip.address");
	private  final String JEDIS_MAX_ACTIVE = PropertiesUtil.getInstance().getValue("redis.pool.maxActive");
	private  final String JEDIS_MAX_IDLE = PropertiesUtil.getInstance().getValue("redis.pool.maxIdle");
	private  final String JEDIS_MAX_WAIT = PropertiesUtil.getInstance().getValue("redis.pool.maxWait");
	private  final String JEDIS_TEST_ON_BORROW = PropertiesUtil.getInstance().getValue("redis.pool.testOnBorrow");
	private  final String JEDIS_TEST_ON_RETURN = PropertiesUtil.getInstance().getValue("redis.pool.testOnReturn");
	private  final String JEDIS_IP = PropertiesUtil.getInstance().getValue("redis.ip");
	private  final String JEDIS_PORT = PropertiesUtil.getInstance().getValue("redis.port");
	private  final Integer JEDIS_CONNECTION_TIMEOUT = Integer.valueOf(PropertiesUtil.getInstance().getValue("redis.connectionTimeOut"));
	private  final String JEDIS_CLIENT_PASSWORD = PropertiesUtil.getInstance().getValue("redis.clientPassWord");
	
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
        		JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxActive(Integer.valueOf(JEDIS_MAX_ACTIVE));  
                config.setMaxIdle(Integer.valueOf(JEDIS_MAX_IDLE));  
                config.setMaxWait(Long.valueOf(JEDIS_MAX_WAIT)); 
                config.setTestOnBorrow(Boolean.valueOf(JEDIS_TEST_ON_BORROW));  
                config.setTestOnReturn(Boolean.valueOf(JEDIS_TEST_ON_RETURN));
                
                /**
                 *如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息
                 *请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒)
                 */
                
                if(StringUtils.isBlank(JEDIS_CLIENT_PASSWORD)) {
                	jedisPool = new JedisPool(config, JEDIS_IP, Integer.valueOf(JEDIS_PORT), 
                    		JEDIS_CONNECTION_TIMEOUT);
                } else {
                	jedisPool = new JedisPool(config, JEDIS_IP, Integer.valueOf(JEDIS_PORT), 
                			JEDIS_CONNECTION_TIMEOUT, JEDIS_CLIENT_PASSWORD);
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
	
	
	/***连接失败的次数***************/
	private int reConnectionTime = 0;
	/**
	 * 
	 * 获取对应数据库的jedis连接
	 * @param dbNum redis数据库编号
	 * @return
	 */
	public Jedis getJedis() {
		Jedis jedis = null;
		// 捕捉异常;
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			LoggerUtil.addExceptionLog(e);
			reConnectionTime++;
			exceptionBroken(jedis);
			closeJedis(jedis);
			System.out.println("获取cx Jedis连接失败,开始尝试重置连接池!:" + reConnectionTime);
			if (reConnectionTime % 10 == 0) {
				MailService.send(SERVER_IP_ADDRESS + " DSPAdControlServer广告计费系统redis连接池连接出现问题", "尝试重置连接池" + reConnectionTime + "次了,邮件通知!");
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
	
}
