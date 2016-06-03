import java.io.IOException;

import org.junit.Test;

import com.zhidian3g.dsp.adPostback.util.HttpUtil;

/**
 * Created by cjl on 2016/3/7.
 */
public class AdPostbackControllerTest {

    public static final String SITE_URL = "http://localhost:8080/";


    /*@Test
    public void sendWinNoticeRequest() throws IOException {

        String baseUrl = SITE_URL + "/winNotice.shtml";
        String id = "1";
        Long price = 888L;
        WinNoticeParam param = new WinNoticeParam();
        param.setAdId(1L);
        param.setCreativePackageId(2L);
        param.setFeeType(1);
        param.setLandingPageId(3L);
        param.setMediaCategory(1);
        param.setMediaPlatformId(1);
        param.setRequestTime(DateUtil.format(new Date()));
        String data = JSON.toJSONString(param);
        String sign = MD5Util.encodeLowerCase(data + AdPostbackHelper.ZHISHU_DSP_MD5_KEY, "UTF-8");
        data = URLEncoder.encode(data, "UTF-8");
        StringBuffer url = new StringBuffer(baseUrl)
                .append("?id=").append(id)
                .append("&price=").append(price)
                .append("&data=").append(data)
                .append("&sign=").append(sign);
        HttpUtil.doGet(url.toString());
    }

    @Test
    public void sendAdExposureRequest() throws IOException {

        String baseUrl = SITE_URL + "/adExposure.shtml";
        String id = "1";
        Long price = 888L;
        WinNoticeParam param = new WinNoticeParam();
        param.setAdId(1L);
        param.setCreativePackageId(2L);
        param.setFeeType(1);
        param.setLandingPageId(3L);
        param.setMediaCategory(1);
        param.setMediaPlatformId(1);
        param.setRequestTime(DateUtil.format(new Date()));
        String data = JSON.toJSONString(param);
        String sign = MD5Util.encodeLowerCase(data + AdPostbackHelper.ZHISHU_DSP_MD5_KEY, "UTF-8");
        data = URLEncoder.encode(data, "UTF-8");
        StringBuffer url = new StringBuffer(baseUrl)
                .append("?id=").append(id)
                .append("&price=").append(price)
                .append("&data=").append(data)
                .append("&sign=").append(sign);
        HttpUtil.doGet(url.toString());
    }

    @Test
    public void sendAdClickRequest() throws IOException {

        String baseUrl = SITE_URL + "/adClick.shtml";
        String id = "1";
        Long price = 888L;
        WinNoticeParam param = new WinNoticeParam();
        param.setAdId(1L);
        param.setCreativePackageId(2L);
        param.setFeeType(1);
        param.setLandingPageId(3L);
        param.setMediaCategory(1);
        param.setMediaPlatformId(1);
        param.setRequestTime(DateUtil.format(new Date()));
        String data = JSON.toJSONString(param);
        String sign = MD5Util.encodeLowerCase(data + AdPostbackHelper.ZHISHU_DSP_MD5_KEY, "UTF-8");
        data = URLEncoder.encode(data, "UTF-8");
        StringBuffer url = new StringBuffer(baseUrl)
                .append("?id=").append(id)
                .append("&price=").append(price)
                .append("&data=").append(data)
                .append("&sign=").append(sign);
        System.out.println(HttpUtil.doGet(url.toString()));
    }*/

    @Test
    public void doRequest() {
        String toUrl = "http://postback.nextading.com/adExposure.shtml?adxId=2&mediaId=2&userId=123&requestId=20160311-145759_bidreq_201-1125-6Rhb-1&adId=1&adSlotType=1&createId=1&landingPageId=1&height=100&width=640&requestAdDateTime=2016-03-21_10:47:39&price=%%WIN_PRICE%%";
        try {
            System.out.println(HttpUtil.doGet(toUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
