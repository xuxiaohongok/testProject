package com.zhidian.ad.billing.service.impl;

import com.zhidian.ad.billing.constant.BillingType;
import com.zhidian.ad.billing.constant.LoggerEnum;
import com.zhidian.ad.billing.constant.ReturnCode;
import com.zhidian.ad.billing.manager.AdManager;
import com.zhidian.ad.billing.manager.BalanceManager;
import com.zhidian.ad.billing.utils.JedisKeyUtils;
import com.zhidian.ad.billing.service.AdService;
import com.zhidian.ad.billing.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by chenwanli on 2016/3/14.
 */
@Service("adService")
public class AdServiceImpl implements AdService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 添加广告
     *
     * @param accountId   账号id
     * @param adId        广告id
     * @param dayBudget   日预算
     * @param totalBudget 总预算
     * @return
     */
    public int addAd(String sign, long datetime, long accountId, long adId, long dayBudget, long totalBudget) {
        //校验签名
        int signCheck = SignUtils.checkSign(sign, datetime, accountId, adId, dayBudget, totalBudget);
        if (signCheck != ReturnCode.SUCCESS_CODE) {
            return signCheck;
        }


        Jedis jedis = jedisPool.getResource();
        try {

            //账号是否存在
            if (!jedis.exists(JedisKeyUtils.getAccountKey(accountId))) {
                BalanceManager.newBalance(accountId, 0, jedis);
            }

            //广告是否存在
            if (!jedis.exists(JedisKeyUtils.getAdKey(accountId, adId))) {
                //新增
                return AdManager.addAd(accountId, adId, dayBudget, totalBudget, jedis);
            } else {
                //修改
                return AdManager.refreshAd(accountId, adId, dayBudget, totalBudget, jedis);
            }


        } catch (Exception e) {
            LoggerUtil.setExceptionLog(e);
            return ReturnCode.ERROR_CODE;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }

    }


    /**
     * 修改广告日预算
     *
     * @param accountId   账号id
     * @param adId        广告id
     * @param dayBudget   日预算
     * @param totalBudget 总预算
     * @return
     */

    public int refreshAd(String sign, long datetime, long accountId, long adId, long dayBudget, long totalBudget) {
        return addAd(sign, datetime, accountId, adId, dayBudget, totalBudget);
    }


    /**
     * 扣费
     *
     * @param accountId 账号id
     * @param adId      广告id
     * @param cost      扣减金额
     * @return
     */
    public int rebate(String sign, long datetime, long accountId, long adId, int cost, BillingType type) {
        //校验签名
        int signCheck = SignUtils.checkSign(sign, datetime, accountId, adId, cost, type);
        if (signCheck != ReturnCode.SUCCESS_CODE) {
            return signCheck;
        }

        Jedis jedis = jedisPool.getResource();
        try {

            //账号是否存在
            if (!jedis.exists(JedisKeyUtils.getAccountKey(accountId))) {
                return ReturnCode.ACCOUNT_NOT_FOUND_CODE;
            }

            //广告是否存在
            if (!jedis.exists(JedisKeyUtils.getAdKey(accountId, adId))) {
                return ReturnCode.AD_NOT_FOUND_CODE;
            } else {
                //扣费
                return AdManager.rebate(accountId, adId, cost, type, jedis);
            }

        } catch (Exception e) {
            LoggerUtil.setExceptionLog(e);
            return ReturnCode.ERROR_CODE;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * 账号是否可用
     * @param sign
     * @param datetime
     * @param accountId
     * @param adId
     * @return
     */
    public int isUseUp(String sign, long datetime, long accountId, long adId) {
        //校验签名
        int signCheck = SignUtils.checkSign(sign, datetime, accountId, adId);
        if (signCheck != ReturnCode.SUCCESS_CODE) {
            return signCheck;
        }
        Jedis jedis = jedisPool.getResource();
        try {

            //账号是否存在
            if (!jedis.exists(JedisKeyUtils.getAccountKey(accountId))) {
                return ReturnCode.ACCOUNT_NOT_FOUND_CODE;
            }

            //广告是否存在
            if (!jedis.exists(JedisKeyUtils.getAdKey(accountId, adId))) {
                return ReturnCode.AD_NOT_FOUND_CODE;
            } else {
                //判断是否可用
                return AdManager.isUseUp(accountId, adId, jedis);
            }

        } catch (Exception e) {
            LoggerUtil.setExceptionLog(e);
            return ReturnCode.ERROR_CODE;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }


}
