import com.alibaba.fastjson.JSON;
import com.zhidian.remote.vo.request.*;
import com.zhidian.remote.vo.response.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by cjl on 2016/3/16.
 */
public class ParamTest {


    private AdRequestParam getAdRequestParam() {
        AdRequestParam param = new AdRequestParam();
        param.setIp("127.0.0.1");
        param.setSerialNumber("201603230001");
        param.setUserId("youdao001");
        param.setBidType(2);
        param.setMediaPlatformId(3);
        param.setTerminalType(1);
        param.setFeeType(1);

        PageParam page = new PageParam();
        page.setMediaCategory(1);
        page.setPageType(1);
        page.setPageCategory(1);
        param.setPage(page);

        MobileAppParam mobileApp = new MobileAppParam();
        mobileApp.setAppId("123456");
        mobileApp.setAppCategory(1);
        mobileApp.setAppName("天天酷跑");
        mobileApp.setAppPackageName("com.zhidian3g");
        param.setMobileApp(mobileApp);

        MobileParam mobile = new MobileParam();
        mobile.setUuId("ADFA1234");
        mobile.setDeviceOS(1);
        mobile.setDeviceType(1);
        mobile.setAndroidId("androidId");
        mobile.setImei("imei");
        mobile.setImsi("imsi");
        mobile.setMac("mac");
        mobile.setIdfa("idfa");
        mobile.setLang(2);
        mobile.setOrientation(0);
        mobile.setBrand("苹果");
        mobile.setModel("iphone7");
        mobile.setCarrier("移动");
        mobile.setNetworkType(1);
        param.setMobile(mobile);

        List<ImpParam> imps = new ArrayList<ImpParam>();
        ImpParam imp = new ImpParam();
        imp.setImpId("impId001");
        imp.setShowType(1);
        imp.setAdType("1");
        imp.setBidMinimum(15000L);
        imp.setAdPosition(1);
        imp.setAdMulit(1);

        List<ImageAdTypeParam> imageAdType = new ArrayList<ImageAdTypeParam>();
        ImageAdTypeParam imageAdTypeParam = new ImageAdTypeParam();
        imageAdTypeParam.setHeight(320);
        imageAdTypeParam.setWidth(50);
        imageAdType.add(imageAdTypeParam);
        //imp.setImageAdType(imageAdType);

        List<NativeAdTypeParam> nativeAdTypes = new ArrayList<NativeAdTypeParam>();
        NativeAdTypeParam nativeAdType = new NativeAdTypeParam();
        nativeAdType.setPlcmtcnt(1);
        List<AssetParam> assets = new ArrayList<AssetParam>();
        AssetParam asset = new AssetParam();
        asset.setId(1);
        //asset.setIsRequiredAd(1);
        TitleParam title = new TitleParam();
        title.setLen(100);
        ImgParam img = new ImgParam();
        img.setW(320);
        img.setH(50);
        asset.setTitle(title);
        asset.setImg(img);
        assets.add(asset);
        nativeAdType.setAssets(assets);
        nativeAdTypes.add(nativeAdType);
        //imp.setNativeAdTypes(nativeAdTypes);

        imp.setUnSupportAdType("1,2");
        imp.setAdCategory("3,4");
        imp.setUnSupportAdCategory("5,6");
        imp.setClickType(1);
        imps.add(imp);
        imps.add(imp);
        param.setImps(imps);
        return param;
    }

    @Test
    public void adRequestParamToJson() {
        String result = JSON.toJSONString(getAdRequestParam());
        System.out.println(result);
    }



    @Test
    public void adRequestParamFromJson() {
        AdRequestParam param = JSON.parseObject(JSON.toJSONString(getAdRequestParam()), AdRequestParam.class);
        assertEquals("127.0.0.1", param.getIp());
        assertEquals("苹果", param.getMobile().getBrand());
        //assertEquals("100", param.getImps().get(0).getNativeAdTypes().get(0).getAssets().get(0).getTitle().getLen().toString());
    }

    public AdResponseEntity getAdResponseEntity() {
        AdResponseEntity entity = new AdResponseEntity();
        entity.setSerialNumber("201603230001");
        List<ImpBidEntity> impBids = new ArrayList<ImpBidEntity>();
        ImpBidEntity impBid = new ImpBidEntity();
        impBid.setImpId("implId0001");
        List<BidEntity> bids = new ArrayList<BidEntity>();
        BidEntity bidEntity = new BidEntity();
        bidEntity.setIsHasAd(0);
        bidEntity.setAdPrice(15000L);
        bidEntity.setBidType(2);
        bidEntity.setAdType(1);
        List<ImageAdEntity> imageAds = new ArrayList<ImageAdEntity>();
        ImageAdEntity imageAdEntity = new ImageAdEntity();
        imageAdEntity.setWidth(320);
        imageAdEntity.setHeight(100);
        imageAds.add(imageAdEntity);
        //bidEntity.setImageAds(imageAds);
        List<NativeAdEntity> nativeAds = new ArrayList<NativeAdEntity>();
        NativeAdEntity nativeAdEntity = new NativeAdEntity();
        nativeAds.add(nativeAdEntity);
        //bidEntity.setNativeAds(nativeAds);
        bids.add(bidEntity);
        impBid.setBids(bids);
        impBids.add(impBid);
        entity.setImpBids(impBids);
        return entity;
    }

    @Test
    public void adResponseEntityToJson() {
        String result = JSON.toJSONString(getAdResponseEntity());
        System.out.println(result);
    }



    @Test
    public void adResponseEntityFromJson() {
        AdResponseEntity entity = JSON.parseObject(JSON.toJSONString(getAdResponseEntity()), AdResponseEntity.class);
        assertEquals("201603230001", entity.getSerialNumber());
        assertEquals("1", entity.getImpBids().get(0).getBids().get(0).getAdType().toString());
    }
}
