package com.zhidian3g.dsp.timerjob;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import com.zhidian3g.common.constant.RedisConstant;
import com.zhidian3g.dsp.redisClient.JedisPools;
import com.zhidian3g.dsp.util.AdControlUtil;
import com.zhidian3g.dsp.util.DateUtil;
import com.zhidian3g.dsp.util.LoggerUtil;

/**
 * 
 * 广告控制频次
 * @author Administrator
 *
 */
public class DSPAdControlCountTimer implements Job{
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		LoggerUtil.addTimeLog("======dsp定时调整广告频次======" + DateUtil.getDateTime());
		JedisPools jedisPools = JedisPools.getInstance();
		Jedis jedis = jedisPools.getJedis();
		
		Set<String> adIdSet = new HashSet<String>();
		Map<Double, String> scoreMap = new HashMap<Double, String>();
		Set<Tuple> delSetTuple = jedis.zrangeWithScores(RedisConstant.AD_DEL_KEY, 0, -1);
		
		for(Tuple tuple : delSetTuple) {
			adIdSet.add(tuple.getElement());
			scoreMap.put(tuple.getScore(), tuple.getElement());
		}
		
		LoggerUtil.addTimeLog("======delScoreMap======" + scoreMap);
		
		Set<Tuple> adTuple = jedis.zrangeWithScores(RedisConstant.AD_IDS_KEY, 0, -1);
		for(Tuple tuple : adTuple) {
			adIdSet.add(tuple.getElement());
			scoreMap.put(tuple.getScore(), tuple.getElement());
		}
		LoggerUtil.addTimeLog("======allScoreMap======" + scoreMap);
		
		AdControlUtil.setAdControlTimes(adIdSet);
		jedis.del(RedisConstant.AD_DEL_KEY);
		jedis.zadd(RedisConstant.AD_IDS_KEY, scoreMap);
		jedisPools.closeJedis(jedis);
		LoggerUtil.addTimeLog("======dsp定时调整广告频次======");
	}

}