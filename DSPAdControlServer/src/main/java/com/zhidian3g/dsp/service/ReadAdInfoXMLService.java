package com.zhidian3g.dsp.service;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import redis.clients.jedis.Jedis;

import com.zhidian3g.common.constant.AdConstant;
import com.zhidian3g.common.constant.RedisConstant;
import com.zhidian3g.dsp.ad.AdMessage;
import com.zhidian3g.dsp.ad.AdSlotMessage;
import com.zhidian3g.dsp.redisClient.JedisPools;
import com.zhidian3g.dsp.util.AdControlUtil;
import com.zhidian3g.dsp.util.DateUtil;
import com.zhidian3g.dsp.util.JsonUtil;
import com.zhidian3g.dsp.util.LoggerUtil;

public class ReadAdInfoXMLService{
	
    public static void main(String[] args) throws Exception {
    	getAllAdInfo(true); 
   }
    /**
     * 获取广告信息
     * @throws JDOMException
     * @throws IOException
     */
	public static void getAllAdInfo(boolean reflushAllAd) {
		JedisPools jedisPools = null;
		Jedis jedis = null;
		try {
			jedisPools = JedisPools.getInstance();
			jedis = jedisPools.getJedis();
			//先删除掉所有投放中的广告
			
			//获取所有广告id
			Set<String> adSortSet = null;//
			Set<String> lastDayAdIdSet = jedis.hkeys(RedisConstant.INIT_AD_DAYBUDGET);
			
			if(reflushAllAd) {
				LoggerUtil.addTimeLog("==凌晨删除所有广告信息===========");
				jedis.del(RedisConstant.AD_IDS_KEY);
				Set<String> delAdKey = new HashSet<String>();
				for(String adId : lastDayAdIdSet) {
					delAdKey.addAll(delAdMessage(adId));
					delAdKey.add(RedisConstant.AD_DAYBUDGET + adId);
				}
				
				delAdKey.add(RedisConstant.INIT_AD_DAYBUDGET);
				jedis.del(delAdKey.toArray(new String[0]));
				LoggerUtil.addTimeLog("==凌晨删除所有广告信息=========" + delAdKey);
				
			} else {
				LoggerUtil.addTimeLog("==读取广告配置文件广告重新调整投放===========");
				jedis.del(RedisConstant.AD_IDS_KEY);
				adSortSet = jedis.zrevrange(RedisConstant.AD_IDS_KEY, 0, -1);
			}
			
			//获取广告配置文件
			SAXBuilder builder = new SAXBuilder();
			String filePath = ReadAdInfoXMLService.class.getResource("/adInfo.xml").getPath();
			Document read_doc = builder.build(filePath);
			Element adInfo = read_doc.getRootElement();
			List<Element> list = adInfo.getChildren("ad");
			String nowDateString = DateUtil.getDateTime();
			Map<String, Long> adPriceMap = new HashMap<String, Long>();
			for(int i = 0;i < list.size();i++) {
			    Element e = list.get(i);
			    String adId = e.getChildText("adId");
			    String name = e.getChildText("name");
			    String title = e.getChildText("title");
			    String startDate = e.getChildText("startDate");
			    String endDate = e.getChildText("endDate");
			    
				/**
				 * 广告开始时间未到，不生成
				 * 大于开始时间，或都等于
				 */
			    startDate = startDate + " 00:00:00";
			    endDate = endDate + " 23:59:59";
				if(DateUtil.compareDate(nowDateString, startDate)){
					LoggerUtil.addTimeLog("广告adId=" + adId + "  " + startDate + " 开启时间还没到");
					jedisPools.closeJedis(jedis);
					continue;
				} else if (DateUtil.compareDate(endDate, nowDateString)) {
					LoggerUtil.addTimeLog("广告adId=" + adId + "  " + startDate + " 开启时间已经过期");
					jedisPools.closeJedis(jedis);
					continue;
				}
			    
			    Integer clickType = Integer.valueOf(e.getChildText("clickType"));
			    Integer adCategory = Integer.valueOf(e.getChildText("adCategory"));
			    Long adAcountCost = Long.valueOf(e.getChildText("adAccountCost")) * AdConstant.AD_MONEY_UNIT;
			    long adDayBudget = Long.valueOf(e.getChildText("adDayBudget")) * AdConstant.AD_MONEY_UNIT;
			    Long adPrice = (long) (Double.valueOf(e.getChildText("adPrice")) * AdConstant.AD_MONEY_UNIT);
			    //保存对应广告的价格
			    adPriceMap.put(adId, adPrice);
			    
			    //添加刷新广告费用
			    DSPAdControlServerBillingService.addAdOrRefreshBudget(adId, adDayBudget);
			    
			    //设置广告信息
			    AdMessage adMessage = new AdMessage();
			    adMessage.setAdId(adId);
			    adMessage.setAdAccountCost(adAcountCost);
			    adMessage.setAdCategory(adCategory);
			    adMessage.setAdDayBudget(adDayBudget);
			    adMessage.setAdPrice(adPrice);
			    adMessage.setClickType(clickType);
			    adMessage.setName(name);
			    adMessage.setTitle(title);
			    jedis.set(RedisConstant.AD_BASE + adId, JsonUtil.toJson(adMessage));
			    
			    //添加素材
			    List<Element> listAdMaterial =  e.getChildren("adSlot");
			    for(Element adMaterElement : listAdMaterial) {
			    	Integer adSlotType = Integer.valueOf(adMaterElement.getChildText("adSlotType"));
			    	String createId = adMaterElement.getChildText("createId");
			    	String landingPageId = adMaterElement.getChildText("landingPageId");
			    	String landingPage = adMaterElement.getChildText("landingPage");
			    	String adm = adMaterElement.getChildText("adm");
			    	AdSlotMessage adSlotMessage = new AdSlotMessage();
			    	adSlotMessage.setAdSlotType(adSlotType);
			    	adSlotMessage.setCreateId(createId);
			    	adSlotMessage.setAdm(adm);
			    	adSlotMessage.setLandingPage(landingPage);
			    	adSlotMessage.setLandingPageId(landingPageId);
			    	jedis.set(RedisConstant.AD_ADSLOT + adId + "_" + adSlotType, JsonUtil.toJson(adSlotMessage));
			    }
			    
			    LoggerUtil.addTimeLog("广告adId=" + adId + " 基本信息初始化完毕");
			    
			    //保留配置文件的广告
			    if(adSortSet != null) {
			    	adSortSet.remove(adId);
			    }
			}
			
			if(adSortSet != null && adSortSet.size() != 0) {
				jedis.zrem(RedisConstant.AD_IDS_KEY, adSortSet.toArray(new String[0]));
				Set<String> adSetString = new HashSet<String>();
				for(String adId : adSortSet) {
					adSetString.addAll(delAdMessage(adId));
				}
				jedis.del(adSetString.toArray(new String[0]));
				LoggerUtil.addTimeLog("删除的广告信息为：" + adSetString);
			}
			
			
			//广告频次设置
			AdControlUtil.setAdControlTimes(adPriceMap.keySet());
			
			for(Entry<String, Long> adPriceEntry : adPriceMap.entrySet()) {
				jedis.zadd(RedisConstant.AD_IDS_KEY, adPriceEntry.getValue(), adPriceEntry.getKey());
			}
			
		} catch (Exception e) {
			jedisPools.exceptionBroken(jedis);
			e.printStackTrace();
		} 
        jedisPools.closeJedis(jedis);
	}

	/**
	 * 移除广告信息
	 * @param pipeline
	 * @param adId
	 * @throws Exception
	 */
	private static Set<String> delAdMessage(String adId) throws Exception{
		Set<String> adSetString = new HashSet<String>();
		String delAdBaseKey = RedisConstant.AD_BASE + adId;
		String delAdSlotFly = RedisConstant.AD_ADSLOT + adId + "_" + AdConstant.ADSLOT_FLY; 
		String delAdSlotBanner = RedisConstant.AD_ADSLOT + adId + "_" + AdConstant.ADSLOT_BANNER; 
		adSetString.add(delAdBaseKey);
		adSetString.add(delAdSlotFly);
		adSetString.add(delAdSlotBanner);
		return adSetString;
	}
}