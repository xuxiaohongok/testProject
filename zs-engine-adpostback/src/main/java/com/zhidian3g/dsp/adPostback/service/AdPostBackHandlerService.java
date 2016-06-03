package com.zhidian3g.dsp.adPostback.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.zhidian.ad.billing.constant.BillingType;
import com.zhidian.ad.billing.service.PostBackService;
import com.zhidian.ad.billing.utils.Md5SignUtils;
import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.common.util.DateUtil;
import com.zhidian.common.util.JsonUtil;
import com.zhidian3g.dsp.adPostback.constant.RedisConstant;
import com.zhidian3g.dsp.adPostback.util.LoggerUtil;
import com.zhidian3g.dsp.adPostback.web.action.AdPostbackHelper;
import com.zhidian3g.dsp.adPostback.web.vo.AdOperationLogMessage;
import com.zhidian3g.dsp.adPostback.web.vo.AdPostBackParments;

@Service
public class AdPostBackHandlerService {

	@Resource
	private PostBackService postBackService;
	private JedisPools jedisPools = JedisPools.getInstance();
	
	/**
	 * 竞价业务处理
	 * @param adPostBackParments
	 */
	public void adWinHandler(AdPostBackParments adPostBackParments) {
		AdOperationLogMessage adOperationLogMessage = getAdOperationMessage(adPostBackParments);
		if(adOperationLogMessage == null) {
			return;
		}
		
		decreaseAdCount(adOperationLogMessage.getAid());
		//设置回调回来的结算单价
		adOperationLogMessage.setCp(AdPostbackHelper.getPrice(adPostBackParments.getCp(), adOperationLogMessage.getChid().intValue()));
		adOperationLogMessage.setLogcode(3);
		adOperationLogMessage.setRt(DateUtil.getDateTime());
		LoggerUtil.addWinNoticeLog(adOperationLogMessage);
	}
	
	
	public void adShowHandler(AdPostBackParments adPostBackParments) {
		AdOperationLogMessage adOperationLogMessage = getAdOperationMessage(adPostBackParments);
		if(adOperationLogMessage == null) {
			return;
		}
		
		Long dateTime = System.currentTimeMillis();
		String rId = adOperationLogMessage.getRid();
		Long accountId = adOperationLogMessage.getAcid();
		Long adId = adOperationLogMessage.getAid();
		
		Long price = AdPostbackHelper.getPrice(adPostBackParments.getCp(), adOperationLogMessage.getChid().intValue());
		Long rebatePrice = price;
		//说明已经达到控制的广告量
		Jedis jedis = null;
		try {
			
			//判断是否是掌游渠道
			if(adPostBackParments.getAt() == 4) {
				rebatePrice = rebatePrice * 100;
			}
			
			String sign = Md5SignUtils.sign(dateTime, rId,  accountId,  adId, rebatePrice, BillingType.CPM);
			//1可用，-1账号找不到 -2 广告找不到， 2 账号余额不足，3：日预算不足，4：总预算不足
			int responseCode = postBackService.rebate(sign, dateTime, rId,  accountId,  adId, rebatePrice, BillingType.CPM);
			jedis = jedisPools.getJedis();
			if(responseCode != 1) {
				if(responseCode == 3 || responseCode == 4 || responseCode == 2) {
//					decreaseAdCount(jedis, adId);
					//说明费用已经用光
					jedis.lpush(RedisConstant.DEL_ADID,  responseCode + ":::" + adId);
					CommonLoggerUtil.addBaseLog("==广告id=" +adId+ "=费用已经用光需要删除");
				} else {
					CommonLoggerUtil.addErrQuest("==广告id=" +adId+ "=账号找不  "   + responseCode);
					jedis.lpush(RedisConstant.DEL_ADID,  responseCode + ":::" + adId);
				}
			}
			
//			else {
//				decreaseAdCount(jedis, adId);
//			}
			
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e, JsonUtil.toJson(adOperationLogMessage));
		};
		
		jedisPools.closeJedis(jedis);
		adOperationLogMessage.setCp(price);
		adOperationLogMessage.setLogcode(4);
		adOperationLogMessage.setRt(DateUtil.getDateTime());
		LoggerUtil.addAdShowLog(adOperationLogMessage);
	}


	/**
	 * 扣减广告展示次数
	 * @param jedis
	 * @param adId
	 */
	private void decreaseAdCount(Long adId) {
		Jedis jedis = jedisPools.getJedis();
		try {
			//扣费成功
			Long adControlCount = jedis.hincrBy(RedisConstant.AD_ID_CONTROL_COUNT, adId + "", -1);
			if(adControlCount <=0) {
				//设置成过滤的广告
				jedis.sadd(RedisConstant.AD_STOP_IDS, adId + "");
				CommonLoggerUtil.addLog("adCountLog", "adId=" + adId + "已经达到设定的限定值,已经停止投放");
			} else {
				CommonLoggerUtil.addLog("adCountLog", "剩下的广告次数为：" + adControlCount);
			}
		} catch (Exception e) {
			jedisPools.exceptionBroken(jedis);
			CommonLoggerUtil.addExceptionLog(e);
		}
		
		jedisPools.closeJedis(jedis);
	}
	
	public void adClickHandler(AdPostBackParments adPostBackParments) {
		AdOperationLogMessage adOperationLogMessage = getAdOperationMessage(adPostBackParments);
		if(adOperationLogMessage == null) {
			return;
		}
		
		adOperationLogMessage.setLogcode(5);
		adOperationLogMessage.setRt(DateUtil.getDateTime());
		LoggerUtil.addAdClickLog(adOperationLogMessage);
	}


	private AdOperationLogMessage getAdOperationMessage(
			AdPostBackParments adPostBackParments) {
		Jedis jedis = jedisPools.getJedis();
		String adOperationLogMessageString = jedis.get(adPostBackParments.getAt() + "-" + adPostBackParments.getRid()); 
		AdOperationLogMessage adOperationLogMessage = null;
		
		if(adOperationLogMessageString != null) {
			 adOperationLogMessage = JsonUtil.fromJson(adOperationLogMessageString, AdOperationLogMessage.class);
		} else {
			adPostBackParments.setRt(DateUtil.getDateTime());
			CommonLoggerUtil.addErrQuest(JsonUtil.toJson(adPostBackParments));
		}
		
		jedisPools.closeJedis(jedis);
		return adOperationLogMessage;
	}
	
	
}
