package com.zhidian3g.dsp.timerjob;

import java.util.Set;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import redis.clients.jedis.Jedis;
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
		Set<String> pcAdIdSet = jedis.zrevrange(RedisConstant.AD_PC_IDS, 0, -1);
		Set<String> androidAdIdSet = jedis.zrevrange(RedisConstant.AD_ANDROID_IDS, 0, -1);
		Set<String> iosAdIdSet = jedis.zrevrange(RedisConstant.AD_IOS_IDS, 0, -1);
		androidAdIdSet.addAll(pcAdIdSet);
		androidAdIdSet.addAll(iosAdIdSet);
		AdControlUtil.setAdControlTimes(androidAdIdSet);
		jedisPools.closeJedis(jedis);
		LoggerUtil.addTimeLog("======dsp定时调整广告频次======");
	}

}
