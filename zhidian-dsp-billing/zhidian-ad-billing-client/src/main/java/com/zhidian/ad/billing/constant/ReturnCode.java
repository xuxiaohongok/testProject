package com.zhidian.ad.billing.constant;

/**
 * Created by chenwanli on 2016/3/16.
 */
public class ReturnCode {

    /**
     * 公共返回
     */
    //成功
    public static final int SUCCESS_CODE = 1;
    //异常返回
    public static final int ERROR_CODE = 0;
    //签名错误
    public static final int SIGN_ERROR_CODE = 400;
    //签名超时
    public static final int SIGN_OVERTIME_ERROR_CODE = 401;

    public static final int ACCOUNT_NOT_FOUND_CODE=-1;

    public static final int AD_NOT_FOUND_CODE=-2;


    /**
     * 充值接口
     */
    //必须为正整数
    public static final int MUST_POSITIVE_INTEGER_CODE = -1;

    //充值成功，但是账号不存在
    public static final int SUCCESS_AND_ACCOUNT_NOT_EXISTS_CODE = 2;


    /**
     * 扣费-是否可用
     */
    //余额不足
    public static final int BALANCE_NOT_ENOUGH_CODE=2;
    //日预算不足
    public static final int DAY_BUDGET_NOT_ENOUGH_CODE=3;
    //总预算不足
    public static final int TOTAL_BUDGET_NOT_ENOUGH_CODE=4;


}
