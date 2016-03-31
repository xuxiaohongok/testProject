package com.zhidian3g.dsp.util;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;

import com.zhidian3g.dsp.redisClient.JedisPools;

public class CXJedisUtil {
	private static JedisPools JEDIS_POOLS = JedisPools.getInstance();
	private static final String REDIS_AD = "ad_";
	private static final String REDIS_AD_BANNER = "ad_banner_";
	private static final String REDIS_AD_SCREEN = "ad_screen_";
	private static final String REDIS_AD_PUSH = "ad_push_";
	private static final String REDIS_AD_DEVELOPER = "ad_developer_";
	
	/**
	 * 根据广告id删除对应的广告
	 * @param adId
	 */
	public static boolean deleteAdOrder(String orderKey, String adId) {
		Jedis adJedis = null;
		boolean isDel = false;
		try{
			adJedis = JEDIS_POOLS.getJedis();
			if(adJedis == null) return false;
			adJedis.zrem(orderKey, adId);
			isDel = true;
			LoggerUtil.addBaseLog("广告：[" + adId + "] 已删除 从排序中" + orderKey);
		} catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(adJedis);
			LoggerUtil.addExceptionLog(e);
		} catch (Exception e) {
			LoggerUtil.addExceptionLog(e);
		} finally {
			JEDIS_POOLS.closeJedis(adJedis);
		}
		return isDel;
	}

	/**
	 * 批量删除缓存中的广告
	 * @param adId
	 */
	public static boolean deleteAdIdListFromRedis(List<Long> adIdList) {
		Jedis adJedis = null;
		boolean isDel = true;
		try{
			adJedis = JEDIS_POOLS.getJedis();
			if(adJedis == null) return false;
			
			Set<String> delAllAdIdSet = new LinkedHashSet<String>(); 
			for(Long adId : adIdList) {
				Set<String> delKeySet = getResinDelKeySet(adId);
				delAllAdIdSet.addAll(delKeySet);
			}
			
			long count = adJedis.del(delAllAdIdSet.toArray(new String[delAllAdIdSet.size()])); 
			System.out.println("delMappingCount=" + count);
			JEDIS_POOLS.closeJedis(adJedis);
			System.out.println("总共删除：" +  delAllAdIdSet + "条广告");
			System.out.println("批量删除广告：[" + adIdList + "] 已删除");
		} catch(JedisException e) {
			e.printStackTrace();
			isDel =false;
		} catch(Exception e) {
			e.printStackTrace();
			isDel =false;
		} finally {
			JEDIS_POOLS.closeJedis(adJedis);
		}
		
		return isDel;
	}
	
	/**
	 * 
	 * 通过广告id,获取广告各种对应的要删除广告类别,收集起来批量删除
	 * @param adId
	 * @param adJedis
	 * @return
	 */
	private static Set<String> getResinDelKeySet(Long adId) {
		String redis_ad_id = REDIS_AD + adId;
		String redis_ad_banner = REDIS_AD_BANNER + adId;
		String redis_ad_screen = REDIS_AD_SCREEN + adId;
		String redis_ad_push = REDIS_AD_PUSH + adId;
		String redis_ad_developer = REDIS_AD_DEVELOPER + adId + "_" + DateUtil.getDate();
		
		Set<String> delKeySet = new LinkedHashSet<String>();
		delKeySet.add(redis_ad_id);
		delKeySet.add(redis_ad_banner);
		delKeySet.add(redis_ad_push);
		delKeySet.add(redis_ad_screen);
		delKeySet.add(redis_ad_developer);
		
		System.out.println("adId=" + adId + ";delKeySet=" + delKeySet);
		return delKeySet;
	}
	
	
	/**
	 *通过相应的key和class类型获取redis的类对象 
	 * @param jedis
	 * @param key
	 * @param type
	 * @return
	 */
	public static <T> T getClassObjectByJedis(Jedis jedis, String key, Class<T> type) {
		T t = null;
		try {
			if(jedis.exists(key)) {
				String appJsonData = jedis.get(key);
				t = JsonUtil.fromJson(appJsonData, type);
			}
		} catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
		} catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void hset(Jedis jedis, String key, String field, String value) {
        try {
			jedis.hset(key, field, value);
		} catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
    public static String hget(Jedis jedis, String key, String field) {
        String value = null;
		try {
			value = jedis.hget(key, field);
		} catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return value;
    }
    
    public static boolean hexists(Jedis jedis, String key, String field) {
		boolean flag = false;
		try {
			flag = jedis.hexists(key, field);
		} catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public static void set(Jedis jedis, String key, String value) {
        try {
			jedis.set(key, value);
		} catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 
	 * 通过管道
	 * @param pipeline
	 * @param key
	 * @param value
	 */
	public static void setByPipeline(Pipeline pipeline, String key, String value) {
		pipeline.set(key, value);
    }
    
    public static String get(Jedis jedis, String key) {
        String value = null;
		try {
			value = jedis.get(key);
		} catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return value;
    }
    
    public static void delByPipeline(Jedis jedis, String... keys) {
		try {
			Pipeline p = jedis.pipelined();
			for (String key : keys) {
				p.del(key);
			}
			p.sync();
		}  catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public static Long delByJedis(Jedis jedis, String... keys) {
		try {
			return jedis.del(keys);
		}  catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0l;
	}
    
    public static void incr(Jedis jedis, String key) {
        try {
			jedis.incr(key);
		} catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void hincr(Jedis jedis, String key, String field) {
        try {
			jedis.hincrBy(key, field, 1);
		} catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	public static boolean exists(Jedis jedis, String key) {
		boolean flag = false;
		try {
			flag = jedis.exists(key);
		} catch (JedisException e) {
			JEDIS_POOLS.exceptionBroken(jedis);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	 public static void setex(Jedis jedis, String key, int seconds, String value) {
	    	try {
				jedis.setex(key, seconds, value);
			} catch (JedisException e) {
				JEDIS_POOLS.exceptionBroken(jedis);
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    
		/**
		 * 往redis放进通过json对象转换成json字符串
		 * @param jedis
		 * @param key
		 * @param object
		 */
		public static void setexObjectToRedis(Jedis jedis, String key, int seconds, Object object) {
			try {
				setex(jedis, key, seconds, JsonUtil.toJson(object));
			} catch (JedisException e) {
				JEDIS_POOLS.exceptionBroken(jedis);
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static List<String> brpop(Jedis jedis, int seconds, String... key) {
			try {
				return jedis.brpop(seconds, key);
			} catch (JedisException e) {
				JEDIS_POOLS.exceptionBroken(jedis);
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		/**
		 * 获取正则表达式对应的redis key
		 * @param jedis
		 * @param deleteAdKey
		 * @return
		 */
		public static Set<String> keys(Jedis jedis, String key) {
			try {
				return jedis.keys(key);
			} catch (JedisException e) {
				JEDIS_POOLS.exceptionBroken(jedis);
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		
	
}
