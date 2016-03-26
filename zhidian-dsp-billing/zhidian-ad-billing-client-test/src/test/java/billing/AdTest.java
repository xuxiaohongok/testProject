package billing;

import com.zhidian.ad.billing.constant.BillingType;
import com.zhidian.ad.billing.service.AdService;
import com.zhidian.ad.billing.service.BalanceService;
import com.zhidian.ad.billing.utils.Md5SignUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by chenwanli on 2016/3/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:consumer.xml")
public class AdTest {

    private static long accountId = 3L;

    private static long adId = 3L;

    @Autowired
    private AdService adService;


    @Test
    public void addAd() {
        long timeMillis = System.currentTimeMillis();
        String sign = Md5SignUtils.sign(timeMillis, accountId, adId, 1000, 1000);

        int j = adService.addAd(sign, timeMillis, accountId, adId, 1000, 1000);
        System.out.println(j);
    }

    @Test
    public void refreshAd() {
        long timeMillis = System.currentTimeMillis();
        String sign = Md5SignUtils.sign(timeMillis, accountId, adId, 10000, 10000);

        int j = adService.refreshAd(sign, timeMillis, accountId, adId, 10000, 10000);
        System.out.println(j);
    }


    @Test
    public void rebate() {
        long timeMillis = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            String sign = Md5SignUtils.sign(timeMillis, accountId, adId, 10000, BillingType.CPM);
            int j = adService.rebate(sign, timeMillis, accountId, adId, 10000, BillingType.CPM);
            System.out.println(j);
        }
    }

    @Test
    public void isUseUp() {
        long timeMillis = System.currentTimeMillis();
        String sign = Md5SignUtils.sign(timeMillis, accountId, adId);
        int j = adService.isUseUp(sign, timeMillis, accountId, adId);
        System.out.println(j);
    }


}
