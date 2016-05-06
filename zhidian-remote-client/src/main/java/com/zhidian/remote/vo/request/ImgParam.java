package com.zhidian.remote.vo.request;

/**
 * Created by cjl on 2016/3/23.
 * img图片
 */
public class ImgParam {

    /**
     * 图片类型
     */
    private Integer typeId;

    /**
     * 宽
     */
    private Integer w;

    /**
     * 高
     */
    private Integer h;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }
}
