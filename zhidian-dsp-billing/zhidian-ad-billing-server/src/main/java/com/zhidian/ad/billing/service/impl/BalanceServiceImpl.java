package com.zhidian.ad.billing.service.impl;

import com.zhidian.ad.billing.constant.LoggerEnum;
import com.zhidian.ad.billing.constant.ReturnCode;
import com.zhidian.ad.billing.service.BalanceService;
import com.zhidian.ad.billing.manager.BalanceManager;
import com.zhidian.ad.billing.utils.JedisKeyUtils;
import com.zhidian.ad.billing.utils.LoggerUtil;
import com.zhidian.ad.billing.utils.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by chenwanli on 2016/3/14.
 */
@Service("balanceService")
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 添加账号
     * @param accountId 账号id
     * @param adfees 账号余额
     * @return
     */
    public int addBalance(String sign,long datetime,long accountId, long adfees) {
        //校验签名
        int signCheck = SignUtils.checkSign(sign, datetime, accountId, adfees);
        if(signCheck!=ReturnCode.SUCCESS_CODE){
            return signCheck;
        }

        Jedis jedis = jedisPool.getResource();
        try {

            //账号是否存在
            if (!jedis.exists(JedisKeyUtils.getAccountKey(accountId))) {
                return BalanceManager.newBalance(accountId, adfees, jedis);
            } else {
                //如果账号存在，查找之前的账号金额，获取变化数值，修改相应账号余额
                return BalanceManager.refreshBalance(accountId, adfees, jedis);
            }
        }catch (Exception e) {
            LoggerUtil.setExceptionLog(e);
            return ReturnCode.ERROR_CODE;
        } finally {
            if(jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * 充值账号
     * @param accountId 账号id
     * @param rechargeFees 充值数目
     * @return
     */
    public int rechargeBalance(String sign,long datetime,long accountId, long rechargeFees) {
        //校验签名
        int signCheck = SignUtils.checkSign(sign, datetime, accountId, rechargeFees);
        if(signCheck!=ReturnCode.SUCCESS_CODE){
            return signCheck;
        }

        Jedis jedis = jedisPool.getResource();
        //充值数额需要大于0
        if(rechargeFees<=0){
            LoggerUtil.addLogegerMessage(LoggerEnum.ERR_REQUEST_LOG,String.format("账号：%d 充值：%d 异常，数目为负数",accountId,rechargeFees));
            return ReturnCode.MUST_POSITIVE_INTEGER_CODE;
        }

        try {
            //账号是否存在
            if (!jedis.exists(JedisKeyUtils.getAccountKey(accountId))) {
                BalanceManager.newBalance(accountId, rechargeFees, jedis);
                LoggerUtil.addLogegerMessage(LoggerEnum.ERR_REQUEST_LOG,String.format("账号：%d 充值：%d 异常，不存在的账号",accountId,rechargeFees));
                return ReturnCode.SUCCESS_AND_ACCOUNT_NOT_EXISTS_CODE;
            } else {
                //充值
                return  BalanceManager.rechargeBalance(accountId, rechargeFees, jedis);

            }
        } catch (Exception e) {
            LoggerUtil.setExceptionLog(e);
            return ReturnCode.ERROR_CODE;
        }finally {
            if(jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * 统计后扣费
     * @param sign
     * @param datetime
     * @param accountId
     * @param cost 扣费金额
     * @return
     */
    public int  deduct(String sign,long datetime,long accountId, long cost) {

        //校验签名
        int signCheck = SignUtils.checkSign(sign, datetime, accountId, cost);
        if(signCheck!=ReturnCode.SUCCESS_CODE){
            return signCheck;
        }

        Jedis jedis = jedisPool.getResource();

        try {
            //账号是否存在
            if (!jedis.exists(JedisKeyUtils.getAccountKey(accountId))) {
                return ReturnCode.ACCOUNT_NOT_FOUND_CODE;
            } else {
                //充值
                return  BalanceManager.deduct(accountId, cost, jedis);

            }
        } catch (Exception e) {
            LoggerUtil.setExceptionLog(e);
            return ReturnCode.ERROR_CODE;
        }finally {
            if(jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
    }

}
