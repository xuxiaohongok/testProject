package com.zhidian.remote.vo.request;

import java.util.List;

/**
 * Created by cjl on 2016/3/23.
 * nativeAdType广告位信息
 */
public class NativeAdTypeParam {

    /**
     * 广告位竞价对象数量
     */
    private Integer plcmtcnt;

    /**
     * 广告素材信息数组
     */
    private List<AssetParam> assets;

    public Integer getPlcmtcnt() {
        return plcmtcnt;
    }

    public void setPlcmtcnt(Integer plcmtcnt) {
        this.plcmtcnt = plcmtcnt;
    }

    public List<AssetParam> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetParam> assets) {
        this.assets = assets;
    }
}
