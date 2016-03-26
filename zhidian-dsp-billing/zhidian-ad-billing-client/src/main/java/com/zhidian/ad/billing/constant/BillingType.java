package com.zhidian.ad.billing.constant;

/**
 * Created by chenwanli on 2016/3/17.
 */
public enum BillingType {
    CPM("按展示计费", 1);

    private String name;
    private int index;

    private BillingType(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
