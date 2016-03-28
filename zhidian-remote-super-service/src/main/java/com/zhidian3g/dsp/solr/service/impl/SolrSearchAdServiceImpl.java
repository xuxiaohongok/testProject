package com.zhidian3g.dsp.solr.service.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.TypeReference;
import com.zhidian.dsp.constant.RedisConstant;
import com.zhidian3g.common.redisClient.JedisPools;
import com.zhidian3g.common.util.CommonLoggerUtil;
import com.zhidian3g.common.util.JedisUtil;
import com.zhidian3g.common.util.JsonUtil;
import com.zhidian3g.dsp.solr.documentmanager.AdDocumentManager;
import com.zhidian3g.dsp.solr.service.SolrSearchAdService;
import com.zhidian3g.dsp.vo.solr.SearchAdCondition;

@Service
public class SolrSearchAdServiceImpl implements SolrSearchAdService {
	
	private AdDocumentManager adDocumentManager = AdDocumentManager.getInstance();
	private final JedisPools jedisPools = JedisPools.getInstance();
	
	@Override
	public Map<String, Object> searchAdFormSolr(SearchAdCondition ad) {
		Long adId = null;
		String adxType = ad.getAdxType();
		String adShowType = ad.getShowType();
		String adHW = ad.getAdHW();
		Integer len = ad.getLength();
		
		//初始化媒体类型查询参数
		StringBuffer conditionStringBuffer = new StringBuffer(adxType);
		if(StringUtils.isNotBlank(adShowType)) {
			//广告要展示的类型
			conditionStringBuffer.append(addCommonKey(adShowType));
		}
		//添加广告长宽过滤
		if(StringUtils.isNoneBlank(adHW)) {
			conditionStringBuffer.append(addCommonKey(adHW));
		}
		
		StringBuffer fifterConditionStringBuffer = new StringBuffer();
		//限制标题字数控制
		if(len != null) {
			fifterConditionStringBuffer.append("length:{0 TO " + len +"]");
		}
		
		String condition = conditionStringBuffer.toString();
		String fifterCondition = fifterConditionStringBuffer.toString();
		System.out.println("condition=" + condition + ";fifterCondition=" + fifterCondition);
		
		/**从solr获取广告***/
		List<Long> adIdList = adDocumentManager.searchAdDocumentId(condition, transformSolrMetacharactor(fifterCondition));
		if(adIdList.size() ==  0) {
			CommonLoggerUtil.addBaseLog("==根据条件获取不了广告=" + JsonUtil.toJson(ad));
			return null;
		}
		
		//获取到广告进行筛选
		adId = adIdList.get(0);
		Jedis jedis = jedisPools.getJedis();
		String adJsonString = JedisUtil.get(jedis, RedisConstant.AD_NATIVE + adId);
		Map<String, Object> map = JsonUtil.fromJsonType(adJsonString, new TypeReference<Map<String, Object>>(){});
		jedisPools.closeJedis(jedis);
		return map;
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
