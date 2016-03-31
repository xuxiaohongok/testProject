package com.zhidian3g.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhidian3g.common.log.RequestLoggerHander;
import com.zhidian3g.common.log.ShowLoggerHander;
import com.zhidian3g.common.log.WinLoggerHander;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;


public class LoggerUtil {
	
	private static final String ERRREQUESTLOG = "errRequestLog";
	private static final String EXCEPTIONLOG = "exceptionLog";
	
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
	 * 打印基本日志
	 */
	public static void addBaseLog(String message) {
		Logger logger = LoggerFactory.getLogger("baseLog");
		logger.info(message);
	}
	
	/**
	 * 保存日志
	 */
	public static void addLog(Class c, String message) {
		Logger logger = LoggerFactory.getLogger(c);
		logger.info(message);
	}
	
	/**
	 * 保存日志
	 */
	public static void addLog(String loggerName, String message) {
		Logger logger = LoggerFactory.getLogger(loggerName);
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
		Logger logger = LoggerFactory.getLogger(EXCEPTIONLOG);
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


	public static void addErrQuest(String errRequestLog) {
		Logger logger = LoggerFactory.getLogger(ERRREQUESTLOG);
		logger.info(errRequestLog);
	}
	
	public static void addPcLog(Object logMessage) {
		Logger logger = LoggerFactory.getLogger("pcLog");
		logger.info(JsonUtil.bean2Json(logMessage));
	}
	
	/**
	 * 
	 * @param logName 对应的日志名称
	 * @param logMessage 日志信息
	 */
	private static ActorSystem system = ActorSystem.create("CyLog");
	private static ActorRef requestLoggerHander = system.actorOf(Props.create(RequestLoggerHander.class), "requestLoggerHander");
	private static ActorRef winLoggerHander = system.actorOf(Props.create(WinLoggerHander.class), "winLoggerHander");
	private static ActorRef showLoggerHander = system.actorOf(Props.create(ShowLoggerHander.class), "showLoggerHander");
	public static void addRequestLogegerMessage(Object logMessage) {
		requestLoggerHander.tell(logMessage, null);
	}
	
	public static void addWinLogegerMessage(Object logMessage) {
		winLoggerHander.tell(logMessage, null);
	}
	
	public static void addShowLogegerMessage(Object logMessage) {
		showLoggerHander.tell(logMessage, null);
	}
	
}
