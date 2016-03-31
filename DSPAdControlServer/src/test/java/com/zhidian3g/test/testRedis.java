//package com.zhidian3g.test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Test;
//
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.Pipeline;
//import redis.clients.jedis.exceptions.JedisException;
//
//import com.zhidian3g.dsp.redisClient.JedisPools;
//import com.zhidian3g.nextad.util.CXJedisUtil;
//import com.zhidian3g.nextad.util.DateUtil;
//import com.zhidian3g.nextad.util.JedisUtil;
//import com.zhidian3g.nextad.util.LoggerUtil;
//import com.zhidian3g.nextad.util.PropertiesConstant;
//
//public class testRedis {
//	JedisPools jedisPools = JedisPools.getInstance();
//	
//	
//	@Test
//	public void testGetOk() {
//		int ok = get();
//		System.out.println("ok=" + ok);
//	}
//	
//	public int get() {
//		for(int i=0; i<10; i++) {
//			if(i == 6) return i;
//			System.out.println(i);
//		}
//		return 10;
//	}
//	
//	@Test
//	public void testDel() {
//		JedisPools jedisPools = JedisPools.getInstance();
//		Jedis appJedis = jedisPools.getAppJedis();
//		String pid = "84ACD11194F1CE6AECB681B3848DD812";
//		try {
//			/** * 推送应用信息更新*/
//			String appPushReidsKey = PropertiesConstant.REDIS_PUSH_APP_DETAILMESSAGE__ID + pid;
//			appJedis.del(appPushReidsKey);
//			
//			/**
//			 * 积分墙应用信息更新
//			 */
//			String appIntegralWallReidsKey = PropertiesConstant.REDIS_APP_INTEGRALWALL_ID + pid;
//			String appIntegralWallDetailReidsKey = PropertiesConstant.REDIS_INTEGRALWALL_APP_DETAILMESSAGE__ID + pid;
//			appJedis.del(appIntegralWallReidsKey, appIntegralWallDetailReidsKey);
//			jedisPools.closeJedis(appJedis);
//		} catch (Exception e) {
//		}
//	}
//	
//	@Test
//	public void testGet() {
//		JedisPools jedisPools = JedisPools.getInstance();
//		Jedis appJedis = jedisPools.getAppJedis();
//		String pid = "84ACD11194F1CE6AECB681B3848DD812";
//		try {
//			/** * 推送应用信息更新*/
//			String appPushReidsKey = PropertiesConstant.REDIS_PUSH_APP_DETAILMESSAGE__ID + pid;
//			appJedis.del(appPushReidsKey);
//			
//			/**
//			 * 积分墙应用信息更新
//			 */
//			String appIntegralWallDetailReidsKey = PropertiesConstant.REDIS_INTEGRALWALL_APP_DETAILMESSAGE__ID + pid;
//			System.out.println(appJedis.get(appIntegralWallDetailReidsKey));
//			jedisPools.closeJedis(appJedis);
//		} catch (Exception e) {
//			
//		}
//	
//	}
//	
//	@Test
//	public void testRedis() {
//		Jedis adJedis = JedisPools.getInstance().getAdJedis();
//		Pipeline pipeline = adJedis.pipelined();
//		String deleteAdKey = "ad_delete_" + 1 + "_";
//		Set<String> keys = JedisUtil.keys(adJedis, deleteAdKey + "*");  
//		if(keys != null && keys.size() != 0) {
//			long count = adJedis.del(keys.toArray(new String[keys.size()]));  
//			System.out.println("删除之前的删除的广告版本条数为=" + count);
//		}
//		
//		pipeline.set("stest", "sadgasdg");
//		pipeline.sync();
//		JedisPools.getInstance().closeJedis(adJedis);
//	}
//	
//	@Test
//	public void testStopAppRedis() {
//		Jedis stopJedis = JedisPools.getInstance().getStopJedis();
//		String stopAppAdKey = "stop_app";
//		stopJedis.sadd(stopAppAdKey, "A6654FD826C870663529880F91F45463");
//		JedisPools.getInstance().closeJedis(stopJedis);
//		System.out.println("stop ok");
//	}
//	
//	@Test
//	public void testIsExistApp() {
//		Jedis stopAccessJedis = jedisPools.getStopJedis();
//		String stopKey = "stop_app";
//		if(stopAccessJedis.sismember(stopKey, "A6654FD826C870663529880F91F45463")) {
//			LoggerUtil.addBaseLog("=====没用的app======");
//		}
//	}
//	
//	@Test
//	public void teseGetAll() {
//		Jedis pointsJedis = JedisPools.getInstance().getPointJedis();
//		String allUserTaskKey = PropertiesConstant.AD_USER_TASK + "*_com.tyj.onepiece";
//		System.out.println(pointsJedis.keys(allUserTaskKey));
//	}
//	
//	@Test
//	public void testAdRedisKey() {
//		String everyDayRefreshAdIndexRedisKey = PropertiesConstant.EVERY_DAY_REFRESH_AD_INDEX + DateUtil.getDate();
//		JedisPools jedisPools = JedisPools.getInstance();
//		Jedis adJedis = jedisPools.getAppJedis();
//		
//		System.out.println("adJedis=" + adJedis.keys("*"));
//		jedisPools.closeJedis(adJedis);
//	}
//	
//
//	
//	@Test
//	public void testDelAdIdKey() {
////		JedisUtil.deleteAdIdFromRedis(1000000L);
//		List<Long> adIdList = new ArrayList<Long>();
//		adIdList.add(1000017L);
//		adIdList.add(1000027L);
//		adIdList.add(1000030L);
//		JedisUtil.deleteAdIdListFromRedis(adIdList);
//		System.out.println("OK");
//	}
//	
////	@Test
////	public void testList() {
////		JedisPools jedisPools = JedisPools.getInstance();
////		Jedis jedis = jedisPools.getJedisDB8();
////		for(int i=0; i<100; i++) {
////			jedis.set(BillingConstant.BUDGET_PREFIX + i, "budget" + i);
////		}
////		jedisPools.closeJedis(jedis);
////	}
////	
////	@Test
////	public void testGetList() {
////		JedisPools jedisPools = JedisPools.getInstance();
////		Jedis jedis = jedisPools.getJedisDB8();
////		Set<String> setKeys = jedis.keys(BillingConstant.BUDGET_PREFIX + "*");
////		for(String key : setKeys) {
////			jedis.set(key, jedis.get(key) + "sagasdgasdgasdgasdgsdagsdasdgasdgasddgasdgasdgdadgsa");
////		}
////		jedisPools.closeJedis(jedis);
////	}
//}
