package com.zhidian3g.dsp.solr.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.zhidian.dsp.constant.DspConstant;
import com.zhidian.dsp.constant.RedisConstant;
import com.zhidian3g.common.redisClient.JedisPools;
import com.zhidian3g.common.util.CommonLoggerUtil;
import com.zhidian3g.common.util.DateUtil;
import com.zhidian3g.common.util.JedisUtil;
import com.zhidian3g.common.util.JsonUtil;
import com.zhidian3g.dsp.solr.documentmanager.AdDocumentManager;
import com.zhidian3g.dsp.solr.documentmanager.AdDspDocumentManager;
import com.zhidian3g.dsp.solr.service.SolrSearchAdService;
import com.zhidian3g.dsp.vo.solr.RedisImageAd;
import com.zhidian3g.dsp.vo.solr.SearchAd;
import com.zhidian3g.dsp.vo.solr.SearchAdCondition;
import com.zhidian3g.dsp.vo.solr.RedisNativeAd;

@Service
public class SolrSearchAdServiceImpl implements SolrSearchAdService {
	
	private AdDspDocumentManager adDocumentManager = AdDspDocumentManager.getInstance();
	private final JedisPools jedisPools = JedisPools.getInstance();
	
	@Override
	public SearchAd searchAdFormSolr(SearchAdCondition searchAdCondition) {
		Long adId = null;
		
		//添加广告筛选区域
		String ip = searchAdCondition.getIp();
		
		//系统过滤
		String os = searchAdCondition.getOsPlatform();
		//adx过滤
		String adxType = searchAdCondition.getAdxType();
		//广告创意过滤
		String adType = searchAdCondition.getAdType();
		//查询相关广告行业
		String adCategory = searchAdCondition.getAdCategory();
		//排除不要的行业广告
		String unSupportAdCategory = searchAdCondition.getUnSupportAdCategory();
		
		//初始化媒体类型查询参数
		StringBuffer conditionStringBuffer = new StringBuffer(adxType);
		//获取广告的系统平台
		if(StringUtils.isNotBlank(os)) {
			conditionStringBuffer.append(addCommonKey(os));
		}
		//广告要展示的类型
		if(StringUtils.isNotBlank(adType)) {
			conditionStringBuffer.append(addCommonKey(adType));
		}
		
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
		
		//获取到广告进行筛选
		adId = adIdList.get(0);
		Jedis jedis = jedisPools.getJedis();
		
		String adJsonString = null;
		
		SearchAd searchAd = new SearchAd();
		//原生广告
//		if(adShowType.equals(DspConstant.AD_SHOW_TYPE + DspConstant.NATIVE_AD_TYPE)) {
//			adJsonString = JedisUtil.get(jedis, RedisConstant.AD_NATIVE + adId);
//			RedisNativeAd redisNativeAd = JsonUtil.fromJson(adJsonString, RedisNativeAd.class);
//			searchAd.setRedisNativeAd(redisNativeAd);
//		} else if(adShowType.equals(DspConstant.AD_SHOW_TYPE + DspConstant.IMAGE_TYPE)) {
//			adJsonString = JedisUtil.get(jedis, RedisConstant.AD_IMAGE + adId);
//			RedisImageAd redisImageAd = JsonUtil.fromJson(adJsonString, RedisImageAd.class);
//			searchAd.setRedisImageAd(redisImageAd);
//		}
		
		jedisPools.closeJedis(jedis);
		return searchAd;
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
