package com.zhidian.dsp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdUtil {
	private static Pattern p =  Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
	public static String getNameToMd5(String url){
		return MD5Util.encode(getHost(url));
	}
	
	public static String getHost(String url){
		if(url==null||url.trim().equals("")){
			return "";
		}
		String host = "";
		Matcher matcher = p.matcher(url);  
		
		if(matcher.find()){
		   host = matcher.group();  
		}
		return host;
	}
}
