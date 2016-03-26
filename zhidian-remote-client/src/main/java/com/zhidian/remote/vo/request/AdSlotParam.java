package com.zhidian.remote.vo.request;

/**
 * Created by cjl on 2016/2/29.
 *广告位信息
 */
public class AdSlotParam {

    /**
     * 宽
     * 是否必须：是
     */
    private Integer width;

    /**
     * 高
     * 是否必须：是
     */
    private Integer height;

    /**
     * 底价
     * 是否必须：是
     * (1元=10000单位，例如，15000代表1.5元)
     */
    private Integer bidMinimum;

    /**
     * 广告位位置
     * 是否必须：否
     */
    private Integer adPosition;

    /**
     * 点击交互类型
     * 是否必须：是
     * 0、打开网站
     * 1、下载应用
     * 3、拨打电话
     * 4、发送短信
     * 5、发邮件
     * 6、播放视频
     */
    private Integer clickType;

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

    public Integer getBidMinimum() {
        return bidMinimum;
    }

    public void setBidMinimum(Integer bidMinimum) {
        this.bidMinimum = bidMinimum;
    }

    public Integer getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(Integer adPosition) {
        this.adPosition = adPosition;
    }

    public Integer getClickType() {
        return clickType;
    }

    public void setClickType(Integer clickType) {
        this.clickType = clickType;
    }
}
