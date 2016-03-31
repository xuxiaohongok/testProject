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
  					Double score = jedis.zscore(RedisConstant.AD_IDS_KEY, delAdId);
  					jedis.zrem(RedisConstant.AD_IDS_KEY, delAdId);
  					jedis.lrem(RedisConstant.BACKUP_DEL_ADID, 0, delAdId);
  					jedis.zadd(RedisConstant.AD_DEL_KEY, score,delAdId);
  					LoggerUtil.addTimeLog("===========删除的广告Id============delAdId=" + delAdId);
				} else {
					LoggerUtil.addTimeLog("=======cx=====没有删除的广告Id=============");
				}
			} catch (Exception e) {
				jedisPools.exceptionBroken(jedis);
				e.printStackTrace();
			} finally {
				jedisPools.closeJedis(jedis);
			}
		}
	
	}

}
