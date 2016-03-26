package com.zhidian.remote.vo.request;

import java.util.List;

/**
 * Created by cjl on 2016/2/29.
 * 广告引擎接口参数对象
 */
public class AdRequestParam {


    /**
     * Ip地址
     * 是否必须：是
     */
    private String ip;

    /**
     * 流水号
     * 是否必须：是
     */
    private String serialNumber;

    /**
     * 媒体平台用户唯一Id
     * 是否必须：否
     */
    private String userId;

    /**
     * 竞价方式
     * 是否必须：是
     * 1、固定
     * 2、竞价
     */
    @Deprecated
    private Integer bidType;

    /**
     * 媒体平台ID
     * 是否必须：是
     * 1、畅言
     * 2、adview
     */
    private Integer mediaPlatformId;

    /**
     * 终端类型
     * 是否必须：是
     * 1、移动端
     * 2、PC端
     */
    private Integer terminalType;

    /**
     * 计费模式
     * 1、cpm
     * 2、cpc
     * 3、cpa
     */
    private Integer feeType;

    /**
     * 页面请求信息信息对象
     * 是否必须：否
     */
    private PageParam page;

    /**
     * 移动应用信息对象
     * 是否必须：否
     */
    private MobileAppParam mobileApp;

    /**
     * 移动设备信息对象
     * 是否必须：否
     */
    private MobileParam mobile;

    /**
     * 广告位信息对象
     * 是否必须：否
     */
    @Deprecated
    private AdSlotParam adSlot;

    /**
     * 广告要求对象
     * 是否必须：否
     */
    @Deprecated
    private AdConditionParam adCondition;

    /**
     * 广告位对象数组
     * 是否必须：是
     */
    private List<ImpParam> imps;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Deprecated
    public Integer getBidType() {
        return bidType;
    }

    @Deprecated
    public void setBidType(Integer bidType) {
        this.bidType = bidType;
    }

    public Integer getMediaPlatformId() {
        return mediaPlatformId;
    }

    public void setMediaPlatformId(Integer mediaPlatformId) {
        this.mediaPlatformId = mediaPlatformId;
    }

    public Integer getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    public PageParam getPage() {
        return page;
    }

    public void setPage(PageParam page) {
        this.page = page;
    }

    public MobileAppParam getMobileApp() {
        return mobileApp;
    }

    public void setMobileApp(MobileAppParam mobileApp) {
        this.mobileApp = mobileApp;
    }

    public MobileParam getMobile() {
        return mobile;
    }

    public void setMobile(MobileParam mobile) {
        this.mobile = mobile;
    }

    @Deprecated
    public AdSlotParam getAdSlot() {
        return adSlot;
    }

    @Deprecated
    public void setAdSlot(AdSlotParam adSlot) {
        this.adSlot = adSlot;
    }

    @Deprecated
    public AdConditionParam getAdCondition() {
        return adCondition;
    }

    @Deprecated
    public void setAdCondition(AdConditionParam adCondition) {
        this.adCondition = adCondition;
    }

    public List<ImpParam> getImps() {
        return imps;
    }

    public void setImps(List<ImpParam> imps) {
        this.imps = imps;
    }
}


