package com.zhidian.dsp.control.task;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;

import com.zhidian.common.contans.RedisConstant;
import com.zhidian.common.redisClient.JedisPools;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.common.util.SpringContextUtil;
import com.zhidian.dsp.control.service.impl.AdHanderService;
import com.zhidian.dsp.util.AdControlLoggerUtil;

/**
 * NextAd广告删除监控线程
 * @author Administrator
 */
public class DSPAdDelTask implements Runnable {
	JedisPools jedisPools = JedisPools.getInstance();
	AdHanderService adHanderService = (AdHanderService)SpringContextUtil.getBean("adHanderService");
	
	@Override
	public void run() {
		CommonLoggerUtil.addBaseLog("*****************系统启动广告删除队列**************");
		while(true) {
			Jedis jedis = null;
			try {
				jedis = jedisPools.getJedis();
				//删除广告逻辑处理
				String delAdId = jedis.brpoplpush(RedisConstant.DEL_ADID,  RedisConstant.BACKUP_DEL_ADID, 1800);
  				if(StringUtils.isNotBlank(delAdId)) {
					//从广告排序中移除
//  					ZRANGEBYSCORE
  					String[] delAdIdValues = delAdId.split(":::");
  					
  					Integer adDelStatus = Integer.valueOf(delAdIdValues[0]);
  					Long adId = Long.valueOf(delAdIdValues[1]);
  					
  					//-1账号找不到 -2 广告找不到， 2 账号余额不足，3：日预算不足，4：总预算不足
  					/**1 ios 2 安卓 **************/
  					if(adDelStatus == 3) {
  						AdControlLoggerUtil.addTimeLog("=======日预算不足======" + adId);
  						adHanderService.delAd(adId, 5);
  					} else if(adDelStatus == 4) {
  						adHanderService.delAd(adId, 6);
  						AdControlLoggerUtil.addTimeLog("=======总预算不足===delAdId=" + adId);
  					}  else if(adDelStatus == 2) {
  						adHanderService.delAd(adId, 7);
  						AdControlLoggerUtil.addTimeLog("=======总预算不足======delAdId=" + adId);
  					} else {
  						AdControlLoggerUtil.addTimeLog("====adDelStatus" + adDelStatus + "=========delAdId=" + adId);
  						adHanderService.delAd(adId);
  					}
  					jedis.lrem(RedisConstant.BACKUP_DEL_ADID, 0, delAdId);
				} else {
					CommonLoggerUtil.addBaseLog("=======dsp=====没有删除的广告Id=============");
				}
  				
			} catch (Exception e) {
				jedisPools.exceptionBroken(jedis);
				CommonLoggerUtil.addExceptionLog(e);
			} finally {
				jedisPools.closeJedis(jedis);
			}
		}
	
	}

}
