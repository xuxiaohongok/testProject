package com.zhidian3g.dsp.util;

import java.util.Set;

import redis.clients.jedis.Jedis;

import com.zhidian3g.common.constant.RedisConstant;
import com.zhidian3g.dsp.ad.AdMessage;
import com.zhidian3g.dsp.redisClient.JedisPools;

public class AdControlUtil {
	
	/**
	 * 广告频次设置
	 */
	public static void setAdControlTimes(Set<String> adIdSet) {
		LoggerUtil.addTimeLog("=============广告频次设置setAdControlTimes调用开始==============" + adIdSet);
		
		if(adIdSet == null || adIdSet.size() == 0) {
			LoggerUtil.addTimeLog("=============广告频次设置调用 传过来的参数值为空==============");
			return;
		}
		
		JedisPools jedisPools = JedisPools.getInstance();
		Jedis jedis = jedisPools.getJedis();
		//获取当前的广告id
		for(String adId : adIdSet) {
			//获取每个广告当前的日预算
			try {
				//日预算key
				AdMessage adMessage = JsonUtil.fromJson(jedis.get(RedisConstant.AD_BASE + adId), AdMessage.class);
				
				String adIdBugetKey = RedisConstant.AD_DAYBUDGET + adId;
				Long adDayBuget = Long.valueOf(jedis.get(adIdBugetKey));
				Long price = adMessage.getAdPrice();
				int subTimes = getSubTimes();
				Long count = (adDayBuget*1000)/(price*subTimes);//千次展示
				//广告频次次数key
				jedis.hset(RedisConstant.AD_ID_CONTROL_COUNT, adId, count + "");
			} catch (Exception e) {
				jedisPools.exceptionBroken(jedis);
				LoggerUtil.addExceptionLog(e);
			}
		}
		
		jedis.del(RedisConstant.AD_STOP_IDS);
		jedisPools.closeJedis(jedis);
		LoggerUtil.addTimeLog("==============广告频次设置setAdControlTimes调用结束================");
	}

	private static int getSubTimes() {
		int subTime = (24 - DateUtil.getHour()) * 2;
		int mimute = DateUtil.getMinute();
		if(mimute >= 45) {
			subTime--;
		}
		return subTime;
	}
}
