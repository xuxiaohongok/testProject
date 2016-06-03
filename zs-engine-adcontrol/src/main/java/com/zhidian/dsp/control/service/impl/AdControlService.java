package com.zhidian.dsp.control.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import com.zhidian.ad.billing.result.BillingResult;
import com.zhidian.ad.billing.service.BillingQueryService;
import com.zhidian.ad.billing.utils.Md5SignUtils;
import com.zhidian.ad.vo.redis.RedisAdBaseMessage;
import com.zhidian.common.contans.RedisConstant;
import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.common.util.DateUtil;
import com.zhidian.common.util.JsonUtil;
import com.zhidian.dsp.util.AdControlLoggerUtil;

@Service
public class AdControlService {
	@Resource
	private BillingQueryService billQueryService;
	private JedisPools jedisPools = JedisPools.getInstance();
	
	/**
	 * 具体广告频次设置
	 */
	public void setAdControlTimes(Long adId) {
		CommonLoggerUtil.addBaseLog("=============广告投放设置投放速度==============" + adId);
		if(adId == null) {
			CommonLoggerUtil.addBaseLog("=============广告投放设置投放速度传过来的参数值为空==============");
			return;
		}
		
		Jedis jedis = jedisPools.getJedis();
		//获取缓存中广告信息
		RedisAdBaseMessage adMessage = JsonUtil.fromJson(jedis.get(RedisConstant.AD_BASE + adId), RedisAdBaseMessage.class);
		//获取广告当前的日预算
		Long accountId = adMessage.getAccountId();
		Long dateTime = System.currentTimeMillis();
		
		String billSign = Md5SignUtils.sign(dateTime, accountId, Long.valueOf(adId));
		//获取当前的日预算
		long nowAdBudget = 0l;
		try {
			BillingResult<Long>  billResult = billQueryService.getDayBudget(billSign, dateTime, accountId, Long.valueOf(adId));
			nowAdBudget = billResult.getValue();
			System.out.println("nowAdBudget=" + nowAdBudget);
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
		}
		//设置广告的投放次数
		setAdTimePutCount(jedis, adMessage, nowAdBudget);
		jedisPools.closeJedis(jedis);
		CommonLoggerUtil.addBaseLog("==============告投放设置投放速度调用结束================");
	}
	
	/**
	 * 广告频次设置
	 */
	public void setAdAllControlTimes(List<Long> adIdList) {
		AdControlLoggerUtil.addTimeLog("=============广告批量速度设置setAdAllControlTimes调用开始==============" + adIdList);
		if(adIdList == null || adIdList.size() == 0) {
			AdControlLoggerUtil.addTimeLog("=============广告频次设置调用 传过来的参数值为空==============");
			return;
		}
		
		Jedis jedis = jedisPools.getJedis();
		Long dateTime = System.currentTimeMillis();
		Set<Long> adIdSet = new HashSet<Long>(adIdList);
		String sign = Md5SignUtils.sign(dateTime, adIdSet);
		Map<Long, Long> adDayBudgetMap = billQueryService.queryDayBudget(sign, dateTime, adIdSet);
		
		//获取当前的广告id
		for(Long adId : adIdSet) {
			//获取缓存中广告信息
			RedisAdBaseMessage adMessage = JsonUtil.fromJson(jedis.get(RedisConstant.AD_BASE + adId), RedisAdBaseMessage.class);
			setAdTimePutCount(jedis, adMessage, adDayBudgetMap.get(adId));
		}
		
		jedisPools.closeJedis(jedis);
		AdControlLoggerUtil.addTimeLog("==============广告频次设置setAdControlTimes调用结束================");
	}

	/**
	 * 
	 * 广告投放次数设置
	 * @param jedis
	 * @param adMessage
	 * @param nowAdBudget
	 */
	private void setAdTimePutCount(Jedis jedis, RedisAdBaseMessage adMessage, long nowAdBudget) {
		//获取每个广告当前的日预算
		try {
			int adxType = adMessage.getAdxType().intValue();
			String adId = adMessage.getId() + "";
			String priceString = jedis.hget(RedisConstant.ADX_AD_STANDARD_PRICE, adMessage.getAdxType() + "");
			Long price = 0l;
			if(priceString != null) {
				price = Long.valueOf(priceString);
			} else {
				AdControlLoggerUtil.addTimeLog("adx标准价格为空   adxType=======" + adxType);
				return;
			}
			
			int subTimes = getSubTimes();
			Long count = (nowAdBudget*1000)/(price*subTimes);//千次展示
			//广告频次次数key
			jedis.hset(RedisConstant.AD_ID_CONTROL_COUNT, adId, count + "");
			AdControlLoggerUtil.addTimeLog("adId=" + adId + "时间限制的次数=" + count + ";nowAdBudget=" + nowAdBudget);
		} catch (Exception e) {
			jedisPools.exceptionBroken(jedis);
			CommonLoggerUtil.addExceptionLog(e);
		}
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
