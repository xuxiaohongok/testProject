package com.zhidian3g.common.util;


public class Utils {
	
	/**
	 * 对象为空判断
	 * @param object
	 * @return
	 */
	public static boolean isNull(Object object){
		if(object==null){
			return true;
		}
		return false;
	}
	
	/**
	 * 对象不为空判断
	 * @param object
	 * @return
	 */
	public static boolean isNotNull(Object object){
		if(object!=null){
			return true;
		}
		return false;
	}
	
	/**
	 * 程序休眠多少秒
	 * @param time
	 */
	public static void sleepTime(long second) {
		try {
			Thread.sleep(second*1000);
		} catch (InterruptedException e) {
			CommonLoggerUtil.addExceptionLog(e);
		} 
	}
}
