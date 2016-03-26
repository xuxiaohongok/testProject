package com.zhidian.ad.billing.service;

import com.zhidian.ad.billing.constant.BillingType;

/**
 * Created by chenwanli on 2016/3/14.
 */
public interface AdService {

    /**
     * 添加广告
     * @param accountId 账号id
     * @param adId 广告id
     * @param dayBudget 日预算
     * @param totalBudget 总预算
     * @return
     */
    int addAd(String sign,long datetime,long accountId,long adId,long dayBudget,long totalBudget);

    /**
     * 修改广告日预算
     * @param accountId 账号id
     * @param adId 广告id
     * @param dayBudget 日预算
     * @param totalBudget 总预算
     * @return
     */
    int refreshAd(String sign,long datetime,long accountId,long adId,long dayBudget,long totalBudget);

    /**
     * 扣费
     * @param accountId 账号id
     * @param adId 广告id
     * @param cost 扣减金额
     * @return
     */
    int rebate(String sign,long datetime,long accountId, long adId, int cost,BillingType type);

    /**
     * 是否可用
     * @param sign
     * @param datetime
     * @param accountId
     * @param adId
     * @return
     */
    int isUseUp(String sign,long datetime,long accountId, long adId);
}
