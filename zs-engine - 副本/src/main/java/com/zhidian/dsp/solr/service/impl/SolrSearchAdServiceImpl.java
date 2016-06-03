package com.zhidian.dsp.solr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.google.common.base.Joiner;
import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.common.util.DateUtil;
import com.zhidian.common.util.JsonUtil;
import com.zhidian.dsp.constant.DspConstant;
import com.zhidian.dsp.constant.RedisConstant;
import com.zhidian.dsp.solr.documentmanager.AdDspDocumentManager;
import com.zhidian.dsp.solr.service.SolrSearchAdService;
import com.zhidian.dsp.vo.ad.AdImageMessage;
import com.zhidian.dsp.vo.ad.AdLandpageMessage;
import com.zhidian.dsp.vo.ad.AdMaterialMessage;
import com.zhidian.dsp.vo.ad.RedisAdBaseMessage;
import com.zhidian.dsp.vo.solr.SearchAd;
import com.zhidian.dsp.vo.solr.SearchAdCondition;
import com.zhidian.dsp.vo.solr.SearchAdMateriolCondition;

@Service
public class SolrSearchAdServiceImpl implements SolrSearchAdService {
	
	private AdDspDocumentManager adDocumentManager = AdDspDocumentManager.getInstance();
	private final JedisPools jedisPools = JedisPools.getInstance();
	
	@Override
	public SearchAd searchAdFormSolr(SearchAdCondition searchAdCondition, SearchAdMateriolCondition searchAdMateriolCondition) {
		//添加广告筛选区域
		String ipArea = searchAdCondition.getArea();
		
		//获取广告终端
		String terminalType = DspConstant.AD_TERMINALTYPE + searchAdCondition.getTerminalType();
		//系统过滤
		String os = searchAdCondition.getOsPlatform();
		
		//网络类型
		String networkType = searchAdCondition.getNetworkType();
		
		//adx过滤
		String adxType = DspConstant.ADX_TYPE + searchAdCondition.getAdxType();
		//adPrice
		Long adPrice = searchAdCondition.getAdPrice();
		
		//查询相关广告行业
		String adCategory = searchAdCondition.getAdCategory();
		//排除不要的行业广告
		String unSupportAdCategory = searchAdCondition.getUnSupportAdCategory();
		
		//初始化媒体类型查询参数
		StringBuffer conditionStringBuffer = new StringBuffer(adxType);
		
		if(StringUtils.isNotBlank(ipArea)) {
			conditionStringBuffer.append(addCommonKey(ipArea));
		}
		
		//获取广告终端
		conditionStringBuffer.append(addCommonKey(terminalType));
		
		//获取广告的系统平台
		conditionStringBuffer.append(addCommonKey(os));
		
		//网络类型
		if(networkType != null) {
			conditionStringBuffer.append(addCommonKey(networkType));
		}
		
//		//广告要展示的类型
		
		//添加时间段
		String time = "hour" + DateUtil.getHour();
		conditionStringBuffer.append(addCommonKey(time));
		
		StringBuffer fifterConditionStringBuffer = new StringBuffer("adPrice:[" + adPrice + " TO *] ");
		if(StringUtils.isNotBlank(adCategory)) {
			fifterAdCondition(adCategory, fifterConditionStringBuffer, "+adCategory");
		} else if(StringUtils.isNoneBlank(unSupportAdCategory)) {
			fifterAdCondition(unSupportAdCategory, fifterConditionStringBuffer, "-adCategory");
		}
		
		//获取
		Jedis jedis = jedisPools.getJedis();
		Set<String> stopIdKeys = jedis.smembers(RedisConstant.AD_STOP_IDS); 
		String stopId = null;
		//设置停止投放的广告
		if(CollectionUtils.isNotEmpty(stopIdKeys)) {
			stopId = "-adId:(" + Joiner.on(" OR ").join(stopIdKeys) + ")";
			fifterConditionStringBuffer.append(stopId);
		}
		
		String condition = conditionStringBuffer.toString();
		String fifterCondition = fifterConditionStringBuffer.toString();
		System.out.println("condition=" + condition + ";fifterCondition=" + fifterCondition);
		
		/**从solr获取广告***/
		List<Long> adIdList = adDocumentManager.searchAdDocumentId(transformSolrMetacharactor(condition), fifterCondition);
		if(adIdList.size() ==  0) {
			CommonLoggerUtil.addBaseLog("==根据条件获取不了广告=" + JsonUtil.toJson(searchAdCondition));
			jedisPools.closeJedis(jedis);
			return null;
		}
		
		System.out.println("adIdList=" + adIdList);
		//根据素材条件获取相应的广告素材
		Integer meterialType = searchAdMateriolCondition.getMeterialType();
		String imagesHWS = searchAdMateriolCondition.getImageHW();
		String oneImageHW = null;
		if(meterialType != 4 && meterialType != 5 ) { 
			oneImageHW = imagesHWS;
			System.out.println("oneImageHW=" + oneImageHW);
		}
		
		//一张图片的大小
		//广告创意过滤
		String adType = DspConstant.AD_TYPE + searchAdMateriolCondition.getAdType();
		StringBuffer adMaterialCondition = new StringBuffer(adType + " ");
		StringBuffer adMaterialFifterCondition = new StringBuffer();
		
		if(StringUtils.isNotBlank(stopId)) {
			adMaterialFifterCondition.append(stopId + " ");
		}
		/**
		 * 广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		 */
		if(meterialType == 1) {
			adMaterialCondition.append(oneImageHW);
		} else if(meterialType == 2) {
			adMaterialCondition.append(oneImageHW);
			Integer tLen = searchAdMateriolCondition.gettLen();
			adMaterialFifterCondition.append("+tLen:{0 TO " + tLen +"]");
		} else if(meterialType == 3) {
			adMaterialCondition.append(oneImageHW);
			Integer tLen = searchAdMateriolCondition.gettLen();
			Integer dLen = searchAdMateriolCondition.getdLen();
			adMaterialFifterCondition.append("+tLen:{0 TO " + tLen +"]");
			adMaterialFifterCondition.append(" +dLen:{0 TO " + dLen +"]");
		} else if(meterialType == 4) {
			String[] imageHWArrays = imagesHWS.split(";");
			adMaterialCondition.append(imageHWArrays[0]);
			for(int i=1; i< imageHWArrays.length; i++) {
				adMaterialCondition.append(" AND " + imageHWArrays[i]);
			}
			Integer tLen = searchAdMateriolCondition.gettLen();
			adMaterialFifterCondition.append("+tLen:{0 TO " + tLen +"]");
		} else if(meterialType == 5) {
			Integer tLen = searchAdMateriolCondition.gettLen();
			adMaterialFifterCondition.append("+tLen:{0 TO " + tLen +"]");
		}
		
		adMaterialCondition.append(" AND " + DspConstant.METERIALTYPE + meterialType);
		System.out.println("adMaterialCondition=" + adMaterialCondition + ";adMaterialFifterCondition=" + adMaterialFifterCondition);
		List<GroupCommand> listadMterial = adDocumentManager.searchAdMeterMessage(adMaterialCondition.toString(), adMaterialFifterCondition.toString());
		if(listadMterial == null || listadMterial.size() == 0) {
			CommonLoggerUtil.addBaseLog("==根据素材包条件获取不到信息=======" + adMaterialCondition.toString() + "==" + adMaterialFifterCondition.toString());
			jedisPools.closeJedis(jedis);
			return null;
		}
		
		//搜集广告信息
		Map<Long, SolrDocumentList> adAdMaterilmap = new HashMap<Long, SolrDocumentList>();
		for (GroupCommand groupCommand : listadMterial) {
			List<Group> groups = groupCommand.getValues();
			for (Group group : groups) {
				SolrDocumentList solrDocumentList = group.getResult();
//				System.out.println(group.getGroupValue() + "==num=" + solrDocumentList.getNumFound() + "=" + solrDocumentList);
				Long adMatertailAdId = (Long) solrDocumentList.get(0).getFieldValue("adId");
				if(adIdList.contains(adMatertailAdId)) {
					adAdMaterilmap.put(adMatertailAdId, solrDocumentList);
				}
			}
		} 
		
		//没有合适的广告并集
		if(adAdMaterilmap.size() == 0) {
			CommonLoggerUtil.addBaseLog("==根据素材包条件获取不到信息=======" + adMaterialCondition.toString() + "==" + adMaterialFifterCondition.toString());
			jedisPools.closeJedis(jedis);
			return null;
		} else {
			System.out.println("adAdMaterilmap=" + adAdMaterilmap.keySet() + ";" + adAdMaterilmap);
		}
		
		//优化广告逻辑
//		Set<Long> adIdSet = adAdMaterilmap.keySet();
		List<Long> mapAdIdList = new ArrayList<Long>(adAdMaterilmap.keySet());
		
		//获取到广告进行筛选
		Long adId = mapAdIdList.get(chooseSolrDocument(mapAdIdList.size() - 1));
		System.out.println("mapAdIdList=" + mapAdIdList + "随机adId=" + adId);
		
		SolrDocumentList solrDocumentList =  adAdMaterilmap.get(adId);
		SolrDocument solrDocument = solrDocumentList.get(chooseSolrDocument(solrDocumentList.size() - 1));
		
		int createId = (Integer)solrDocument.getFieldValue("createId");
		int materialId = (Integer)solrDocument.getFieldValue("meterialId");
		
		String adBaseRedisKey = RedisConstant.AD_BASE + adId;
		//落地页的选择
		String adLandingPageKey =  RedisConstant.AD_MATERTIAL_LANDINGPAGE + adId + "_" + createId + "_" + materialId;
		String createPackageMessageKey = RedisConstant.AD_CREATE_MATERIAL + createId + "_" + materialId;
		
		//缓存中取数据
		Pipeline pipeline = jedis.pipelined();
		Response<String> responseaAdBase = pipeline.get(adBaseRedisKey);
		Response<String> responseaCreatePackageMessage = pipeline.get(createPackageMessageKey);
		Response<Map<String, String>> responseAdLandingPage = pipeline.hgetAll(adLandingPageKey);
		pipeline.sync();
		
		//缓存数据转换成对象 
		RedisAdBaseMessage adBaseMessage = JsonUtil.fromJson(responseaAdBase.get(), RedisAdBaseMessage.class);
		AdMaterialMessage redisAdCreateMaterialMessage = JsonUtil.fromJson(responseaCreatePackageMessage.get(), AdMaterialMessage.class);
		Map<String, String> map = responseAdLandingPage.get();
//		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		SearchAd searchAd = new SearchAd();
		String[] landpingKeyArray = map.keySet().toArray(new String[0]);
		int mapIndex = chooseSolrDocument(map.size() - 1);
		String jsonLandingPageString = map.get(landpingKeyArray[mapIndex]);
		AdLandpageMessage adLandingPageMessage = JsonUtil.fromJson(jsonLandingPageString, AdLandpageMessage.class);
		Map<String, AdImageMessage> redisAdImageMap = null;
		if(meterialType == 1 || meterialType == 2 || meterialType == 3) {//说明是纯图片
			redisAdImageMap = redisAdCreateMaterialMessage.getMapAdImage();
		}  else if(meterialType == 4) {//多图片现在过滤
			redisAdImageMap = redisAdCreateMaterialMessage.getMapAdImage();
		} 
		
		searchAd.setRedisAdImageMap(redisAdImageMap);
		searchAd.setRedisAdBaseMessage(adBaseMessage);
		searchAd.setAdMaterialMessage(redisAdCreateMaterialMessage);
		searchAd.setAdLandingPageMessage(adLandingPageMessage);
		jedisPools.closeJedis(jedis);
		return searchAd;
	}
		
	/**
	 * 随机获取广告id集合的一个ID
	 * @param result
	 */
	private int chooseSolrDocument(int count){
		int random = (int)Math.rint(Math.random() * (count));
		return random;
	}
	
	private void fifterAdCondition(String adCategory, StringBuffer fifterConditionStringBuffer, String addOrRemove) {
		String[] adCategoryArray = adCategory.split(",");
		fifterConditionStringBuffer.append(addOrRemove + ":(");
		int len = adCategoryArray.length;
		for(int i=0; i<len; i++) {
			if(i == 0) {
				fifterConditionStringBuffer.append(DspConstant.AD_CATEGORY + adCategoryArray[i]); 
			} else {
				fifterConditionStringBuffer.append(" OR " + DspConstant.AD_CATEGORY + adCategoryArray[i]);
			}
		}
		fifterConditionStringBuffer.append(") ");
		System.out.println("========" + fifterConditionStringBuffer);
	}
	
	/**
	 * 添加查询关键字
	 * @param contentValue
	 * @return
	 */
	private String addCommonKey(String contentValue) {
		return " AND " + contentValue;
	}
	
	/**
	 * 过滤转义符
	 * @param input
	 * @return
	 */
	private String transformSolrMetacharactor(String input){
		//solr内部转义符
//		String ok = ClientUtils.escapeQueryChars(fifterCondition);
        StringBuffer sb = new StringBuffer();
        String regex = "[+\\-&|!(){}\\[\\]^\"~*?:(\\)]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            matcher.appendReplacement(sb, "\\\\"+matcher.group());
        }
        matcher.appendTail(sb);
        return sb.toString(); 
    }

}
