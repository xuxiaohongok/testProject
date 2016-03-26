package com.zhidian.remote.vo.response;

import java.util.Map;

/**
 * Created by cjl on 2016/3/23.
 * imageAd图片
 */
public class ImageAdEntity {

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
     * 广告下载包名
     * 是否必须：否
     */
    private String adPackage;

    /**
     * 广告apk包下载地址
     * 当clickType为1时，必须
     */
    private String adDownload;


    /**
     * 素材地址
     * 是否必须：是
     */
    private String adMUrl;

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

    public String getAdPackage() {
        return adPackage;
    }

    public void setAdPackage(String adPackage) {
        this.adPackage = adPackage;
    }

    public String getAdDownload() {
        return adDownload;
    }

    public void setAdDownload(String adDownload) {
        this.adDownload = adDownload;
    }

    public String getAdMUrl() {
        return adMUrl;
    }

    public void setAdMUrl(String adMUrl) {
        this.adMUrl = adMUrl;
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
