package com.zhidian3g.nextad.test;

import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import com.zhidian3g.common.constant.RedisConstant;
import com.zhidian3g.nextad.redisClient.JedisPools;


/**
 * 权重轮询调度算法(WeightedRound-RobinScheduling)-Java实现
 * @author huligong
 * */
public class TestOk {
	private  WeightedRoundRobinScheduling weightedRoundRobinScheduling = null;
	
    private static TestOk  testOk = new TestOk();// 上一次选择的服务器
    
    private TestOk() {
    	weightedRoundRobinScheduling = new WeightedRoundRobinScheduling();
    	Jedis jedis = JedisPools.getInstance().getJedis();
    	Set<Tuple> adTuple = jedis.zrangeWithScores(RedisConstant.AD_ANDROID_IDS, 0, -1);
    	weightedRoundRobinScheduling.init(adTuple);
    };
    
    public static TestOk getInstance() {
    	return testOk;
    }
   
}