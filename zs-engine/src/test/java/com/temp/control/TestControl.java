package com.temp.control;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.zhidian.dsp.constant.DspConstant;
import com.zhidian.dsp.constant.RedisConstant;
import com.zhidian3g.common.redisClient.JedisPools;
import com.zhidian3g.common.util.JsonUtil;
import com.zhidian3g.dsp.solr.service.impl.SolrDMIAdServiceImpl;
import com.zhidian3g.dsp.vo.ad.RedisAdBaseMessage;
import com.zhidian3g.dsp.vo.ad.RedisAdCreateMaterialMessage;
import com.zhidian3g.dsp.vo.ad.RedisAdImage;
import com.zhidian3g.dsp.vo.ad.RedisAdLandingPageMessage;
import com.zhidian3g.dsp.vo.adcontrol.AdBaseMessage;
import com.zhidian3g.dsp.vo.adcontrol.AdMaterialMessage;

public class TestControl {

	private JedisPools jedisPools = JedisPools.getInstance();
	
	@Test
	public void testAddAd() {
		Jedis jedis = jedisPools.getJedis();
		int adxType = 3;
		//广告基本信息
		Long adId = 3l;
		RedisAdBaseMessage redisAdBaseMessage = new RedisAdBaseMessage();
		redisAdBaseMessage.setAdCategory(1);
		redisAdBaseMessage.setAdId(adId);
		redisAdBaseMessage.setAdName("散人私服");
		redisAdBaseMessage.setAdPackageName("com.alicall.androidzb");
		redisAdBaseMessage.setAdPrice(8000);
		redisAdBaseMessage.setClickType(1);
//		Map<String, Object> extendMap = new HashMap<String, Object>(); 
//		redisAdBaseMessage.setExtendMessage(JsonUtil.toJson(extendMap));
		
		
		String adBaseRedisKey = RedisConstant.AD_BASE + adId;
		jedis.set(adBaseRedisKey, JsonUtil.toJson(redisAdBaseMessage));
		
		int createId = 1;
		int materialId = 1;
		RedisAdCreateMaterialMessage redisAdCreateMaterialMessage = new RedisAdCreateMaterialMessage();
		redisAdCreateMaterialMessage.setCreateId(createId);
		redisAdCreateMaterialMessage.setMeterialId(materialId);
		redisAdCreateMaterialMessage.setMeterialType(1);
//		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		String createPackageMessageKey = RedisConstant.AD_CREATE_MATERIAL + adId + "_" + createId + "_" + materialId;
		jedis.set(createPackageMessageKey, JsonUtil.toJson(redisAdCreateMaterialMessage));
		
		RedisAdImage redisAdImage = new RedisAdImage();
		int width = 360;
		int height = 360;
		redisAdImage.setHeight(height);
		redisAdImage.setWidth(width);
		String imageHW = height + "-" + width;
		redisAdImage.setImgURL("http://d.zhidian3g.cn/zhidian-smart/cy/1/360x360.jpg");
		String imageKey = RedisConstant.AD_IMAGE + adId + "_" + createId + "_" + materialId + "_" + imageHW;
		jedis.set(imageKey, JsonUtil.toJson(redisAdImage));
		
		//落地页的选择
		RedisAdLandingPageMessage adLandingPageMessage = new RedisAdLandingPageMessage();
		adLandingPageMessage.setId(1);
		adLandingPageMessage.setLandingPageUrl("http://d.zhidian3g.cn/zdy6/static/srsf/16022958976.html");
		
		//落地页的选择
		RedisAdLandingPageMessage adLandingPageMessage1 = new RedisAdLandingPageMessage();
		adLandingPageMessage1.setId(2);
		adLandingPageMessage1.setLandingPageUrl("http://www.baidu.com");
		
		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
		jedis.hset(adLandingPageKey, "2", JsonUtil.toJson(adLandingPageMessage1));
		
		AdBaseMessage adBaseMessage = new AdBaseMessage();
		adBaseMessage.setAdCategory(1);
		adBaseMessage.setAdType(1);
		adBaseMessage.setAdxType(adxType);
		adBaseMessage.setAreas("广东省");
		adBaseMessage.setId(adId);
		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
		adBaseMessage.setTerminalType(1);
		adBaseMessage.setTimeZones("22-00, 23-00, 00-00, 18-00");
		
		List<AdMaterialMessage> list = new ArrayList<AdMaterialMessage>();
		list.add(new AdMaterialMessage(createId, materialId, redisAdCreateMaterialMessage.getMeterialType(), imageHW, null, null));
		adBaseMessage.setAdMaterialMessageList(list);
	
		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
	}
	
	@Test
	public void testAddAd2() {
		Long adId = 2l;
		Jedis jedis = jedisPools.getJedis();
		int adxType = 3;
		//广告基本信息
		RedisAdBaseMessage redisAdBaseMessage = new RedisAdBaseMessage();
		redisAdBaseMessage.setAdCategory(1);
		redisAdBaseMessage.setAdId(adId);
		redisAdBaseMessage.setAdName("散人私服");
		redisAdBaseMessage.setAdPackageName("com.alicall.androidzb");
		redisAdBaseMessage.setAdPrice(8000);
		redisAdBaseMessage.setClickType(1);
		
		String adBaseRedisKey = RedisConstant.AD_BASE + adId;
		jedis.set(adBaseRedisKey, JsonUtil.toJson(redisAdBaseMessage));
		
		int createId = 2;
		int materialId = 2;
		int materialId2 = 3;
		String title = "散人私服 散人私服 散人私服";
		String detail = "散人私服 散人私服 描述";
		
		RedisAdCreateMaterialMessage redisAdCreateMaterialMessage = new RedisAdCreateMaterialMessage();
		redisAdCreateMaterialMessage.setCreateId(createId);
		redisAdCreateMaterialMessage.setMeterialId(materialId);
		redisAdCreateMaterialMessage.setMeterialType(2);
		redisAdCreateMaterialMessage.setTitle(title);
//		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		String createPackageMessageKey = RedisConstant.AD_CREATE_MATERIAL + adId + "_" + createId + "_" + materialId;
		jedis.set(createPackageMessageKey, JsonUtil.toJson(redisAdCreateMaterialMessage));
		
		
		RedisAdCreateMaterialMessage redisAdCreateMaterialMessage2 = new RedisAdCreateMaterialMessage();
		redisAdCreateMaterialMessage2.setCreateId(createId);
		redisAdCreateMaterialMessage2.setMeterialId(materialId2);
		redisAdCreateMaterialMessage2.setMeterialType(3);
		redisAdCreateMaterialMessage2.setTitle(title);
		redisAdCreateMaterialMessage2.setDetail(detail);
//		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		String createPackageMessageKey2 = RedisConstant.AD_CREATE_MATERIAL + adId + "_" + createId + "_" + materialId2;
		jedis.set(createPackageMessageKey2, JsonUtil.toJson(redisAdCreateMaterialMessage2));
		
		RedisAdImage redisAdImage = new RedisAdImage();
		int width = 160;
		int height = 120;
		redisAdImage.setHeight(height);
		redisAdImage.setWidth(width);
		String imageHW = height + "-" + width;
		redisAdImage.setImgURL("http://d.zhidian3g.cn/zhidian-smart/youdao/160x120.jpg");
		String imageKey1 = RedisConstant.AD_IMAGE + adId + "_" + createId + "_" + materialId + "_" + imageHW;
		jedis.set(imageKey1, JsonUtil.toJson(redisAdImage));
		
		RedisAdImage redisAdImage2 = new RedisAdImage();
		int width1 = 170;
		int height1 = 130;
		redisAdImage2.setHeight(height1);
		redisAdImage2.setWidth(width1); 
		String imageHW2 = height1 + "-" + width1;
		redisAdImage2.setImgURL("http://d.zhidian3g.cn/zhidian-smart/youdao/170x130.jpg");
		String imageKey2 = RedisConstant.AD_IMAGE + adId + "_" + createId + "_" + materialId2 + "_" + imageHW2;
		jedis.set(imageKey2, JsonUtil.toJson(redisAdImage2));
		
		//落地页的选择
		RedisAdLandingPageMessage adLandingPageMessage = new RedisAdLandingPageMessage();
		adLandingPageMessage.setId(1);
		adLandingPageMessage.setLandingPageUrl("http://d.zhidian3g.cn/zdy6/static/srsf/16022958976.html");
		
		//落地页的选择
		RedisAdLandingPageMessage adLandingPageMessage1 = new RedisAdLandingPageMessage();
		adLandingPageMessage1.setId(2);
		adLandingPageMessage1.setLandingPageUrl("http://www.baidu2.com");
		
		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
		jedis.hset(adLandingPageKey, "2", JsonUtil.toJson(adLandingPageMessage1));
		
		AdBaseMessage adBaseMessage = new AdBaseMessage();
		adBaseMessage.setAdCategory(1);
		adBaseMessage.setAdType(1);
		adBaseMessage.setAdxType(adxType);
		adBaseMessage.setAreas("广东省");
		adBaseMessage.setId(adId);
		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
		adBaseMessage.setTerminalType(1);
		adBaseMessage.setTimeZones("15-00, 16-00, 17-00, 18-00");
		
		List<AdMaterialMessage> list = new ArrayList<AdMaterialMessage>();
		list.add(new AdMaterialMessage(createId, materialId, redisAdCreateMaterialMessage.getMeterialType(), imageHW, title.length(), null));
		list.add(new AdMaterialMessage(createId, materialId2, redisAdCreateMaterialMessage2.getMeterialType(), imageHW2 + " " + imageHW, title.length(), detail.length()));
		adBaseMessage.setAdMaterialMessageList(list);
	
		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
		
	}
	
}
