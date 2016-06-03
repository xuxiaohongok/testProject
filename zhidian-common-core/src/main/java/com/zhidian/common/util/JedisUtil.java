package com.zhidian.common.util;


import redis.clients.jedis.Jedis;
import com.zhidian.common.redisClient.JedisPools;

public class JedisUtil {
	/**
	 *通过相应的key和class类型获取redis的类对象 
	 * @param jedis
	 * @param key
	 * @param type
	 * @return
	 */
	private static JedisPools JEDIS_POOLS = JedisPools.getInstance();
	
	public static <T> T getClassObjectByJedis(Jedis jedis, String key, Class<T> type) {
		T t = null;
		try {
			if(jedis.exists(key)) {
				String appJsonData = jedis.get(key);
				t = JsonUtil.fromJson(appJsonData, type);
			}
		} catch (Exception e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			CommonLoggerUtil.addExceptionLog(e);
		}
		return t;
	}
	
	/**
	 * 往redis放进通过json对象转换成json字符串
	 * @param jedis
	 * @param key
	 * @param object
	 */
	public static void setObjectToRedis(Jedis jedis, String key, Object object) {
		try {
			jedis.set(key, JsonUtil.toJson(object));
		} catch (Exception e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			CommonLoggerUtil.addExceptionLog(e);
		}
	}
	
	public static void set(Jedis jedis, String key, String value) {
        try {
			jedis.set(key, value);
		} catch (Exception e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			CommonLoggerUtil.addExceptionLog(e);
		}
    }
    
    public static String get(Jedis jedis, String key) {
        String value = null;
		try {
			value = jedis.get(key);
		} catch (Exception e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			CommonLoggerUtil.addExceptionLog(e);
		}
		
        return value;
    }
    
	public static boolean exists(Jedis jedis, String key) {
		boolean flag = false;
		try {
			flag = jedis.exists(key);
		} catch (Exception e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			CommonLoggerUtil.addExceptionLog(e);
		}
		return flag;
	}
	
}
