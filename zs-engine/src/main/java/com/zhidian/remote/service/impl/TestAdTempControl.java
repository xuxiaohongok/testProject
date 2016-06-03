//package com.zhidian.remote.service.impl;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import redis.clients.jedis.Jedis;
//import com.zhidian.common.redisClient.JedisPools;
//import com.zhidian.common.util.JsonUtil;
//import com.zhidian.dsp.constant.DspConstant;
//import com.zhidian.dsp.constant.RedisConstant;
//import com.zhidian.dsp.solr.documentmanager.AdDspDocumentManager;
//import com.zhidian.dsp.solr.service.impl.SolrDMIAdServiceImpl;
//import com.zhidian.dsp.solr.vo.AdBaseDocumentSourceMessage;
//import com.zhidian.dsp.vo.ad.RedisAdBaseMessage;
//import com.zhidian.dsp.vo.adcontrol.AdMaterialDocumentMessage;
//import com.zhidian.remote.service.Condition;
//
//public class TestAdTempControl {
//
//	private static JedisPools jedisPools = JedisPools.getInstance();
//	
//	public static void testDelAll() {
//		AdDspDocumentManager.getInstance().deleteAllDocument();
//		jedisPools.getJedis().flushDB();
//	}
//	
//	public static void testAddAd(Condition condition) {
////		adxType4 AND terminalType1 AND os-ios AND adType1 AND 9-00
//		Jedis jedis = jedisPools.getJedis();
//		//广告基本信息
//		int adCategory = 15;
//		int adType = 1;
//		int terminalType = 1;
//		
//		Long adId = condition.getAdId();
//		int height = condition.getHeight();
//		int width = condition.getWidth();
//		String imageURL = condition.getImageURL();
//		int adxType = condition.getAdxType();
////		Map<String,Object> map = new HashMap<String, Object>();
////		map.put("adid", "1");
////		map.put("adVersion", "1.0.0");
////		map.put("adFileName", "zy007_bbtan.apk");
//		
//		setRedisBaseMessage(jedis, adId, null, adCategory);
//		
//		int createId = 1;
//		int materialId = 1;
//		int materialType = 1;
////		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
//		setMaterialMessage(jedis, adId, createId, materialId, materialType, null, null);
//		
//		//设置图片
//		String imageHW = setImage(jedis,  adId, height,  width,  createId,  materialId, imageURL);
//		
//		//落地页的选择
//		RedisAdLandingPageMessage adLandingPageMessage = new RedisAdLandingPageMessage();
//		adLandingPageMessage.setId(1);
//		adLandingPageMessage.setLandingPageUrl("http://d.zhidian.cn/zdy6/static/srsf/16022958976.html");
//		
//		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
//		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
//		
//		AdBaseDocumentSourceMessage adBaseMessage = new AdBaseDocumentSourceMessage();
//		adBaseMessage.setAdCategory(adCategory);
//		adBaseMessage.setAdxType(adxType);
//		adBaseMessage.setAreas("广东省");
//		adBaseMessage.setId(adId);
//		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
//		adBaseMessage.setTerminalType(terminalType);
//		adBaseMessage.setTimeZones("9-00, 10-00, 11-00, 12-00, 13-00, 14-00, 15-00, 16-00, 17-00, 18-00");
//		
//		List<AdMaterialDocumentSourceMessage> list = new ArrayList<AdMaterialDocumentSourceMessage>();
//		list.add(new AdMaterialDocumentSourceMessage(createId, materialId, materialType, imageHW, null, null));
//		adBaseMessage.setAdMaterialMessageList(list);
//	
//		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
//		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
//	}
//
//	private static void setRedisBaseMessage(Jedis jedis, Long adId,
//			Map<String, Object> extendMap, int adCategory) {
//		RedisAdBaseMessage redisAdBaseMessage = new RedisAdBaseMessage();
//		redisAdBaseMessage.setAdCategory(adCategory);
//		redisAdBaseMessage.setAdId(adId);
//		redisAdBaseMessage.setAdName("散人私服" + adId);
//		redisAdBaseMessage.setAdPackageName("com.alicall.androidzb");
//		redisAdBaseMessage.setAdPrice(8000);
//		redisAdBaseMessage.setClickType(1);
//		if(extendMap != null) {
//			redisAdBaseMessage.setExtendMessage(JsonUtil.toJson(extendMap));
//		}
//		
//		String adBaseRedisKey = RedisConstant.AD_BASE + adId;
//		jedis.set(adBaseRedisKey, JsonUtil.toJson(redisAdBaseMessage));
//	}
//
//	private static com.zhidian.dsp.vo.ad.AdMaterialMessage setMaterialMessage(Jedis jedis,
//			Long adId, int createId, int materialId, int materialType, String title, String detail) {
//		adma redisAdCreateMaterialMessage = new RedisAdCreateMaterialMessage();
//		redisAdCreateMaterialMessage.setCreateId(createId);
//		redisAdCreateMaterialMessage.setMeterialId(materialId);
//		redisAdCreateMaterialMessage.setMeterialType(materialType);
//		redisAdCreateMaterialMessage.setTitle(title);
//		redisAdCreateMaterialMessage.setDetail(detail);
//		String createPackageMessageKey = RedisConstant.AD_CREATE_MATERIAL + adId + "_" + createId + "_" + materialId;
//		jedis.set(createPackageMessageKey, JsonUtil.toJson(redisAdCreateMaterialMessage));
//		return redisAdCreateMaterialMessage;
//	}
//
//	private static String setImage(Jedis jedis, Long adId,int h, int w,  int createId, int materialId, String url) {
//		AdImage redisAdImage = new AdImage();
//		redisAdImage.setHeight(h);
//		redisAdImage.setWidth(w);
//		String imageHW = h + "-" + w;
//		redisAdImage.setImgURL(url);
//		String imageKey = RedisConstant.AD_IMAGE + adId + "_" + createId + "_" + materialId + "_" + imageHW;
//		jedis.set(imageKey, JsonUtil.toJson(redisAdImage));
//		return imageHW;
//	}
//	
//	public static void testAddAd2() {
//		Long adId = 2l;
//		Jedis jedis = jedisPools.getJedis();
//		int adxType = 3;
//		int adCategory = 1;
//		//广告基本信息
//		int createId = 2;
//		int materialId = 2;
//		String title = "散人私服 散人私服 散人私服" + adId;
////		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
//		int width = 160;
//		int height = 120;
//		int meterialType = 2;
//		String imageHW = height + "-" + width;
//		
//		
//		setRedisBaseMessage(jedis, adId, null, adCategory);
//		setMaterialMessage(jedis, adId, createId, materialId, meterialType, title, null);
//		setImage(jedis, adId, height, width, createId, materialId, "http://d.zhidian.cn/zhidian-smart/youdao/160x120.jpg");
//		
//		//落地页的选择
//		RedisAdLandingPageMessage adLandingPageMessage = new RedisAdLandingPageMessage();
//		adLandingPageMessage.setId(1);
//		adLandingPageMessage.setLandingPageUrl("http://d.zhidian.cn/zdy6/static/srsf/16022958976.html");
//		
//		//落地页的选择
//		RedisAdLandingPageMessage adLandingPageMessage1 = new RedisAdLandingPageMessage();
//		adLandingPageMessage1.setId(2);
//		adLandingPageMessage1.setLandingPageUrl("http://www.baidu2.com");
//		
//		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
//		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
//		jedis.hset(adLandingPageKey, "2", JsonUtil.toJson(adLandingPageMessage1));
//		
//		AdBaseDocumentSourceMessage adBaseMessage = new AdBaseDocumentSourceMessage();
//		adBaseMessage.setAdCategory(adCategory);
//		adBaseMessage.setAdxType(adxType);
//		adBaseMessage.setAreas("广东省");
//		adBaseMessage.setId(adId);
//		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
//		adBaseMessage.setTerminalType(1);
//		adBaseMessage.setTimeZones("09-00, 10-00, 11-00, 12-00, 13-00,14-00, 15-00, 16-00, 17-00, 18-00");
//		
//		List<AdMaterialDocumentSourceMessage> list = new ArrayList<AdMaterialDocumentSourceMessage>();
//		list.add(new AdMaterialDocumentSourceMessage(createId, materialId, meterialType, imageHW, title.length(), null));
//		adBaseMessage.setAdMaterialMessageList(list);
//	
//		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
//		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
//	}
//	
//	public static void testAddAd3() {
//		Long adId = 3l;
//		Jedis jedis = jedisPools.getJedis();
//		int adxType = 3;
//		int adCategory = 1;
//		//广告基本信息
//		int createId = 2;
//		String title = "散人私服 散人私服 散人私服" + adId;
//		String detail = "散人私服 散人私服 描述 散人私服 散人私服 描述 散人私服 散人私服 描述" + adId;
////		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
//		
//		int materialId2 = 3;
//		int meterialType2 = 3;
//		int width1 = 170;
//		int height1 = 130;
//		String imageHW2 = height1 + "-" + width1;
//		setRedisBaseMessage(jedis, adId, null, adCategory);
//		setMaterialMessage(jedis, adId, createId, materialId2, meterialType2, title, detail);
//		setImage(jedis, adId, height1, width1, createId, materialId2, "http://d.zhidian.cn/zhidian-smart/youdao/170x130.jpg");
//		
//		//落地页的选择
//		RedisAdLandingPageMessage adLandingPageMessage = new RedisAdLandingPageMessage();
//		adLandingPageMessage.setId(1);
//		adLandingPageMessage.setLandingPageUrl("http://d.zhidian.cn/zdy6/static/srsf/16022958976.html");
//		
//		//落地页的选择
//		RedisAdLandingPageMessage adLandingPageMessage1 = new RedisAdLandingPageMessage();
//		adLandingPageMessage1.setId(2);
//		adLandingPageMessage1.setLandingPageUrl("http://www.baidu2.com");
//		
//		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
//		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
//		jedis.hset(adLandingPageKey, "2", JsonUtil.toJson(adLandingPageMessage1));
//		
//		AdBaseDocumentSourceMessage adBaseMessage = new AdBaseDocumentSourceMessage();
//		adBaseMessage.setAdCategory(adCategory);
//		adBaseMessage.setAdxType(adxType);
//		adBaseMessage.setAreas("广东省");
//		adBaseMessage.setId(adId);
//		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
//		adBaseMessage.setTerminalType(1);
//		adBaseMessage.setTimeZones("09-00, 10-00, 11-00, 12-00, 13-00,14-00, 15-00, 16-00, 17-00, 18-00");
//		
//		List<AdMaterialDocumentSourceMessage> list = new ArrayList<AdMaterialDocumentSourceMessage>();
//		list.add(new AdMaterialDocumentSourceMessage(createId, materialId2, meterialType2, imageHW2, title.length(), detail.length()));
//		adBaseMessage.setAdMaterialMessageList(list);
//	
//		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
//		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
//	}
//	
//	public static void testAddAd4() {
//		Long adId = 4l;
//		Jedis jedis = jedisPools.getJedis();
//		int adxType = 4;
//		int adCategory = 15;
//		//广告基本信息
//		int createId = 2;
//		int materialId = 2;
//		String title = "散人私服 散人私服 散人私服" + adId;
////		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
//		int meterialType = 4;
//		
//		int height = 50;
//		int width = 60;
//		String imageHW = height + "-" + width;
//		
//		int height1 = 70;
//		int width1 = 80;
//		String imageHW1 = height1 + "-" + width1;
//		
//		
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("adid", "4");
//		map.put("adVersion", "1.0.0");
//		map.put("adFileName", "test.apk");
//		setRedisBaseMessage(jedis, adId, null, adCategory);
//		setMaterialMessage(jedis, adId, createId, materialId, meterialType, title, null);
//		setImage(jedis, adId, height, width, createId, materialId, "http://d.zhidian.cn/zhidian-smart/youdao/50x60.jpg");
//		setImage(jedis, adId, height1, width1, createId, materialId, "http://d.zhidian.cn/zhidian-smart/youdao/70x80.jpg");
//		
//		//落地页的选择
//		RedisAdLandingPageMessage adLandingPageMessage = new RedisAdLandingPageMessage();
//		adLandingPageMessage.setId(1);
//		adLandingPageMessage.setLandingPageUrl("http://d.zhidian.cn/zdy6/static/srsf/16022958976.html");
//		
//		//落地页的选择
//		RedisAdLandingPageMessage adLandingPageMessage1 = new RedisAdLandingPageMessage();
//		adLandingPageMessage1.setId(2);
//		adLandingPageMessage1.setLandingPageUrl("http://www.baidu2.com");
//		
//		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
//		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
//		jedis.hset(adLandingPageKey, "2", JsonUtil.toJson(adLandingPageMessage1));
//		
//		AdBaseDocumentSourceMessage adBaseMessage = new AdBaseDocumentSourceMessage();
//		adBaseMessage.setAdCategory(adCategory);
//		adBaseMessage.setAdxType(adxType);
//		adBaseMessage.setAreas("广东省");
//		adBaseMessage.setId(adId);
//		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
//		adBaseMessage.setTerminalType(1);
//		adBaseMessage.setTimeZones("09-00, 10-00, 11-00, 12-00, 13-00,14-00, 15-00, 16-00, 17-00, 18-00");
//		
//		List<AdMaterialDocumentSourceMessage> list = new ArrayList<AdMaterialDocumentSourceMessage>();
//		list.add(new AdMaterialDocumentSourceMessage(createId, materialId, meterialType, imageHW + " " + imageHW1, title.length(), null));
//		adBaseMessage.setAdMaterialMessageList(list);
//	
//		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
//		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
//	}
//	
//	
//	public static void statictestAddAd5() {
//		Long adId = 5l;
//		Jedis jedis = jedisPools.getJedis();
//		int adxType = 4;
//		int adCategory = 15;
//		//广告基本信息
//		int createId = 2;
//		int materialId = 2;
//		String title = "文字链广告" + adId;
////		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
//		int meterialType = 5;
//		
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("adid", "5");
//		map.put("adVersion", "1.0.0");
//		map.put("adFileName", "test5.apk");
//		setRedisBaseMessage(jedis, adId, null, adCategory);
//		setMaterialMessage(jedis, adId, createId, materialId, meterialType, title, null);
//		
//		//落地页的选择
//		RedisAdLandingPageMessage adLandingPageMessage = new RedisAdLandingPageMessage();
//		adLandingPageMessage.setId(1);
//		adLandingPageMessage.setLandingPageUrl("http://d.zhidian.cn/zdy6/static/srsf/16022958976.html");
//		
//		//落地页的选择
//		RedisAdLandingPageMessage adLandingPageMessage1 = new RedisAdLandingPageMessage();
//		adLandingPageMessage1.setId(2);
//		adLandingPageMessage1.setLandingPageUrl("http://www.baidu2.com");
//		
//		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
//		jedis.hset(adLandingPageKey, "1", JsonUtil.toJson(adLandingPageMessage));
//		jedis.hset(adLandingPageKey, "2", JsonUtil.toJson(adLandingPageMessage1));
//		
//		AdBaseDocumentSourceMessage adBaseMessage = new AdBaseDocumentSourceMessage();
//		adBaseMessage.setAdCategory(adCategory);
//		adBaseMessage.setAdxType(adxType);
//		adBaseMessage.setAreas("广东省");
//		adBaseMessage.setId(adId);
//		adBaseMessage.setOsPlatform(DspConstant.OS_ANDROID + " " + DspConstant.OS_IOS);
//		adBaseMessage.setTerminalType(1);
//		adBaseMessage.setTimeZones("09-00, 10-00, 11-00, 12-00, 13-00,14-00, 15-00, 16-00, 17-00, 18-00");
//		
//		List<AdMaterialDocumentSourceMessage> list = new ArrayList<AdMaterialDocumentSourceMessage>();
//		list.add(new AdMaterialDocumentSourceMessage(createId, materialId, meterialType, null, title.length(), null));
//		adBaseMessage.setAdMaterialMessageList(list);
//	
//		SolrDMIAdServiceImpl solrDMIAdServiceImpl = new SolrDMIAdServiceImpl();
//		solrDMIAdServiceImpl.addAdToSolr(adBaseMessage);
//	}
//}
