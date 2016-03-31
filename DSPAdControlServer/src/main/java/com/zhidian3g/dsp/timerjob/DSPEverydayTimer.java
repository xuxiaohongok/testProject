package com.zhidian3g.dsp.timerjob;

import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import com.zhidian3g.dsp.service.ReadAdInfoXMLService;
import com.zhidian3g.dsp.util.DateUtil;
import com.zhidian3g.dsp.util.LoggerUtil;

public class DSPEverydayTimer implements Job{
	private int excuteCount = 0;
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("dsp广告控制器系统每天定时器启动======");
		ReadAdInfoXMLService.getAllAdInfo(true);
	    LoggerUtil.addBaseLog("凌晨"+ DateUtil.getDateTime() +"恢复日预算  执行次数为：" + excuteCount);
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
