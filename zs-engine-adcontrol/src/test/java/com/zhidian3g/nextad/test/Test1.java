package com.zhidian3g.nextad.test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;

import com.google.common.base.Joiner;
import com.zhidian.common.contans.RedisConstant;
import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.dsp.solr.service.SolrDMIAdService;
import com.zhidian.dsp.solr.vo.AdBaseDocumentSourceMessage;


public class Test1 {
	
//	@Test
//	public void testSolrDMISerive() {
//		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/application-context.xml");
//		SolrDMIAdService solrDMIAdService = (SolrDMIAdService)context.getBean("solrDMIAdService");
//		AdBaseDocumentSourceMessage adDocumentBaseMessage  = new AdBaseDocumentSourceMessage();
//		adDocumentBaseMessage.setAdCategory(1);
//		solrDMIAdService.addAdToSolr(adDocumentBaseMessage);
//	}
	
	@Test
	public void testOK() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("key", null);
		String areasStrings = Joiner.on(",").skipNulls().join(map.values()).replace("|", ",");
	}
	
	JedisPools jedisPools = JedisPools.getInstance();
	@Test
	public void testBaseRedis() {
		Jedis jedis = jedisPools.getJedis();
		jedis.sadd(RedisConstant.AD_STOP_IDS, "7");
		jedisPools.closeJedis(jedis);
	}
	
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("key1", "");
		map.put("key2", " ");
		map.put("key", null);
		String areasStrings = Joiner.on(",").skipNulls().join(map.values()).replace("|", ",");
		System.out.println(areasStrings);
	}
	
}
