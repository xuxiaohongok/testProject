package com.zhidian.ad.billing.service;

/**
 * Created by chenwanli on 2016/3/14.
 */
public interface BalanceService {

    /**
     * 添加账号
     * @param accountId 账号id
     * @param adfees 账号余额
     * @return
     */
    int addBalance(String sign,long datetime,long accountId,long adfees);

    /**
     * 充值账号
     * @param accountId 账号id
     * @param rechargeFees 充值数目
     * @return
     */
    int rechargeBalance(String sign,long datetime,long accountId,long rechargeFees);



}
