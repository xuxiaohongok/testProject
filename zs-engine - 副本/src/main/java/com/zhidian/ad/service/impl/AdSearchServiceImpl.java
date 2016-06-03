package com.zhidian.ad.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import com.alibaba.fastjson.JSON;
import com.zhidian.ad.service.AdSearchService;
import com.zhidian.common.ip.IpAreaUtil;
import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.common.util.DateUtil;
import com.zhidian.common.util.JsonUtil;
import com.zhidian.common.util.PropertiesUtil;
import com.zhidian.dsp.constant.DspConstant;
import com.zhidian.dsp.constant.PropertieConstant;
import com.zhidian.dsp.log.AdOperationLogMessage;
import com.zhidian.dsp.solr.service.SolrSearchAdService;
import com.zhidian.dsp.util.LoggerUtil;
import com.zhidian.dsp.vo.ad.AdImageMessage;
import com.zhidian.dsp.vo.ad.AdLandpageMessage;
import com.zhidian.dsp.vo.ad.AdMaterialMessage;
import com.zhidian.dsp.vo.ad.RedisAdBaseMessage;
import com.zhidian.dsp.vo.solr.SearchAd;
import com.zhidian.dsp.vo.solr.SearchAdCondition;
import com.zhidian.dsp.vo.solr.SearchAdMateriolCondition;
import com.zhidian.remote.vo.request.AdRequestParam;
import com.zhidian.remote.vo.request.AssetParam;
import com.zhidian.remote.vo.request.DataParam;
import com.zhidian.remote.vo.request.ImageAdTypeParam;
import com.zhidian.remote.vo.request.ImgParam;
import com.zhidian.remote.vo.request.ImpParam;
import com.zhidian.remote.vo.request.MobileAppParam;
import com.zhidian.remote.vo.request.MobileParam;
import com.zhidian.remote.vo.request.NativeAdTypeParam;
import com.zhidian.remote.vo.request.TitleParam;
import com.zhidian.remote.vo.response.AdResponseEntity;
import com.zhidian.remote.vo.response.BidEntity;
import com.zhidian.remote.vo.response.ImageAdEntity;
import com.zhidian.remote.vo.response.ImpBidEntity;
import com.zhidian.remote.vo.response.NativeAdEntity;
import com.zhidian.remote.vo.response.NativeImageEntity;


/**
 * @描述: 广告处理接口
 * @版本号: V1.0 .
 */
@Service
public class AdSearchServiceImpl implements AdSearchService{
	@Override
	public String adSearchHander(String adMessage) {
		return "接口已失效！";
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
		//添加请求日志
		LoggerUtil.addRequestLogMessage(adMessage);
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
			if(terminalType == 2) {
				CommonLoggerUtil.addErrQuest("=============目前不支持pc端=============");
				//获取所有的广告位
				noAd(adResponseEntity, param);
				return JSON.toJSONString(adResponseEntity);
			}
			
			String ip = param.getIp();
			String ipArea = IpAreaUtil.getGGCity(ip);
			System.out.println("ipArea=" + ipArea);
			
			String userId = param.getUserId();
			String requestTime = DateUtil.get("yyyy-MM-dd_HH:mm:ss");
			
			//String userId = param.getUserId();
			MobileParam mobile = param.getMobile();
			MobileAppParam mobileAppParam =  param.getMobileApp();
			String appId = "";
			if(mobileAppParam != null) {
				appId = mobileAppParam.getAppId();
			}
			
			//获取系统类型1、IOS  2、Android  3、windows_phone
			Integer OS = mobile.getDeviceOS();
			Integer networkType = mobile.getNetworkType();
			
			//获取所有的广告位
			List<ImpParam> listImplList = param.getImps();
			for(ImpParam impParam : listImplList) {
				//广告位id
				String impId = impParam.getImpId();
				//广告位放回对象
				ImpBidEntity impBidEntity = new ImpBidEntity();
				impBidEntity.setImpId(impId);
				
				//获取广告展示类型:也就是创意类型
				Integer showType = impParam.getShowType();
				
				//广告创意类型
				String adType = impParam.getAdType();
				Integer adTypeId = Integer.valueOf(adType.split(",")[0]);
				
				//竞价低价
				Long minPrice = impParam.getBidMinimum();
				Long bidPrice = minPrice;
				if(adxType == 2) {
					bidPrice += 100;
				} else if(adxType == 4) {
					minPrice = minPrice * 100;
					bidPrice = minPrice;
				}
				
				String adCategory = impParam.getAdCategory();
				String unSupportAdCategory = impParam.getUnSupportAdCategory();
				
				//广告位竞价对象列表
				List<BidEntity> listBidEntities = new ArrayList<BidEntity>();
				
				//广告基本信息检索条件
				SearchAdCondition searchAdCondition = new SearchAdCondition(adxType, bidPrice, terminalType,
						OS, networkType, ipArea);
				
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
				searchAdMateriolCondition.setAdType(adTypeId);
				
				if(adTypeId == DspConstant.AD_TYPE_NATIVE) {
					NativeAdTypeParam nativeAdTypeParam  = impParam.getNativeAdType();
					//获取竞价对象数量
					Integer bids = nativeAdTypeParam.getPlcmtcnt();
					
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
						
//						Integer no = asseet.getId();
						//如果是不必要的广告条件暂时过滤掉
//						if(isAdRequire == 0) {
//							CommonLoggerUtil.addBaseLog("序号为" + no + "为非必须广告过滤掉==");
//							continue;
//						}
						
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
					String imageString = null;
					if(StringUtils.isNotBlank(imageHWs)) {
						imageString = imageHWs.substring(0, imageHWs.length() -1);
						searchAdMateriolCondition.setImageHW(imageString);
					}
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
						AdMaterialMessage adMaterialMessage = searchAd.getAdMaterialMessage();
						//广告落地页信息
						AdLandpageMessage adLandingPageMessage = searchAd.getAdLandingPageMessage();
						Long adId = redisAdBaseMessage.getId();
						Integer responseAdCategory = redisAdBaseMessage.getAdCategory();
						Long createId = adMaterialMessage.getCreateId();
						Long materialId = adMaterialMessage.getMaterialId();
						
						//落地页
						String landingPage = adLandingPageMessage.getLandPageUrl();
						Long landingPageId = adLandingPageMessage.getId();
							
						//设置原生广告信息
						NativeAdEntity nativeAdEntity = new NativeAdEntity();
						nativeAdEntity.setClickType(1);
						nativeAdEntity.setTitle(adMaterialMessage.getTitle());
						nativeAdEntity.setLandingPage(landingPage);
						nativeAdEntity.setAdCategory(responseAdCategory);
						
						//广告名称
						if(isWithAppName) {
							nativeAdEntity.setAdName(redisAdBaseMessage.getName());
						}
						//广告包名
						if(isWithPackageName) {
							nativeAdEntity.setAdPackage(redisAdBaseMessage.getAdPackageName());
						}
						
						//图片信息填充
						if(meterialType != 4 && meterialType != 5) {
							AdImageMessage reAdImage = searchAd.getRedisAdImageMap().get(imageString);
							Map<Integer,NativeImageEntity> imagesMap = new HashMap<Integer, NativeImageEntity>();
							setImageEntity(reAdImage, imagesMap);
							nativeAdEntity.setImagesMap(imagesMap);
						} else if(meterialType == 4) {
							String[] imageArrays = imageString.split(";");
							Map<String, AdImageMessage> redisAdImageMap = searchAd.getRedisAdImageMap();
							Map<Integer,NativeImageEntity> imagesMap = new HashMap<Integer, NativeImageEntity>();
							for(String imageHW : imageArrays) {
								AdImageMessage reAdImage = redisAdImageMap.get(imageHW);
								setImageEntity(reAdImage, imagesMap);
							}
							nativeAdEntity.setImagesMap(imagesMap);
						}
						
						//扩展字段的设置
						Map<String, Object> adxTypeAdextendMessage = null;
						//添加渠道广告扩展参数
						if(adxType == 2) {
							adxTypeAdextendMessage = new HashMap<String, Object>();
							adxTypeAdextendMessage.put("adid", adId);
							adxTypeAdextendMessage.put("cid", materialId);
							nativeAdEntity.setExtendObject(adxTypeAdextendMessage);
						} else if(adxType == 4) {
							adxTypeAdextendMessage = new HashMap<String, Object>();
							adxTypeAdextendMessage.put("adid", adId);
							nativeAdEntity.setExtendObject(adxTypeAdextendMessage);
						}
						
						String abId = adxType + "_" + appId + "_" + showType + "_" + impParam.getAdPosition();
						String commonParments = setParments(serialNumber, adxType, requestTime);
						
						Map<String, String> callBackURLMap = getCallBackURL(commonParments, adxType);
						nativeAdEntity.setWinUrls(callBackURLMap.get("adWinURL"));
						nativeAdEntity.setExposureUrls(callBackURLMap.get("adShowURL"));
						nativeAdEntity.setClickUrls(callBackURLMap.get("adClickURL"));
						
						//广告位竞价对象
						bidEntity = getBaseBidEntity(bidType, minPrice, DspConstant.NATIVE_AD_TYPE);
						bidEntity.setNativeAd(nativeAdEntity);
						
						Integer biddingType = redisAdBaseMessage.getBiddingType();
						setAdReveiceLog(serialNumber, adxType, appId, ip, ipArea,
								userId, requestTime, minPrice, bidPrice,
								imageString, adId, redisAdBaseMessage.getAccountId(), createId, materialId, landingPageId, abId,
								biddingType, OS);
						
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
						bidEntity.setIsHasAd(HAS_NO_AD);
					} else {
						bidEntity = getBaseBidEntity(bidType, minPrice, DspConstant.IMAGE_TYPE);
						
						//缓存中的广告基本信息
						RedisAdBaseMessage redisAdBaseMessage = searchAd.getRedisAdBaseMessage();
						//落地页信息
						AdLandpageMessage adLandingPageMessage = searchAd.getAdLandingPageMessage();
						AdImageMessage redisAdImage = searchAd.getRedisAdImageMap().get(imageHW);
						AdMaterialMessage adMaterialMessage = searchAd.getAdMaterialMessage();
						
						Long adId = redisAdBaseMessage.getId();
						Integer redisAdCategory = redisAdBaseMessage.getAdCategory();
						
						ImageAdEntity imageAdEntity = new ImageAdEntity(); 
						Long landingPageId = adLandingPageMessage.getId();
						
						//设置广告信息
						imageAdEntity.setLandingPage(adLandingPageMessage.getLandPageUrl());
						imageAdEntity.setAdCategory(redisAdCategory);
						imageAdEntity.setClickType(1);
						imageAdEntity.setAdPackage(redisAdBaseMessage.getAdPackageName());
						imageAdEntity.setAdDownload(adLandingPageMessage.getLandPageUrl());
						
						//广告图片信息
						imageAdEntity.setWidth(redisAdImage.getWidth());
						imageAdEntity.setHeight(redisAdImage.getHeight());
						imageAdEntity.setAdMUrl(PropertieConstant.IMAGES_SERVER + redisAdImage.getImageURL());
						
						Long materialId = adMaterialMessage.getMaterialId();
						Long createId = adMaterialMessage.getCreateId();
						
						//渠道媒体的广告位标识
						String abId = adxType + "_" + appId + "_" + showType + "_" + impParam.getAdPosition();
						Integer biddingType = redisAdBaseMessage.getBiddingType();
						
						//回传地址设置
						String commonParments = setParments(serialNumber, adxType, requestTime);
						
						Map<String, String> callBackURLMap = getCallBackURL(commonParments, adxType);
						imageAdEntity.setWinUrls(callBackURLMap.get("adWinURL"));
						imageAdEntity.setExposureUrls(callBackURLMap.get("adShowURL"));
						imageAdEntity.setClickUrls(callBackURLMap.get("adClickURL"));
						
						
						Map<String, Object> adxTypeAdextendMessage = null;
						//添加渠道广告扩展参数
						if(adxType == 2) {
							adxTypeAdextendMessage = new HashMap<String, Object>();
							adxTypeAdextendMessage.put("adid", adId);
							adxTypeAdextendMessage.put("cid", materialId);
							imageAdEntity.setExtendObject(adxTypeAdextendMessage);
						} else if(adxType == 4) {
							adxTypeAdextendMessage = new HashMap<String, Object>();
							adxTypeAdextendMessage.put("adid", adId);
							imageAdEntity.setExtendObject(adxTypeAdextendMessage);
						}
						
						//扩展字段的设置
//						String extendMessage = redisAdBaseMessage.getExtendMessage();
//						if(extendMessage != null) {
//							imageAdEntity.setExtendObject(JsonUtil.fromJsonType(extendMessage, new TypeReference<Map<String, Object>>(){}));	
//						}
						
						bidEntity.setImageAd(imageAdEntity);
						setAdReveiceLog(serialNumber, adxType, appId, ip, ipArea, userId, requestTime, minPrice, bidPrice, 
								imageHW, adId, redisAdBaseMessage.getAccountId(), createId, 
								materialId, landingPageId, abId, biddingType, OS);
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
		
		return JSON.toJSONString(adResponseEntity);
		
	}
	
	private String setParments(String serialNumber, Integer adxType, String requestTime) {
		return "rid=" + serialNumber + "&at=" + adxType  + "&rat=" + requestTime;
	}

//	private String setParments(Integer bidType, String serialNumber,
//			Integer adxType, String userId, String requestTime, String appId,
//			Long minPrice, Long bidPrice, String imageString,Long acId, Long adId,
//			Long createId, Long materialId, Long landingPageId, String abId) {
//		return "rId=" + serialNumber + "&uId="+ userId + "&hws="  
//								+ imageString + "&acId=" + acId + "&aId=" +  adId + "&cId=" +  createId 
//								+ "&mId=" + materialId + "&lId=" + landingPageId
//				 				+ "&chId=" + adxType + "&mdId=" + appId  + "&fT=" + bidType
//				 				+ "&mP=" + minPrice + "&bP=" + bidPrice + "&abId=" + abId
//				 				+ "&rat=" + requestTime + "&rt=" + requestTime;
//	}

	private JedisPools jedisPools = JedisPools.getInstance();
	//设置广告获取日志
	private void setAdReveiceLog(String serialNumber, Integer adxType, String appId,
			String ip, String ipArea, String userId, String requestTime,
			Long minPrice, Long bidPrice, String imageString, Long adId, Long acId,
			Long createId, Long materialId, Long lId, String abId, Integer biddingType, Integer OS) {
		
		AdOperationLogMessage adOperationLogMessage = new AdOperationLogMessage(DspConstant.LOG_RECEIVE_CODE);
		adOperationLogMessage.setRid(serialNumber);
		adOperationLogMessage.setUid(userId);
		adOperationLogMessage.setHws(imageString);
		adOperationLogMessage.setAid(adId);
		adOperationLogMessage.setCid(createId);
		adOperationLogMessage.setMid(materialId);
		adOperationLogMessage.setLid(lId);
		adOperationLogMessage.setChid(adxType);
		adOperationLogMessage.setMdid(appId);
		adOperationLogMessage.setFt(biddingType);
		adOperationLogMessage.setMp(minPrice);
		adOperationLogMessage.setBp(bidPrice);
		
		//媒体广告位的表示
		adOperationLogMessage.setAbid(abId);
		adOperationLogMessage.setRat(requestTime.replace("_", " "));
		adOperationLogMessage.setRt(requestTime.replace("_", " "));
		adOperationLogMessage.setAa(ipArea);
		adOperationLogMessage.setIp(ip);
		adOperationLogMessage.setOs(OS);
		adOperationLogMessage.setAcid(acId);
		System.out.println(adOperationLogMessage);
		LoggerUtil.addAdReceiveLogMessage(adOperationLogMessage);
		
		Jedis jedis = jedisPools.getJedis();
		try {
			adOperationLogMessage.setLogcode(null);
			adOperationLogMessage.setRt(null);
			jedis.setex(adxType + "-" + serialNumber, PropertieConstant.AD_VALID_TIME, JsonUtil.toJson(adOperationLogMessage));
		} catch (Exception e) {
			jedisPools.exceptionBroken(jedis);
			CommonLoggerUtil.addExceptionLog(e);
		}
		jedisPools.closeJedis(jedis);
		
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
		CommonLoggerUtil.addBaseLog("==获取不到广告========");
	}

	/**
	 * 图片设置
	 * @param reAdImage
	 * @param imagesMap
	 */
	private void setImageEntity(AdImageMessage reAdImage,
			Map<Integer, NativeImageEntity> imagesMap) {
		NativeImageEntity nativeImage = new NativeImageEntity();
		nativeImage.setTypeId(1);
		nativeImage.setW(reAdImage.getWidth());
		nativeImage.setH(reAdImage.getHeight());
		nativeImage.setUrl(PropertieConstant.IMAGES_SERVER + reAdImage.getImageURL());
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
		String adWinURL = PropertieConstant.AD_WIN_POST_BACK_URL+"?" + commonParments + "&cp=" + callBackPrice;
		//展示回调地址
		String adShowURL = PropertieConstant.AD_SHOW_POST_BACK_URL+"?" +commonParments + "&cp=" + callBackPrice;
		//点击回调地址
		String adClickURL = PropertieConstant.AD_CLICK_POST_BACK_URL+"?" + commonParments;
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("adWinURL", adWinURL);
		map.put("adShowURL", adShowURL);
		map.put("adClickURL", adClickURL);
		return map;
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