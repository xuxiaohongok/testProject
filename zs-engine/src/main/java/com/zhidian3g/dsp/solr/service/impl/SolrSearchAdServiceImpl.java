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
import com.zhidian3g.dsp.solr.service.SolrSearchAdService;
import com.zhidian3g.dsp.vo.solr.RedisImageAd;
import com.zhidian3g.dsp.vo.solr.SearchAd;
import com.zhidian3g.dsp.vo.solr.SearchAdCondition;
import com.zhidian3g.dsp.vo.solr.RedisNativeAd;

@Service
public class SolrSearchAdServiceImpl implements SolrSearchAdService {
	
	private AdDocumentManager adDocumentManager = AdDocumentManager.getInstance();
	private final JedisPools jedisPools = JedisPools.getInstance();
	
	@Override
	public SearchAd searchAdFormSolr(SearchAdCondition ad) {
		Long adId = null;
		
		//系统过滤
		String os = ad.getOsType();
		String adxType = ad.getAdxType();
		String adType = ad.getAdType();
		String adShowType = ad.getShowType();
		String adHW = ad.getImageTypeHWs();
		//标题的长宽
		Integer len = ad.getTH();
		
		//初始化媒体类型查询参数
		StringBuffer conditionStringBuffer = new StringBuffer(adxType);
		
		//获取广告的系统平台
		if(StringUtils.isNotBlank(os)) {
			conditionStringBuffer.append(addCommonKey(os));
		}
		
		//广告要展示的类型
		if(StringUtils.isNotBlank(adShowType)) {
			conditionStringBuffer.append(addCommonKey(adShowType));
		}
		
		//广告要展示的类型
		if(StringUtils.isNotBlank(adType)) {
			conditionStringBuffer.append(addCommonKey(adType));
		}
		
		//添加广告长宽过滤
		if(StringUtils.isNoneBlank(adHW)) {
			if(adHW.contains(";")) {
				String[] imageArray = adHW.split(";");
				//切割得到不同的类型图片长宽
				for(String imageHW : imageArray) {
					conditionStringBuffer.append(addCommonKey(imageHW));
				}
			} else {//广告位类型图片广告
				conditionStringBuffer.append(addCommonKey(adHW));
			}
		}
		
		//添加时间段
		String time = DateUtil.getHour() + ":00";
		conditionStringBuffer.append(addCommonKey(time));
		
		StringBuffer fifterConditionStringBuffer = new StringBuffer();
		//限制标题字数控制
		if(len != null) {
			fifterConditionStringBuffer.append("length:{0 TO " + len +"]");
		}
		
		String condition = conditionStringBuffer.toString();
		String fifterCondition = fifterConditionStringBuffer.toString();
		System.out.println("condition=" + condition + ";fifterCondition=" + fifterCondition);
		
		/**从solr获取广告***/
		List<Long> adIdList = adDocumentManager.searchAdDocumentId(transformSolrMetacharactor(condition), fifterCondition);
		if(adIdList.size() ==  0) {
			CommonLoggerUtil.addBaseLog("==根据条件获取不了广告=" + JsonUtil.toJson(ad));
			return null;
		}
		
		//获取到广告进行筛选
		adId = adIdList.get(0);
		Jedis jedis = jedisPools.getJedis();
		
		String adJsonString = null;
		
		SearchAd searchAd = new SearchAd();
		//原生广告
		if(adShowType.equals(DspConstant.AD_SHOW_TYPE + DspConstant.NATIVE_AD_TYPE)) {
			adJsonString = JedisUtil.get(jedis, RedisConstant.AD_NATIVE + adId);
			RedisNativeAd redisNativeAd = JsonUtil.fromJson(adJsonString, RedisNativeAd.class);
			searchAd.setRedisNativeAd(redisNativeAd);
		} else if(adShowType.equals(DspConstant.AD_SHOW_TYPE + DspConstant.IMAGE_TYPE)) {
			adJsonString = JedisUtil.get(jedis, RedisConstant.AD_IMAGE + adId);
			RedisImageAd redisImageAd = JsonUtil.fromJson(adJsonString, RedisImageAd.class);
			searchAd.setRedisImageAd(redisImageAd);
		}
		
		jedisPools.closeJedis(jedis);
		return searchAd;
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
