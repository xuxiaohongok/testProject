package com.zhidian3g.common.redisClient;

import redis.clients.jedis.JedisPoolConfig;

import com.zhidian3g.common.util.PropertiesUtil;

public class JedisComMessage {
	/**
	 * redis 客户端连接配置类
	 * @author Administrator
	 *
	 */
	public  final static String JEDIS_MAX_ACTIVE = PropertiesUtil.getInstance().getValue("redis.pool.maxActive");
	public  final static  String JEDIS_MAX_IDLE = PropertiesUtil.getInstance().getValue("redis.pool.maxIdle");
	public  final static  String JEDIS_MAX_WAIT = PropertiesUtil.getInstance().getValue("redis.pool.maxWait");
	public  final static  String JEDIS_TEST_ON_BORROW = PropertiesUtil.getInstance().getValue("redis.pool.testOnBorrow");
	public  final static  String JEDIS_TEST_ON_RETURN = PropertiesUtil.getInstance().getValue("redis.pool.testOnReturn");
	
	public  final static  String JEDIS_IP = PropertiesUtil.getInstance().getValue("redis.ip");
	public  final static  Integer JEDIS_PORT = Integer.valueOf(PropertiesUtil.getInstance().getValue("redis.port"));
	
	public  final static  String JEDIS_IP2 = PropertiesUtil.getInstance().getValue("redis2.ip");
	public  final static  Integer JEDIS_PORT2 = Integer.valueOf(PropertiesUtil.getInstance().getValue("redis2.port"));
	
	public  final static  Integer JEDIS_CONNECTION_TIMEOUT = Integer.valueOf(PropertiesUtil.getInstance().getValue("redis.connectionTimeOut"));
	public  final static String JEDIS_CLIENT_PASSWORD = PropertiesUtil.getInstance().getValue("redis.clientPassWord");
	
	public  final static JedisPoolConfig getJedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(Integer.valueOf(JEDIS_MAX_ACTIVE));  
		config.setMaxIdle(Integer.valueOf(JEDIS_MAX_IDLE));  
		config.setMaxWait(Long.valueOf(JEDIS_MAX_WAIT));  
		config.setTestOnBorrow(Boolean.valueOf(JEDIS_TEST_ON_BORROW));  
		config.setTestOnReturn(Boolean.valueOf(JEDIS_TEST_ON_RETURN));
		config.setWhenExhaustedAction((byte)1);
		return config;
	}
}
