package com.zhidian.ad.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhidian.ad.service.AdSearchService;
import com.zhidian.dsp.constant.DspConstant;
import com.zhidian.dsp.constant.PropertieConstant;
import com.zhidian.dsp.util.ImageUrlCache;
import com.zhidian.remote.vo.request.AdConditionParam;
import com.zhidian.remote.vo.request.AdRequestParam;
import com.zhidian.remote.vo.request.AdSlotParam;
import com.zhidian.remote.vo.request.AssetParam;
import com.zhidian.remote.vo.request.DataParam;
import com.zhidian.remote.vo.request.ImageAdTypeParam;
import com.zhidian.remote.vo.request.ImgParam;
import com.zhidian.remote.vo.request.ImpParam;
import com.zhidian.remote.vo.request.MobileParam;
import com.zhidian.remote.vo.request.NativeAdTypeParam;
import com.zhidian.remote.vo.request.TitleParam;
import com.zhidian.remote.vo.response.AdResponseEntity;
import com.zhidian.remote.vo.response.BidEntity;
import com.zhidian.remote.vo.response.ImageAdEntity;
import com.zhidian.remote.vo.response.ImpBidEntity;
import com.zhidian.remote.vo.response.NativeAdEntity;
import com.zhidian.remote.vo.response.NativeImageEntity;
import com.zhidian3g.common.util.CommonLoggerUtil;
import com.zhidian3g.common.util.DateUtil;
import com.zhidian3g.common.util.JsonUtil;
import com.zhidian3g.common.util.PropertiesUtil;
import com.zhidian3g.dsp.solr.service.SolrSearchAdService;
import com.zhidian3g.dsp.vo.ad.RedisAdBaseMessage;
import com.zhidian3g.dsp.vo.ad.RedisAdCreateMaterialMessage;
import com.zhidian3g.dsp.vo.ad.RedisAdImage;
import com.zhidian3g.dsp.vo.ad.RedisAdLandingPageMessage;
import com.zhidian3g.dsp.vo.solr.SearchAd;
import com.zhidian3g.dsp.vo.solr.SearchAdCondition;
import com.zhidian3g.dsp.vo.solr.SearchAdMateriolCondition;


/**
 * @描述: 广告处理接口
 * @版本号: V1.0 .
 */
@Service
public class AdSearchServiceImpl implements AdSearchService{
	private static Integer  ADX_ADVIEW = 2;
	private static Integer ADX_YOUDAO = 3;
	
	private static String AD_TYPE_IMAGE  = "1";//目前只支持图片广告这种创意类型
	private static String AD_TYPE_FLASH = "2";
	private static String AD_TYPE_HTML5 = "3";
	private static String AD_TYPE_NATIVE ="4";
	private static String AD_TYPE_VIEW = "5";
	private static String AD_TYPE_GIF ="6";
	
	private static String AD_CATEGORY_GAME = "1";
	
	private static String SUPPORT_SHOW_TYPE = "1,2,3,4,6,7,8";
	
	
	@Override
	public String adSearchHander(String adMessage) {
		Logger logger = LoggerFactory.getLogger("apiRequestLog");
		logger.info("======ok===adMessage:" + adMessage);
		
		AdRequestParam param = JSON.parseObject(adMessage, AdRequestParam.class);
		Integer mediaPlatformId = param.getMediaPlatformId();//1、畅言 2、adview 3、有道
		String serialNumber = param.getSerialNumber();//流水号
		String ip = param.getIp();
		Integer bidType = param.getBidType();//竞价类型
		Integer terminalType = param.getTerminalType();//终端类型
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("serialNumber", serialNumber);//序列号
		map.put("bidType", bidType);//竞价方式
		
		if(terminalType==2){//如果是pc端，直接返回广告获取不到
			map.put("isHasAd", 1);//是否获取到广告 0、获取到 1、获取不到
			
			return JSON.toJSONString(map);
		}else{
			
			//手机端
			if(ADX_YOUDAO == mediaPlatformId){//有道的话先用默认数据，再根据文档确定要给他们什么样的数据
				map.put("isHasAd", 0);
				map.put("adPrice", 10000);//广告出价
				map.put("bidType", 2);//竞价方式
				
				map.put("adType",1);//广告类型 1、图片
				
				map.put("width", 720);//宽度
				map.put("height", 100);//高度
				map.put("adCategory", 15);//广告类别 
				map.put("clickType", 1);//1、打开网站
				
				map.put("landingPage","http://sina.cn/");//广告落地页
				map.put("adMUrl",//素材地址
						"http://d.zhidian3g.cn/zhidian-smart/uploads/advertiserimages/201509/2015092217509587421.png");
				
				map.put("adPackage", "com.alicall.androidzb");//广告下载包名
				map.put("adName", "阿里通网络电话");//广告名称
				map.put("adTitle", "阿里通、通天下");//广告标题
				map.put("adSubTitle", "阿里通、通天下，让您的沟通更轻松！");//广告副标题
				map.put("iconUrl", //广告图标logo地址
						"http://d.zhidian3g.cn/zhidian-smart/uploads/appimages/201512/2015122910532026020.png");
				
//				map.put("winUrls","http://postback.nextadingtest.com/winNotice.shtml?adxId=3&mediaId=D41D8CD98F00B204E9800998ECF8427E&userId=C69A46BF899000012AE21C8C8BC01B7F&requestId=cyIdTest1&adId=1&adBlockKey=0&adSlotType=1&createId=1&landingPageId=1&height=320&width=50&requestAdDateTime=2016-03-14_13:29:07&price=4000&cyRId=cyIdTest1");//竞价成功回调
//				map.put("exposureUrls","http://postback.nextadingtest.com/adExposure.shtml?adxId=3&mediaId=D41D8CD98F00B204E9800998ECF8427E&userId=C69A46BF899000012AE21C8C8BC01B7F&requestId=cyIdTest1&adId=1&adBlockKey=0&adSlotType=1&createId=1&landingPageId=1&height=320&width=50&requestAdDateTime=2016-03-14_13:29:07&price=4000&cyRId=cyIdTest1");//曝光成功回调
//				map.put("clickUrls","http://postback.nextadingtest.com/adClick.shtml?adxId=3&mediaId=D41D8CD98F00B204E9800998ECF8427E&userId=C69A46BF899000012AE21C8C8BC01B7F&requestId=cyIdTest1&adId=1&adBlockKey=0&adSlotType=1&createId=1&landingPageId=1&height=320&width=50&requestAdDateTime=2016-03-14_13:29:07");//点击回调
//				map.put("activeUrls","htt://www.nextad.com");//激活成功回调
				
				map.put("winUrls","http://postback.nextading.com/winNotice.shtml?adxId=3&mediaId=D41D8CD98F00B204E9800998ECF8427E&userId=C69A46BF899000012AE21C8C8BC01B7F&requestId=cyIdTest1&adId=1&adBlockKey=0&adSlotType=1&createId=1&landingPageId=1&height=320&width=50&requestAdDateTime=2016-03-14_13:29:07&price=4000&cyRId=cyIdTest1");//竞价成功回调
				map.put("exposureUrls","http://postback.nextading.com/adExposure.shtml?adxId=3&mediaId=D41D8CD98F00B204E9800998ECF8427E&userId=C69A46BF899000012AE21C8C8BC01B7F&requestId=cyIdTest1&adId=1&adBlockKey=0&adSlotType=1&createId=1&landingPageId=1&height=320&width=50&requestAdDateTime=2016-03-14_13:29:07&price=4000&cyRId=cyIdTest1");//曝光成功回调
				map.put("clickUrls","http://postback.nextading.com/adClick.shtml?adxId=3&mediaId=D41D8CD98F00B204E9800998ECF8427E&userId=C69A46BF899000012AE21C8C8BC01B7F&requestId=cyIdTest1&adId=1&adBlockKey=0&adSlotType=1&createId=1&landingPageId=1&height=320&width=50&requestAdDateTime=2016-03-14_13:29:07");//点击回调
				map.put("activeUrls","htt://www.nextad.com");//激活成功回调
				
				
				String jsonData = JSON.toJSONString(map);
				return jsonData;
			}
			
			AdSlotParam adSlot= param.getAdSlot();
			AdConditionParam adCondition = param.getAdCondition();
		
			Integer width = adSlot.getWidth();
			Integer height = adSlot.getHeight();
			Integer showType = adCondition.getShowType();//广告展示形式 1、固定2、悬浮3、插屏4、开屏5、原生广告6、横幅广告7、图文8、纯文字14、视屏前15、视频中16、视频后
			String adType = adCondition.getAdType();//只允许的创意类型 1、图片 2、flash 3、html5 4、native 5、视频 6、gif
			String unSupportAdType = adCondition.getUnSupportAdType();//只允许的创意类型
			
			String adCategory = adCondition.getAdCategory();//只允许的广告行业分类
			String unSupportAdCategory = adCondition.getUnSupportAdCategory();//不允许的广告行业分类
			Integer bidMinimum = adSlot.getBidMinimum();//底价
			
			String userId = param.getUserId();
			if(userId==null){
				userId = "123";
			}
			
			//******禁止的创意类型和广告行业分类********
			if(showType==null||"".equals(showType)){
				map.put("isHasAd", 1);//是否获取到广告 0、获取到 1、获取不到
				System.out.println("没有填入对应的广告展示形式");
				return JSON.toJSONString(map);
			}
			
			if(SUPPORT_SHOW_TYPE.indexOf(showType+"")==-1){//目前支持的广告展示形式 1,2,3,4,6,7,8
				map.put("isHasAd", 1);//是否获取到广告 0、获取到 1、获取不到
				return JSON.toJSONString(map);
			}
			
			
			//******禁止的创意类型和广告行业分类********先判断创意类型和广告行业类型，如果这个条件不符合，那么不获取广告
			System.out.println("...(unSupportAdType!=null&&unSupportAdType.indexOf(AD_TYPE_IMAGE)>=0):" + ((unSupportAdType!=null&&unSupportAdType.indexOf(AD_TYPE_IMAGE)>=0)));
			System.out.println("...(unSupportAdCategory!=null&&unSupportAdCategory.indexOf(AD_CATEGORY_GAME)>=0) : " + ((unSupportAdCategory!=null&&unSupportAdCategory.indexOf(AD_CATEGORY_GAME)>=0)));
			if((unSupportAdType!=null&&unSupportAdType.indexOf(AD_TYPE_IMAGE)>=0)  || //创意类型
					(unSupportAdCategory!=null&&unSupportAdCategory.indexOf(AD_CATEGORY_GAME)>=0)){//广告行业分类
				//目前只有一个广告，是散人广告，该广告是图片广告创意类型，行业分类为游戏
				map.put("isHasAd", 1);//是否获取到广告 0、获取到 1、获取不到
				System.out.println("不允许的广告创意或广告行业分类");
				return JSON.toJSONString(map);
			}
			
			//**************如果创意展示不允许图片广告，那么不进行展示
			if(adType==null||adType.indexOf(AD_TYPE_IMAGE)==-1){
				map.put("isHasAd", 1);//是否获取到广告 0、获取到 1、获取不到
				System.out.println("不在运行的广告展示范围内");
				return JSON.toJSONString(map);
			}
			
			if((adCategory!=null)&&adCategory.indexOf(AD_CATEGORY_GAME)==-1){
				map.put("isHasAd", 1);//是否获取到广告 0、获取到 1、获取不到
				System.out.println("只允许的广告行业类型");
				return JSON.toJSONString(map);
			}
			
			//**************如果没有对应广告，那么广告获取不成功
			String key = null;
			if(ADX_ADVIEW == mediaPlatformId){
				key = "2_" + width +height;
			}else if(ADX_YOUDAO == mediaPlatformId){
				key = "3_" + width +height;
			}
			
			String imageUrl = ImageUrlCache.getInstance().byAdxIdAndWHToImageUrl(key);
			System.out.println("...key:" + key);
			if(imageUrl==null){//
				map.put("isHasAd", 1);//是否获取到广告 0、获取到 1、获取不到
				System.out.println("没有匹配到图片");
				return JSON.toJSONString(map);
			}
			
			
			
			map.put("isHasAd", 0);//是否获取到广告 0、获取到 1、获取不到
			map.put("adType",AD_TYPE_IMAGE);//广告类型 1、图片
			map.put("adCategory", AD_CATEGORY_GAME);//广告类别
			map.put("clickType", 1);//1、打开网站
			//*****新增广告id
			map.put("feeType", 1);//计费类型  1、cpm 2、cpc 3、cpa
			map.put("adId", 1);//现在默认散人的广告id是1
			
			map.put("width", width);//宽度
			map.put("height", height);//高度
			
			if(bidMinimum<=0){
				bidMinimum=20000;
			}
			map.put("adPrice", bidMinimum);//广告出价,目前出价，直接按照低价进行出价
			
			
			//目前只支持纯图片广告
			map.put("landingPage","http://d.zhidian3g.cn/zdy6/static/srsf/3/16011556969.html");//广告落地页
			map.put("adMUrl",imageUrl);//素材地址
			
			map.put("adPackage", "http://x.zhidian3g.cn/apk/game/srsf/201602/youle_16011556969_zip.apk");//广告下载包名
			map.put("adName", "散人私服");//广告名称
			
			if("7,8".indexOf(showType)>=0){//当展示类型是图文或者纯文字时，才需要填写主副标题及logo
				map.put("adTitle", "犀利爆率 全名神器");//广告标题
				map.put("adSubTitle", "重塑网游新榜单！！！");//广告副标题
				map.put("iconUrl", //广告图标logo地址
						"http://d.zhidian3g.cn/zhidian-smart/uploads/appimages/201512/2015122910532026020.png");
			}
			
			Map<String,Object> extendObjectMap = new HashMap<String, Object>();
			extendObjectMap.put("adid", 1);
			extendObjectMap.put("cid", 1);
			
			map.put("extendObject", extendObjectMap);
			
			String requestTime = DateUtil.get("yyyy-MM-dd_HH:mm:ss");
			
//			map.put("winUrls","http://postback.nextadingtest.com/winNotice.shtml?" +
//					"adxId=2&mediaId=2&userId="+userId+"&requestId="+serialNumber+"&adId=1&adSlotType=1&createId=1&landingPageId=1&height="+height+"&width="+width+"&requestAdDateTime="
//					+requetTime+"&price=%%WIN_PRICE%%");//竞价成功回调
//			map.put("exposureUrls","http://postback.nextadingtest.com/adExposure.shtml?" +
//					"adxId=2&mediaId=2&userId="+userId+"&requestId="+serialNumber+"&adId=1&adSlotType=1&createId=1&landingPageId=1&height="+height+"&width="+width+"&requestAdDateTime="
//					+requetTime+"&price=%%WIN_PRICE%%");//曝光成功回调
//			map.put("clickUrls","http://postback.nextadingtest.com/adClick.shtml?" +
//					"adxId=2&mediaId=2&userId="+userId+"&requestId="+serialNumber+"&adId=1&adSlotType=1&createId=1&landingPageId=&height="+height+"&width="+width+"&requestAdDateTime="
//					+requetTime);//点击回调
//			map.put("activeUrls","htt://www.nextad.com");//激活成功回调
			
			map.put("winUrls","http://postback.nextading.com/winNotice.shtml?" +
					"adxId=2&mediaId=2&userId="+userId+"&requestId="+serialNumber+"&adId=1&adSlotType=1&createId=1&landingPageId=1&height="+height+"&width="+width+"&requestAdDateTime="
					+requestTime+"&price=%%WIN_PRICE%%");//竞价成功回调
			map.put("exposureUrls","http://postback.nextading.com/adExposure.shtml?" +
					"adxId=2&mediaId=2&userId="+userId+"&requestId="+serialNumber+"&adId=1&adSlotType=1&createId=1&landingPageId=1&height="+height+"&width="+width+"&requestAdDateTime="
					+requestTime+"&price=%%WIN_PRICE%%");//曝光成功回调
			map.put("clickUrls","http://postback.nextading.com/adClick.shtml?" +
					"adxId=2&mediaId=2&userId="+userId+"&requestId="+serialNumber+"&adId=1&adSlotType=1&createId=1&landingPageId=&height="+height+"&width="+width+"&requestAdDateTime="
					+requestTime);//点击回调
			map.put("activeUrls","htt://www.nextad.com");//激活成功回调
			
			
			String jsonData = JSON.toJSONString(map);
			System.out.println("....jsonData:" + jsonData);
			return jsonData;
		}
	}
	
	@Resource
	private SolrSearchAdService solrSearchAdService;
	private final String H_W = "-";
	//获取到广告
	private final Integer HAS_AD = 0;
		//获取不到广告
	private final Integer HAS_NO_AD = 1;
	
	@Override
	public String adSearchHanderV1(String adMessage) {
		long startTime = System.currentTimeMillis();
		AdResponseEntity adResponseEntity = null;
		AdRequestParam param = null;
		try {
			adResponseEntity = new AdResponseEntity();
			param = JsonUtil.fromJson(adMessage, AdRequestParam.class);
			
			if(!AdHelper.checkRequestParam(param)) {
				CommonLoggerUtil.addErrQuest("=============参数验证不通过=============");
				//获取所有的广告位
				noAd(adResponseEntity, param);
				return JSON.toJSONString(adResponseEntity);
			}
			
			
			Integer bidType = param.getBidType();
			String serialNumber = param.getSerialNumber();
			adResponseEntity.setSerialNumber(serialNumber);
			//返回总的广告位广告列表
			List<ImpBidEntity> responseListImpBidEntities = new ArrayList<ImpBidEntity>();
			
			//获取请求参数第一层
			Integer adxType = param.getMediaPlatformId();
			//获取终端类型：电脑、移动端 1、移动端2、PC端
			Integer terminalType = param.getTerminalType();
			
			String ip = param.getIp();
			String userId = param.getUserId();
			String requestTime = DateUtil.get("yyyy-MM-dd_HH:mm:ss");
			
//		String userId = param.getUserId();
			MobileParam mobile = param.getMobile();
			
			//获取系统类型1、IOS  2、Android  3、windows_phone
			Integer OS = mobile.getDeviceOS();
			
			//获取所有的广告位
			List<ImpParam> listImplList = param.getImps();
			for(ImpParam impParam : listImplList) {
				//广告位id
				String impId = impParam.getImpId();
				//广告位放回对象
				ImpBidEntity impBidEntity = new ImpBidEntity();
				impBidEntity.setImpId(impId);
//			CommonLoggerUtil.addBaseLog("传过来的参数impParam：" + JsonUtil.toJson(param));
				//是否符合广告的请求判断标志
				
				//获取广告展示类型:也就是创意类型
				Integer showType = impParam.getShowType();
				
				//广告创意类型
				String adType = impParam.getAdType();
				Integer adTypeId = Integer.valueOf(adType.split(",")[0]);
				
				//竞价低价
				Long adPrice = impParam.getBidMinimum();
				String adCategory = impParam.getAdCategory();
				String unSupportAdCategory = impParam.getUnSupportAdCategory();
				
				//广告位竞价对象列表
				List<BidEntity> listBidEntities = new ArrayList<BidEntity>();
				
				//广告基本信息检索条件
				SearchAdCondition searchAdCondition = new SearchAdCondition(adxType, terminalType,
						getOSString(OS), adTypeId, ip);
				//搜索对应的行业广告
				if(StringUtils.isNotBlank(adCategory)) {
					searchAdCondition.setAdCategory(adCategory);
				}
				//不支持行业广告
				if(StringUtils.isNotBlank(unSupportAdCategory)) {
					searchAdCondition.setUnSupportAdCategory(unSupportAdCategory);
				}
				
				/**
				 * 广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
				 */
				Integer meterialType = null; 
				//广告查询条件对象
				SearchAdMateriolCondition searchAdMateriolCondition = new SearchAdMateriolCondition();
				if(adTypeId == DspConstant.AD_TYPE_NATIVE) {
					NativeAdTypeParam nativeAdTypeParam  = impParam.getNativeAdType();
					//获取竞价对象数量
					Integer bids = nativeAdTypeParam.getPlcmtcnt();
//				for(int i=0; i<bids; i++) {
//					
//				}
					
					//广告位竞价对象
					BidEntity bidEntity = null;
					//获取广告条件
					List<AssetParam> listAssetParams = nativeAdTypeParam.getAssets();
					StringBuffer imageHWs = new StringBuffer();
					boolean isHasTitle = false;
					boolean ishasDetail = false;
					boolean isWithAppName = false;
					boolean isWithPackageName = false;
					int imageCount = 0;
					for(AssetParam asseet:listAssetParams) {
						//条件是否为必须
						Integer isAdRequire = asseet.getIsRequired();
						
//					Integer no = asseet.getId();
						//如果是不必要的广告条件暂时过滤掉
//					if(isAdRequire == 0) {
//						CommonLoggerUtil.addBaseLog("序号为" + no + "为非必须广告过滤掉==");
//						continue;
//					}
						
						TitleParam titleParam = asseet.getTitle();
						//设置标题条件
						if(titleParam != null) {
							Integer len = titleParam.getLen();
							searchAdMateriolCondition.settLen(len);
							isHasTitle = true;
							continue;
						}
						
						//设置图片条件
						ImgParam imgParam = asseet.getImg();
						if(imgParam != null) {
//						Integer imageTypeId = imgParam.getTypeId();
//						String imagePix = null;
//						//获取主图片
//						if(imageTypeId == 1) {
//							imagePix = DspConstant.IMAGE_MAIN;
//						} else if(imageTypeId == 2) {
//							//获取logo图片
//							imagePix = DspConstant.IMAGE_LOGO;
//						} else if(imageTypeId == 3) {
//							imagePix = DspConstant.IMAGE_ICON;
//						}
							imageHWs.append(imgParam.getH() + H_W + imgParam.getW() + ";");
							imageCount++;
							continue;
						}
						
						DataParam dataParam = asseet.getData();
						if(dataParam != null) {
							Integer dataType = dataParam.getTypeId();
							//请求广告的描述长度限制
							if(dataType == 3) {
								ishasDetail = true;
								searchAdMateriolCondition.setdLen(dataParam.getLen());
							} else if(dataType ==1 ) {
								isWithAppName = true;
							} else if(dataType == 2) {
								isWithPackageName = true;
							}
						}
					}
					
					//设置要检索的图片条件
					String imageString = imageHWs.substring(0, imageHWs.length() -1);
					searchAdMateriolCondition.setImageHW(imageString);
					if(isHasTitle) {//有标题
						if(imageCount == 1 && !ishasDetail) {//说明是图片+标题广告
							meterialType = 2;
						} else if(imageCount == 1 && ishasDetail) {//图片+标题 +描述
							meterialType = 3;
						} else if(imageCount == 0) {
							meterialType = 5; //文字链广告
						} else {//多图片广告
							meterialType = 4;
						}
					} else {//没有标题说明是纯图片
						if(imageCount == 1) {//没有标题说明是纯图片
							meterialType = 1;
						} else {
							unHasAd(impBidEntity, bids, "=========没有标题且不是一张图片的广告===" + JsonUtil.toJson(impParam));
							responseListImpBidEntities.add(impBidEntity);
							continue;
						}
					}
					
					//填充素材类别
					searchAdMateriolCondition.setMeterialType(meterialType);
					SearchAd searchAd = solrSearchAdService.searchAdFormSolr(searchAdCondition, searchAdMateriolCondition);
					//根据条件获取不到广告
					if(searchAd == null) {
						unHasAd(impBidEntity, bids, "=根据条件获取不了广告===" + JsonUtil.toJson(impParam));
						responseListImpBidEntities.add(impBidEntity);
						continue;
					} else {//获取到相关的原生广告
						//广告基本信息
						RedisAdBaseMessage redisAdBaseMessage = searchAd.getRedisAdBaseMessage();
						//广告素材信息
						RedisAdCreateMaterialMessage adMaterialMessage = searchAd.getRedisAdCreateMaterialMessage();
						//广告落地页信息
						RedisAdLandingPageMessage adLandingPageMessage = searchAd.getAdLandingPageMessage();
						
						Long adId = redisAdBaseMessage.getAdId();
						Integer responseAdCategory = redisAdBaseMessage.getAdCategory();
						Integer createId =adMaterialMessage.getCreateId();
						
						//落地页
						String landingPage = adLandingPageMessage.getLandingPageUrl();
						Integer landingPageId = adLandingPageMessage.getId();
						
//					String apkDownloadParms = "zd_userId=" + userId + "&zd_requestId="+ serialNumber + "&zd_adId=" + adId+ "&zd_createId=" +createId + "&zd_landingPageId=" + landingPageId + "&requestAdDateTime=" + requestTime;
//					if(landingPage.contains("?")) {
//						landingPage = landingPage +  "&" + apkDownloadParms;
//					} else {
//						landingPage = landingPage +  "?" + apkDownloadParms; 
//					}
						
						//设置原生广告信息
						NativeAdEntity nativeAdEntity = new NativeAdEntity();
						nativeAdEntity.setTitle(adMaterialMessage.getTitle());
						nativeAdEntity.setLandingPage(landingPage);
						nativeAdEntity.setAdCategory(responseAdCategory);
						//广告名称
						if(isWithAppName) {
							nativeAdEntity.setAdName(redisAdBaseMessage.getAdName());
						}
						//广告包名
						if(isWithPackageName) {
							nativeAdEntity.setAdPackage(redisAdBaseMessage.getAdPackageName());
						}
						
						//图片信息填充
						if(meterialType != 4 && meterialType != 5) {
							RedisAdImage reAdImage = searchAd.getRedisAdImageMap().get(imageString);
							System.out.println(searchAd.getRedisAdImageMap() + "imageString=" + imageString);
							Map<Integer,NativeImageEntity> imagesMap = new HashMap<Integer, NativeImageEntity>();
							setImageEntity(reAdImage, imagesMap);
							nativeAdEntity.setImagesMap(imagesMap);
						} else if(meterialType == 4) {
							String[] imageArrays = imageString.split(";");
							Map<String, RedisAdImage> redisAdImageMap = searchAd.getRedisAdImageMap();
							Map<Integer,NativeImageEntity> imagesMap = new HashMap<Integer, NativeImageEntity>();
							for(String imageHW : imageArrays) {
								RedisAdImage reAdImage = redisAdImageMap.get(imageHW);
								setImageEntity(reAdImage, imagesMap);
							}
							nativeAdEntity.setImagesMap(imagesMap);
						}
						
						//扩展字段的设置
						String extendMessage = redisAdBaseMessage.getExtendMessage();
						if(extendMessage != null) {
							nativeAdEntity.setExtendObject(JsonUtil.fromJsonType(extendMessage, new TypeReference<Map<String, Object>>(){}));	
						}
						
						String commonParments = "userId=" + userId + "&requestId="+ serialNumber + "&adId=" + adId+ "&adBlockKey=" +  impId  +  "&adSlotType=" +  showType + "&createId=" +createId + "&landingPageId=" + landingPageId + "&requestAdDateTime=" + requestTime;
						Map<String, String> callBackURLMap = getCallBackURL(commonParments, adxType);
						nativeAdEntity.setWinUrls(callBackURLMap.get("adWinURL"));
						nativeAdEntity.setExposureUrls(callBackURLMap.get("adShowURL"));
						nativeAdEntity.setClickUrls(callBackURLMap.get("adClickURL"));
						
						//广告位竞价对象
						bidEntity = getBaseBidEntity(bidType, adPrice, DspConstant.NATIVE_AD_TYPE);
						bidEntity.setNativeAd(nativeAdEntity);
					}
					listBidEntities.add(bidEntity);
				} else if(adTypeId == DspConstant.AD_TYPE_IMAGE) {
					//广告位竞价对象
					BidEntity bidEntity = null;
					//目前返回一个图片广告
					//广告查询条件对象
					//主图片
					ImageAdTypeParam imageAdTypeParam = impParam.getImageAdType();
					meterialType = 1;//设置纯图片
					String imageHW = imageAdTypeParam.getHeight() + H_W + imageAdTypeParam.getWidth();
					//设置素材类型
					searchAdMateriolCondition.setMeterialType(meterialType);
					//设置图片的条件
					searchAdMateriolCondition.setImageHW(imageHW);
					SearchAd searchAd = solrSearchAdService.searchAdFormSolr(searchAdCondition, searchAdMateriolCondition);
					if(searchAd == null) {
						bidEntity = new BidEntity();
						CommonLoggerUtil.addBaseLog("根据条件获取不到图片广告=" + JsonUtil.toJson(searchAdCondition));
						bidEntity.setIsHasAd(HAS_NO_AD);
					} else {
						bidEntity = getBaseBidEntity(bidType, adPrice, DspConstant.IMAGE_TYPE);
						//缓存中的广告
						RedisAdBaseMessage redisAdBaseMessage = searchAd.getRedisAdBaseMessage();
						RedisAdLandingPageMessage adLandingPageMessage = searchAd.getAdLandingPageMessage();
						RedisAdImage redisAdImage = searchAd.getRedisAdImageMap().get(imageHW);
						Long adId = redisAdBaseMessage.getAdId();
						Integer redisAdCategory = redisAdBaseMessage.getAdCategory();
						
						ImageAdEntity imageAdEntity = new ImageAdEntity(); 
						Integer createId =redisAdBaseMessage.getAdCategory();
						int landingPageId = adLandingPageMessage.getId();
						
						//设置广告信息
						imageAdEntity.setLandingPage(adLandingPageMessage.getLandingPageUrl());
						imageAdEntity.setAdCategory(redisAdCategory);
						imageAdEntity.setClickType(redisAdBaseMessage.getClickType());
						imageAdEntity.setAdPackage(redisAdBaseMessage.getAdPackageName());
						imageAdEntity.setAdDownload(adLandingPageMessage.getLandingPageUrl());
						
						//广告图片信息
						imageAdEntity.setWidth(redisAdImage.getWidth());
						imageAdEntity.setHeight(redisAdImage.getHeight());
						imageAdEntity.setAdMUrl(redisAdImage.getImgURL());
						
						//回传地址设置
						String commonParments = "userId=" + userId + "&requestId="+ serialNumber + "&adId=" + adId
								+ "&adBlockKey=" +  impId  +  "&adSlotType=" +  showType + "&createId=" +createId 
								+ "&landingPageId=" + landingPageId + "&requestAdDateTime=" + requestTime;
						
						Map<String, String> callBackURLMap = getCallBackURL(commonParments, adxType);
						imageAdEntity.setWinUrls(callBackURLMap.get("adWinURL"));
						imageAdEntity.setExposureUrls(callBackURLMap.get("adShowURL"));
						imageAdEntity.setClickUrls(callBackURLMap.get("adClickURL"));
						
						//扩展字段的设置
						String extendMessage = redisAdBaseMessage.getExtendMessage();
						if(extendMessage != null) {
							imageAdEntity.setExtendObject(JsonUtil.fromJsonType(extendMessage, new TypeReference<Map<String, Object>>(){}));	
						}
						
						bidEntity.setImageAd(imageAdEntity);
					}
					//广告竞价对象
					listBidEntities.add(bidEntity);
				} else {
					System.out.println("==没有符合类型的广告============");
					noAd(adResponseEntity, param);
					return JSON.toJSONString(adResponseEntity);
				}
				
				//条件
				impBidEntity.setBids(listBidEntities);
				responseListImpBidEntities.add(impBidEntity);
			}
			adResponseEntity.setImpBids(responseListImpBidEntities);
			long endTime = System.currentTimeMillis();
			System.out.println("用时==" + (endTime - startTime));
			return JSON.toJSONString(adResponseEntity);
		} catch (Exception e) {
			noAd(adResponseEntity, param);
			CommonLoggerUtil.addErrQuest("=============异常获取不到广告=============");
			CommonLoggerUtil.addExceptionLog(e);
		}
		
		return null;
		
	}

	private void noAd(AdResponseEntity adResponseEntity, AdRequestParam param) {
		List<ImpParam> listImplList = param.getImps();
		//返回总的广告位广告列表
		List<ImpBidEntity> responseListImpBidEntities = new ArrayList<ImpBidEntity>();
		ImpBidEntity rImp = new ImpBidEntity();
		List<BidEntity> rBidList = new ArrayList<BidEntity>();
		BidEntity rBid= new BidEntity();
		rBid.setIsHasAd(this.HAS_NO_AD);
		rBidList.add(rBid);
		rImp.setImpId(listImplList.get(0).getImpId());
		rImp.setBids(rBidList);
		responseListImpBidEntities.add(rImp);
		adResponseEntity.setImpBids(responseListImpBidEntities);
	}

	/**
	 * 图片设置
	 * @param reAdImage
	 * @param imagesMap
	 */
	private void setImageEntity(RedisAdImage reAdImage,
			Map<Integer, NativeImageEntity> imagesMap) {
		NativeImageEntity nativeImage = new NativeImageEntity();
		nativeImage.setTypeId(1);
		nativeImage.setW(reAdImage.getWidth());
		nativeImage.setH(reAdImage.getHeight());
		nativeImage.setUrl(reAdImage.getImgURL());
		imagesMap.put(1,nativeImage);
	}


	/**
	 * 获取不到广告
	 * @param imps
	 * @param map
	 * @return
	 */
	private void unHasAd(ImpBidEntity rImp,int bidCount, String text){
		List<BidEntity> rBidList = new ArrayList<BidEntity>();
		for(int i=0;i<bidCount;i++) {
			BidEntity rBid= new BidEntity();
			rBid.setIsHasAd(HAS_NO_AD);
			rBidList.add(rBid);
		}
		rImp.setBids(rBidList);
		CommonLoggerUtil.addErrQuest(text);
	}
	
	/**
	 * 
	 * 获取媒体adx的回调地址
	 * @param commonParments
	 * @param adxType
	 * @return
	 */
	/**配置文件常量***************************************/
	private final PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
	private Map<String, String> getCallBackURL(String commonParments, int adxType) {
		String callBackPrice = propertiesUtil.getValue(PropertieConstant.CX_PRICE_TYPE + adxType);
		//赢得竞价的回调地址
		String adWinURL = PropertieConstant.AD_WIN_POST_BACK_URL+"?" + commonParments + "&price=" + callBackPrice;
		//展示回调地址
		String adShowURL = PropertieConstant.AD_SHOW_POST_BACK_URL+"?" +commonParments + "&price=" + callBackPrice;
		//点击回调地址
		String adClickURL = PropertieConstant.AD_CLICK_POST_BACK_URL+"?" + commonParments;
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("adWinURL", adWinURL);
		map.put("adShowURL", adShowURL);
		map.put("adClickURL", adClickURL);
		return map;
	}

	/**
	 * 获取系统类型
	 * @param osTypeId
	 * @return
	 */
	private String getOSString(int osTypeId) {
		if(osTypeId == 1) {
			return DspConstant.OS_IOS;
		} else if(osTypeId == 2) {
			return DspConstant.OS_ANDROID;
		} else if(osTypeId == 3) {
			return DspConstant.OS_WINDOWS;
		}
		
		return null;
	}
	
	
	/**
	 * 获取竞价对象基本信息
	 * @param bidType
	 * @param adPrice
	 * @param adType
	 * @return
	 */
	private BidEntity getBaseBidEntity(Integer bidType, Long adPrice, int adType) {
		BidEntity bidEntity = new BidEntity();
		bidEntity.setIsHasAd(HAS_AD);
		bidEntity.setBidType(bidType);
		bidEntity.setAdPrice(adPrice);
		//设置广告类型
		bidEntity.setAdType(adType);
		return bidEntity;
	}

}