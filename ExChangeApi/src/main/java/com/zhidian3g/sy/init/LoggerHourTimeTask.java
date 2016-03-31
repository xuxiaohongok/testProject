package com.zhidian3g.sy.init;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zhidian3g.common.constant.PropertyConstant;
import com.zhidian3g.common.utils.LoggerUtil;

/**
 * 
 * 每个小时自动生成空字符日志，方便统计用
 * @author Administrator
 *
 */
public class LoggerHourTimeTask implements Job{
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String[] logNames = PropertyConstant.EVERYHOUR_LOG_NAMES.split(",");
		for(String loggerName : logNames) {
			LoggerUtil.addLog(loggerName, "");
		}
	}
	
	
}
