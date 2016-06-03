package com.zhidian3g.dsp.adPostback.web.action;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian3g.dsp.adPostback.util.Decrypter;
import com.zhidian3g.dsp.adPostback.util.PropertiesUtil;
import com.zhidian3g.dsp.adPostback.util.ZYPricedec;

/**
 * Created by cjl on 2016/3/3.
 */
public class AdPostbackHelper {

    public static final String ZHISHU_DSP_MD5_KEY = PropertiesUtil.getValue(PropertiesUtil.MD5_SIGN_KEY);

    public static final String ADVIEW_SECRETKEY = PropertiesUtil.getValue(PropertiesUtil.ADVIEW_ENCKEYSTR);

    public static final String ADVIEW_SIGNKEY = PropertiesUtil.getValue(PropertiesUtil.ADVIEW_SIGNKEYSTR);


    /**
     * 根据不同ADX解析价格
     * @param priceStr
     * @param mediaId
     * @return
     */
    public static Long getPrice(String priceStr, Integer mediaId) {
    	Long price = null;
        try {
            if (mediaId.intValue() == 2) {// adView
                price = getAdViewPrice(priceStr);
            } else if (mediaId.intValue() == 4) {// 不需要
                price = getZPlayPrice(priceStr);
            } else {
                price = Long.valueOf(priceStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    /**
     * 获取解析ADView 竞价成功价格
     * 解密参考：https://developers.google.com/ad-exchange/rtb/response-guide/decrypt-price?hl=zh-CN
     * 编码格式的CPM或CPC价格*10000，如价格为CPM价格0.6元，则取值0.6*10000=6000。在这种情况下，WIN_PRICE的解码值为 6000。
     * 如果在投放广告时还不知道展示费用，该宏将被字符串 UNKNOWN 代替。
     * @param priceStr
     * @return
     */
    private static Long getAdViewPrice(String priceStr) {
        Long result = null;
        try {
            byte[] bytes = Decrypter.decryptSafeWebStringToByte(priceStr, ADVIEW_SECRETKEY, ADVIEW_SIGNKEY);
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
            result = dis.readLong();
        } catch (Exception e) {
        	CommonLoggerUtil.addExceptionLog(e);
        	CommonLoggerUtil.addErrQuest("adview价格解密出错=" + priceStr);
            result = 0l;
        }
        return result;
    }

    /**
     * 
     * 获取掌游竞价成功价格
     * @param priceStr
     * @return
     */
    private static ZYPricedec zyPricedec = ZYPricedec.getInstance();
    private static Long getZPlayPrice(String priceStr) {
        Double result = zyPricedec.decode(priceStr);
        long value = 0l;
        try {
			value = result.longValue();
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
			CommonLoggerUtil.addErrQuest("掌游价格解密出错=" + priceStr);
		}
        return value;
    }
    
    public static void main(String[] args) {
    	AdPostbackHelper.getZPlayPrice("NjkgV8PnZ8c21jta8SXY5A");
	}
}
