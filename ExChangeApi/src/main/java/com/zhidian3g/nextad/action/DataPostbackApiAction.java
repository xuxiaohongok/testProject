package com.zhidian3g.nextad.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import redis.clients.jedis.Jedis;

import com.zhidian3g.common.constant.RedisConstant;
import com.zhidian3g.common.utils.DateUtil;
import com.zhidian3g.common.utils.IPUtil;
import com.zhidian3g.common.utils.IpUtil2;
import com.zhidian3g.common.utils.JsonUtil;
import com.zhidian3g.common.utils.LoggerUtil;
import com.zhidian3g.nextad.redisClient.JedisPools;
import com.zhidian3g.nextad.service.DSPAdBillingService;
import com.zhidian3g.nextad.vo.AdDownLoadPostBackMessage;
import com.zhidian3g.nextad.vo.AdPostBackMessage;

/**
 * @author Administrator
 */
@Controller
public class DataPostbackApiAction {
	
	private JedisPools jedisPools = JedisPools.getInstance();
	
	@Resource(name="dspAdBillingService")
	private DSPAdBillingService dspAdBillingService;
	
	/**
	 * 
	 * 竞价成功回调地址
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/cyWinCallBack.shtml", method=RequestMethod.GET)
	public void cyWinCallBackURL(HttpServletRequest request, HttpServletResponse response, AdPostBackMessage adPostBackMessage) {
		String ip = IPUtil.getIpAddr(request);
		adPostBackMessage.setIp(ip);
		System.out.println("===cyWinCallBack=====" + ip + ";ip2=" + IpUtil2.getIpAddr(request));
		adPostBackMessage.setRequestTime(DateUtil.getDateTime());
		
		adPostBackMessage.setRequestAdDateTime(adPostBackMessage.getRequestAdDateTime().replace("_", " "));
		LoggerUtil.addWinLogegerMessage(adPostBackMessage);
		response.setStatus(200);
	}
	
	/**
	 * 
	 * 竞价成功回调地址
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/adShow.shtml", method=RequestMethod.GET)
	public void adShowURL(HttpServletRequest request, HttpServletResponse response, AdPostBackMessage adPostBackMessage) {
		System.out.println("adPostBackMessage=" + adPostBackMessage.getRequestAdDateTime());
		String ip = IPUtil.getIpAddr(request);
		adPostBackMessage.setIp(ip);
		adPostBackMessage.setRequestAdDateTime(adPostBackMessage.getRequestAdDateTime().replace("_", " "));
		
		String userId = adPostBackMessage.getUserId();
		adPostBackMessage.setRequestTime(DateUtil.getDateTime());
		Double price = Double.valueOf(adPostBackMessage.getPrice());
		String adId = adPostBackMessage.getAdId();
		Integer adSlotType = adPostBackMessage.getAdSlotType();
		
		Jedis jedis = jedisPools.getJedis();
		
		if(jedis == null) {
			LoggerUtil.addShowLogegerMessage(adPostBackMessage);
			response.setStatus(200);
			return;
		}
		
		try {
			int osType = adPostBackMessage.getOsType();
			String adIdsKey = getAdIdsKey(osType);
			//判断广告是否是存在的
			if(adIdsKey == null || jedis.zrank(adIdsKey, adId) == null) {
				jedisPools.closeJedis(jedis);
				response.setStatus(200);
				LoggerUtil.addBillingLog("adIdsKey=" + adIdsKey + "; adId=" + adId + "==========不存在======");
				return;
			}
			
			//说明已经达到控制的广告量
			Long adControlCount = jedis.hincrBy(RedisConstant.AD_ID_CONTROL_COUNT, adId, -1);
			if(adControlCount <=0) {
				//设置成过滤的广告
				jedis.sadd(RedisConstant.AD_STOP_IDS, adId);
				LoggerUtil.addBillingLog("adId=" + adId + "已经达到设定的限定值,已经停止投放");
			}
			
			String userAdKey = RedisConstant.USER_AD_ADSLOT + userId;
			String adSlotTypeAdValue = jedis.hget(userAdKey, adSlotType + "");
			if(adSlotTypeAdValue == null) {
				adSlotTypeAdValue = adId;
				jedis.hset(userAdKey, adSlotType + "", adSlotTypeAdValue);
			} else {
				if(!adSlotTypeAdValue.contains(adId)) {
					adSlotTypeAdValue =  adSlotTypeAdValue + "," + adId;
					jedis.hset(userAdKey, adSlotType + "", adSlotTypeAdValue);
				}
			}
			
			//进行扣费
			Integer statusCode = dspAdBillingService.rebate(adId, price);
			if(statusCode == 0 || statusCode == 2) {
				//说明费用已经用光
				jedis.lpush(RedisConstant.DEL_ADID,  osType + ":::" + adId);
				LoggerUtil.addBillingLog("==广告id=" +adId+ "=费用已经用光需要删除");
			} 
			jedisPools.closeJedis(jedis);
		} catch (Exception e) {
			LoggerUtil.addExceptionLog(e);
			jedisPools.exceptionBroken(jedis);
			jedisPools.closeJedis(jedis);
		} 
		LoggerUtil.addShowLogegerMessage(adPostBackMessage);
		response.setStatus(200);
	}
	
	/**
	 * 根据平台获取对应的广告key集合
	 * @param osType
	 * @return
	 */
	private String getAdIdsKey(Integer osType) {
		if(osType == 2) {
			return RedisConstant.AD_ANDROID_IDS;
		} else if(osType == 1) {
			return RedisConstant.AD_IOS_IDS;
		} else {
			return RedisConstant.AD_PC_IDS;
		}
	}

	/**
	 * 
	 * 竞价成功回调地址
	 * @param request
	 * @param response
	 * @return
	 */
	private Logger cyClickLog = LoggerFactory.getLogger("cyClickLog");
	@RequestMapping(value = "/adClick.shtml", method=RequestMethod.GET)
	public void adClick(HttpServletRequest request, HttpServletResponse response, AdPostBackMessage adPostBackMessage) {
		String ip = IPUtil.getIpAddr(request);
		adPostBackMessage.setIp(ip);
		System.out.println("===adClick=====" + ip + ";ip2=" + IpUtil2.getIpAddr(request));
		adPostBackMessage.setRequestTime(DateUtil.getDateTime());
		adPostBackMessage.setRequestAdDateTime(adPostBackMessage.getRequestAdDateTime().replace("_", " "));
		cyClickLog.info(JsonUtil.bean2Json(adPostBackMessage));
		response.setStatus(200);
	}
	
	/**
	 * 
	 * 竞价成功回调地址
	 * @param request
	 * @param response
	 * @return
	 */
	private Logger cyDownloadPostBackLog = LoggerFactory.getLogger("cyDownloadPostBackLog");
	@RequestMapping(value = "/addpb.shtml", method=RequestMethod.POST)
	public void downLoadPostBackLog(HttpServletRequest request, HttpServletResponse response, AdDownLoadPostBackMessage adPostBackMessage) {
		String ip = IPUtil.getIpAddr(request);
		adPostBackMessage.setIp(ip);
		adPostBackMessage.setRequestTime(DateUtil.getDateTime());
		adPostBackMessage.setRequestAdDateTime(adPostBackMessage.getRequestAdDateTime().replace("_", " "));
		cyDownloadPostBackLog.info(JsonUtil.bean2Json(adPostBackMessage));
		response.setStatus(200);
	}

}

