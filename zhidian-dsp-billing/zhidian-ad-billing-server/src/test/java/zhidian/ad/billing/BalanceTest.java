package zhidian.ad.billing;

import com.zhidian.ad.billing.service.BalanceService;
import com.zhidian.ad.billing.utils.Md5SignUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by chenwanli on 2016/3/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/spring-context.xml")
public class BalanceTest {

    @Autowired
    private BalanceService balanceService;

    private static long accountId=1L;


    @Test
    public void addBalance(){
        long timeMillis = System.currentTimeMillis();
        String sign = Md5SignUtils.sign(timeMillis, accountId, 100000L);

        int j = balanceService.addBalance(sign, timeMillis, accountId, 100000L);
        System.out.println(j);
    }

    @Test
    public void refreshBalance(){
        long timeMillis = System.currentTimeMillis();
        String sign = Md5SignUtils.sign(timeMillis, accountId, 800000);
        int j = balanceService.addBalance(sign, timeMillis, accountId, 800000);
        System.out.println(j);
    }

    @Test
    public void rechargeBalance(){
        long timeMillis = System.currentTimeMillis();
        String sign = Md5SignUtils.sign(timeMillis,accountId, 100000000000L);
        int j = balanceService.rechargeBalance(sign, timeMillis, accountId, 100000000000L);
        System.out.println(j);
    }




}
