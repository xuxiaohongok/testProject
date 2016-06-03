package com.zhidian.dsp.control.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.google.common.base.Joiner;
import com.zhidian.ad.billing.constant.ReturnCode;
import com.zhidian.ad.billing.service.BalanceService;
import com.zhidian.ad.billing.service.BillingQueryService;
import com.zhidian.ad.billing.utils.Md5SignUtils;
import com.zhidian.ad.domain.AccountFeeMessage;
import com.zhidian.ad.domain.AdBaseMessage;
import com.zhidian.ad.domain.AdCreateRelationMessage;
import com.zhidian.ad.domain.AdImageMessage;
import com.zhidian.ad.domain.AdLandpageMessage;
import com.zhidian.ad.domain.AdMaterialMessage;
import com.zhidian.ad.domain.AdPutStrategyMessage;
import com.zhidian.ad.domain.AdTargetsMessage;
import com.zhidian.ad.service.AdService;
import com.zhidian.ad.vo.redis.RedisAdBaseMessage;
import com.zhidian.common.contans.RedisConstant;
import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.common.util.DateUtil;
import com.zhidian.common.util.JsonUtil;
import com.zhidian.dsp.solr.service.SolrDMIAdService;
import com.zhidian.dsp.solr.vo.AdBaseDocumentSourceMessage;
import com.zhidian.dsp.solr.vo.AdMaterialDocumentSourceMessage;
import com.zhidian.dsp.util.AdControlLoggerUtil;
import com.zhidian.dsp.util.AreasUtil;

@Service
public class AdHanderService {
	
	@Resource
	private AdService adService;
	
	@Resource(name="solrDMIAdService")
	private SolrDMIAdService solrDMIAdService;
	
	@Resource(name="billAdService")
	private com.zhidian.ad.billing.service.AdService adBillService;
	
	@Resource(name="billBalanceService")
	private BalanceService balanceService;
	
	@Resource
	private BillingQueryService billingQueryService;
	
	@Resource
	private AdControlService adControlService;
	
	private JedisPools jedisPools = JedisPools.getInstance();
	
	/**
	 * 
	 * 广告账户充值
	 */
	public String rechargeBalance(Long accountId, Long rechargeFees) {
		AdControlLoggerUtil.addTimeLog("====调用广告账户充值接口=accountId=" + accountId + ";充值：" + rechargeFees);
		Long dateTime = System.currentTimeMillis();
		try {
			String sign = Md5SignUtils.sign(dateTime, accountId, rechargeFees);
			int responseCode = balanceService.rechargeBalance(sign, dateTime, accountId, rechargeFees);
			if(responseCode == ReturnCode.SUCCESS_CODE) {
				AdControlLoggerUtil.addTimeLog("===广告账成功充值==========");
			} else {
				AdControlLoggerUtil.addTimeLog("===广告账充值失败==========");
			}
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
			return "充值失败";
		}
		return "ok";
	}
	
	
	/**
	 * 系统启动初始化广告
	 */
	public void addAllAd(boolean isCleanAllMessage) {
		Jedis jedis  = null;
		//清楚所有的广告
		try {
			
			jedis  = jedisPools.getJedis();
			AdControlLoggerUtil.addTimeLog("====调用清除广告索引文档==============");
			solrDMIAdService.delAllAd();
			
			//需要清除缓存
			if(isCleanAllMessage) {
				Long dateTime = System.currentTimeMillis();
				String sign = Md5SignUtils.sign(dateTime);
				AdControlLoggerUtil.addTimeLog("==================清除广告日预算==============");
				billingQueryService.clearAllDayBudget(sign, dateTime);
			}
			
			List<Long> adIdList = adService.getAdList();
			int adIdListSize = 0;
			if(adIdList != null && adIdList.size() != 0) {
				adIdListSize = adIdList.size();
			} else {
				AdControlLoggerUtil.addTimeLog("==没有符合投放条件的广告==============");
				jedisPools.closeJedis(jedis);
				return;
			}
			
			//先设置暂停所有广告的投放
			String[] adIdArrays = new String[adIdListSize];
			for(int i=0; i<adIdListSize; i++) {
				adIdArrays[i] = adIdList.get(i) + "";
			}
			jedis.sadd(RedisConstant.AD_STOP_IDS, adIdArrays);
			
			List<Long> adPutAdIdList = new ArrayList<Long>();
			for(int i=0; i<adIdListSize; i++) {
				Long adId = adIdList.get(i);
				boolean isScuessful = adPutHandler(adId, jedis);
				if(isScuessful) {
					adPutAdIdList.add(adId);
				}
			}
			
			System.out.println("adPutAdIdList=" + adPutAdIdList);
			if(adPutAdIdList.size() != 0) {
				adControlService.setAdAllControlTimes(adPutAdIdList);
				jedis.del(RedisConstant.AD_STOP_IDS);
				adService.updateAdListStatus(adPutAdIdList);;
			} else {
				AdControlLoggerUtil.addTimeLog("==没有投放投放策略的广告==============");
			}
		} catch (Exception e) {
			AdControlLoggerUtil.addExceptionLog(e);
		}
		jedisPools.closeJedis(jedis);
	}
	
	
	/**
	 * @param adId
	 */
	public boolean addAd(Long adId) {
		//先暂停广告的投放清楚索引文档、缓存
		boolean isSucessful = true;
		try {
			AdControlLoggerUtil.addTimeLog("===添加广告前删除之前广告索引文档=========" + adId);
			solrDMIAdService.delAdFromSolr(adId);
			//
			Jedis jedis  = jedisPools.getJedis();
			Set<String> adKeySet = jedis.smembers(RedisConstant.AD_KEY_SET + adId);
			
			if(CollectionUtils.isNotEmpty(adKeySet)) {
				jedis.del(adKeySet.toArray(new String[0]));
				AdControlLoggerUtil.addTimeLog("==添加广告前删除广告缓存==" + adKeySet);
			}
			//广告设置停止投放
			jedis.sadd(RedisConstant.AD_STOP_IDS, adId + "");
			
			isSucessful = adPutHandler(adId, jedis);
			//广告符合投放条件设置广告速度次数
			if(isSucessful) {
				//设置广告投放速度
				adControlService.setAdControlTimes(adId);
				//更改广告的投放状态为投放中
				adService.updateAdStatus(adId, 4);
			} else {
				AdControlLoggerUtil.addTimeLog("adId=" + adId + ";广告投放失败=============");
			}
			
			jedis.srem(RedisConstant.AD_STOP_IDS, adId + "");
			jedisPools.closeJedis(jedis);
		} catch (Exception e) {
			AdControlLoggerUtil.addExceptionLog(e,  DateUtil.getDateTime() + "添加广告索引失败");
			isSucessful = false;
		}
		
		return isSucessful;
	}


	/**
	 * 广告投放逻辑处理
	 * @param adId
	 * @param jedis
	 */
	private boolean adPutHandler(Long adId, Jedis jedis) {
		boolean isScuessful = false;
		try {
			AdBaseMessage adBaseMessage = adService.getAdBaseMessage(adId);
			
			if(adBaseMessage == null) {
				AdControlLoggerUtil.addTimeLog(adId + "==广告条件不符合,获取不到===========");
				return isScuessful;
			}
			
			String nowDateString = DateUtil.getDateTime();
			Date startDate = adBaseMessage.getPutStartTime();
			if(startDate == null) {
				AdControlLoggerUtil.addTimeLog(adBaseMessage.getId() + "==投放起始日期为空");
				return isScuessful;
			}
			
			String adStartDateString = DateUtil.get(startDate, "yyyy-MM-dd HH:mm:ss");
			if(DateUtil.isMax_date(nowDateString, adStartDateString)){
				AdControlLoggerUtil.addTimeLog(adBaseMessage.getId() + "==投放起始日期未到");
				return isScuessful;
			} 
			
			/**
			 * 广告结束日时间已超过，不生成
			 */
			Date endDate = adBaseMessage.getPutEndTime();
			if(endDate != null){
				String endDateString = DateUtil.get(endDate, "yyyy-MM-dd") + " 23:59:59";
				if(DateUtil.isMax_date(endDateString, nowDateString)){
					AdControlLoggerUtil.addTimeLog("adId" + adId + "=====已过期=======");
					return isScuessful;
				}
			}
			
			//设置投放策略
			Map<Integer, Object> putStatryMap = adPutStatry(adId);
			if(!(Boolean)putStatryMap.get(0)) {
				AdControlLoggerUtil.addTimeLog("adId" + adId + "====不符合投放策略条件=======");
				return isScuessful;
			}
			
			Set<String> adKeySet = new HashSet<String>();
			
			List<AdMaterialDocumentSourceMessage> adMaterialMessageList = getMaterialDocumentList(jedis, adId, adKeySet);
			if(adMaterialMessageList == null) {
				AdControlLoggerUtil.addTimeLog("adId" + adId + "=====广告暂时没有投放素材=======");
				return isScuessful;
			}
			
			long dateTime = System.currentTimeMillis();
			long accountId = adBaseMessage.getAccountId();
			long dayBudget = adBaseMessage.getDayBudget();
			long totalBudget = adBaseMessage.getTotalBudget();
			String adSign = Md5SignUtils.sign(dateTime, accountId, adId, dayBudget, totalBudget);
			//广告计费的初始化
			int status = adBillService.addAd(adSign, dateTime, accountId, adId, dayBudget, totalBudget);
			
			//根据状态值进行相对应的判断
			if(status == ReturnCode.ACCOUNT_NOT_FOUND_CODE) {
				//没有对应的账户,需要添加广告账户
				AdControlLoggerUtil.addTimeLog("=====需要添加广告账户=======" + accountId);
				
				long dateTime2 = System.currentTimeMillis();
				AccountFeeMessage accountFeeMessage = adService.getAccountFeeMessage(accountId);
				
				if(accountFeeMessage == null) {
					AdControlLoggerUtil.addTimeLog("=====广告没有对用的账户,添加失败=======");
					return isScuessful;
				}
				
				Long balance = accountFeeMessage.getBalance();
				String balanceSign =  Md5SignUtils.sign(dateTime2, accountId, balance);
				
				//添加账户
				int addBalanceStatus = balanceService.addBalance(balanceSign, dateTime2, accountId, balance);
				if(addBalanceStatus == ReturnCode.SUCCESS_CODE) {
					AdControlLoggerUtil.addTimeLog("=====成功添加广告账户=======");
				} else {
					AdControlLoggerUtil.addTimeLog("=====广告添加账户失败=======");
					return isScuessful;
				}
				
				//判断账户是否成功，重新添加广告
				status = adBillService.addAd(adSign, dateTime, accountId, adId, dayBudget, totalBudget);
			}
			
			//账户余额不足
			if(status == ReturnCode.BALANCE_NOT_ENOUGH_CODE) {
				AdControlLoggerUtil.addTimeLog("=====广告账户余额不足,添加失败=======");
				adService.updateAdStatus(adId, 7);
				return isScuessful;
			} else if(status == ReturnCode.TOTAL_BUDGET_NOT_ENOUGH_CODE) {
				//广告预算用光
				AdControlLoggerUtil.addTimeLog("=====广告总预算用光,添加失败=======");
				adService.updateAdStatus(adId, 6);
				return isScuessful;
			} else if(status == ReturnCode.DAY_BUDGET_NOT_ENOUGH_CODE) {
				//广告日预算用关
				AdControlLoggerUtil.addTimeLog("=====广告日预算用关,添加失败=======");
				adService.updateAdStatus(adId, 5);
				return isScuessful;
			}
			
			Integer adxType = adBaseMessage.getAdxType().intValue();
			AdBaseDocumentSourceMessage adDocumentBaseMessage  = new AdBaseDocumentSourceMessage();
			adDocumentBaseMessage.setAdCategory(adBaseMessage.getAdCategory());
			adDocumentBaseMessage.setAdxType(adBaseMessage.getAdxType().intValue());
			adDocumentBaseMessage.setId(adBaseMessage.getId());
			adDocumentBaseMessage.setTerminalType(adBaseMessage.getTerminalType());
			adDocumentBaseMessage.setTimeZones(putStatryMap.get(1).toString());
			adDocumentBaseMessage.setAdPrice(adBaseMessage.getAdPrice());
			
			//网络设置
			if(putStatryMap.get(4) != null) {
				adDocumentBaseMessage.setNetworkType(putStatryMap.get(4).toString());
			}
			if(putStatryMap.get(8) != null) {
				adDocumentBaseMessage.setOsPlatform(putStatryMap.get(8).toString());
			}
			
			adDocumentBaseMessage.setAreas(putStatryMap.get(5).toString());
			
			String adBaseRedisKey = setRedisBaseMessage(jedis, adBaseMessage);
			adKeySet.add(adBaseRedisKey);
			jedis.sadd(RedisConstant.AD_KEY_SET + adId, adKeySet.toArray(new String[0]));
			
			//设置投放素材
			adDocumentBaseMessage.setAdMaterialMessageList(adMaterialMessageList);
			
			//调用广告引擎添加广告索引文档
			isScuessful = solrDMIAdService.addAdToSolr(adDocumentBaseMessage);
			//判断索引文档是否添加成功
			if(!isScuessful) {
				AdControlLoggerUtil.addTimeLog("adId=" + adId + "=========索引文档添加失败==");
			} else {
				AdControlLoggerUtil.addTimeLog("=adId=" + adId + "=========已投放==");
			}
		} catch (Exception e) {
			AdControlLoggerUtil.addExceptionLog(e);
		}
		return isScuessful;
	}
	
	public String delAd(Long adId, Integer adPutState) {
		AdControlLoggerUtil.addTimeLog("删除广告adId=" + adId);
		try {
			boolean isScuess = delAdFromSolrAndRedis(adId);
			if(isScuess) {
				//更改广告位暂停状态
				adService.updateAdStatus(adId, adPutState);
				AdControlLoggerUtil.addTimeLog("更改广告adId=" + adId + "状态为" + adPutState);
				return "ok";
			}
		} catch (Exception e) {
			AdControlLoggerUtil.addExceptionLog(e);
		}
		return "fail";
	}
	
	public String delAd(Long adId) {
		boolean isScuess = delAdFromSolrAndRedis(adId);
		if(isScuess) {
			return "ok";
		}
		return "fail";
	}

	private boolean delAdFromSolrAndRedis(Long adId) {
		Jedis jedis = jedisPools.getJedis();
		//删除索引文档
		try {
			AdControlLoggerUtil.addTimeLog("删除广告" + adId + "索引");
			solrDMIAdService.delAdFromSolr(adId);
			//删除缓存
			Set<String> adKeySet = jedis.smembers(RedisConstant.AD_KEY_SET + adId);
			if(adKeySet != null && adKeySet.size() != 0) {
				jedis.del(adKeySet.toArray(new String[0]));
				AdControlLoggerUtil.addTimeLog("==删除广告缓存==" + adKeySet);
			}
			return true;
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
		}
		return false;
	}
	
	/**
	 * 1 时间段 2 手机品牌 3 运营商 4 网络类型  5 投放地区  6  7  8 操作系统
	 * 广告投放策略
	 * @param adId
	 * @return
	 */
	private Map<Integer, Object> adPutStatry(Long adId) {
		List<AdPutStrategyMessage> adPutStrategyMessageList = adService.getAdPutStrategyMessage(adId);
		Map<Integer, Object> strategyMap = new HashMap<Integer, Object>();
		for(AdPutStrategyMessage adPutStrategyMessage : adPutStrategyMessageList) {
			long parentId = adPutStrategyMessage.getParentId();
			String value = adPutStrategyMessage.getTargetItemIds();
			AdTargetsMessage adTargetsMessage = adService.getAdTargetsMessage(value);
			String allTargetValue = adTargetsMessage.getAllValues();
			
			if(parentId == 1) {//星期时间段获取
				StringBuffer dayTime = new StringBuffer();//当前日期的时间段
				String weekDayMath = DateUtil.getWeekDay() + "_";
				String[] weekTimeArrays = allTargetValue.split(",");
				for(String weekTime : weekTimeArrays) {
					if(weekTime.contains(weekDayMath)) {
						dayTime.append("hour" + weekTime.split("_")[1] + " ");
					}
				}
				
				if(StringUtils.isBlank(dayTime)) {
					CommonLoggerUtil.addBaseLog("=adId=" + adId + "=========今天不投放广告==");
					strategyMap.put(0, false);
					return strategyMap;
				}
				
				strategyMap.put(1, dayTime);
			}
			
			else if(parentId == 3) {//网络运营商
				strategyMap.put(3, allTargetValue);
			}
			
			else if(parentId == 4) {//网络类型
				strategyMap.put(4, allTargetValue);
			}
			
			else if(parentId == 5) {
				String[] areasStringArray = allTargetValue.split(",");
				Map<String, String> areaMap = new HashMap<String, String>();
				for(String area : areasStringArray) {
					areaMap.put(area, AreasUtil.getArea(area));
				}
				
				if(areaMap.size() == 0) {
					continue;
				}
				
				String areasStrings = Joiner.on(",").skipNulls().join(areaMap.values()).replace("|", ",");
				strategyMap.put(5, areasStrings);
			}
			
			else if(parentId == 7) {
				strategyMap.put(7, allTargetValue);
			}

			else if(parentId == 8) {
				strategyMap.put(8, allTargetValue);
			}
		}
		
		//判读没有限制条件的策略
		Set<Integer> strategyCategory = strategyMap.keySet();
		if(!strategyCategory.contains(1)) {
			CommonLoggerUtil.addBaseLog("==投放时间不限制===============");
			StringBuffer dayTime = new StringBuffer();//当前日期的时间段
			for(int i= 0; i<24; i++) {
				dayTime.append("hour" + i + " ");
			}
			strategyMap.put(1, dayTime.toString().trim());
		}
		
		if(!strategyCategory.contains(2)) {
			CommonLoggerUtil.addBaseLog("==投放手机品牌不限制===============");
		}
		
		if(!strategyCategory.contains(3)) {
			CommonLoggerUtil.addBaseLog("==运营商不限制===============");
			strategyMap.put(3, adService.getAdTargetsByParentIdMessage(3l).getAllValues());
		}
		
		if(!strategyCategory.contains(4)) {
			CommonLoggerUtil.addBaseLog("==网络类型不限制===============");
			strategyMap.put(4, adService.getAdTargetsByParentIdMessage(4l).getAllValues());
		}
		
		if(!strategyCategory.contains(5)) {
			CommonLoggerUtil.addBaseLog("==所有地区都投放=================");
			strategyMap.put(5, AreasUtil.getAllAreas());
		}
		
		if(!strategyCategory.contains(8)) {
			CommonLoggerUtil.addBaseLog("==操作系统不限制=================");
			strategyMap.put(8, adService.getAdTargetsByParentIdMessage(8l).getAllValues());
		}
		
		//0=true表示该广告投放策略有限
		strategyMap.put(0, true);
		return strategyMap;
	}
	
	private List<AdMaterialDocumentSourceMessage> getMaterialDocumentList(Jedis jedis, Long adId, Set<String> adKeySet) {
		//创建索引的文档元数据
		List<AdMaterialDocumentSourceMessage> adMaterialDocumentMessageList = new ArrayList<AdMaterialDocumentSourceMessage>();
		try {
			List<AdCreateRelationMessage> adMatertialMessageList = adService.getAdCreateRelationMessageList(adId);
			if(adMatertialMessageList != null && adMatertialMessageList.size() > 0) {
				Set<String> adMaterialSet = new HashSet<String>();
				Set<String> landPageKey = new HashSet<String>();
				for(AdCreateRelationMessage adCreateRelationMessage : adMatertialMessageList) {
					long createId = adCreateRelationMessage.getCreateId();
					long materialId = adCreateRelationMessage.getMaterialId();
					int materialType = adCreateRelationMessage.getMaterialType();
					Long landPageId = adCreateRelationMessage.getLandPageId();
					
					//已添加到缓存标志
					String markString = "adMaterial=" + createId + "=" + materialId;
					if(!adMaterialSet.contains(markString)) {
						AdMaterialMessage adMaterialMessage = adService.getAdMaterialMessage(createId, materialId);
						
						//添加素材的索引文档
						Integer tLen = adMaterialMessage.getTitle() == null ? null : adMaterialMessage.getTitle().length(); 
						Integer dLen = adMaterialMessage.getDescription() == null ? null : adMaterialMessage.getDescription().length(); 
						
						StringBuffer imageHWs = new StringBuffer();
						Map<String, AdImageMessage> mapAdImage = new HashMap<String, AdImageMessage>();
						if(materialType!=5) {
							List<AdImageMessage> adImageMessageList = adService.getAdImageMessage(createId, materialId);
							for(AdImageMessage adImageMessage : adImageMessageList) {
								String imageHW = adImageMessage.getHeight() + "-" + adImageMessage.getWidth();
								mapAdImage.put(imageHW, adImageMessage);
								imageHWs.append(imageHW + " ");
							}
						}
						
						//添加素材图片信息
						if(mapAdImage.size() != 0) {
							adMaterialMessage.setMapAdImage(mapAdImage);
						}
						
						//素材信息保存缓存中
						String createPackageMessageKey = RedisConstant.AD_CREATE_MATERIAL + adMaterialMessage.getCreateId() + "_" + adMaterialMessage.getMaterialId();
						jedis.setex(createPackageMessageKey, DateUtil.getDifferentTimeMillis(), JsonUtil.toJson(adMaterialMessage));
						
						AdMaterialDocumentSourceMessage adMaterialDocumentMessage = new AdMaterialDocumentSourceMessage(createId, materialId, materialType, imageHWs.toString().trim(), tLen, dLen);
						adMaterialDocumentMessageList.add(adMaterialDocumentMessage);
						adMaterialSet.add(markString);
					} 
					
					//获取素材信息落地页
					AdLandpageMessage adLandpageMessage = adService.getAdLandPageMessage(landPageId);
					String adLandingPageKey = RedisConstant.AD_MATERTIAL_LANDINGPAGE + adId + "_" + createId + "_"   + materialId;
					
					//收集广告落地页key
					jedis.hset(adLandingPageKey, landPageId + "", JsonUtil.toJson(adLandpageMessage));
					landPageKey.add(adLandingPageKey);
				}
				
				//落地页过期时间
				for(String landPage : landPageKey) {
					jedis.expire(landPage, DateUtil.getDifferentTimeMillis());
					//添加到广告信息key中
					adKeySet.add(landPage);
				}
			} else {
				adMaterialDocumentMessageList = null;
			}
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
		}
		
		return adMaterialDocumentMessageList;
	
	}
	
	private String setRedisBaseMessage(Jedis jedis, AdBaseMessage adBaseMessage) {
		Long adId = adBaseMessage.getId();
		RedisAdBaseMessage redisAdBaseMessage = new RedisAdBaseMessage();
		BeanUtils.copyProperties(adBaseMessage, redisAdBaseMessage);
		
		String adBaseRedisKey = RedisConstant.AD_BASE + adId;
		jedis.setex(adBaseRedisKey, DateUtil.getDifferentTimeMillis(), JsonUtil.toJson(redisAdBaseMessage));
		return adBaseRedisKey;
	}

}
