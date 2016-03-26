package com.zhidian.remote.vo.response;

import java.util.Map;

/**
 * Created by cjl on 2016/3/2.
 * 返回广告信息对象
 */
public class AdResponseParam {

    /**
     * 获取到广告 0
     */
    public static final int IS_HAS_AD_TURE = 0;

    /**
     * 获取不到广告 1
     */
    public static final int IS_HAS_AD_FALSE = 1;

    /**
     * 竞价方式 固定 1
     */
    public static final int BID_TYPE_FIX = 1;

    /**
     * 竞价方式 竞价 2
     */
    public static final int BID_TYPE_BIDDING = 2;

    /**
     * 广告类型 1、图片
     */
    public static final int AD_TYPE_PIC = 1;

    /**
     * 广告类型 2、flash
     */
    public static final int AD_TYPE_FLASH = 2;

    /**
     * 广告类型 3、html5
     */
    public static final int AD_TYPE_HTML5 = 3;

    /**
     * 广告类型 4、native
     * 1、图片
     */
    public static final int AD_TYPE_NATIVE = 4;

    /**
     * 广告类型 5、视频
     * 1、图片
     */
    public static final int AD_TYPE_VEDIO = 5;

    /**
     * 广告类型 6、gif
     */
    public static final int AD_TYPE_GIF = 6;


    /**
     * 流水号（调用广告引擎时流水号）
     * 是否必须：是
     */
    private String serialNumber;

    /**
     * 是否获取到广告
     * 是否必须：是
     * 0、获取到
     * 1、获取不到广告
     */
    private Integer isHasAd;

    /**
     * 广告出的竞价
     * (1元=10000单位，例如，15000代表1.5元)
     * 是否必须：是
     */
    private Integer adPrice;

    /**
     * 竞价方式
     * 1、固定
     * 2、竞价
     * 是否必须：是
     */
    private Integer bidType;

    /**
     * 广告类型
     * 是否必须：是
     * 1、图片
     * 2、flash
     * 3、html5
     * 4、native
     * 5、视频
     * 6、gif
     */
    private Integer adType;

    /**
     * 广告宽度
     * 是否必须：是
     */
    private Integer width;

    /**
     * 广告高度
     * 是否必须：是
     */
    private Integer height;

    /**
     * 广告类别
     * 是否必须：是
     * <p/>
     * 1、游戏
     * 2、汽车
     * 3、金融/P2P
     * 4、旅游
     * 5、房地产
     * 6、家居生活
     * 7、母婴
     * 8、奢侈品
     * 9、服装配饰
     * 10、手机数码
     * 11、食品生鲜
     * 12、教育
     * 13、医疗保健
     * 14、艺术与娱乐
     * 15、生活服务
     * 16、农林牧渔
     * 17、票务
     * 18、其他
     */
    private Integer adCategory;

    /**
     * 点击行为
     * 是否必须：是
     */
    private Integer clickType;

    /**
     * 广告落地页
     * 是否必须：否
     */
    private String landingPage;

    /**
     * 素材地址
     * 是否必须：是
     */
    private String adMUrl;

    /**
     * html代码段
     * 是否必须：否
     */
    private String htmlCode;

    /**
     * 广告下载包名
     * 是否必须：否
     */
    private String adPackage;

    /**
     * 广告名称
     * 是否必须：否
     */
    private String adName;

    /**
     * 广告标题
     * 是否必须：否
     */
    private String adTitle;

    /**
     * 广告副标题
     * 是否必须：否
     */
    private String adSubTitle;

    /**
     * 广告图标地址
     * 是否必须：否
     */
    private String iconUrl;

    /**
     * 竞价成功回调地址组(用单引号隔开)
     * 是否必须：是
     */
    private String winUrls;

    /**
     * 曝光回调地址组(用单引号隔开)
     * 是否必须：是
     */
    private String exposureUrls;

    /**
     * 点击回调地址组(用单引号隔开)
     * 是否必须：是
     */
    private String clickUrls;

    /**
     * 激活成功回调地址组(用单引号隔开)
     * 是否必须：否
     */
    private String activeUrls;

    /**
     * 扩展对象
     */
    private Map<String, Object> extendObject;


    public AdResponseParam() {
        super();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getIsHasAd() {
        return isHasAd;
    }

    public void setIsHasAd(Integer isHasAd) {
        this.isHasAd = isHasAd;
    }

    public Integer getAdPrice() {
        return adPrice;
    }

    public void setAdPrice(Integer adPrice) {
        this.adPrice = adPrice;
    }

    public Integer getBidType() {
        return bidType;
    }

    public void setBidType(Integer bidType) {
        this.bidType = bidType;
    }

    public Integer getAdType() {
        return adType;
    }

    public void setAdType(Integer adType) {
        this.adType = adType;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getAdCategory() {
        return adCategory;
    }

    public void setAdCategory(Integer adCategory) {
        this.adCategory = adCategory;
    }

    public Integer getClickType() {
        return clickType;
    }

    public void setClickType(Integer clickType) {
        this.clickType = clickType;
    }

    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }

    public String getAdMUrl() {
        return adMUrl;
    }

    public void setAdMUrl(String adMUrl) {
        this.adMUrl = adMUrl;
    }

    public String getHtmlCode() {
        return htmlCode;
    }

    public void setHtmlCode(String htmlCode) {
        this.htmlCode = htmlCode;
    }

    public String getAdPackage() {
        return adPackage;
    }

    public void setAdPackage(String adPackage) {
        this.adPackage = adPackage;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdSubTitle() {
        return adSubTitle;
    }

    public void setAdSubTitle(String adSubTitle) {
        this.adSubTitle = adSubTitle;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getWinUrls() {
        return winUrls;
    }

    public void setWinUrls(String winUrls) {
        this.winUrls = winUrls;
    }

    public String getExposureUrls() {
        return exposureUrls;
    }

    public void setExposureUrls(String exposureUrls) {
        this.exposureUrls = exposureUrls;
    }

    public String getClickUrls() {
        return clickUrls;
    }

    public void setClickUrls(String clickUrls) {
        this.clickUrls = clickUrls;
    }

    public String getActiveUrls() {
        return activeUrls;
    }

    public void setActiveUrls(String activeUrls) {
        this.activeUrls = activeUrls;
    }

    public Map<String, Object> getExtendObject() {
        return extendObject;
    }

    public void setExtendObject(Map<String, Object> extendObject) {
        this.extendObject = extendObject;
    }
}
