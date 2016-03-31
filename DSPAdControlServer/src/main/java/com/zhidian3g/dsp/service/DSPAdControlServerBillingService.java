package com.zhidian3g.dsp.service;

import java.util.HashMap;
import java.util.Map;
import redis.clients.jedis.Jedis;
import com.zhidian3g.common.constant.RedisConstant;
import com.zhidian3g.dsp.redisClient.JedisPools;
import com.zhidian3g.dsp.util.DateUtil;
import com.zhidian3g.dsp.util.JsonUtil;
import com.zhidian3g.dsp.util.LoggerUtil;

public class DSPAdControlServerBillingService {

	/**
	 * 0 代表失败 1 代表成功
	 */
	private final static String AD_OPTION_FAIL_STATUS = "0"; 
	private final static String AD_OPTION_SCUESS_STATUS = "1";
	
	//需要考虑事务的情况
	public static String addAdOrRefreshBudget(String adId, Long dayBudget) {
		JedisPools cxJedisPools = JedisPools.getInstance();
		LoggerUtil.addBillingLog("===广告计算接口调用=======addAdBudget====adId===" + adId + ";dayBudget=" + dayBudget);
		Map<String, String> addStatus = new HashMap<String, String>();

		String date = DateUtil.getDate();
		Jedis jedis = cxJedisPools.getJedis();
		
		//保存所有广告初始化的key
		String allAdInitBudgetKey = RedisConstant.INIT_AD_DAYBUDGET;
		
		//先判断广告是否存在
		//广告的日预算key
		String adIdBugetKey = RedisConstant.AD_DAYBUDGET + adId;
		String adIdBugetStringValue = jedis.get(adIdBugetKey);
		
		//如果是空说明是第一次初始化
		if(adIdBugetStringValue == null) {
			jedis.setex(adIdBugetKey, DateUtil.getDifferentTimeMillis(),dayBudget + "");
			boolean isExists = jedis.exists(allAdInitBudgetKey);
			
			jedis.hset(allAdInitBudgetKey, adId, dayBudget + "");
			if(!isExists) {
				jedis.expire(allAdInitBudgetKey, DateUtil.getDifferentTimeMillis());
			}
			
			LoggerUtil.addBillingLog(date + "广告：" + adId + "第一次初始化" + ";当天的日预算为：" + dayBudget);
			addStatus.put(AD_OPTION_SCUESS_STATUS, "scuess");
			cxJedisPools.closeJedis(jedis);
			return JsonUtil.toJson(addStatus);
		} else {
			int adIdBugetValue = Integer.valueOf(adIdBugetStringValue);
			//判断日预算是否有更新的值
			int dayBedugetUpdateCount = 0;
			//获取今天广告的初始化值
			String initAdBudgetStringValue = jedis.hget(allAdInitBudgetKey, adId);
			
			if(initAdBudgetStringValue!=null) {
				int initAdBudgetValue = Integer.valueOf(initAdBudgetStringValue);
				if(dayBudget == initAdBudgetValue) {//如果相等说明当天的预算量没有改变
					LoggerUtil.addBillingLog(date + "广告：" + adId + " 更新 日预算没变还是为：" + dayBudget);
				} else {
					jedis.hset(allAdInitBudgetKey, adId, dayBudget + "");
					dayBedugetUpdateCount = (int)(dayBudget - initAdBudgetValue);
					LoggerUtil.addBillingLog(date + "广告：" + adId + "改变日预算从" + initAdBudgetValue + "改变为：" + dayBudget);
				}
			} else {
				LoggerUtil.addBillingLog("=====广告添加异常出现没有保存初始化广告日预算量 adId为：" + adId);
				addStatus.put(AD_OPTION_FAIL_STATUS, "广告添加异常出现没有保存初始化广告日预算量 adId为：" + adId);
				cxJedisPools.closeJedis(jedis);
				return JsonUtil.toJson(addStatus);
			}
			
			if(dayBedugetUpdateCount != 0) {//说明日预算变更了
				adIdBugetValue = adIdBugetValue + dayBedugetUpdateCount;
				if(adIdBugetValue <= 0) {
					LoggerUtil.addBillingLog(date + "广告 adId:：" + adId + "日预算量调整后广告当天的日预算已经用完了，超出的服务条数为：" + adIdBugetValue);
					jedis.setex(adIdBugetKey, DateUtil.getDifferentTimeMillis(), adIdBugetValue + ""); //现在日预算量是已经负数了
					addStatus.put(AD_OPTION_FAIL_STATUS,  date + "广告 adId:：" + adId + "日预算量调整后广告当天的日预算已经用完了，超出的服务条数为：" + adIdBugetValue);
					cxJedisPools.closeJedis(jedis);
					return JsonUtil.toJson(addStatus);
				} else {
					LoggerUtil.addBillingLog(date + "广告 adId:：" + adId + "日预算量调整后, 日预算还有：" + adIdBugetValue);
					jedis.setex(adIdBugetKey, DateUtil.getDifferentTimeMillis(), adIdBugetValue + "");
					addStatus.put(AD_OPTION_SCUESS_STATUS, "scuess");
					cxJedisPools.closeJedis(jedis);
					return JsonUtil.toJson(addStatus);
				}
			} else {
				//判断当天的预算量是否用光
				if(adIdBugetValue <= 0) {
					LoggerUtil.addBillingLog(date + "广告：" + adId + "日预算已经用关");
					addStatus.put(AD_OPTION_FAIL_STATUS, date + "广告：" + adId + "日预算已经用关");
					cxJedisPools.closeJedis(jedis);
					return JsonUtil.toJson(addStatus);
				} else {
					LoggerUtil.addBillingLog(date + "广告：" + adId + "开启");
					addStatus.put(AD_OPTION_SCUESS_STATUS, "scuess");
					cxJedisPools.closeJedis(jedis);
					return JsonUtil.toJson(addStatus);
				}
			}
		}
	}
}
