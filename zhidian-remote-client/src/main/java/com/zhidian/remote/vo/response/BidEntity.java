package com.zhidian.remote.vo.response;

/**
 * Created by cjl on 2016/3/23.
 */
public class BidEntity {

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
    private Long adPrice;

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
     * 图片广告对象array
     */
    private ImageAdEntity imageAd;

    /**
     * 原生广告对象array
     */
    private NativeAdEntity nativeAd;

    public Integer getIsHasAd() {
        return isHasAd;
    }

    public void setIsHasAd(Integer isHasAd) {
        this.isHasAd = isHasAd;
    }

    public Long getAdPrice() {
        return adPrice;
    }

    public void setAdPrice(Long adPrice) {
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

    public ImageAdEntity getImageAd() {
        return imageAd;
    }

    public void setImageAd(ImageAdEntity imageAd) {
        this.imageAd = imageAd;
    }

    public NativeAdEntity getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(NativeAdEntity nativeAd) {
        this.nativeAd = nativeAd;
    }
}

