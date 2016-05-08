package com.zhidian3g.dsp.solr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import com.zhidian.dsp.constant.DspConstant;
import com.zhidian.dsp.constant.RedisConstant;
import com.zhidian3g.common.redisClient.JedisPools;
import com.zhidian3g.common.util.CommonLoggerUtil;
import com.zhidian3g.common.util.DateUtil;
import com.zhidian3g.common.util.JsonUtil;
import com.zhidian3g.dsp.solr.documentmanager.AdDspDocumentManager;
import com.zhidian3g.dsp.solr.service.SolrSearchAdService;
import com.zhidian3g.dsp.vo.ad.RedisAdLandingPageMessage;
import com.zhidian3g.dsp.vo.ad.RedisAdBaseMessage;
import com.zhidian3g.dsp.vo.adcontrol.AdBaseMessage;
import com.zhidian3g.dsp.vo.solr.SearchAd;
import com.zhidian3g.dsp.vo.solr.SearchAdCondition;
import com.zhidian3g.dsp.vo.solr.SearchAdMateriolCondition;

@Service
public class SolrSearchAdServiceImpl implements SolrSearchAdService {
	
	private AdDspDocumentManager adDocumentManager = AdDspDocumentManager.getInstance();
	private final JedisPools jedisPools = JedisPools.getInstance();
	
	@Override
	public SearchAd searchAdFormSolr(SearchAdCondition searchAdCondition, SearchAdMateriolCondition searchAdMateriolCondition) {
		Long adId = null;
//		DspConstant.ADX_TYPE + adxType, DspConstant.AD_TERMINALTYPE+terminalType, getOSString(OS), DspConstant.AD_TYPE + adTypeId, ip
		//添加广告筛选区域
		String ip = searchAdCondition.getIp();
		
		//获取广告终端
		String terminalType = DspConstant.AD_TERMINALTYPE + searchAdCondition.getTerminalType();
		//系统过滤
		String os = searchAdCondition.getOsPlatform();
		//adx过滤
		String adxType = DspConstant.ADX_TYPE + searchAdCondition.getAdxType();
		//广告创意过滤
		String adType = DspConstant.AD_TYPE + searchAdCondition.getAdType();
		//查询相关广告行业
		String adCategory = searchAdCondition.getAdCategory();
		//排除不要的行业广告
		String unSupportAdCategory = searchAdCondition.getUnSupportAdCategory();
		
		//初始化媒体类型查询参数
		StringBuffer conditionStringBuffer = new StringBuffer(adxType);
		
		//获取广告终端
		conditionStringBuffer.append(addCommonKey(terminalType));
		
		//获取广告的系统平台
		conditionStringBuffer.append(addCommonKey(os));
		
		//广告要展示的类型
		conditionStringBuffer.append(addCommonKey(adType));
		
		//添加时间段
		String time = DateUtil.getHour() + "-00";
		conditionStringBuffer.append(addCommonKey(time));
		
		StringBuffer fifterConditionStringBuffer = new StringBuffer();
		if(StringUtils.isNotBlank(adCategory)) {
			fifterAdCondition(unSupportAdCategory, fifterConditionStringBuffer, "+adCategory");
		} else if(StringUtils.isNoneBlank(unSupportAdCategory)) {
			fifterAdCondition(adCategory, fifterConditionStringBuffer, "-adCategory");
		}
		
//		//限制标题字数控制
//		if(len != null) {
//			fifterConditionStringBuffer.append("length:{0 TO " + len +"]");
//		}
		
		String condition = conditionStringBuffer.toString();
		String fifterCondition = fifterConditionStringBuffer.toString();
		System.out.println("condition=" + condition + ";fifterCondition=" + fifterCondition);
		
		/**从solr获取广告***/
		List<Long> adIdList = adDocumentManager.searchAdDocumentId(transformSolrMetacharactor(condition), fifterCondition);
		if(adIdList.size() ==  0) {
			CommonLoggerUtil.addBaseLog("==根据条件获取不了广告=" + JsonUtil.toJson(searchAdCondition));
			return null;
		}
		
		//根据素材条件获取相应的广告素材
		Integer meterialType = searchAdMateriolCondition.getMeterialType();
		String imagesHWS = searchAdMateriolCondition.getImageHW();
		
		StringBuffer adMaterialCondition = new StringBuffer();
		StringBuffer adMaterialFifterCondition = new StringBuffer();
		/**
		 * 广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		 */
		if(meterialType == 1) {
			adMaterialCondition.append(imagesHWS.substring(0, imagesHWS.length() -1));
		} else if(meterialType == 2) {
			adMaterialCondition.append(imagesHWS.substring(0, imagesHWS.length() -1));
			Integer tLen = searchAdMateriolCondition.gettLen();
			adMaterialFifterCondition.append("tLen:{0 TO " + tLen +"]");
		} else if(meterialType == 3) {
			adMaterialCondition.append(imagesHWS.substring(0, imagesHWS.length() -1));
			Integer tLen = searchAdMateriolCondition.gettLen();
			Integer dLen = searchAdMateriolCondition.getdLen();
			adMaterialFifterCondition.append("+tLen:{0 TO " + tLen +"]");
			adMaterialFifterCondition.append(" +dLen:{0 TO " + dLen +"]");
		} else if(meterialType == 4) {
			String[] imageHWArrays = imagesHWS.split(",");
			adMaterialCondition.append(imageHWArrays[0]);
			for(int i=1; i< imageHWArrays.length; i++) {
				adMaterialCondition.append(" AND " + imageHWArrays[i]);
			}
			Integer tLen = searchAdMateriolCondition.gettLen();
			adMaterialFifterCondition.append("+tLen:{0 TO " + tLen +"]");
		} else if(meterialType == 5) {
			Integer tLen = searchAdMateriolCondition.gettLen();
			adMaterialCondition.append("+tLen:{0 TO " + tLen +"]");
		}
		
		List<GroupCommand> listadMterial = adDocumentManager.searchAdMeterMessage(adMaterialCondition.toString(), adMaterialFifterCondition.toString());
		if(listadMterial == null || listadMterial.size() == 0) {
			CommonLoggerUtil.addBaseLog("==根据素材包条件获取不到信息=======" + adMaterialCondition.toString() + "==" + adMaterialFifterCondition.toString());
			return null;
		}
		
		
		//搜集广告信息
		Map<Long, SolrDocumentList> adAdMaterilmap = new HashMap<Long, SolrDocumentList>();
		for (GroupCommand groupCommand : listadMterial) {
			List<Group> groups = groupCommand.getValues();
			for (Group group : groups) {
				SolrDocumentList solrDocumentList = group.getResult();
				Long adMatertailAdId = (Long) solrDocumentList.get(0).getFieldValue("adId");
				if(adIdList.contains(adMatertailAdId)) {
					adAdMaterilmap.put(adId, solrDocumentList);
				}
			}
		}
		
		//没有合适的广告并集
		if(adAdMaterilmap.size() == 0) {
			CommonLoggerUtil.addBaseLog("==根据素材包条件获取不到信息=======" + adMaterialCondition.toString() + "==" + adMaterialFifterCondition.toString());
			return null;
		}
		
		//优化广告逻辑
		Set<Long> adIdSet = adAdMaterilmap.keySet();
		//获取到广告进行筛选
		adId = adIdList.get(0);
		
		SolrDocumentList solrDocumentList =  adAdMaterilmap.get(adId);
		SolrDocument solrDocument = solrDocumentList.get(chooseSolrDocument(solrDocumentList.size() - 1));
		
		int createId = (Integer)solrDocument.getFieldValue("createId");
		int meterialId = (Integer)solrDocument.getFieldValue("meterialId");
		
		//获取
		Jedis jedis = jedisPools.getJedis();
		
		String adBaseRedisKey = RedisConstant.AD_BASE + adId;
		//落地页的选择
		String adLandingPageKey = RedisConstant.AD_CREATE_LANDINGPAGE + "" + adId + "_" + createId;
		
//		广告素材类型1 纯图片 2 图文 3 图文描述(单图) 4 图文描述(多图) 5纯文字链接
		if(meterialType == 1) {//说明是纯图片
			String imageKey = RedisConstant.AD_IMAGE + adId + "_" + createId + "_" + meterialId + "";
			jedis.get("");
		} else if(meterialType == 2) {
			
		} else if(meterialType == 3) {
			
		} else if(meterialType == 4) {
			
		} else if(meterialType == 5) {
			
		} 
		
		Map<String, String> map = jedis.hgetAll(adLandingPageKey);
		String[] landpingKeyArray = map.keySet().toArray(new String[0]);
		int mapIndex = chooseSolrDocument(map.size());
		String jsonLandingPageString = map.get(landpingKeyArray[mapIndex]);
		RedisAdLandingPageMessage adLandingPageMessage = JsonUtil.fromJson(jsonLandingPageString, RedisAdLandingPageMessage.class);
		RedisAdBaseMessage adBaseMessage = JsonUtil.fromJson(jedis.get(adBaseRedisKey), RedisAdBaseMessage.class);
		
		jedisPools.closeJedis(jedis);
		return null;
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
		fifterConditionStringBuffer.append(")");
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
