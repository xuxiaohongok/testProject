package com.zhidian3g.nextad.service;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import com.zhidian3g.common.constant.RedisConstant;
import com.zhidian3g.common.utils.DateUtil;
import com.zhidian3g.common.utils.LoggerUtil;
import com.zhidian3g.nextad.redisClient.JedisPools;

@Service(value="dspAdBillingService")
public class DSPAdBillingService {
	private JedisPools jedisPools = JedisPools.getInstance();
	
	public Integer rebate(String adId, Double price) {
		Integer statusCode = null;
		String adBudegetKey = RedisConstant.AD_DAYBUDGET + adId;
		Jedis jedis = null;
		try {
			jedis = jedisPools.getJedis();
			String date = DateUtil.getDateTime();
			if(jedis != null) {
				if(!jedis.exists(adBudegetKey)) {
					//不存在的广告
					statusCode = -1;
					LoggerUtil.addBillingLog("=====不存在广告===:" + adId);
				} else {
					//存在广告的日预算处理
					long adPrice = price.longValue()/1000;
					long count = jedis.decrBy(adBudegetKey, adPrice);
					LoggerUtil.addBillingLog(date + " 广告adId" + adId + "扣取费用:" + adPrice + ";剩下日预算为======="+ count);
					if(count < 0) {
						//日预算已经用关
						statusCode = 0;
						LoggerUtil.addBillingLog(date + " 广告adId" + adId + " 日预算已经用关======count="+ (count+1));
					} else if(count == 0) {
						statusCode = 2;
						LoggerUtil.addBillingLog(DateUtil.getDateTime() + "  广告adId" + adId + " 日预算刚好用关");
					} else {
						//成功扣取
						statusCode = 1;
					}
				}
			}
		} catch (Exception e) {
			LoggerUtil.addExceptionLog(e);
			statusCode = 5;
			jedisPools.exceptionBroken(jedis);
		} finally {
			jedisPools.closeJedis(jedis);
		}
		return statusCode;
	}
}
