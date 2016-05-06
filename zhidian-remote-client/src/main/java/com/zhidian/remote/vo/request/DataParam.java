package com.zhidian.remote.vo.request;

/**
 * Created by cjl on 2016/4/7.
 */
public class DataParam {

    /**
     * 数据类别
     * 必填
     */
    private Integer typeId;

    /**
     * 类别要求的长度
     * 非必填
     */
    private Integer len;

    /**
     * 名称
     * 非必填
     */
    private String name;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getLen() {
        return len;
    }

    public void setLen(Integer len) {
        this.len = len;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
