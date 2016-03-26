package com.zhidian.ad.billing.manager;

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
 * Created by chenwanli on 2016/3/14.
 */
public class BalanceManager {



    /**
     * 充值账号
     * @param id
     * @param rechargeBalance
     * @param jedis
     * @return
     */
    public static int rechargeBalance(long id, long rechargeBalance,Jedis jedis) {
        Pipeline pipelined = jedis.pipelined();

        //账户余额(用于竞价扣减)
        pipelined.hincrBy(JedisKeyUtils.getAccountKey(id),JedisKeyUtils.getAccountBalanceKey(id), rechargeBalance);
        //记录账户金额，用于变动时计算
        pipelined.hincrBy(JedisKeyUtils.getAccountKey(id),JedisKeyUtils.getAccountBalanceLogKey(id), rechargeBalance);

        List<Object> results =  pipelined.syncAndReturnAll();
        //检查redis返回结果是否异常
        String resultErr = RedisCheckUtils.getResultErr(results);
        if(resultErr!=null){
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("充值账号id：%d  金额：%d 异常 redis返回%s",id,rechargeBalance,resultErr));
            return ReturnCode.ERROR_CODE;
        }

        LoggerUtil.addLogegerMessage(LoggerEnum.API_REQUEST_LOG, String.format("账号id：%d 充值：%d 成功",id,rechargeBalance));
        return ReturnCode.SUCCESS_CODE;

    }


    /**
     * 添加账号
     * @param id
     * @param adfees
     * @param jedis
     * @return
     */
    public static int newBalance(long id, long adfees, Jedis jedis) {
        Pipeline pipelined = jedis.pipelined();
        //账户余额(用于竞价扣减)
        pipelined.hset(JedisKeyUtils.getAccountKey(id),JedisKeyUtils.getAccountBalanceKey(id), adfees + "");

        //记录账户金额，用于变动时计算
        pipelined.hset(JedisKeyUtils.getAccountKey(id),JedisKeyUtils.getAccountBalanceLogKey(id), adfees + "");

        List<Object> results = pipelined.syncAndReturnAll();
        //检查redis返回结果是否异常
        String resultErr = RedisCheckUtils.getResultErr(results);
        if(resultErr!=null){
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("账号id：%d  金额：%d 添加异常 redis返回%s",id,adfees,resultErr));
            return ReturnCode.ERROR_CODE;
        }

        LoggerUtil.addLogegerMessage(LoggerEnum.API_REQUEST_LOG, String.format("添加账号id：%d  金额：%d 成功",id,adfees));
        return ReturnCode.SUCCESS_CODE;
    }

    /**
     * 修改账号
     * @param id
     * @param adfees
     * @param jedis
     * @return
     */
    public static int refreshBalance(long id, long adfees, Jedis jedis) {
        String balanceLogStr = jedis.hget(JedisKeyUtils.getAccountKey(id),JedisKeyUtils.getAccountBalanceLogKey(id));
        if(NumberUtils.isNumber(balanceLogStr)){

            long balanceLog = Long.parseLong(balanceLogStr);
            //未修改值，直接返回
            if(balanceLog==adfees){
                return ReturnCode.SUCCESS_CODE;
            }

            //账户金额变化值
            long change=adfees-balanceLog;

            Pipeline pipelined = jedis.pipelined();
            //账户余额(用于竞价扣减)
            pipelined.hincrBy(JedisKeyUtils.getAccountKey(id),JedisKeyUtils.getAccountBalanceKey(id), change);
            //记录账户金额，用于变动时计算
            pipelined.hincrBy(JedisKeyUtils.getAccountKey(id),JedisKeyUtils.getAccountBalanceLogKey(id), change);

            List<Object> results = pipelined.syncAndReturnAll();

            //检查redis返回结果是否异常
            String resultErr = RedisCheckUtils.getResultErr(results);
            if(resultErr!=null){
                LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("刷新账号id：%d  金额：%d 异常 redis返回%s",id,adfees,resultErr));
                return ReturnCode.ERROR_CODE;
            }

            LoggerUtil.addLogegerMessage(LoggerEnum.API_REQUEST_LOG, String.format("修改账号id：%d 原金额 %d --> 金额：%d 成功",id,balanceLog,adfees));
            return ReturnCode.SUCCESS_CODE;
        }else{
            LoggerUtil.addLogegerMessage(LoggerEnum.EXCEPTION_LOG, String.format("修改账号id：%d 至金额：%d 失败，账号原有金额记录异常",id,adfees));
            return ReturnCode.ERROR_CODE;
        }
    }

    public static int deduct(long accountId, long cost, Jedis jedis) {

        //记录账户金额，用于变动时计算
        Long aLong = jedis.hincrBy(JedisKeyUtils.getAccountKey(accountId), JedisKeyUtils.getAccountBalanceLogKey(accountId), cost);
        LoggerUtil.addLogegerMessage(LoggerEnum.API_REQUEST_LOG, String.format("账号id：%d 统计后扣费金额：%d 成功",accountId,cost));
        return ReturnCode.SUCCESS_CODE;
    }
}
