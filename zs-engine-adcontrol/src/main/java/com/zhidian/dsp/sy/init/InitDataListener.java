package com.zhidian.dsp.sy.init;

import java.text.ParseException;

import javax.servlet.ServletContext;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import redis.clients.jedis.Jedis;

import com.zhidian.common.contans.PropertyConstant;
import com.zhidian.common.contans.RedisConstant;
import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.common.util.DateUtil;
import com.zhidian.common.util.SpringContextUtil;
import com.zhidian.dsp.control.service.impl.AdHanderService;
import com.zhidian.dsp.control.task.DSPAdDelTask;
import com.zhidian.dsp.timerjob.DSPAdControlCountTimer;
import com.zhidian.dsp.timerjob.DSPEverydayTimer;
import com.zhidian.dsp.util.AreasUtil;

public class InitDataListener implements InitializingBean, ServletContextAware {
	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void setServletContext(ServletContext arg0) {
		AdHanderService adHanderService = (AdHanderService)SpringContextUtil.getBean("adHanderService");
		Jedis jedis = JedisPools.getInstance().getJedis();
		
		CommonLoggerUtil.addBaseLog("========系统初始化=======");
		jedis.hset(RedisConstant.ADX_AD_STANDARD_PRICE, 1 + "", "12000");
		jedis.hset(RedisConstant.ADX_AD_STANDARD_PRICE, 2 + "", "12000");
		jedis.hset(RedisConstant.ADX_AD_STANDARD_PRICE, 3 + "", "12000");
		jedis.hset(RedisConstant.ADX_AD_STANDARD_PRICE, 4 + "", "12000");
		
		try {
			Class.forName("com.zhidian.dsp.util.AreasUtil");
		} catch (ClassNotFoundException e) {
			CommonLoggerUtil.addExceptionLog(e);
		}
		
		//需要初始化的环境
		//开启广告删除队列监控服务
		CommonLoggerUtil.addBaseLog("========初始化开启广告删除队列监控服务=======");
		new Thread(new DSPAdDelTask()).start();
		CommonLoggerUtil.addBaseLog("========初始化广告=======");
		
		//判读今天凌晨是否已经刷新了广告
		boolean isHasEx = jedis.exists(RedisConstant.DAY_AD_HAS_REFRESH + DateUtil.getDate());
		if(isHasEx) {
			adHanderService.addAllAd(false);
		}  else {
			adHanderService.addAllAd(true);
			jedis.setex(RedisConstant.DAY_AD_HAS_REFRESH + DateUtil.getDate(), DateUtil.getDifferentTimeMillis(), "ok");
		}
		
		CommonLoggerUtil.addBaseLog("========初始化广告结束=======");
		
		CommonLoggerUtil.addBaseLog("========初始化广告每天广告刷新定时任务=======");
		dspEveryDayTimeTask();
		dspAdControlCountTimer();
		CommonLoggerUtil.addBaseLog("========系统初始化结束=======");
	}
	
	/***nextAd每天定时任务*************************************/
	private void dspEveryDayTimeTask() {
		System.out.println("***dsp每天定时任务启动***************");
		try{
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail jobDetail = new JobDetail("simpleJob", Scheduler.DEFAULT_GROUP, DSPEverydayTimer.class);
			CronTrigger cTrigger = new CronTrigger("myTrigger", Scheduler.DEFAULT_GROUP, PropertyConstant.DSP_EVERYDAY_REFRESH_TIMER);
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