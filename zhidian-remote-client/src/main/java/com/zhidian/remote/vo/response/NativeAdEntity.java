package com.zhidian.remote.vo.response;

import java.util.Map;

/**
 * Created by cjl on 2016/3/23.
 * 原生广告对象
 */
public class NativeAdEntity {

    /**
     * 对应的广告素材条件id
     */
    private Integer id;

    /**
     * 标题
     * 非必须
     */
    private String title;

    /**
     * 描述(有些媒体多个描述)
     * 非必须
     */
    private String[] descriptions;

    /**
     * 图片map集合（key图片类型， value 为nativeImage）
     * 否
     */
    private Map<Integer, NativeImageEntity> imagesMap;

    /**
     * 图片素材地址
     * 非必须
     */
    @Deprecated
    private String imgURL;

    /**
     * 广告行业类别
     */
    private Integer adCategory;

    /**
     * 点击行为
     */
    private Integer clickType;

    /**
     * 广告落地页
     * 非必须
     */
    private String landingPage;

    /**
     * 广告下载包名
     * 非必须
     */
    private String adPackage;

    /**
     * 广告名称
     * 非必须
     */
    private String adName;

    /**
     * 竞价成功回调地址组(用单引号隔开)
     */
    private String winUrls;

    /**
     *	曝光回调地址组(用单引号隔开)
     */
    private String exposureUrls;

    /**
     * 点击回调地址组(用单引号隔开)
     */
    private String clickUrls;

    /**
     * 激活成功回调地址组(用单引号隔开)
     */
    private String activeUrls;
    /**
     * 扩展对象
     * 非必须
     */
    private Map<String, Object> extendObject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String[] descriptions) {
        this.descriptions = descriptions;
    }

    public Map<Integer, NativeImageEntity> getImagesMap() {
        return imagesMap;
    }

    public void setImagesMap(Map<Integer, NativeImageEntity> imagesMap) {
        this.imagesMap = imagesMap;
    }

    @Deprecated
    public String getImgURL() {
        return imgURL;
    }

    @Deprecated
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
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

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
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
