package com.zhidian.ad.billing;

import com.zhidian.ad.billing.service.AdService;
import com.zhidian.ad.billing.service.BalanceService;
import com.zhidian.ad.billing.utils.Md5SignUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by chenwanli on 2016/3/16.
 */
public class BillingTest {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext  context = new ClassPathXmlApplicationContext(new String[] {"consumer.xml"});
        context.start();
        BalanceService balanceService = (BalanceService)context.getBean("balanceService");
        long timeMillis = System.currentTimeMillis();
        String sign = Md5SignUtils.sign(timeMillis, 1, 100000);

        int i = balanceService.addBalance("8036E35A293E9A1EE1CB22C8D5E25881", timeMillis, 1, 100000);
        System.out.println(i);
        int j = balanceService.addBalance(sign, timeMillis, 1, 100000);
        System.out.println(j);

        AdService adService = (AdService)context.getBean("adService");

    }
}
