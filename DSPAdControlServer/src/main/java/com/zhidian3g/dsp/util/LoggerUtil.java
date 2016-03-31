package com.zhidian3g.dsp.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志文件处理类
 * @author Administrator
 *
 */
public class LoggerUtil {
	
	public static void addErrQequestLog(String errRequestLogMessage) {
		Logger logger = LoggerFactory.getLogger("ERRREQUESTLOG");
		logger.debug(errRequestLogMessage);
	}
	
	public static void addBaseLog(String baseLog) {
		Logger logger = LoggerFactory.getLogger("BASELOG");
		logger.info(baseLog);
	}
	
	public static void addTimeLog(String baseLog) {
		Logger logger = LoggerFactory.getLogger("TIMELOG");
		logger.info(baseLog);
	}
	
	private static final Logger billingLog = LoggerFactory.getLogger("billingLog");
	/**
	 * 
	 * @param t 对应的类
	 * @param logMessage 账户日志信息
	 */
	public static void addBillingLog(String logMessage) {
		billingLog.info(logMessage);
	}
	

	/**
	 * 保存异常日志
	 */
	public static void addExceptionLog(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		e.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		Logger logger = LoggerFactory.getLogger("exceptionlog");
		logger.info(buffer.toString());
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



}
