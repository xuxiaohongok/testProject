package com.zhidian.ad.billing.constant;

/**
 * Created by chenwanli on 2016/3/14.
 */
public class Constant {

    public static final String ACCOUNT_KEY_BEGIN="account_";

    public static final String AD_KEY_BEGIN="ad_";

    public static final String AD_HKEY_DAY_BUDGET_BEGIN="day_budget_";

    public static final String AD_HKEY_DAY_BUDGET_LOG_BEGIN="day_budget_log_";

    public static final String AD_HKEY_TOTAL_BUDGET_BEGIN="total_budget_";

    public static final String AD_HKEY_TOTAL_BUDGET_LOG_BEGIN="total_budget_log_";



    public  static final String ACCOUNT_BALANCE_LOG_KEY_BEGIN="account_balance_log_";

    public  static final String ACCOUNT_BALANCE_KEY_BEGIN="account_balance_";


    //最小余额，低于此值返回余额不足
    public static final int MIN_BALANCE=0;
    //最小日预算，低于此值返回日预算不足
    public static final int MIN_DAY_BUDGET=0;
    //最小总预算，低于此值返回总预算不足
    public static final int MIN_TOTAL_BUDGET=0;

    //签名超时时间（5分钟）
    public static final long SIGN_OVERTIME_SIZE=300000;





}
