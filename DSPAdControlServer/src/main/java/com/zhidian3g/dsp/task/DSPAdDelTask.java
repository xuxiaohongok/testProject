package com.zhidian3g.dsp.task;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

import com.zhidian3g.common.constant.RedisConstant;
import com.zhidian3g.dsp.redisClient.JedisPools;
import com.zhidian3g.dsp.util.LoggerUtil;

/**
 * NextAd广告删除监控线程
 * @author Administrator
 */
public class DSPAdDelTask implements Runnable {
	JedisPools jedisPools = JedisPools.getInstance();
	
	@Override
	public void run() {
		LoggerUtil.addBaseLog("*****************系统启动广告删除队列**************");
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
  					
  					Integer osType = Integer.valueOf(delAdIdValues[0]);
  					String adId = delAdIdValues[1];
  					
  					/**1 ios 2 安卓 **************/
  					if(osType == 2) {
  						jedis.zrem(RedisConstant.AD_ANDROID_IDS, adId);
  						LoggerUtil.addTimeLog("===========删除的广告Id============delAdId=" + adId);
  					} else if (osType == 1) {
  						jedis.zrem(RedisConstant.AD_IOS_IDS, adId);
  						LoggerUtil.addTimeLog("===========删除的广告Id============delAdId=" + adId);
  					} else {
  						jedis.zrem(RedisConstant.AD_PC_IDS, adId);
  						LoggerUtil.addTimeLog("===========没有对应的类型广告Id============delAdId=" + adId);
  					}
  					
  					jedis.lrem(RedisConstant.BACKUP_DEL_ADID, 0, delAdId);
				} else {
					LoggerUtil.addTimeLog("=======dsp=====没有删除的广告Id=============");
				}
			} catch (Exception e) {
				jedisPools.exceptionBroken(jedis);
				LoggerUtil.addExceptionLog(e);
			} finally {
				jedisPools.closeJedis(jedis);
			}
		}
	
	}

}
