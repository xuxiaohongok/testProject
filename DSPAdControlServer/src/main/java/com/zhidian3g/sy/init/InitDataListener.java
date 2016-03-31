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
import com.zhidian3g.dsp.service.ReadAdInfoXMLService;
import com.zhidian3g.dsp.task.DSPAdDelTask;
import com.zhidian3g.dsp.timerjob.DSPAdControlCountTimer;
import com.zhidian3g.dsp.timerjob.DSPEverydayTimer;
import com.zhidian3g.dsp.util.LoggerUtil;

public class InitDataListener implements InitializingBean, ServletContextAware {
	@Override
	public void afterPropertiesSet() throws Exception {

	}

	
	@Override
	public void setServletContext(ServletContext arg0) {
		LoggerUtil.addBaseLog("========系统初始化=======");
		
		//需要初始化的环境
		//开启广告删除队列监控服务
		LoggerUtil.addBaseLog("========初始化开启广告删除队列监控服务=======");
		new Thread(new DSPAdDelTask()).start();
		LoggerUtil.addBaseLog("========初始化广告=======");
		ReadAdInfoXMLService.getAllAdInfo(false);
		LoggerUtil.addBaseLog("========初始化广告结束=======");
		
		LoggerUtil.addBaseLog("========初始化广告每天广告刷新定时任务=======");
		dspEveryDayTimeTask();
		dspAdControlCountTimer();
		LoggerUtil.addBaseLog("========系统初始化结束=======");
	}
	
	/***nextAd每天定时任务*************************************/
	private void dspEveryDayTimeTask() {
		System.out.println("***dsp每天定时任务启动***************");
		try{
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail jobDetail = new JobDetail("simpleJob", Scheduler.DEFAULT_GROUP, DSPEverydayTimer.class);
			CronTrigger cTrigger = new CronTrigger("myTrigger",
				Scheduler.DEFAULT_GROUP, PropertyConstant.DSP_EVERYDAY_REFRESH_TIMER);
			scheduler.scheduleJob(jobDetail, cTrigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/***cxAd每天定时任务*************************/
	private void dspAdControlCountTimer() {
		System.out.println("***dsp 广告控制频率任务启动*************************");
		try{
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail jobDetail = new JobDetail("dspDdcontrolCountTimerJob", Scheduler.DEFAULT_GROUP, DSPAdControlCountTimer.class);
			CronTrigger cTrigger = new CronTrigger("dspDdcontrolCountTimer",
					Scheduler.DEFAULT_GROUP, PropertyConstant.DSP_ADCONTROL_COUNT_TIMER);
			scheduler.scheduleJob(jobDetail, cTrigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}