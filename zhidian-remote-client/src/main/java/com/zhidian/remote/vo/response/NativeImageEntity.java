package com.zhidian.remote.vo.response;

/**
 * Created by cjl on 2016/4/7.
 */
public class NativeImageEntity {

    /**
     * 图片类型
     * 必须
     */
    private Integer typeId;

    /**
     * 图片长
     * 必须
     */
    private Integer h;

    /**
     * 图片宽
     * 必须
     */
    private Integer w;

    /**
     * 图片地址
     * 必须
     */
    private String url;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
