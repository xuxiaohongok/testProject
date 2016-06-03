package com.zhidian.dsp.timerjob;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import redis.clients.jedis.Jedis;

import com.zhidian.common.contans.RedisConstant;
import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.common.util.SpringContextUtil;
import com.zhidian.dsp.control.service.impl.AdControlService;
import com.zhidian.dsp.solr.service.SolrDMIAdService;
import com.zhidian.dsp.util.AdControlLoggerUtil;

/**
 * 
 * 广告控制频次
 * @author Administrator
 *
 */
public class DSPAdControlCountTimer implements Job{
	private AdControlService adControlService = (AdControlService)SpringContextUtil.getBean("adControlService");
	private SolrDMIAdService solrDMIAdService = (SolrDMIAdService)SpringContextUtil.getBean("solrDMIAdService");
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		AdControlLoggerUtil.addTimeLog("======dsp定时调整广告频次开始======");
		JedisPools jedisPools = JedisPools.getInstance();
		Jedis jedis = jedisPools.getJedis();
		
		//获取投放中的广告
		List<Long> adIdList = solrDMIAdService.getAllAdId();
		//设置投放中广告频次
		adControlService.setAdAllControlTimes(adIdList);
		//
		jedis.del(RedisConstant.AD_STOP_IDS);
		jedisPools.closeJedis(jedis);
		AdControlLoggerUtil.addTimeLog("======dsp定时调整广告频次结束======");
	}

}
