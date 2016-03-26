package com.zhidian.remote.vo.request;

/**
 * Created by cjl on 2016/2/29.
 * 移动设备信息
 */
public class MobileParam {


    /**
     * 移动设备唯一标识
     * 是否必须：是
     */
    private String uuId;

    /**
     * 设备操作系统
     * 是否必须：是
     * 1、IOS
     * 2、Android
     * 3、windows_phone
     */
    private Integer deviceOS;

    /**
     * 设备类型
     * 是否必须：是
     * 0、未识别设备
     * 1、iphone手机2、ipad 3、ipod  4、Android手机5、Android pad
     */
    private Integer deviceType;

    /**
     * 安卓设备androidId
     * 是否必须：否
     */
    private String androidId;

    /**
     * 安卓手机imei唯一标识
     * 是否必须：否
     */
    private String imei;

    /**
     * 手机imsi卡号
     * 是否必须：否
     */
    private String imsi;

    /**
     * mac地址唯一标识
     * 是否必须：否
     */
    private String mac;

    /**
     * Iphone唯一标识
     * 是否必须：否
     */
    private String idfa;

    /**
     * 手机终端语言
     * 是否必须：否
     * （android、ios、wp）
     */
    @Deprecated
    private Integer lang;

    /**
     * 设备横竖方向
     * 是否必须：否
     * (0、竖向1、横向)
     */
    private Integer orientation;

    /**
     * 设备品牌
     * 是否必须：否
     */
    private String brand;

    /**
     * 设备机型
     * 是否必须：否
     */
    private String model;

    /**
     * 运营商
     * 是否必须：否
     */
    private String carrier;

    /**
     * 无线网络类型
     * 是否必须：否
     * wifi、2g、3g、4g
     */
    private Integer networkType;

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public Integer getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(Integer deviceOS) {
        this.deviceOS = deviceOS;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    @Deprecated
    public Integer getLang() {
        return lang;
    }

    @Deprecated
    public void setLang(Integer lang) {
        this.lang = lang;
    }

    public Integer getOrientation() {
        return orientation;
    }

    public void setOrientation(Integer orientation) {
        this.orientation = orientation;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Integer getNetworkType() {
        return networkType;
    }

    public void setNetworkType(Integer networkType) {
        this.networkType = networkType;
    }
}
