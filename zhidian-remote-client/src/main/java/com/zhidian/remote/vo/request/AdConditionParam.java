package com.zhidian.remote.vo.request;

/**
 * Created by cjl on 2016/2/29.
 * 广告要求
 */
public class AdConditionParam {

    /**
     * 允许展示类型
     * 是否必须：是
     * 1、固定
     * 2、悬浮
     * 3、插屏
     */
    private Integer showType;

    /**
     * 允许广告创意类型类型
     * 是否必须：是
     *（可传多个用英文单逗号隔开 如：1,2）
     * 1、图片
     * 2、flash
     * 3、html5
     * 4、native
     * 5、视频
     * 6、gif
     */
    private String adType;

    /**
     * 不允许的广告创意类型
     * 是否必须：否
     * （可传多个用英文单逗号隔开 如：1,2）
     */
    private String unSupportAdType;

    /**
     * 只允许投那些广告类型
     * 是否必须：否
     * （可传多个用英文单逗号隔开 如：1,2）
     */
    private String adCategory;

    /**
     * 不能投那些广告类型
     * 是否必须：否
     * （可传多个用英文单逗号隔开 如：1,2）
     */
    private String unSupportAdCategory;

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
}
