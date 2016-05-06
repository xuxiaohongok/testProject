package com.zhidian.remote.vo.request;

/**
 * Created by cjl on 2016/3/23.
 *
 * imp广告位
 */
public class ImpParam {

    /**
     * 广告位id	广告位对应的唯一id
     */
    private String impId;

    /**
     * 展示类型
     * 1、固定
     * 2、悬浮
     * 3、插屏
     * 4、开屏
     * 5、原生广告
     * 6、横幅广告
     * 7、视频前贴片
     * 8、视频中贴片
     * 9、视频后贴片
     */
    private Integer showType;

    /**
     * 广告类型
     * 1、图片广告
     * 2、图形文字链广告
     * 5、Flash广告
     * 6、html5广告
     * 7、native 广告
     * 8、视频广告
     * 9、Gif广告
     * 10、MRAID v2.0广告
     * （可传多个，以，分隔）
     * （adtype输入值，对应的imageAdTypes, nativeAdTypes为必填）
     */
    private String adType;

    /**
     * 底价(1元=10000单位，例如，15000代表1.5元)
     */
    private Long bidMinimum;

    /**
     * 广告位位置
     * 非必须
     */
    private Integer adPosition;

    /**
     * 一个广告位的条数 (可不填，默认1条，目前只针对图片类型广告)
     * 非必须
     */
    private Integer adMulit;

    /**
     * 图片广告类型对象
     * json array数组字符串
     * 非必须
     */
    private ImageAdTypeParam imageAdType;

    /**
     * 原生广告类型对象
     * json array数组字符串
     * 非必须
     */
    private NativeAdTypeParam nativeAdType;

    /**
     * 不允许的广告类型（可传多个用英文单逗号隔开 如：1,2）
     * 非必须
     */
    private String unSupportAdType;

    /**
     * 只允许投那些广告行业类型
     * （可传多个用英文单逗号隔开 如：1,2）
     * 非必须
     */
    private String adCategory;

    /**
     * 不能投那些广告行业类型（可传多个用英文单逗号隔开 如：1,2）
     * 非必须
     */
    private String unSupportAdCategory;

    /**
     * 点击交互类型
     * 0、打开网站
     * 1、下载应用
     * 3、拨打电话
     * 4、发送短信
     * 5、发邮件
     * 6、播放视频
     * 非必须
     */
    private Integer clickType;


    public String getImpId() {
        return impId;
    }

    public void setImpId(String impId) {
        this.impId = impId;
    }

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public Long getBidMinimum() {
        return bidMinimum;
    }

    public void setBidMinimum(Long bidMinimum) {
        this.bidMinimum = bidMinimum;
    }

    public Integer getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(Integer adPosition) {
        this.adPosition = adPosition;
    }

    public Integer getAdMulit() {
        return adMulit;
    }

    public void setAdMulit(Integer adMulit) {
        this.adMulit = adMulit;
    }

    public ImageAdTypeParam getImageAdType() {
        return imageAdType;
    }

    public void setImageAdType(ImageAdTypeParam imageAdType) {
        this.imageAdType = imageAdType;
    }

    public NativeAdTypeParam getNativeAdType() {
        return nativeAdType;
    }

    public void setNativeAdType(NativeAdTypeParam nativeAdType) {
        this.nativeAdType = nativeAdType;
    }

    public String getUnSupportAdType() {
        return unSupportAdType;
    }

    public void setUnSupportAdType(String unSupportAdType) {
        this.unSupportAdType = unSupportAdType;
    }

    public String getAdCategory() {
        return adCategory;
    }

    public void setAdCategory(String adCategory) {
        this.adCategory = adCategory;
    }

    public String getUnSupportAdCategory() {
        return unSupportAdCategory;
    }

    public void setUnSupportAdCategory(String unSupportAdCategory) {
        this.unSupportAdCategory = unSupportAdCategory;
    }

    public Integer getClickType() {
        return clickType;
    }

    public void setClickType(Integer clickType) {
        this.clickType = clickType;
    }
}







