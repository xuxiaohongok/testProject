package com.zhidian.remote.vo.request;

/**
 * Created by cjl on 2016/2/29.
 * 移动应用信息对象
 */
public class MobileAppParam {

    /**
     * 媒体平台唯一应用标识
     * 是否必须：否
     */
    private String appId;

    /**
     * 应用分类
     * 是否必须：否
     */
    private Integer appCategory;

    /**
     * 应用名称
     * 是否必须：否
     */
    private String appName;

    /**
     * 应用包名
     * 是否必须：否
     */
    private String appPackageName;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getAppCategory() {
        return appCategory;
    }

    public void setAppCategory(Integer appCategory) {
        this.appCategory = appCategory;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }
}
