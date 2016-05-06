package com.zhidian.remote.vo.request;

/**
 * Created by cjl on 2016/3/23.
 * asset素材信息
 */
public class AssetParam {

    /**
     * 广告序号
     */
    private Integer id;

    /**
     * 是否必须的广告
     * 0：否
     * 1：必须
     */
    private Integer isRequired;

    /**
     * 广告标题信息对象
     */
    private TitleParam title;

    /**
     * 广告素材图片信息对象
     */
    private ImgParam img;

    /**
     * 数据描述对象
     */
    private DataParam data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    public TitleParam getTitle() {
        return title;
    }

    public void setTitle(TitleParam title) {
        this.title = title;
    }

    public ImgParam getImg() {
        return img;
    }

    public void setImg(ImgParam img) {
        this.img = img;
    }

    public DataParam getData() {
        return data;
    }

    public void setData(DataParam data) {
        this.data = data;
    }
}
