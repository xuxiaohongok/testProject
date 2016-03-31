//package com.zhidian3g.test;
//
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.zhidian3g.cx.vo.CXAddAdMessage;
//import com.zhidian3g.cx.webservice.admatching.CXAdMatchingDMIService;
//import com.zhidian3g.cx.webservice.billing.CXAdControlServerBillingService;
//import com.zhidian3g.nextad.util.JsonUtil;
//import com.zhidian3g.nextad.util.MD5Util;
//import com.zhidian3g.nextad.util.PropertiesUtil;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:/spring/spring.xml"})
//public class CXWebServiceTest extends AbstractJUnit4SpringContextTests {
//
//	@Resource
//	private CXAdControlServerBillingService cxAdControlServerBillingService;
//	
//	@Resource
//	private CXAdMatchingDMIService cxAdMatchingDMIService;
//	
//	
//	@Test
//	public void testSetWall() {
//		String statusValue = cxAdControlServerBillingService.addAdOrRefreshBudget(getMd5Key(1l), 1l, 0l);
//		System.out.println(statusValue);
//		Map<Integer, String> map = JsonUtil.fromJson(statusValue, Map.class);
//		System.out.println(map.get("0"));
//	}
//	
//	@Test
//	public void testAdMatchingDMIService() {
//		cxAdMatchingDMIService.addAd(new CXAddAdMessage(123456897l, "123456897", null, 
//											null, "广东省", 1, 4, "test.zhidian.test"));
//	}
///**
// * 加密解密内部使用的key
// */
//	private String ENCRYPT_KEY= PropertiesUtil.getInstance().getValue("server.encrypt.key");
//	private String getMd5Key(Long adId) {
//		String md5 = "";
//		md5 = MD5Util.encode(ENCRYPT_KEY + "_____"+adId );
//		return md5;
//	}
//	
//}
