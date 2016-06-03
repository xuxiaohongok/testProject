package com.zhidian.base.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.google.common.base.Joiner;
import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.dsp.constant.DspConstant;

public class BaseTest {
	
	JedisPools jedisPools = JedisPools.getInstance();
	
	@Test
	public void testOk() {
		int i=0;
		try {
			while(true) {
				System.out.println(i++);
				Jedis jedis = jedisPools.getJedis();
				jedis.exists("");
			}
		} catch (Exception e) {
			System.out.println("========************=====" + i);
			e.printStackTrace();
		}
	}

	@Test
	public void testSet() {
		Set<String> adIdSet = new HashSet<String>();
		adIdSet.add("1");
		adIdSet.add("2");
		adIdSet.add("3");
		String ok = "(" + Joiner.on(" OR ").join(adIdSet) + ")";
		System.out.println(ok);
		String[] testStringArray = {"4"};
		System.out.println(Joiner.on(" OR ").join(testStringArray));
	}
	
	@Test
    public void testSplit () {
        String str = "1,2";
        str = str.replaceAll(",", " OR " + DspConstant.AD_CATEGORY);
        System.out.println("str=" + str);
//        Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
//        List<String> stringList = splitter.splitToList(str);
//        for (String s : stringList) {
//            System.out.println(s + "============");
//        }
    }
}
