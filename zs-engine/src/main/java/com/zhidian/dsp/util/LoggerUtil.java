package com.zhidian.dsp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhidian.common.util.JsonUtil;
import com.zhidian.dsp.log.AdOperationLogMessage;


public class LoggerUtil {
	
	private static final Logger requestAdLog = LoggerFactory.getLogger("requestAdLog");
	private static final Logger receiveAdLog = LoggerFactory.getLogger("receiveAdLog");
	private static final Logger adRequestMessageLog = LoggerFactory.getLogger("adRequestMessageLog");
	/**
	 * 
	 * @param logName 对应的日志名称
	 * @param logMessage 日志信息
	 */
	public static void addRequestLogMessage(Object logMessage) {
		requestAdLog.info(logMessage.toString());
	}
	
	public static void addAdReceiveLogMessage(AdOperationLogMessage logMessage) {
		receiveAdLog.info(JsonUtil.toJson(logMessage));
	}
	
	public static void addAdRequestMessageLog(AdOperationLogMessage logMessage) {
		adRequestMessageLog.info(JsonUtil.toJson(logMessage));
	}
	
	
}
