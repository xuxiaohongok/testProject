package com.zhidian3g.nextad.test;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

import com.zhidian.ad.billing.service.BillingQueryService;
import com.zhidian.ad.billing.utils.Md5SignUtils;
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
import com.zhidian.common.util.SpringContextUtil;
import com.zhidian.dsp.control.service.ZSAdControlOSSService;
import com.zhidian.dsp.control.service.impl.AdControlService;
import com.zhidian.dsp.control.service.impl.AdHanderService;
import com.zhidian.dsp.solr.service.SolrDMIAdService;
import com.zhidian.dsp.solr.vo.AdBaseDocumentSourceMessage;
import com.zhidian.dsp.solr.vo.AdMaterialDocumentSourceMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/application-context.xml"})
public class SpringServiceTestCase extends AbstractJUnit4SpringContextTests {
	@Resource
	private AdService adService;
	
	@Resource(name="billAdService")
	private com.zhidian.ad.billing.service.AdService billAdService;
	
	private JedisPools jedisPools = JedisPools.getInstance();
	
	@Resource(name="solrDMIAdService")
	private SolrDMIAdService solrDMIAdService;
	
	@Resource
	private ZSAdControlOSSService dspAdControlOSSService;
	
	@Resource
	private BillingQueryService billingQueryService;
	
	@Resource
	private AdHanderService adHanderService;
	
	@Test
	public void testAdBillQueryService() {
		 long timeMillis = System.currentTimeMillis();
		  String sign = Md5SignUtils.sign(timeMillis, 36l, 8l, 100000, 100000);
	      int j = billAdService.addAd(sign, timeMillis, 36l, 8l, 100000, 100000);
	      System.out.println(j);
		
	}
	@Test
	public void testGetBillQueryService() {
		adHanderService.addAd(77l);
	}
	
	@Test
	public void testGetAllAdIds() {
//		System.out.println(solrDMIAdService.getAllAdId());
//		 AdControlService adControlService2 = (AdControlService)SpringContextUtil.getBean("adControlService");
		 SolrDMIAdService solrDMIAdService1 = (SolrDMIAdService)SpringContextUtil.getBean("solrDMIAdService");
		 System.out.println(solrDMIAdService1.getAllAdId());
	}
	
	@Test
	public void testSetAdxAdPrice() {
		Jedis jedis = jedisPools.getJedis();
		jedis.hset(RedisConstant.ADX_AD_STANDARD_PRICE, 1 + "", "12000");
		jedis.hset(RedisConstant.ADX_AD_STANDARD_PRICE, 3 + "", "12000");
		jedis.hset(RedisConstant.ADX_AD_STANDARD_PRICE, 2 + "", "12000");
		jedisPools.closeJedis(jedis);
	}
	
	@Test
	public void testAddAd() {
		dspAdControlOSSService.addAd(77l);
	}
	
	@Test
	public void testUpdateAdStatus() {
		adService.updateAdStatus(71L, 1);
	}
	
	@Test
	public void testUpdateAdListStatus() {
		List<Long> adIdList = new ArrayList<Long>();
		adIdList.add(70l);
		adIdList.add(71l);
		adIdList.add(77l);
		adService.updateAdListStatus(adIdList);
	}
	
	@Test
	public void testRefreshAdListStatus() {
		Jedis jedis = jedisPools.getJedis();
		List<Long> adIdList = adService.getAdList();
		System.out.println("adIdList=" + adIdList);
		String[] adIdArrays = new String[adIdList.size()];
		for(int i=0; i<adIdList.size(); i++) {
			adIdArrays[i] = adIdList.get(i) + "";
		}
		jedis.sadd(RedisConstant.AD_STOP_IDS, adIdArrays);
		jedisPools.closeJedis(jedis);
	}
	
	@Test
	public void testDataBase() {
		List<Long> adList = adService.getAdList();
		String nowDateString = DateUtil.getDateTime();
		for(Long adId : adList) {
			System.out.println(adId);
		}
	}
	
	@Test
	public void getAdBaseMessage() {
		System.out.println(adService.getAdBaseMessage(60l));
	}
	
	private Map<String, Object> adPutStatry(Long adId) {
		List<AdPutStrategyMessage> adPutStrategyMessageList = adService.getAdPutStrategyMessage(adId);
		Map<String, Object> strategyMap = new HashMap<String, Object>();
		for(AdPutStrategyMessage adPutStrategyMessage : adPutStrategyMessageList) {
			long parentId = adPutStrategyMessage.getParentId();
			String value = adPutStrategyMessage.getTargetItemIds();
			AdTargetsMessage adTargetsMessage = adService.getAdTargetsMessage(value);
			String allTargetValue = adTargetsMessage.getAllValues();
			
			if(parentId == 1) {//星期时间段获取
				String[] weekTimeArrays = allTargetValue.split(",");
				StringBuffer dayTime = new StringBuffer();//当前日期的时间段
				String weekDayMath = DateUtil.getWeekDay() + "_";
				for(String weekTime : weekTimeArrays) {
					if(weekTime.contains(weekDayMath)) {
						dayTime.append("hour" + weekTime.split("_")[1] + " ");
					}
				}
				strategyMap.put("weekTime", dayTime);
			}
			
			else if(parentId == 3) {//网络运营商
				System.out.println("运营商=" + allTargetValue);
//				strategyMap.put("weekTime", allTargetValue);
			}
			
			else if(parentId == 4) {//网络类型
				System.out.println("网络类型=" + allTargetValue);
				strategyMap.put("netType", allTargetValue);
			}
			
			else if(parentId == 5) {
				System.out.println("投放地区=" + allTargetValue);
				strategyMap.put("areas", allTargetValue);
			}
			
			else if(parentId == 7) {
				System.out.println("设备类型=" + allTargetValue);
			}

			else if(parentId == 8) {
				System.out.println("操作系统=" + allTargetValue);
				strategyMap.put("os", allTargetValue);
			}
			System.out.println("parentId=" + parentId + "=========" + adTargetsMessage.getAllValues());
		}
	
		return strategyMap;
	}
	
	@Test
	public void testGetLandPageId() {
		AdLandpageMessage adLandpageMessage = adService.getAdLandPageMessage(1l);
		System.out.println(adLandpageMessage);
	}
	
	@Test
	public void testGetAdPutStrategyMessage() {
		List<AdPutStrategyMessage> adPutStrategyMessageList = adService.getAdPutStrategyMessage(60l);
		for(AdPutStrategyMessage adPutStrategyMessage : adPutStrategyMessageList) {
			long parentId = adPutStrategyMessage.getParentId();
			String value = adPutStrategyMessage.getTargetItemIds();
			AdTargetsMessage adTargetsMessage = adService.getAdTargetsMessage(value);
			String allTargetValue = adTargetsMessage.getAllValues();
			
			if(parentId == 1) {//星期时间段获取
				String[] weekTimeArrays = allTargetValue.split(",");
				StringBuffer dayTime = new StringBuffer();//当前日期的时间段
				String weekDayMath = DateUtil.getWeekDay() + "_";
				for(String weekTime : weekTimeArrays) {
					if(weekTime.contains(weekDayMath)) {
						dayTime.append("hour" + weekTime.split("_")[1] + " ");
					}
				}
			}
			
			else if(parentId == 3) {//网络运营商
				System.out.println("运营商=" + allTargetValue);
			}
			else if(parentId == 4) {//网络类型
				System.out.println("网络类型=" + allTargetValue);
			}
			
			else if(parentId == 5) {
				System.out.println("投放地区=" + allTargetValue);
			}
			
			else if(parentId == 7) {
				System.out.println("设备类型=" + allTargetValue);
			}

			else if(parentId == 8) {
				System.out.println("操作系统=" + allTargetValue);
			}
			
			System.out.println("parentId=" + parentId + "=========" + adTargetsMessage.getAllValues());
		}
	}
	
	@Test
	public void testTime() {
		System.out.println(DateUtil.getHour());
	}
	
	@Test
	public void testGetAdMaterialMessage() {
		System.out.println(adService.getAdMaterialMessage(1l, 1l));
	}
	
	@Test
	public void testGetAdImageMessage() {
		System.out.println(adService.getAdImageMessage(1l, 1l));
	}
	
	@Test
	public void testGetAdTargetsMessage() {
		AdTargetsMessage adTargetsMessage = adService.getAdTargetsMessage("176,163,162,161,160,159,158,157,156,155,164,165,166,175,174,173,172,171,170,169,168,167,154,153,152,139,138,137,136,135,134,133,132,131,140,141,142,151,150,149,148,147,146,145,144,143,130,129,176,163,162,161,160,159,158,157,156,155,164,165,166,175,174,173,172,171,170,169,168,167,154,153,152,139,138,137,136,135,134,133,132,131,140,141,142,151,150,149,148,147,146,145,144,143,130,129,129,141,142,144,145,146,147,148,149,150,140,139,130,131,132,133,134,135,136,137,138,151,153,152,165,166,167,168,169,170,175,174,173,164,163,154,155,156,157,158,159,160,161,162,172,171,176,143");
		System.out.println(adTargetsMessage.getAllValues());
	}
	
	@Test
	public void testGetAdTargetsByParentIdMessage() {
		AdTargetsMessage adTargetsMessage = adService.getAdTargetsByParentIdMessage(5l, "id");
		String[] allIds = adTargetsMessage.getAllValues().split(",");
		StringBuffer stringBuffer = new StringBuffer();
		for(String id : allIds) {
			stringBuffer.append((adService.getAdTargetsByParentIdMessage(Long.valueOf(id)).getAllValues() + ","));
		}
		stringBuffer.substring(0, stringBuffer.length() - 1);
		System.out.println(stringBuffer.substring(0, stringBuffer.length() - 1));
	}
	
	private List<AdMaterialDocumentSourceMessage> testMaterialDocument(Jedis jedis, Long adId) {
		List<AdMaterialDocumentSourceMessage> adMaterialDocumentMessageList = new ArrayList<AdMaterialDocumentSourceMessage>();
		List<AdCreateRelationMessage> adMatertialMessageList = adService.getAdCreateRelationMessageList(adId);
		if(adMatertialMessageList != null && adMatertialMessageList.size() > 0) {
			Set<String> adMaterialSet = new HashSet<String>();
			for(AdCreateRelationMessage adCreateRelationMessage : adMatertialMessageList) {
				long createId = adCreateRelationMessage.getCreateId();
				long materialId = adCreateRelationMessage.getMaterialId();
				int materialType = adCreateRelationMessage.getMaterialType();
				Long landPageId = adCreateRelationMessage.getLandPageId();
				
				//已添加到缓存标志
				String markString = "adMaterial=" + createId + "=" + materialId;
				if(!adMaterialSet.contains(markString)) {
					AdMaterialMessage adMaterialMessage = adService.getAdMaterialMessage(createId, materialId);
					setMaterialMessage(jedis, adMaterialMessage);
					
					//添加素材的索引文档
					Integer tLen = adMaterialMessage.getTitle() == null ? null : adMaterialMessage.getTitle().length(); 
					Integer dLen = adMaterialMessage.getDescription() == null ? null : adMaterialMessage.getDescription().length(); 
					
					StringBuffer imageHW = new StringBuffer();
					if(materialType!=5) {
						List<AdImageMessage> adImageMessageList = adService.getAdImageMessage(createId, materialId);
						for(AdImageMessage adImageMessage : adImageMessageList) {
							setImage(jedis, createId, materialId, adImageMessage);
							imageHW.append(adImageMessage.getHeight() + "-" + adImageMessage.getWidth() + " ");
						}
					}
					
					AdMaterialDocumentSourceMessage adMaterialDocumentMessage = new AdMaterialDocumentSourceMessage(createId, materialId, materialType, 
							imageHW.toString().trim(), tLen, dLen);
					adMaterialDocumentMessageList.add(adMaterialDocumentMessage);
					adMaterialSet.add(markString);
				} 
				
				//获取素材信息落地页
				AdLandpageMessage adLandpageMessage = adService.getAdLandPageMessage(landPageId);
				String adLandingPageKey = RedisConstant.AD_MATERTIAL_LANDINGPAGE + adId + "_" + createId + "_"   + materialId;
				jedis.hset(adLandingPageKey, landPageId + "", JsonUtil.toJson(adLandpageMessage));
				
				
			}
		}
		
		return adMaterialDocumentMessageList;
	
	}
	
	@Test
	public void testGetAdCreateRelationMessage() {
		Jedis jedis = jedisPools.getJedis();
		long adId = 1l;
		
		List<AdMaterialDocumentSourceMessage> adMaterialDocumentMessageList = new ArrayList<AdMaterialDocumentSourceMessage>();
		List<AdCreateRelationMessage> adMatertialMessageList = adService.getAdCreateRelationMessageList(adId);
		if(adMatertialMessageList != null && adMatertialMessageList.size() > 0) {
			Set<String> adMaterialSet = new HashSet<String>();
			for(AdCreateRelationMessage adCreateRelationMessage : adMatertialMessageList) {
				long createId = adCreateRelationMessage.getCreateId();
				long materialId = adCreateRelationMessage.getMaterialId();
				int materialType = adCreateRelationMessage.getMaterialType();
				Long landPageId = adCreateRelationMessage.getLandPageId();
				
				//已添加到缓存标志
				String markString = "adMaterial=" + createId + "=" + materialId;
				if(!adMaterialSet.contains(markString)) {
					AdMaterialMessage adMaterialMessage = adService.getAdMaterialMessage(createId, materialId);
					setMaterialMessage(jedis, adMaterialMessage);
					
					//添加素材的索引文档
					Integer tLen = adMaterialMessage.getTitle() == null ? null : adMaterialMessage.getTitle().length(); 
					Integer dLen = adMaterialMessage.getDescription() == null ? null : adMaterialMessage.getDescription().length(); 
					
					StringBuffer imageHW = new StringBuffer();
					if(materialType!=5) {
						List<AdImageMessage> adImageMessageList = adService.getAdImageMessage(createId, materialId);
						for(AdImageMessage adImageMessage : adImageMessageList) {
							setImage(jedis, createId, materialId, adImageMessage);
							imageHW.append(adImageMessage.getHeight() + "-" + adImageMessage.getWidth() + " ");
						}
					}
					
					AdMaterialDocumentSourceMessage adMaterialDocumentMessage = new AdMaterialDocumentSourceMessage(createId, materialId, materialType, 
							imageHW.toString().trim(), tLen, dLen);
					adMaterialDocumentMessageList.add(adMaterialDocumentMessage);
					adMaterialSet.add(markString);
				} 
				
				//获取素材信息落地页
				AdLandpageMessage adLandpageMessage = adService.getAdLandPageMessage(landPageId);
				String adLandingPageKey = RedisConstant.AD_MATERTIAL_LANDINGPAGE + adId + "_" + createId + "_"   + materialId;
				jedis.hset(adLandingPageKey, landPageId + "", JsonUtil.toJson(adLandpageMessage));
			}
		}
	}
	
	
	private void setRedisBaseMessage(Jedis jedis, AdBaseMessage adBaseMessage, Map<String, Object> extendMap) {
		Long adId = adBaseMessage.getId();
		RedisAdBaseMessage redisAdBaseMessage = new RedisAdBaseMessage();
		BeanUtils.copyProperties(adBaseMessage, redisAdBaseMessage);
		
		if(extendMap != null) {
			redisAdBaseMessage.setExtendMessage(JsonUtil.toJson(extendMap));
		}
		
		String adBaseRedisKey = RedisConstant.AD_BASE + adId;
		jedis.set(adBaseRedisKey, JsonUtil.toJson(redisAdBaseMessage));
	}

	/**
	 * 保存素材信息
	 * @param jedis
	 * @param adMaterialMessage
	 */
	private void setMaterialMessage(Jedis jedis, AdMaterialMessage adMaterialMessage) {
		String createPackageMessageKey = RedisConstant.AD_CREATE_MATERIAL + adMaterialMessage.getCreateId() + "_" + adMaterialMessage.getMaterialId();
		jedis.set(createPackageMessageKey, JsonUtil.toJson(adMaterialMessage));
	}

	/**
	 * 保存图片信息
	 * @param jedis
	 * @param createId
	 * @param materialId
	 * @param redisAdImage
	 */
	private void setImage(Jedis jedis, Long createId, Long materialId, AdImageMessage redisAdImage) {
		String imageHW = redisAdImage.getHeight() + "-" + redisAdImage.getWidth();
		redisAdImage.setImageURL(redisAdImage.getImageURL());
		String imageKey = RedisConstant.AD_IMAGE + createId + "_" + materialId + "_" + imageHW;
		jedis.set(imageKey, JsonUtil.toJson(redisAdImage));
	}
}
