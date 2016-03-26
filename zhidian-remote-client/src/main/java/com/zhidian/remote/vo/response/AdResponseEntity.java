package com.zhidian.remote.vo.response;

import java.util.List;

/**
 * Created by cjl on 2016/3/23.
 *
 * 返回广告信息对象adResponse
 */
public class AdResponseEntity {

    /**
     * 流水号（调用广告引擎时流水号）
     */
    private String serialNumber;

    /**
     * 广告位竞价对象数组
     */
    private List<ImpBidEntity> impBids;


    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<ImpBidEntity> getImpBids() {
        return impBids;
    }

    public void setImpBids(List<ImpBidEntity> impBids) {
        this.impBids = impBids;
    }
}
