package com.zhidian3g.nextad.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.alibaba.fastjson.JSON;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidRequest;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidRequest.AdSlot;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidRequest.AdSlot.Position;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidRequest.Mobile;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidResponse;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidResponse.Ad;
import com.sohu.advert.rtb.proto.ChangyanRtb.BidResponse.Ad.ExposureUrl;
import com.zhidian3g.common.constant.PropertyConstant;
import com.zhidian3g.common.constant.RedisConstant;
import com.zhidian3g.common.utils.AdUtil;
import com.zhidian3g.common.utils.DateUtil;
import com.zhidian3g.common.utils.LoggerUtil;
import com.zhidian3g.nextad.domain.AdAllMessage;
import com.zhidian3g.nextad.domain.AdMessage;
import com.zhidian3g.nextad.domain.AdSlotMessage;
import com.zhidian3g.nextad.redisClient.JedisPools;

/**
 * @author Administrator
 *
 */
@Controller
public class CYRquestAction {
	
	private JedisPools jedisPools = JedisPools.getInstance();
	private final Integer HAS_AD = 1;
	private final Integer HAS_NO_AD = 0;
	@RequestMapping(value = "/cyDspTest.shtml", method=RequestMethod.POST, consumes="application/octet-stream")
	public void cyRequestAd(HttpServletRequest rq, HttpServletResponse response) {
		Long time = System.currentTimeMillis();
		try {
			InputStream inputStream = rq.getInputStream();  
			BidRequest  bidRequest  = BidRequest.parseFrom(inputStream);  
			boolean isMobile = bidRequest.hasMobile();
			
			String requestTime = DateUtil.get("yyyy-MM-dd_HH:mm:ss");
			//广告请求唯一id
			String id = bidRequest.getId();
			//dsp保存的对应畅言用户id
			
			String cyUserId = bidRequest.getChangyanId().getChangyanUserId();;//bidRequest.getCyUserId();
//			System.out.println("cyUserId=" + cyUserId);
//			System.out.println("bidRequest=" + bidRequest);
			
			//判断是否是pc端,直接过滤
			if(!isMobile) {
				pcLog(response, inputStream, cyUserId, id);
				return;
				//移动端
			} else {
				int number = bidRequest.getMobile().getPlatform().getNumber();
				if(number != 2) {
					//过滤掉不是安卓平台的
					System.out.println("=====不是安卓平台========" + number);
					return;
				} 
				
				List<AdSlot> adSlotList = bidRequest.getAdslotList();
				//获取广告位信息
				Integer adSlotType = null;
				//广告位的宽度
				Integer width = null;
				//广告位的高度
				Integer height = null;
				// 发布商设置的底价，1元=10000单位
				Integer mimimumCpm = null;
				Integer positionNumber = null; 
				Long adBlockKey = null;
				String url = bidRequest.getUrl();
				//创意类型集合信息
				List<Integer> creativeTypeList = null;
				if(adSlotList != null && adSlotList.size() != 0) {
					AdSlot adSlot = adSlotList.get(0);
					adSlotType = adSlot.getAdslotType();
					
					width = adSlot.getWidth();
					height = adSlot.getHeight();
					// 发布商设置的底价，1元=10000单位
					mimimumCpm = adSlot.getMinimumCpm();
					if(adSlotType == null || (adSlotType != 1 && adSlotType != 13)) {
//						noHaveAd(response, inputStream, cyUserId, id, height, width,  mimimumCpm, "广告展示类型不是浮层、banner获取不到相应的广告adSlotType=" + adSlotType);
						noHaveAd(response, inputStream, cyUserId, id, url, height, width,  mimimumCpm, "", requestTime);
						return;
					} 
					
					//创意类型集合信息
					creativeTypeList = adSlot.getCreativeTypeList();
					Position  position  = adSlot.getPosition();
					positionNumber = position.getNumber();
					adBlockKey = adSlot.getAdBlockKey();
				}  else {
					noHaveAd(response, inputStream, cyUserId, id, url, 0, 0,  mimimumCpm, "", requestTime);
					return;
				}
				
				//如果创意类型不是图片类型
				if(!creativeTypeList.contains(0)) {
					//说明是html 代码段
//					noHaveAd(response, inputStream, cyUserId, id, height, width,  mimimumCpm, "创意类型不是图片类型获取不到相应的广告");
					noHaveAd(response, inputStream, cyUserId, id, url, height, width,  mimimumCpm, "", requestTime);
					return;
				} 
				
				AdAllMessage adAllMessage = getAdMessage(cyUserId, adSlotType);
				if(adAllMessage == null) {
//					noHaveAd(response, inputStream, cyUserId, id, height, width,  mimimumCpm,  "该用户获取不到广告, 广告位adSlotType=" + adSlotType);
					noHaveAd(response, inputStream, cyUserId, id, url,height, width,  mimimumCpm,  "", requestTime);
					return;
				}
				
				//广告用户ip
				String ip = bidRequest.getIp();
				
//				.setPackageName("com.kuzhuan")//暂时不需要
				//banner 浮层 320*50图片 
				//素材地址
				String adm = adAllMessage.getAdm();
				//广告类别
				Integer adCategory = adAllMessage.getAdCategory();
				
				//默认用低价竞价
				Integer maxCpm = mimimumCpm;
				String landingPageId = adAllMessage.getLandingPageId();
				String createId = adAllMessage.getCreateId();
				String adId = adAllMessage.getAdId();
				
				//落地页
				String landingPage = adAllMessage.getLandingPage();
				String apkDownloadParms = "zd_mediaId=" + AdUtil.getNameToMd5(url) + "&zd_userId=" + cyUserId + "&zd_requestId="+ id + "&zd_adId=" + adId+ "&zd_createId=" +createId + "&zd_landingPageId=" + landingPageId + "&requestAdDateTime=" + requestTime;
				if(landingPage.contains("?")) {
					landingPage = landingPage +  "&" + apkDownloadParms;
				} else {
					landingPage = landingPage +  "?" + apkDownloadParms; 
				}
				
				String commonParments = "mediaId=" + AdUtil.getNameToMd5(url) + "&userId=" + cyUserId + "&requestId="+ id + "&adId=" + adId+ "&adBlockKey=" +  adBlockKey  +  "&adSlotType=" +  adSlotType + "&createId=" +createId + "&landingPageId=" + landingPageId + "&height=" + height + "&width=" + width + "&requestAdDateTime=" + requestTime;
				//赢得竞价的回调地址
				String adWinURL = PropertyConstant.AD_WIN_POST_BACK_URL+"?" + commonParments + "&price=%%PRICE%%&cyRId=%%ID%%";
				//展示回调地址
				String adShowURL = PropertyConstant.AD_SHOW_POST_BACK_URL+"?" +commonParments + "&price=%%PRICE%%&cyRId=%%ID%%";
				String adShowURL1 = PropertyConstant.AD_SHOW_POST_BACK_URL_EXTEND +"?" +commonParments + "&price=%%PRICE%%&cyRId=%%ID%%";
				//点击回调地址
				String adClickURL = PropertyConstant.AD_CLICK_POST_BACK_URL+"?" + commonParments;
				String adClickURL1 = PropertyConstant.AD_CLICK_POST_BACK_URL_EXTEND +"?" + commonParments;
				
				com.sohu.advert.rtb.proto.ChangyanRtb.BidResponse.Ad.Builder adBuilder = Ad.newBuilder();
				adBuilder.setAdm(adm) //素材地址
				.setLandingPage(landingPage);
				
//				Long startTime = System.currentTimeMillis();
				long timeNow = System.currentTimeMillis();
				//曝光地址
				ExposureUrl exposureUrl = ExposureUrl.newBuilder()
						.setTimeMillis(0)
						.addExposureUrl(adShowURL)
						.addExposureUrl(adShowURL1)
						.build();
				
				// 最高竞价，1元=10000单位，例如，15000代表1.5元
				adBuilder
				.setType(0)//先默认为图片类型
				.setMaxCpm(maxCpm)
				.setIsCookieMatching(false)
				.setWidth(640)
				.setHeight(100)
				.setCategory(adCategory)//先默认其他行业
				
				//赢得竞价的回调地址
				.setNurl(adWinURL)
				//曝光地址
				.addExposureUrls(exposureUrl)
				//点击成功回调地址
				.addClickUrls(adClickURL)
				.addClickUrls(adClickURL1);
				
				Ad ad = adBuilder.build();
				long endTime = System.currentTimeMillis();
				Long prcocessingTimeMs = endTime - timeNow;
				
				BidResponse bidResponse = BidResponse.newBuilder()
						.setId(id)
						.setProcessingTimeMs(prcocessingTimeMs.intValue())//检索广告时间为10ms
						.addAd(ad)
						.setDspId("nextAdDsp")
						.build();
			
				response.setContentType("application/x-protobuf");
				OutputStream outputStream = response.getOutputStream();  
				bidResponse.writeTo(outputStream); 
				System.out.println("bidResponse=" + bidResponse);  
				outputStream.flush();  
				outputStream.close();
				inputStream.close();
				setLog(cyUserId, id,url, height, width,  mimimumCpm, HAS_AD, JSON.toJSONString(adAllMessage), requestTime);
			}
			
			System.out.println("userTime=" + (System.currentTimeMillis() - time));
		} catch (IOException e) {
			LoggerUtil.addExceptionLog(e);
		}
	}

	private AdAllMessage getAdMessage(String userId, Integer adSlotType) {
		AdAllMessage adAllMessage = null;
		Jedis jedis = jedisPools.getJedis();
		String userAdKey = RedisConstant.USER_AD_ADSLOT + userId;
		Pipeline pipeline = jedis.pipelined();
		Response<Boolean> isTAResponse = pipeline.exists(userAdKey);
		Response<String> valueStringResponse = pipeline.hget(userAdKey, adSlotType + "");
		Response<Set<String>> adSortSetResponse = pipeline.zrevrange(RedisConstant.AD_IDS_KEY, 0, -1);
		Response<Set<String>> stopAdSetResponse = pipeline.smembers(RedisConstant.AD_STOP_IDS);
		pipeline.sync();
		
		Boolean isTA = isTAResponse.get();
		String valueString = valueStringResponse.get();
//		String valueString = null;
		Set<String> adSortSet = adSortSetResponse.get();
		Set<String> stopAdSet = stopAdSetResponse.get();
		LoggerUtil.addBaseLog("当前广告有：[" + adSortSet + "] =需要停止投放的广告有=" + stopAdSet);
		adSortSet.removeAll(stopAdSet);
		LoggerUtil.addBaseLog("过滤掉的广告剩下广告：" + adSortSet);
		
		if(adSortSet == null || adSortSet.size() == 0) {
			jedisPools.closeJedis(jedis);
			LoggerUtil.addBaseLog("过滤广告后没有合适的广告");
			return adAllMessage;
		}
		
		Set<String> redisAdIds = null;
		String receiveAdId = null;
		if(StringUtils.isBlank(valueString)) {
			receiveAdId = adSortSet.iterator().next();
			LoggerUtil.addBaseLog("广告位adSlotType=" + adSlotType + ";第一次投放的广告为：" + receiveAdId);
		} else {
			String[] adIds = valueString.split(",");
			redisAdIds = new HashSet<String>(Arrays.asList(adIds));
			adSortSet.removeAll(redisAdIds);
			LoggerUtil.addBaseLog("用户" + userId+ "在广告位:" + adSlotType + "展示的广告[" + redisAdIds + "]; 剩下的广告=" + adSortSet);
			if(adSortSet.size()>0) {
				receiveAdId = adSortSet.iterator().next();
				LoggerUtil.addBaseLog("获取到的广告id=" + receiveAdId);
			} else {
				LoggerUtil.addBaseLog("已经没有合适的广告");
			}
		}
		
		
		//获取具体的广告信息
		if(receiveAdId != null) {
			String jsonAdBaseData = jedis.get(RedisConstant.AD_BASE + receiveAdId);
			String jsonAdSlotMessage = jedis.get(RedisConstant.AD_ADSLOT  + receiveAdId + "_" +adSlotType);
			AdMessage adMessage = JSON.parseObject(jsonAdBaseData, AdMessage.class);
			AdSlotMessage adSlotMessage = JSON.parseObject(jsonAdSlotMessage, AdSlotMessage.class);
			adAllMessage = new AdAllMessage();
			adAllMessage.setAdId(receiveAdId);
			adAllMessage.setAdCategory(adMessage.getAdCategory());
			adAllMessage.setClickType(adMessage.getClickType());
			adAllMessage.setName(adMessage.getName());
			adAllMessage.setCreateId(adSlotMessage.getCreateId());
			adAllMessage.setLandingPage(adSlotMessage.getLandingPage());
			adAllMessage.setAdm(adSlotMessage.getAdm());
			adAllMessage.setLandingPageId(adSlotMessage.getLandingPageId());
		} 
		
		if(isTA) {
			//说明是今天刚初始化的
			jedis.expire(userAdKey, DateUtil.getDifferentTimeMillis());
		}
		
		//获取所有广告id
		jedisPools.closeJedis(jedis);
		return adAllMessage;
	}

	
	private void pcLog(HttpServletResponse response,
			InputStream inputStream, String cyUserId, String id) throws IOException {
		BidResponse bidResponse = BidResponse.newBuilder()
				.setId(id)
				.setProcessingTimeMs(2)//检索广告时间为10ms
				.setDspId("nextAdDsp")
				.build();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cyUserId", cyUserId);
		map.put("id", id);
		map.put("requestTime", DateUtil.getDateTime());
		map.put("message", "pc端广告过滤");
		LoggerUtil.addPcLog(map);
		response.setContentType("application/x-protobuf");
		OutputStream outputStream = response.getOutputStream();  
		bidResponse.writeTo(outputStream); 
		outputStream.flush();  
		outputStream.close();
		inputStream.close();
	}
	
	/**
	 * 
	 * 获取不到广告
	 * @param response
	 * @param inputStream
	 * @param cyUserId
	 * @param id
	 * @param noHadAdMessage
	 * @throws IOException
	 */
	private void noHaveAd(HttpServletResponse response,
			InputStream inputStream, String cyUserId, String id, String url, int height, int width, int price, String noHadAdMessage, String requestTime) throws IOException {
		BidResponse bidResponse = BidResponse.newBuilder()
				.setId(id)
				.setProcessingTimeMs(2)//检索广告时间为10ms
				.setDspId("nextAdDsp")
				.build();
		setLog(cyUserId, id, url, height, width, price, HAS_NO_AD, noHadAdMessage, requestTime);
		response.setContentType("application/x-protobuf");
		OutputStream outputStream = response.getOutputStream();  
		bidResponse.writeTo(outputStream); 
		outputStream.flush();  
		outputStream.close();
		inputStream.close();
	}

	private void setLog(String cyUserId, String id, String url, int height, int width, int price, int isHasAd, String message, String requestTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cyUserId", cyUserId);
		map.put("url", url);
		map.put("id", id);
		map.put("height", height);
		map.put("width", width);
		map.put("requestTime", requestTime.replace("_", " "));
		map.put("isHasAd", isHasAd);
		map.put("message", message);
		map.put("price", price);
		LoggerUtil.addRequestLogegerMessage(map);
	}
}

