package com.temp.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.common.util.JsonUtil;
import com.zhidian.dsp.constant.DspConstant;
import com.zhidian.dsp.constant.RedisConstant;
import com.zhidian.dsp.solr.documentmanager.AdDspDocumentManager;
import com.zhidian.dsp.solr.service.impl.SolrDMIAdServiceImpl;
import com.zhidian.dsp.solr.vo.AdBaseDocumentSourceMessage;
import com.zhidian.dsp.solr.vo.AdMaterialDocumentSourceMessage;
import com.zhidian.dsp.vo.ad.AdImageMessage;
import com.zhidian.dsp.vo.ad.AdLandpageMessage;
import com.zhidian.dsp.vo.ad.AdMaterialMessage;
import com.zhidian.dsp.vo.ad.RedisAdBaseMessage;

public class TestControl {

	private JedisPools jedisPools = JedisPools.getInstance();
	
	@Test
	public void testDelAll() {
		AdDspDocumentManager.getInstance().deleteAllDocument();
		jedisPools.getJedis().flushDB();
	}
	
	@Test
	public void testAddAd() {
//		adxType4 AND terminalType1 AND os-ios AND adType1 AND 9
		Jedis jedis = jedisPools.getJedis();
		int adxType = 2;
		//广告基本信息
		Long adId = 1l;
		/*Map<String, Object> extendMap = new HashMap<String, Object>(); 
		extendMap.put("adId", adId);
		extendMap.put("cid", 1);*/
		int adCategory = 15;
		int terminalType = 1;
		
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("adid", "1");
//		map.put("adVersion", "1.0.0");
//		map.put("adFileName", "zy007_bbtan.apk");
		
		setRedisBaseMessage(jedis, adId, null, adCategory);
		
		long createId = 1l;
		long materialId = 1;
		int materialType = 1;
//		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		int h = 120;
		int w = 160;
		String imageHW = h + "-" + w;
		setMaterialMessage(jedis, adId, createId, materialId, materialType, null, null, h, w, "http://d.zhidian3g.cn/zhidian-smart/youdao/160x120.jpg");
		
		//落地页的选择
		AdLandpageMessage adLandingPageMessage = new AdLandpageMessage();
		adLandingPageMessage.setId(1l);
		adLandingPageMessage.setLandPageUrl("http://d.zhidian3g.cn/zdy6/static/srsf/3/16011556969.html");
		
		//落地页的选择
//		AdLandpageMessage adLandingPageMessage1 = new AdLandpageMessage();
//		adLandingPageMessage1.setId(2l);
//		adLandingPageMessage1.setLandPageUrl("http://www.baidu.com");
		
		String adLandingPageKey = RedisConstant.AD_MATERTIAL_LANDINGPAGE + adId + "_" + createId + "_" + materialId;
		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
//		jedis.hset(adLandingPageKey, "2", JsonUtil.toJson(adLandingPageMessage1));
		
		AdBaseDocumentSourceMessage adBaseMessage = new AdBaseDocumentSourceMessage();
		adBaseMessage.setAdCategory(adCategory);
		adBaseMessage.setAdxType(adxType);
		adBaseMessage.setAdPrice(8000l);
		adBaseMessage.setAreas("广东省,北京市,广东省广州市");
		adBaseMessage.setId(adId);
		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
		adBaseMessage.setNetworkType("WiFi,2G,3G,4G");
		adBaseMessage.setTerminalType(terminalType);
		adBaseMessage.setTimeZones("hour9, hour10, hour11, hour12, hour13, hour14, hour15, hour16, hour17, hour18");
		
		List<AdMaterialDocumentSourceMessage> list = new ArrayList<AdMaterialDocumentSourceMessage>();
		list.add(new AdMaterialDocumentSourceMessage(createId, materialId, materialType, imageHW, null, null));
		adBaseMessage.setAdMaterialMessageList(list);
	
		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
	}

	private void setRedisBaseMessage(Jedis jedis, Long adId,
			Map<String, Object> extendMap, int adCategory) {
		RedisAdBaseMessage redisAdBaseMessage = new RedisAdBaseMessage();
		redisAdBaseMessage.setAccountId(1l);
		redisAdBaseMessage.setAdCategory(adCategory);
		redisAdBaseMessage.setId(adId);
		redisAdBaseMessage.setName("散人私服" + adId);
		redisAdBaseMessage.setAdPackageName("com.alicall.androidzb");
		if(extendMap != null) {
			redisAdBaseMessage.setExtendMessage(JsonUtil.toJson(extendMap));
		}
		
		String adBaseRedisKey = RedisConstant.AD_BASE + adId;
		jedis.set(adBaseRedisKey, JsonUtil.toJson(redisAdBaseMessage));
	}

	private AdMaterialMessage setMaterialMessage(Jedis jedis,
			Long adId, Long createId, Long materialId, int materialType, String title, String detail, Integer h, Integer w, String url) {
		AdMaterialMessage redisAdCreateMaterialMessage = new AdMaterialMessage();
		redisAdCreateMaterialMessage.setCreateId(createId);
		redisAdCreateMaterialMessage.setMaterialId(materialId);
		redisAdCreateMaterialMessage.setMaterialType(materialType);
		redisAdCreateMaterialMessage.setTitle(title);
		redisAdCreateMaterialMessage.setDescription(detail);
		
		Map<String, AdImageMessage> mapAdImage = new HashMap<String, AdImageMessage>();
		AdImageMessage redisAdImage = new AdImageMessage();
		redisAdImage.setHeight(h);
		redisAdImage.setWidth(w);
		String imageHW = h + "-" + w;
		redisAdImage.setImageURL(url);
		mapAdImage.put(imageHW, redisAdImage);
		redisAdCreateMaterialMessage.setMapAdImage(mapAdImage);
		
		String createPackageMessageKey = RedisConstant.AD_CREATE_MATERIAL + createId + "_" + materialId;
		jedis.set(createPackageMessageKey, JsonUtil.toJson(redisAdCreateMaterialMessage));
		return redisAdCreateMaterialMessage;
	}

	private String setImage(Jedis jedis, Long adId, int h, int w,  long createId, long materialId, String url) {
		AdImageMessage redisAdImage = new AdImageMessage();
		redisAdImage.setHeight(h);
		redisAdImage.setWidth(w);
		String imageHW = h + "-" + w;
		redisAdImage.setImageURL(url);
		String imageKey = RedisConstant.AD_IMAGE + createId + "_" + materialId + "_" + imageHW;
		jedis.set(imageKey, JsonUtil.toJson(redisAdImage));
		return imageHW;
	}
	
	@Test
	public void testImage() {
		Jedis jedis = jedisPools.getJedis();
		AdMaterialMessage redisAdCreateMaterialMessage = new AdMaterialMessage();
		redisAdCreateMaterialMessage.setCreateId(1l);
		redisAdCreateMaterialMessage.setMaterialId(2l);
		redisAdCreateMaterialMessage.setMaterialType(1);
		redisAdCreateMaterialMessage.setTitle("ok");
		redisAdCreateMaterialMessage.setDescription(null);
		AdImageMessage redisAdImage = new AdImageMessage();
		redisAdImage.setHeight(50);
		redisAdImage.setWidth(50);
		String imageHW = 50 + "-" + 150;
		redisAdImage.setImageURL("atest");
		Map<String, AdImageMessage> mapAdImage = new HashMap<String, AdImageMessage>();
		mapAdImage.put(imageHW, redisAdImage);
		mapAdImage.put(imageHW + "2", redisAdImage);
		redisAdCreateMaterialMessage.setMapAdImage(mapAdImage);
		String createPackageMessageKey = RedisConstant.AD_CREATE_MATERIAL + 12 + "_" + 32;
		jedis.set(createPackageMessageKey, JsonUtil.toJson(redisAdCreateMaterialMessage));
		
		System.out.println(jedis.get(createPackageMessageKey));
		AdMaterialMessage adMaterialMessage1 = JsonUtil.fromJson(jedis.get(createPackageMessageKey), AdMaterialMessage.class);
		System.out.println(adMaterialMessage1.getMapAdImage());
		
		jedisPools.closeJedis(jedis);
	}
	
	@Test
	public void testAddAd2() {
		Long adId = 2l;
		Jedis jedis = jedisPools.getJedis();
		int adxType = 3;
		int adCategory = 1;
		//广告基本信息
		long createId = 2;
		long materialId = 2;
		String title = "散人私服 散人私服 散人私服" + adId;
//		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		int width = 160;
		int height = 120;
		int meterialType = 2;
		String imageHW = height + "-" + width;
		
		
		setRedisBaseMessage(jedis, adId, null, adCategory);
		setMaterialMessage(jedis, adId, createId, materialId, meterialType, title, null,height, width, "http://d.zhidian.cn/zhidian-smart/youdao/160x120.jpg");
		
		//落地页的选择
		AdLandpageMessage adLandingPageMessage = new AdLandpageMessage();
		adLandingPageMessage.setId(1l);
		adLandingPageMessage.setLandPageUrl("http://d.zhidian.cn/zdy6/static/srsf/16022958976.html");
		
		//落地页的选择
		AdLandpageMessage adLandingPageMessage1 = new AdLandpageMessage();
		adLandingPageMessage1.setId(2l);
		adLandingPageMessage1.setLandPageUrl("http://www.baidu2.com");
		
		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
		jedis.hset(adLandingPageKey, "2", JsonUtil.toJson(adLandingPageMessage1));
		
		AdBaseDocumentSourceMessage adBaseMessage = new AdBaseDocumentSourceMessage();
		adBaseMessage.setAdCategory(adCategory);
		adBaseMessage.setAdxType(adxType);
		adBaseMessage.setAreas("广东省");
		adBaseMessage.setId(adId);
		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
		adBaseMessage.setTerminalType(1);
		adBaseMessage.setTimeZones("hour9, hour10, hour11, hour12, hour13, hour14, hour15, hour16, hour17, hour18");
		
		List<AdMaterialDocumentSourceMessage> list = new ArrayList<AdMaterialDocumentSourceMessage>();
		list.add(new AdMaterialDocumentSourceMessage(createId, materialId, meterialType, imageHW, title.length(), null));
		adBaseMessage.setAdMaterialMessageList(list);
	
		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
	}
	
	@Test
	public void testAddAd3() {
		Long adId = 3l;
		Jedis jedis = jedisPools.getJedis();
		int adxType = 3;
		int adCategory = 1;
		//广告基本信息
		long createId = 2;
		String title = "散人私服 散人私服 散人私服" + adId;
		String detail = "散人私服 散人私服 描述 散人私服 散人私服 描述 散人私服 散人私服 描述" + adId;
//		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		
		long materialId2 = 3;
		int meterialType2 = 3;
		int width1 = 170;
		int height1 = 130;
		String imageHW2 = height1 + "-" + width1;
		setRedisBaseMessage(jedis, adId, null, adCategory);
		setMaterialMessage(jedis, adId, createId, materialId2, meterialType2, title, detail, height1, width1, "http://d.zhidian.cn/zhidian-smart/youdao/170x130.jpg");
		setImage(jedis, adId, height1, width1, createId, materialId2, "http://d.zhidian.cn/zhidian-smart/youdao/170x130.jpg");
		
		//落地页的选择
		AdLandpageMessage adLandingPageMessage = new AdLandpageMessage();
		adLandingPageMessage.setId(1l);
		adLandingPageMessage.setLandPageUrl("http://d.zhidian.cn/zdy6/static/srsf/16022958976.html");
		
		//落地页的选择
		AdLandpageMessage adLandingPageMessage1 = new AdLandpageMessage();
		adLandingPageMessage1.setId(2l);
		adLandingPageMessage1.setLandPageUrl("http://www.baidu2.com");
		
		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
		jedis.hset(adLandingPageKey, "2", JsonUtil.toJson(adLandingPageMessage1));
		
		AdBaseDocumentSourceMessage adBaseMessage = new AdBaseDocumentSourceMessage();
		adBaseMessage.setAdCategory(adCategory);
		adBaseMessage.setAdxType(adxType);
		adBaseMessage.setAreas("广东省");
		adBaseMessage.setId(adId);
		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
		adBaseMessage.setTerminalType(1);
		adBaseMessage.setTimeZones("hour9, hour10, hour11, hour12, hour13, hour14, hour15, hour16, hour17, hour18");
		
		List<AdMaterialDocumentSourceMessage> list = new ArrayList<AdMaterialDocumentSourceMessage>();
		list.add(new AdMaterialDocumentSourceMessage(createId, materialId2, meterialType2, imageHW2, title.length(), detail.length()));
		adBaseMessage.setAdMaterialMessageList(list);
	
		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
	}
	
	@Test
	public void testAddAd4() {
		Long adId = 4l;
		Jedis jedis = jedisPools.getJedis();
		int adxType = 4;
		int adCategory = 15;
		//广告基本信息
		long createId = 2;
		long materialId = 2;
		String title = "散人私服 散人私服 散人私服" + adId;
//		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		int meterialType = 4;
		
		int height = 50;
		int width = 60;
		String imageHW = height + "-" + width;
		
		int height1 = 70;
		int width1 = 80;
		String imageHW1 = height1 + "-" + width1;
		
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("adid", "4");
		map.put("adVersion", "1.0.0");
		map.put("adFileName", "test.apk");
		setRedisBaseMessage(jedis, adId, null, adCategory);
		setMaterialMessage(jedis, adId, createId, materialId, meterialType, title, null, height, width, "http://d.zhidian.cn/zhidian-smart/youdao/50x60.jpg");
		setImage(jedis, adId, height, width, createId, materialId, "http://d.zhidian.cn/zhidian-smart/youdao/50x60.jpg");
		setImage(jedis, adId, height1, width1, createId, materialId, "http://d.zhidian.cn/zhidian-smart/youdao/70x80.jpg");
		
		//落地页的选择
		AdLandpageMessage adLandingPageMessage = new AdLandpageMessage();
		adLandingPageMessage.setId(1l);
		adLandingPageMessage.setLandPageUrl("http://d.zhidian.cn/zdy6/static/srsf/16022958976.html");
		
		//落地页的选择
		AdLandpageMessage adLandingPageMessage1 = new AdLandpageMessage();
		adLandingPageMessage1.setId(2l);
		adLandingPageMessage1.setLandPageUrl("http://www.baidu2.com");
		
		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
		jedis.hset(adLandingPageKey, "2", JsonUtil.toJson(adLandingPageMessage1));
		
		AdBaseDocumentSourceMessage adBaseMessage = new AdBaseDocumentSourceMessage();
		adBaseMessage.setAdCategory(adCategory);
		adBaseMessage.setAdxType(adxType);
		adBaseMessage.setAreas("广东省");
		adBaseMessage.setId(adId);
		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
		adBaseMessage.setTerminalType(1);
		adBaseMessage.setTimeZones("hour9, hour10, hour11, hour12, hour13, hour14, hour15, hour16, hour17, hour18");
		
		List<AdMaterialDocumentSourceMessage> list = new ArrayList<AdMaterialDocumentSourceMessage>();
		list.add(new AdMaterialDocumentSourceMessage(createId, materialId, meterialType, imageHW + " " + imageHW1, title.length(), null));
		adBaseMessage.setAdMaterialMessageList(list);
	
		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
	}
	
	
	@Test
	public void testAddAd5() {
		Long adId = 5l;
		Jedis jedis = jedisPools.getJedis();
		int adxType = 4;
		int adCategory = 15;
		//广告基本信息
		long createId = 2;
		long materialId = 2;
		String title = "文字链广告" + adId;
//		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		int meterialType = 5;
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("adid", "5");
		map.put("adVersion", "1.0.0");
		map.put("adFileName", "test5.apk");
		setRedisBaseMessage(jedis, adId, null, adCategory);
		setMaterialMessage(jedis, adId, createId, materialId, meterialType, title, null, null ,null , null);
		
		//落地页的选择
		AdLandpageMessage adLandingPageMessage = new AdLandpageMessage();
		adLandingPageMessage.setId(1l);
		adLandingPageMessage.setLandPageUrl("http://d.zhidian.cn/zdy6/static/srsf/16022958976.html");
		
		//落地页的选择
		AdLandpageMessage adLandingPageMessage1 = new AdLandpageMessage();
		adLandingPageMessage1.setId(2l);
		adLandingPageMessage1.setLandPageUrl("http://www.baidu2.com");
		
		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
		jedis.hset(adLandingPageKey, "2", JsonUtil.toJson(adLandingPageMessage1));
		
		AdBaseDocumentSourceMessage adBaseMessage = new AdBaseDocumentSourceMessage();
		adBaseMessage.setAdCategory(adCategory);
		adBaseMessage.setAdxType(adxType);
		adBaseMessage.setAreas("广东省");
		adBaseMessage.setId(adId);
		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
		adBaseMessage.setTerminalType(1);
		adBaseMessage.setTimeZones("hour9, hour10, hour11, hour12, hour13, hour14, hour15, hour16, hour17, hour18");
		
		List<AdMaterialDocumentSourceMessage> list = new ArrayList<AdMaterialDocumentSourceMessage>();
		list.add(new AdMaterialDocumentSourceMessage(createId, materialId, meterialType, null, title.length(), null));
		adBaseMessage.setAdMaterialMessageList(list);
	
		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
	}
}
