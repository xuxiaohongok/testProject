package com.zhidian.ad.billing.utils;

import com.zhidian.ad.billing.constant.LoggerEnum;
import org.apache.log4j.*;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.exceptions.JedisException;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 日志文件处理类
 * @author Administrator
 *
 */
public class LoggerUtil {

	/**
	 * 
	 * @param logName 对应的日志名称
	 * @param logMessage 日志信息
	 */
	public static void addLogegerMessage(LoggerEnum logName, String logMessage) {
		org.slf4j.Logger logger = LoggerFactory.getLogger(logName.toString());
		if(!logName.equals(LoggerEnum.EXCEPTION_LOG)) {
			logger.info(logMessage);
		} else {
			logger.debug(logMessage);
		}
	}
	
	/**
	 * 
	 * @param logName 对应的日志名称
	 * @param logMessage 日志信息
	 */
	public static void addLogegerMessage(String logName, String logMessage) {
		org.slf4j.Logger logger = LoggerFactory.getLogger(logName);
		logger.info(logMessage);
	}
	
	/**
	 * 
	 * @param t 对应的类
	 * @param logMessage 日志信息
	 */
	public static void addLogegerMessage(Class t, String logMessage) {
		org.slf4j.Logger logger = LoggerFactory.getLogger(t);
		logger.info(logMessage);
	}
	
	

	
	public static void setExceptionLog(Exception e) {
		/**
		 * 日志保存
		 */
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		e.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, buffer.toString());
	}
	
	/**
	 * 系统异常字符串日志保存
	 */
	public static void saveExceptionLog(Exception e, String exceptionString) {
		String exceptionLog = getExceptionString(e);
		LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, exceptionLog + " \n " + exceptionString);
	}
	
	public static String getExceptionString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		e.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}

}
