package com.zhidian.ad.billing.utils;

import com.zhidian.ad.billing.constant.Constant;

/**
 * Created by chenwanli on 2016/3/14.
 */
public class JedisKeyUtils {

    /**
     * 获取redis账号key
     *
     * @param accountId 账号id
     * @return
     */
    public static String getAccountKey(long accountId) {
        return Constant.ACCOUNT_KEY_BEGIN + accountId;
    }


    /**
     * 获取redis广告key
     *
     * @param adId 广告id
     * @return
     */
    public static String getAdKey(long accountId,long adId) {
        return new StringBuilder(Constant.AD_KEY_BEGIN).append(accountId).append("_").append(adId).toString();
    }

    /**
     * 获取redis广告日预算key
     *
     * @param adId
     * @return
     */
    public static String getAdDayBudgetKey(Long adId) {
        return Constant.AD_HKEY_DAY_BUDGET_BEGIN + adId;
    }

    /**
     * 获取redis广告日预算log key
     *
     * @param adId
     * @return
     */
    public static String getAdDayBudgetLogKey(Long adId) {
        return Constant.AD_HKEY_DAY_BUDGET_LOG_BEGIN + adId;
    }

    /**
     * 获取redis广告总预算key
     *
     * @param adId
     * @return
     */
    public static String getAdTotalBudgetKey(Long adId) {
        return Constant.AD_HKEY_TOTAL_BUDGET_BEGIN + adId;
    }

    /**
     * 获取redis广告总预算log key
     *
     * @param adId
     * @return
     */
    public static String getAdTotalBudgetLogKey(Long adId) {
        return Constant.AD_HKEY_TOTAL_BUDGET_LOG_BEGIN + adId;
    }


    /**
     * 获取redis账号金额log key
     *
     * @param accountId
     * @return
     */
    public static String getAccountBalanceLogKey(Long accountId) {
        return Constant.ACCOUNT_BALANCE_LOG_KEY_BEGIN + accountId;
    }

    /**
     * 获取redis账号余额key
     *
     * @param accountId
     * @return
     */
    public static String getAccountBalanceKey(Long accountId) {
        return Constant.ACCOUNT_BALANCE_KEY_BEGIN + accountId;
    }

}
