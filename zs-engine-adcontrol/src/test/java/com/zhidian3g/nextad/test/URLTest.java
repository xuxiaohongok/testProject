package com.zhidian3g.nextad.test;

import java.util.TreeMap;
import org.junit.Test;
import com.zhidian.common.util.JsonUtil;
import com.zhidian.common.util.MD5Util;

public class URLTest {
	
	private StringBuffer BASE_URL = new StringBuffer("http://adcontrol.zhidian3g.cn/AdTempOperation?");
	
	@Test
	public void testGetAddAdURL() {
		TreeMap<String, Object> parmentMap = new TreeMap<String, Object>();
		parmentMap.put("dt", System.currentTimeMillis()+"");
		parmentMap.put("operationCode", "1");
		parmentMap.put("adId", "1");
		String dsp_adcontrol_url = setBaseURL(parmentMap);
		System.out.println(dsp_adcontrol_url);
//		dsp_adcontrol_url = setBaseURL(1) + ;
	}
	
	@Test
	public void testAddAccountAdURL() {
		TreeMap<String, Object> parmentMap = new TreeMap<String, Object>();
		parmentMap.put("dt", System.currentTimeMillis()+"");
		parmentMap.put("operationCode", "3");
		parmentMap.put("accountId", "3");
		parmentMap.put("fee", "10");
		
		String dsp_adcontrol_url = setBaseURL(parmentMap);
		System.out.println(dsp_adcontrol_url);
//		dsp_adcontrol_url = setBaseURL(1) + ;
	}
	
	@Test
	public void testDelAdAdURL() {
		TreeMap<String, Object> parmentMap = new TreeMap<String, Object>();
		parmentMap.put("dt", System.currentTimeMillis()+"");
		parmentMap.put("operationCode", "1");
		parmentMap.put("adId", "1");
		String dsp_adcontrol_url = setBaseURL(parmentMap);
		System.out.println(dsp_adcontrol_url);
//		dsp_adcontrol_url = setBaseURL(1) + ;
	}
	
	@Test
	public void testRefreshAdAdURL() {
		TreeMap<String, Object> parmentMap = new TreeMap<String, Object>();
		parmentMap.put("dt", System.currentTimeMillis()+"");
		parmentMap.put("operationCode", "-1");
		String dsp_adcontrol_url = setBaseURL(parmentMap);
		System.out.println(dsp_adcontrol_url);
//		dsp_adcontrol_url = setBaseURL(1) + ;
	}
	
	@Test
	public void testStringEqual() {
		System.out.println("82ead98fda665cf15d80d1c0b048e67d".equals("82ead98fda665cf15d80d1c0b048e67d"));
	}
	
	StringBuffer sign = new StringBuffer();
	private String setBaseURL(TreeMap<String, Object> parmentMap) {
		for(String parment : parmentMap.keySet()) {
			BASE_URL.append(parment + "=" + parmentMap.get(parment) + "&");
		}
		
		System.out.println(JsonUtil.toJson(parmentMap));
		String sn = MD5Util.encrypt(JsonUtil.toJson(parmentMap));
		System.out.println(sn);
		BASE_URL.append("sn=" + sn);
		return  BASE_URL.toString();
	}
}
