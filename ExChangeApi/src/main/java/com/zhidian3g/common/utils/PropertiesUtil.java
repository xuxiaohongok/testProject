package com.zhidian3g.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

 
/**
 *配置文件属性管理类
 * @author 
 *
 */
public class PropertiesUtil {
	
	private static Object lock = new Object();
	private static PropertiesUtil config = null;
	private static ResourceBundle rb = null;
	private static final String CONFIG_FILE = "application";
	
	private PropertiesUtil() {
		rb = ResourceBundle.getBundle(CONFIG_FILE);
	}
	
	public static PropertiesUtil getInstance() {
		synchronized(lock) {
			if(null == config) {
				config = new PropertiesUtil();
			}
		}
		return (config);
	}
	
	public String getValue(String key) {
		String value = null;
		try {
			value = rb.getString(key);
			value=new String(value.getBytes("ISO-8859-1"),"utf-8");
		} catch (MissingResourceException e) {
			LoggerUtil.addExceptionLog(e);
		} catch (UnsupportedEncodingException e) {
			LoggerUtil.addExceptionLog(e);
		} 
		return value;
	}
	
	public static void main(String[] args) {
		System.out.println(PropertiesUtil.getInstance().getValue("solr.zkhost"));
	}
}
