package com.zhidian.remote.vo.response;

import java.util.List;

/**
 * Created by cjl on 2016/3/23.
 * impBid 广告位竞价对象
 */
public class ImpBidEntity {

    /**
     * 广告位id
     */
    private String impId;

    /**
     * 竞价方式
     * 1.固定
     * 2.竞价
     */
    @Deprecated
    private Integer bidType;

    /**
     * 竞价对象数组
     */
    private List<BidEntity> bids;

    public String getImpId() {
        return impId;
    }

    public void setImpId(String impId) {
        this.impId = impId;
    }

    @Deprecated
    public Integer getBidType() {
        return bidType;
    }

    @Deprecated
    public void setBidType(Integer bidType) {
        this.bidType = bidType;
    }

    public List<BidEntity> getBids() {
        return bids;
    }

    public void setBids(List<BidEntity> bids) {
        this.bids = bids;
    }
}
