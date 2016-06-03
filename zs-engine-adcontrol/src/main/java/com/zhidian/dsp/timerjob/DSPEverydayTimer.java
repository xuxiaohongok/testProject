package com.zhidian.dsp.timerjob;

import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import redis.clients.jedis.Jedis;

import com.zhidian.common.contans.RedisConstant;
import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.common.util.DateUtil;
import com.zhidian.common.util.SpringContextUtil;
import com.zhidian.dsp.control.service.impl.AdHanderService;
import com.zhidian.dsp.util.AdControlLoggerUtil;

public class DSPEverydayTimer implements Job{
	
	private int excuteCount = 0;
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		AdControlLoggerUtil.addTimeLog("dsp广告控制器系统每天定时器启动======");
		Jedis jedis = JedisPools.getInstance().getJedis();
		AdHanderService adHanderService = (AdHanderService)SpringContextUtil.getBean("adHanderService");
		adHanderService.addAllAd(true);
		jedis.setex(RedisConstant.DAY_AD_HAS_REFRESH + DateUtil.getDate(), DateUtil.getDifferentTimeMillis(), "ok");
		AdControlLoggerUtil.addTimeLog("广告凌晨恢复日预算  执行次数为：" + excuteCount);
	    JedisPools.getInstance().closeJedis(jedis);
	}
	
	public static void main(String[] args) {
		System.out.println("時間任務初始化=========");
		Scheduler scheduler = null;
		// 启动每天清除日预算和
		// 建立JobDetail
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail jobDetail = new JobDetail("simpleJob", Scheduler.DEFAULT_GROUP, DSPEverydayTimer.class);
			CronTrigger cTrigger = new CronTrigger("myTrigger",
					Scheduler.DEFAULT_GROUP, "0 0,30 * * * ?");
			scheduler.scheduleJob(jobDetail, cTrigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
}
