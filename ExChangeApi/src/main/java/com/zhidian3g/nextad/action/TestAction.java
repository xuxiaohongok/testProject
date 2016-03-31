//package com.zhidian3g.nextad.action;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import com.zhidian3g.common.utils.DateUtil;
//import com.zhidian3g.common.utils.IPUtil;
//import com.zhidian3g.common.utils.IpUtil2;
//import com.zhidian3g.common.utils.JsonUtil;
//import com.zhidian3g.common.utils.LoggerUtil;
//import com.zhidian3g.nextad.vo.AdPostBackMessage;
//
///**
// * @author Administrator
// */
//@Controller
//public class TestAction {
//	
//	
//	/**
//	 * 
//	 * 竞价成功回调地址
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/cyWinCallBack.shtml", method=RequestMethod.GET)
//	public void cyWinCallBackURL(HttpServletRequest request, HttpServletResponse response, AdPostBackMessage adPostBackMessage) {
//		String ip = IPUtil.getIpAddr(request);
//		adPostBackMessage.setIp(ip);
//		System.out.println("===cyWinCallBack=====" + ip + ";ip2=" + IpUtil2.getIpAddr(request));
//		adPostBackMessage.setRequestTime(DateUtil.getDateTime());
//		
//		adPostBackMessage.setRequestAdDateTime(adPostBackMessage.getRequestAdDateTime().replace("_", " "));
//		LoggerUtil.addWinLogegerMessage(adPostBackMessage);
//		response.setStatus(200);
//	}
//	
//	/**
//	 * 
//	 * 竞价成功回调地址
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/adShow.shtml", method=RequestMethod.GET)
//	public void adShowURL(HttpServletRequest request, HttpServletResponse response, AdPostBackMessage adPostBackMessage) {
//		String ip = IPUtil.getIpAddr(request);
//		adPostBackMessage.setIp(ip);
//		String userId = adPostBackMessage.getUserId();
//		System.out.println("===adShow==" + ip + "=userId==" + userId + ";ip2=" + IpUtil2.getIpAddr(request));
//		adPostBackMessage.setRequestTime(DateUtil.getDateTime());
//		Double price = Double.valueOf(adPostBackMessage.getPrice());
//		String adId = adPostBackMessage.getAdId();
//		Integer adSlotType = adPostBackMessage.getAdSlotType();
//		
//		//说明已经达到控制的广告量
//		LoggerUtil.addShowLogegerMessage(adPostBackMessage);
//		adPostBackMessage.setRequestAdDateTime(adPostBackMessage.getRequestAdDateTime().replace("_", " "));
//		response.setStatus(200);
//	}
//	
//	/**
//	 * 
//	 * 竞价成功回调地址
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	private Logger cyClickLog = LoggerFactory.getLogger("cyClickLog");
//	@RequestMapping(value = "/adClick.shtml", method=RequestMethod.GET)
//	public void adClick(HttpServletRequest request, HttpServletResponse response, AdPostBackMessage adPostBackMessage) {
//		String ip = IPUtil.getIpAddr(request);
//		adPostBackMessage.setIp(ip);
//		System.out.println("===adClick=====" + ip + ";ip2=" + IpUtil2.getIpAddr(request));
//		adPostBackMessage.setRequestTime(DateUtil.getDateTime());
//		adPostBackMessage.setRequestAdDateTime(adPostBackMessage.getRequestAdDateTime().replace("_", " "));
//		cyClickLog.info(JsonUtil.bean2Json(adPostBackMessage));
//		response.setStatus(200);
//	}
//
//}
//
