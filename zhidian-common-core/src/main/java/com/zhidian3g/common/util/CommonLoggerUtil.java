package com.zhidian3g.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 日志通用类
 * @author Administrator
 *
 */
public class CommonLoggerUtil {
	/**基本信息****/
	private static Logger baseLog = LoggerFactory.getLogger("baseLog");
	/**错误日志***********/
	private static Logger exceptionLog = LoggerFactory.getLogger("exceptionLog");
	/**错误请求日志*****/
	private static Logger errRequestLog = LoggerFactory.getLogger("errRequestLog");
	
	/**
	 * 打印基本日志
	 */
	public static void addBaseLog(String message) {
		baseLog.info(message);
	}
	
	/**
	 * class打印日志
	 */
	public static void addLog(Class c, String message) {
		Logger logger = LoggerFactory.getLogger(c);
		logger.info(message);
	}
	
	/**
	 * 保存异常日志
	 */
	public static void addExceptionLog(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		e.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		exceptionLog.info(buffer.toString());
	}
	
	/**
	 * 保存异常日志
	 */
	public static String getExceptionString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}


	public static void addErrQuest(String errRequestLogMessage) {
		errRequestLog.info(errRequestLogMessage);
	}
	
}
