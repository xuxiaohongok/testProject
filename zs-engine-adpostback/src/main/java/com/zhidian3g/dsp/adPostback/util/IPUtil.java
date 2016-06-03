package com.zhidian3g.dsp.adPostback.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {
	
	/**
	 * 得到真实IP，如果服务器使用的是反向代理
	 * 一般的得到IP方法，只能得到局域网的IP
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {  
		//请求头判断
		Enumeration enumeration = request.getHeaderNames();
		StringBuffer stringBuffer = new StringBuffer();
		while(enumeration.hasMoreElements()) {
			String name = enumeration.nextElement() + "";
			stringBuffer.append(name + ":" + request.getHeader(name) + ";");
		}
		
	    String ip = request.getHeader("x-forwarded-for");    
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
	        ip = request.getHeader("Proxy-Client-IP");    
	    }    
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
	        ip = request.getHeader("WL-Proxy-Client-IP");    
	    }    
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
	        ip = request.getRemoteAddr();    
	    }    
	    if(ip.contains(",")) {
	    	ip = ip.split(",")[0].trim();
	    }
	    return ip;    
	} 
}
