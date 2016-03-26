package com.zhidian.ad.billing.manager;

import com.zhidian.ad.billing.constant.BillingType;
import com.zhidian.ad.billing.constant.Constant;
import com.zhidian.ad.billing.constant.LoggerEnum;
import com.zhidian.ad.billing.constant.ReturnCode;
import com.zhidian.ad.billing.utils.JedisKeyUtils;
import com.zhidian.ad.billing.utils.LoggerUtil;
import com.zhidian.ad.billing.utils.RedisCheckUtils;
import org.apache.commons.lang3.math.NumberUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.List;

/**
 * Created by chenwanli on 2016/3/16.
 */
public class AdManager {


    /**
     * 刷新广告
     *
     * @param adId
     * @param dayBudget
     * @param totalBudget
     * @param jedis
     * @return
     */
    public static int refreshAd(long accountId, long adId, long dayBudget, long totalBudget, Jedis jedis) {
        Pipeline pipelined = jedis.pipelined();

        //获取日预算，总预算
        pipelined.hget(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdDayBudgetLogKey(adId));
        pipelined.hget(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdTotalBudgetLogKey(adId));
        List<Object> values = pipelined.syncAndReturnAll();
        for (Object value : values) {
            if (!NumberUtils.isNumber(value.toString())) {
                LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("修改广告---账号id：%d 广告id：%d 日预算:%d,总预算：%d 失败，获取原日预算总预算失败", accountId, adId, dayBudget, totalBudget));
                return ReturnCode.ERROR_CODE;
            }
        }

        Long dayBudgetLog = Long.parseLong(values.get(0).toString());
        Long totalBudgetLog = Long.parseLong(values.get(1).toString());

        //新增日预算
        pipelined.hset(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdDayBudgetKey(adId), String.valueOf(dayBudget));
        pipelined.hincrBy(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdDayBudgetLogKey(adId), dayBudget - dayBudgetLog);
        //新增总预算
        pipelined.hset(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdTotalBudgetKey(adId), String.valueOf(totalBudget));
        pipelined.hincrBy(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdTotalBudgetLogKey(adId), totalBudget - totalBudgetLog);

        List<Object> results = pipelined.syncAndReturnAll();
        //检查redis返回结果是否异常
        String resultErr = RedisCheckUtils.getResultErr(results);
        if(resultErr!=null){
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("修改广告---账号id：%d 广告id：%d 日预算:%d-->%d,总预算：%d-->%d  异常 redis返回%s",accountId, adId, dayBudgetLog, dayBudget, totalBudgetLog, totalBudget,resultErr));
            return ReturnCode.ERROR_CODE;
        }

        LoggerUtil.addLogegerMessage(LoggerEnum.API_REQUEST_LOG, String.format("修改广告---账号id：%d 广告id：%d 日预算:%d-->%d,总预算：%d-->%d 成功", accountId, adId, dayBudgetLog, dayBudget, totalBudgetLog, totalBudget));
        return ReturnCode.SUCCESS_CODE;
    }

    /**
     * 添加广告
     *
     * @param adId
     * @param dayBudget
     * @param totalBudget
     * @param jedis
     * @return
     */
    public static int addAd(long accountId, long adId, long dayBudget, long totalBudget, Jedis jedis) {
        Pipeline pipelined = jedis.pipelined();
        //新增日预算
        pipelined.hset(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdDayBudgetKey(adId), String.valueOf(dayBudget));
        pipelined.hset(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdDayBudgetLogKey(adId), String.valueOf(dayBudget));
        //新增总预算
        pipelined.hset(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdTotalBudgetKey(adId), String.valueOf(totalBudget));
        pipelined.hset(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdTotalBudgetLogKey(adId), String.valueOf(totalBudget));

        List<Object> results = pipelined.syncAndReturnAll();

        //检查redis返回结果是否异常
        String resultErr = RedisCheckUtils.getResultErr(results);
        if(resultErr!=null){
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("添加广告---账号id：%d 广告id：%d 日预算:%d,总预算：%d 异常 redis返回%s",accountId, adId, dayBudget, totalBudget,resultErr));
            return ReturnCode.ERROR_CODE;
        }

        LoggerUtil.addLogegerMessage(LoggerEnum.API_REQUEST_LOG, String.format("添加广告---账号id：%d 广告id：%d 日预算:%d,总预算：%d 成功", accountId, adId, dayBudget, totalBudget));
        return ReturnCode.SUCCESS_CODE;
    }

    /**
     * 扣费
     *
     * @param adId
     * @param cost
     * @param jedis
     * @return
     */
    public static int rebate(long accountId, long adId, int cost, BillingType type, Jedis jedis) {
        //redis hash没有hdecrBy,增加负数
        int change = 0 - cost;
        //cpm按千次计费
        if (type.equals(BillingType.CPM)) {
            change = change / 1000;
        } else {
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("账号id：%d 广告id：%d 扣费：%d  扣减异常 未知的付费类型%d", accountId, adId, cost, type.ordinal()));
            return ReturnCode.ERROR_CODE;
        }
        Pipeline pipelined = jedis.pipelined();
        //扣减账户余额
        pipelined.hincrBy(JedisKeyUtils.getAccountKey(accountId), JedisKeyUtils.getAccountBalanceKey(accountId), change);
        //扣减日预算
        pipelined.hincrBy(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdDayBudgetKey(adId), change);
        //扣减总预算
        pipelined.hincrBy(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdTotalBudgetKey(adId), change);
        List<Object> results = pipelined.syncAndReturnAll();


        //检查扣费是否成功，如失败，记录失败日志
        return checkRebateResult(accountId, adId, cost, type, results);
    }

    /**
     * 检查扣费情况
     *
     * @param accountId
     * @param adId
     * @param cost
     * @param type
     * @param results
     * @return
     */
    private static int checkRebateResult(long accountId, long adId, int cost, BillingType type, List<Object> results) {
        Long value0 = toLong(results.get(0).toString(), null);
        if (value0 != null) {
            if (value0 <= Constant.MIN_BALANCE) {
                return ReturnCode.BALANCE_NOT_ENOUGH_CODE;
            }
        } else {
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("账号id：%d 广告id：%d 扣费：%d 扣费类型：%s 扣减账户余额异常 异常信息：%s", accountId, adId, cost, type.name(), results.get(0).toString()));
        }

        Long value1 = toLong(results.get(1).toString(), null);
        if (value1 != null) {
            if (value1 <= Constant.MIN_DAY_BUDGET) {
                return ReturnCode.DAY_BUDGET_NOT_ENOUGH_CODE;
            }
        } else {
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("账号id：%d 广告id：%d 扣费：%d 扣费类型：%s 扣减广告日预算异常 异常信息：%s", accountId, adId, cost, type.name(), results.get(1).toString()));
        }

        Long value2 = toLong(results.get(2).toString(), null);
        if (value2 != null) {
            if (value2 <= Constant.MIN_TOTAL_BUDGET) {
                return ReturnCode.TOTAL_BUDGET_NOT_ENOUGH_CODE;
            }
        } else {
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("账号id：%d 广告id：%d 扣费：%d 扣费类型：%s 扣减广告总预算异常 异常信息：%s", accountId, adId, cost, type.name(), results.get(2).toString()));
        }

        LoggerUtil.addLogegerMessage(LoggerEnum.API_REQUEST_LOG, String.format("账号id：%d 广告id：%d 扣费：%d 扣费类型：%s 扣减成功", accountId, adId, cost, type.name()));
        return ReturnCode.SUCCESS_CODE;
    }


    public static int isUseUp(long accountId, long adId, Jedis jedis) {
        Pipeline pipelined = jedis.pipelined();

        pipelined.hget(JedisKeyUtils.getAccountKey(accountId), JedisKeyUtils.getAccountBalanceKey(accountId));
        pipelined.hget(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdDayBudgetKey(adId));
        pipelined.hget(JedisKeyUtils.getAdKey(accountId, adId), JedisKeyUtils.getAdTotalBudgetKey(adId));

        List<Object> results = pipelined.syncAndReturnAll();

        return checkIsUseUpResult(accountId, adId, results);
    }

    /**
     * 是否可用---检测查询返回结果
     *
     * @param accountId
     * @param adId
     * @param results
     * @return
     */
    private static int checkIsUseUpResult(long accountId, long adId, List<Object> results) {
        Long value0 = toLong(results.get(0).toString(), null);
        if (value0 != null) {
            if (value0 <= Constant.MIN_BALANCE) {
                return ReturnCode.BALANCE_NOT_ENOUGH_CODE;
            }
        } else {
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("账号id：%d 广告id：%d 判断广告是否可用，检查账户余额异常 异常信息：%s", accountId, adId, results.get(0).toString()));
        }

        Long value1 = toLong(results.get(1).toString(), null);
        if (value1 != null) {
            if (value1 <= Constant.MIN_DAY_BUDGET) {
                return ReturnCode.DAY_BUDGET_NOT_ENOUGH_CODE;
            }
        } else {
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("账号id：%d 广告id：%d 判断广告是否可用，检查日预算异常 异常信息：%s", accountId, adId, results.get(1).toString()));
        }

        Long value2 = toLong(results.get(2).toString(), null);
        if (value2 != null) {
            if (value2 <= Constant.MIN_TOTAL_BUDGET) {
                return ReturnCode.TOTAL_BUDGET_NOT_ENOUGH_CODE;
            }
        } else {
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("账号id：%d 广告id：%d 判断广告是否可用，检查总预算异常 异常信息：%s", accountId, adId, results.get(2).toString()));
        }

        return ReturnCode.SUCCESS_CODE;
    }

    /**
     * string to long
     *
     * @param str
     * @param defaultValue 默认值
     * @return
     */
    private static long toLong(String str, Long defaultValue) {
        if (str == null) {
            return defaultValue;
        } else {
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException var4) {
                return defaultValue;
            }
        }
    }
}
