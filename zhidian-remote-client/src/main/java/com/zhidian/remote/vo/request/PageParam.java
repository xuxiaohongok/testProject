package com.zhidian.remote.vo.request;

/**
 * Created by cjl on 2016/2/29.
 * 页面请求信息信息对象
 */
public class PageParam {

    /**
     * 媒体类型
     * 是否必须：否
     */
    private Integer mediaCategory;

    /**
     * 页面类型
     * 是否必须：否
      */
    private Integer pageType;

    /**
     * 页面分类
     * 是否必须：否
     */
    private Integer pageCategory;

    public Integer getMediaCategory() {
        return mediaCategory;
    }

    public void setMediaCategory(Integer mediaCategory) {
        this.mediaCategory = mediaCategory;
    }

    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }

    public Integer getPageCategory() {
        return pageCategory;
    }

    public void setPageCategory(Integer pageCategory) {
        this.pageCategory = pageCategory;
    }
}
