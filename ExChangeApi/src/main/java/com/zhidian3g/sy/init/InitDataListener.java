package com.zhidian3g.sy.init;

import java.text.ParseException;

import javax.servlet.ServletContext;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import com.zhidian3g.common.constant.PropertyConstant;
import com.zhidian3g.common.utils.LoggerUtil;

public class InitDataListener implements InitializingBean, ServletContextAware {
	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void setServletContext(ServletContext arg0) {
		LoggerUtil.addBaseLog("========系统初始化=======");
		
		//需要初始化的环境
		/***设置没个小时日志自动生成*************/ 
		LoggerUtil.addLog(InitDataListener.class, "***********************日志每小时定时任务启动***********************");
		// 启动每天清除日预算和
		// 建立JobDetail
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail jobDetail = new JobDetail("simpleJob", Scheduler.DEFAULT_GROUP, LoggerHourTimeTask.class);
			CronTrigger cTrigger = new CronTrigger("myTrigger", Scheduler.DEFAULT_GROUP, PropertyConstant.SERVER_LOGGER_INTERVAL_TIME);
			scheduler.scheduleJob(jobDetail, cTrigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		LoggerUtil.addLog(InitDataListener.class, "***********************日志每小时定时任务完成启动***********************");
		
		LoggerUtil.addBaseLog("========系统初始化结束=======");
	}
}