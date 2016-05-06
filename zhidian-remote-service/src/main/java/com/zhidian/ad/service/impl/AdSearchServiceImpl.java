package com.zhidian.ad.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zhidian.ad.service.AdSearchService;
import com.zhidian.remote.vo.request.AdConditionParam;
import com.zhidian.remote.vo.request.AdRequestParam;
import com.zhidian.remote.vo.request.AdSlotParam;
import com.zhidian.remote.vo.request.AssetParam;
import com.zhidian.remote.vo.request.DataParam;
import com.zhidian.remote.vo.request.ImageAdTypeParam;
import com.zhidian.remote.vo.request.ImgParam;
import com.zhidian.remote.vo.request.ImpParam;
import com.zhidian.remote.vo.request.NativeAdTypeParam;
import com.zhidian.remote.vo.request.TitleParam;
import com.zhidian.remote.vo.response.BidEntity;
import com.zhidian.remote.vo.response.ImageAdEntity;
import com.zhidian.remote.vo.response.ImpBidEntity;
import com.zhidian.remote.vo.response.NativeAdEntity;
import com.zhidian.remote.vo.response.NativeImageEntity;
import com.zhidian.util.DateUtil;
import com.zhidian.util.ImageUrlCache;
import com.zhidian.util.PropertiesUtil;
import com.zhidian.util.PropertyConstant;


/**
 * @描述: 广告处理接口
 * @版本号: V1.0 .
 */
@Service
public class AdSearchServiceImpl implements AdSearchService{
	private static Integer  ADX_ADVIEW = 2;
	private static Integer ADX_YOUDAO = 3;
	private static Integer ADX_ZHANGYOU = 4;
	
	private static String AD_TYPE_IMAGE  = "1";//目前只支持图片广告这种创意类型
	private static String AD_TYPE_FLASH = "2";
	private static String AD_TYPE_HTML5 = "3";
	private static Integer AD_TYPE_NATIVE =7;
	private static String AD_TYPE_VIEW = "5";
	private static String AD_TYPE_GIF ="6";
	
	private static Integer AD_CATEGORY_GAME = 1;
	
	private static String SUPPORT_SHOW_TYPE = "1,2,3,4,5,6";
//	1	固定
//	2	悬浮
//	3	插屏 
//	4	开屏
//	5	原生广告
//	6	横幅广告
//	7	视频前贴片
//	8	视频中贴片
//	9	视频后贴片
	
	private static Integer NATIVE_IMAGE_MAIN = 1;
	private static Integer NATIVE_IMAGE_LOGO  = 2;
	private static Integer NATIVE_IMAGE_ICON = 3;

	
	
	private static Integer HAS_AD = 0;//货到到广告
	private static Integer UN_HAS_AD = 1;//获取不到广告
	
	
	Logger logger = LoggerFactory.getLogger("apiRequestLog");
	
	@Override
	public String adSearchHander(String adMessage) {
		logger.info("===-----------===ok===adMessage:" + adMessage);
		
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
				System.out.println("不在运行的广告展示范围内2");
				return JSON.toJSONString(map);
			}
			
			if((adCategory!=null&&!"".equals(adCategory))&&adCategory.indexOf(AD_CATEGORY_GAME)==-1){
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
					+requestTime+"&price=${AUCTION_PRICE}");//竞价成功回调
			map.put("exposureUrls","http://postback.nextading.com/adExposure.shtml?" +
					"adxId=2&mediaId=2&userId="+userId+"&requestId="+serialNumber+"&adId=1&adSlotType=1&createId=1&landingPageId=1&height="+height+"&width="+width+"&requestAdDateTime="
					+requestTime+"&price=${AUCTION_PRICE}");//曝光成功回调
			map.put("clickUrls","http://postback.nextading.com/adClick.shtml?" +
					"adxId=2&mediaId=2&userId="+userId+"&requestId="+serialNumber+"&adId=1&adSlotType=1&createId=1&landingPageId=&height="+height+"&width="+width+"&requestAdDateTime="
					+requestTime);//点击回调
			map.put("activeUrls","htt://www.nextad.com");//激活成功回调
			
			
			String jsonData = JSON.toJSONString(map);
			System.out.println("....jsonData:" + jsonData);
			return jsonData;
		}
	}
	
	
	@Override
	public String adSearchHanderV1(String adMessage) {
		Logger logger = LoggerFactory.getLogger("apiRequestLog");
		
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		
		
		AdRequestParam param = JSON.parseObject(adMessage, AdRequestParam.class);
		
		String serialNumber = param.getSerialNumber();//流水号
		Integer terminalType = param.getTerminalType();//1、移动端 2、pc端
		Integer mediaPlatformId = param.getMediaPlatformId();//媒体平台Id
		Integer bidType = param.getBidType();//竞价模式
		Integer feeType = param.getFeeType();//计费模式
		List<ImpParam> imps = param.getImps();//广告位
		int iNum = imps.size();
		
		map.put("serialNumber", serialNumber);
			
			
		List<ImpBidEntity> rImps = new ArrayList<ImpBidEntity>();
		ImpBidEntity rImp = null;
		for(ImpParam imp:imps){//有多少个广告位请求，就要返回多少个bid
			if(terminalType==2){//如果是pc端，直接返回广告获取不到
				rImps.add(this.unHasAd(imp,"没有pc端对应的广告可获取！"));
				continue;
			}else{
				String impId = imp.getImpId();
				Integer showType = imp.getShowType();//广告展示形式 1、固定2、悬浮3、插屏4、开屏5、原生广告6、横幅广告7、图文8、纯文字14、视屏前15、视频中16、视频后
				String adType = imp.getAdType();//只允许的创意类型 1、图片 2、flash 3、html5 4、native 5、视频 6、gif
				String unSupportAdType = imp.getUnSupportAdType();//只允许的创意类型
				String adCategory = imp.getAdCategory();//只允许的广告行业分类
				String unSupportAdCategory = imp.getUnSupportAdCategory();//不允许的广告行业分类
  				
				
				rImp = new ImpBidEntity();
  				rImp.setImpId(impId);
				
				
				Long bidMinimum = imp.getBidMinimum();//底价
				Integer adMulit = imp.getAdMulit();//一个广告位的广告条数
				if(adMulit==null) adMulit = 1;
				
				String userId = param.getUserId();
				if(userId==null){
					userId = "123";
				}
				
				
				
				//*******************************各种限制条件过滤*****************start
				
				//******禁止的创意类型和广告行业分类********
				if(showType==null||"".equals(showType)){
					rImps.add(this.unHasAd(imp,"没有填入对应的广告展示形式！"));
					continue;
				}
				
				if(SUPPORT_SHOW_TYPE.indexOf(showType+"")==-1){//目前支持的广告展示形式 1,2,3,4,6,7,8
					rImps.add(this.unHasAd(imp,".....没有对应的广告展示形式可获取！"));
					continue;
				}
				
				String unSupportAdTypeTemp = "";
				if(ADX_ADVIEW==mediaPlatformId){
					unSupportAdTypeTemp = AD_TYPE_IMAGE + "";
				}else if(ADX_YOUDAO==mediaPlatformId){
					unSupportAdTypeTemp = AD_TYPE_NATIVE+"";
				}else if(ADX_ZHANGYOU==mediaPlatformId){
					unSupportAdTypeTemp=AD_TYPE_IMAGE + "";
				}
				
				//******禁止的创意类型和广告行业分类********先判断创意类型和广告行业类型，如果这个条件不符合，那么不获取广告
				if((unSupportAdType!=null&&!"".equals(unSupportAdType)&&unSupportAdType.indexOf(unSupportAdTypeTemp)>=0)  || //创意类型
						(unSupportAdCategory!=null&&!"".equals(unSupportAdCategory)&&unSupportAdCategory.indexOf(AD_CATEGORY_GAME)>=0)){//广告行业分类
					rImps.add(this.unHasAd(imp,"不允许的广告创意或广告行业分类"));
					continue;
				}
				
				//**************如果创意展示不允许图片广告，那么不进行展示
				if(adType==null||"".equals(adType)||adType.indexOf(unSupportAdTypeTemp)==-1){
					rImps.add(this.unHasAd(imp,"不在运行的广告展示范围内1"));
					continue;
				}
				
				if((adCategory!=null&&!"".equals(adCategory))&&adCategory.indexOf(AD_CATEGORY_GAME+"")==-1){
					rImps.add(this.unHasAd(imp,"只允许的广告行业类型"));
					continue;
				}
				
				//*******************************各种限制条件过滤*****************end
				
				
				ImageAdTypeParam imageAdTypeParam = imp.getImageAdType();
				NativeAdTypeParam nativeAdTypeParam = imp.getNativeAdType();
				
				rImp = new ImpBidEntity();
				rImp.setImpId(impId);
				
				if(nativeAdTypeParam!=null){//原生广告
					
					List<BidEntity> rBids = new ArrayList<BidEntity>();
					
					
					Integer plcmtcnt =  nativeAdTypeParam.getPlcmtcnt();
					BidEntity rBid = null;
					
					
					for(int i = 0;i<plcmtcnt;i++){
						rBid = new BidEntity();
						
						List<AssetParam> assets = nativeAdTypeParam.getAssets();
						
						String tempTitle = "";
						List<String> tempDescriptions = new ArrayList<String>();
						Map<Integer,NativeImageEntity> nateivImagesMap = new HashMap<Integer, NativeImageEntity>();
						
						Integer imgParamNum = 0;//现在asses要求大于两张图片的直接返回空的广告
						
						for(AssetParam asset:assets){
//							Integer assetId = asset.getId();
//								if(asset.getIsRequiredAd()==0)continue;//如果是非必须，那么直接跳过
							
							TitleParam titleParam = asset.getTitle();
							ImgParam  imgParam = asset.getImg();
							DataParam dataParm = asset.getData();
							
							
							
							if(titleParam!=null){
								Integer len = titleParam.getLen();
								if(len > 0){
									if(len ==18){
										tempTitle = "传承动作经典，戳痛手指才过瘾的手游";
									}else if(len == 30){
										tempTitle= "传承动作经典，戳痛手指才过瘾的手游。 全名神器";
									}else if(len == 35){
										tempTitle = "传承动作经典，戳痛手指才过瘾的手游。犀利爆率 全名神器";
									}else{
										continue;
									}
								}
							}
							
							if(imgParam!=null){
								Integer w = imgParam.getW();
								Integer h = imgParam.getH();
								if(w>0&&h>0){
									imgParamNum ++;
									
									logger.info("w:" + w +",h:" + h);
									
									String key = "3_" + w +h;
									
									String imageUrl = ImageUrlCache.getInstance().byAdxIdAndWHToImageUrl(key);
									
									if(imageUrl==null)continue;
									
									NativeImageEntity nativeImage = new NativeImageEntity();
									nativeImage.setTypeId(NATIVE_IMAGE_MAIN);
									nativeImage.setW(w);
									nativeImage.setH(h);
									nativeImage.setUrl(imageUrl);
									
									nateivImagesMap.put(NATIVE_IMAGE_MAIN,nativeImage);
								}
							}
							
							if(dataParm!=null){
								Integer typeId = dataParm.getTypeId();
								Integer len = dataParm.getLen();
								String dName = dataParm.getName();
								
								if(1==typeId){
									tempDescriptions.add("com.alicall.androidzb");
								}else if(2==typeId){
									tempDescriptions.add("散人私服");
								}else if(3==typeId){
									tempDescriptions.add(returnText(len));
								}
								
							}
						}
						
						
						
						NativeAdEntity rNativeAd = new NativeAdEntity();
						
						rNativeAd.setAdCategory(AD_CATEGORY_GAME);
						rNativeAd.setClickType(1);
						rNativeAd.setLandingPage(PropertyConstant.getLandingPage(mediaPlatformId));
						rNativeAd.setAdPackage("com.alicall.androidzb");
						rNativeAd.setAdName("散人私服");
						
						String requestTime = DateUtil.get("yyyy-MM-dd_HH:mm:ss");
						
//						rNativeAd.setWinUrls("http://postback.nextading.com/winNotice.shtml?" +
//								"adxId=2&mediaId=2&userId="+userId+"&requestId="+serialNumber+"&adId=1&adSlotType=1&createId=1&landingPageId=1&requestAdDateTime="
//								+requestTime+"&price=${AUCTION_PRICE}");
//						
//						rNativeAd.setExposureUrls("http://postback.nextading.com/adExposure.shtml?" +
//								"adxId=2&mediaId=2&userId="+userId+"&requestId="+serialNumber+"&adId=1&adSlotType=1&createId=1&landingPageId=1&requestAdDateTime="
//								+requestTime+"&price=${AUCTION_PRICE}");//曝光成功回调
//						rNativeAd.setClickUrls("http://postback.nextading.com/adClick.shtml?" +
//								"adxId=2&mediaId=2&userId="+userId+"&requestId="+serialNumber+"&adId=1&adSlotType=1&createId=1&landingPageId=1&requestAdDateTime="
//								+requestTime);//点击回调
						
						rNativeAd.setWinUrls(PropertyConstant.getWin(mediaPlatformId).replace("{requestTime}", requestTime).replace("{userId}", userId).replace("{serialNumber}", serialNumber));
						rNativeAd.setExposureUrls(PropertyConstant.getShow(mediaPlatformId).replace("{requestTime}", requestTime).replace("{userId}", userId).replace("{serialNumber}", serialNumber));//曝光成功回调
						rNativeAd.setClickUrls(PropertyConstant.getClick(mediaPlatformId).replace("{requestTime}", requestTime).replace("{userId}", userId).replace("{serialNumber}", serialNumber));//点击回调
						
						rNativeAd.setTitle(tempTitle);
						for(String tempDescription:tempDescriptions){
							logger.info("tttt.......:" + tempDescription);
						}
						rNativeAd.setDescriptions(tempDescriptions.toArray(new String[tempDescriptions.size()]));
						rNativeAd.setImagesMap(nateivImagesMap);
						
						rBid.setIsHasAd(0);
						rBid.setAdPrice(bidMinimum);
						rBid.setBidType(bidType);
						rBid.setAdType(AD_TYPE_NATIVE);
						rBid.setNativeAd(rNativeAd);
						rBids.add(rBid);
					}
					
					
					
					rImp.setBids(rBids);
				}else if(imageAdTypeParam!=null){//图片广告
					
					Integer width = imageAdTypeParam.getWidth();
					Integer height = imageAdTypeParam.getHeight();
					
					String key = null;
					if(ADX_ADVIEW == mediaPlatformId){
						key = "2_" + width +height;
					}else if(ADX_YOUDAO == mediaPlatformId){
						key = "3_" + width +height;
					}else if(ADX_ZHANGYOU == mediaPlatformId){
						key = "4_" + width +height;
					}
					
					String imageUrl = ImageUrlCache.getInstance().byAdxIdAndWHToImageUrl(key);
					System.out.println("...key:" + key);
					if(imageUrl==null){//
						rImps.add(this.unHasAd(imp,"没有对应的图片广告类型可展示！"));
						continue;
					}
					
					
					
					
					ImageAdEntity rImageAdEntity = new ImageAdEntity();
					rImageAdEntity.setWidth(width);
					rImageAdEntity.setHeight(height);
					rImageAdEntity.setAdCategory(AD_CATEGORY_GAME);
					rImageAdEntity.setClickType(2);
					
					
					
					
					rImageAdEntity.setLandingPage(PropertyConstant.getLandingPage(mediaPlatformId));
					
					rImageAdEntity.setAdPackage("com.zplay.bbtan");
					
					rImageAdEntity.setAdDownload("http://x.zhidian3g.cn/apk/game/srsf/201602/youle_16011556969_zip.apk");//广告下载地址
					rImageAdEntity.setAdMUrl(imageUrl);
					
					String requestTime = DateUtil.get("yyyy-MM-dd_HH:mm:ss");
					
					rImageAdEntity.setWinUrls(PropertyConstant.getWin(mediaPlatformId).replace("{requestTime}", requestTime).replace("{userId}", userId).replace("{serialNumber}", serialNumber));
					rImageAdEntity.setExposureUrls(PropertyConstant.getShow(mediaPlatformId).replace("{requestTime}", requestTime).replace("{userId}", userId).replace("{serialNumber}", serialNumber));//曝光成功回调
					rImageAdEntity.setClickUrls(PropertyConstant.getClick(mediaPlatformId).replace("{requestTime}", requestTime).replace("{userId}", userId).replace("{serialNumber}", serialNumber));//点击回调
					
					
					
					if(ADX_ADVIEW==mediaPlatformId){
						Map<String,Object> extendObjects = new HashMap<String, Object>();
						extendObjects.put("adid", 1);
						extendObjects.put("cid", 1);
						rImageAdEntity.setExtendObject(extendObjects);
					}else if(ADX_ZHANGYOU==mediaPlatformId){
						Map<String,Object> extendObjects = new HashMap<String, Object>();
						extendObjects.put("adid", "1");
						extendObjects.put("adVersion", "1.0.0");
						extendObjects.put("adFileName", "zy007_bbtan.apk");
						rImageAdEntity.setExtendObject(extendObjects);
					}
					
					List<BidEntity> rBids = new ArrayList<BidEntity>();
					BidEntity rBid = new BidEntity();
					
					rBid.setIsHasAd(HAS_AD);
					rBid.setAdPrice(bidMinimum);
					rBid.setBidType(bidType);
					rBid.setAdType(Integer.parseInt(AD_TYPE_IMAGE));
					rBid.setImageAd(rImageAdEntity);
					
					rBids.add(rBid);
					
					rImp.setBids(rBids);
					
				}else{
					//其他，目前不支持
					rImps.add(this.unHasAd(imp,"没有对应的广告类型"));
					continue;
				}
				
				
			}
			rImps.add(rImp);
		}
		
		
		map.put("impBids", rImps);
		
		
		
		
		
		return JSON.toJSONString(map);
	}
	
	private String returnText(Integer len){
		String text = "《散人sf》是一款以奇幻异世界为背景，战法道三大职业为主角，交织争霸与冒险等经典游戏元素在内的MMORPG手游。《散人sf》在职业、地图、玩法上都高度复刻传统PK动作游戏，深度还原游戏的经典之处，把酣畅pk燃情争霸从电脑适配到移动端。在原有的装备系统上，添加进神器，翅膀，坐骑等游戏流行元素，使游戏从视觉到内容上得到双向扩充，并且着重突破移动端延迟壁垒，为玩家打造出了多人同屏竞技的自由交互环境。";
		Integer tempLen  = text.length();
		if(len == 0) return text;
		
		if(len <= tempLen){
			return text.substring(0, len);
		}else{
			return text;
		}
		
	}
	
	/**
	 * 获取不到广告
	 * @param imps
	 * @param map
	 * @return
	 */
	private ImpBidEntity unHasAd(ImpParam imp,String text){
		
		ImpBidEntity rImp = new ImpBidEntity();
		
		
		
		List<BidEntity> rBidList = new ArrayList<BidEntity>();
		
		BidEntity rBid= new BidEntity();
		rBid.setIsHasAd(this.UN_HAS_AD);
		
		rBidList.add(rBid);
		
		rImp.setImpId(imp.getImpId());
		
		
		rImp.setBids(rBidList);
			
		logger.info(text);
			
		
		return rImp;
	}

}
