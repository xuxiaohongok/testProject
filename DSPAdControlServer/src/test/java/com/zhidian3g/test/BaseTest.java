package com.zhidian3g.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Tuple;

import com.zhidian3g.common.constant.RedisConstant;
import com.zhidian3g.dsp.redisClient.JedisPools;
import com.zhidian3g.dsp.util.AdControlUtil;
import com.zhidian3g.dsp.util.DateUtil;
import com.zhidian3g.dsp.util.LoggerUtil;

public class BaseTest {
	
	@Test
	public void testCollection() {
		List<String> testList = new ArrayList<String>();
		for(String test : testList) {
			System.out.println(test);
		}
		System.out.println("==================");
	}
	
	@Test
	public void testNum() {
		int hour = DateUtil.getHour();
		for(int i=0; i<24;i++) {
			hour = i;
			if(hour<=1 || hour>=10) {
				System.out.println(hour + "符合投放时间");
			} else {
				System.out.println(hour + "不符合投放时间");
			}
		}
	}
	
	@Test
	public void testAdControlUtil() {
		JedisPools jedisPools = JedisPools.getInstance();
		Jedis jedis = jedisPools.getJedis();
		Pipeline pipeline = jedis.pipelined();
		Response<Set<String>> adSortSetResponse = pipeline.zrevrange(RedisConstant.AD_ANDROID_IDS, 0, -1);
		Response<Set<String>> stopAdSetResponse = pipeline.smembers(RedisConstant.AD_STOP_IDS);
		pipeline.sync();
		
//		String valueString = null;
		Set<String> adSortSet = adSortSetResponse.get();
		System.out.println("==================" + adSortSet);
		Set<String> stopAdSet = stopAdSetResponse.get();
		adSortSet.removeAll(stopAdSet);
		System.out.println("=================1=" + adSortSet);
		System.out.println("当前广告有：[" + adSortSet + "] =需要停止投放的广告有=" + stopAdSet);
		System.out.println("过滤掉的广告剩下广告：" + adSortSet);
//		AdControlUtil.setAdControlTimes(adIdSet);
		
		
		Map<Double, String> scoreMap = new HashMap<Double, String>();
		scoreMap.put(800.0, "12");
		scoreMap.put(800.0, "13");
		jedis.zadd("test" ,scoreMap);
		
		
		jedisPools.closeJedis(jedis);
	}
	
	@Test
	public void testAdControlUtil12() {
		JedisPools jedisPools = JedisPools.getInstance();
		Jedis jedis = jedisPools.getJedis();
		if(jedis.zrank(RedisConstant.AD_ANDROID_IDS, "2") == null) {
			System.out.println("空值");
		} else {
			System.out.println("存在");
		}
//		boolean ok123 = jedis.sismember(RedisConstant.AD_IDS_KEY, "2");
		jedisPools.closeJedis(jedis);
	}
	
	
	@Test
	public void testSystem() {
		try {
			int i = ok();
			System.out.println("int =" + i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Integer ok() {
		if(true)System.exit(0);
		return null;
	}
	
	@Test
	public void subString() {
		String ok = "/uploads/addownloads/201504/com.tyj.onepiece_0918559136963.apk";
		System.out.println(ok.substring(ok.lastIndexOf("/") + 1));
	}
}
