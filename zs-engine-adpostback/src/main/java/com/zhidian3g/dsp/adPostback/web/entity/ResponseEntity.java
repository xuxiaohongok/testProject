package com.zhidian3g.dsp.adPostback.web.entity;

import com.alibaba.fastjson.JSON;

/**
 * Created by cjl on 2016/3/3.
 */
public class ResponseEntity {

    public static final int SUCCESS_200 = 200;

    public static final int FAIL_404 = 404;

    private Integer code;

    private String msg;


    public ResponseEntity() {
        super();
    }

    public ResponseEntity(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取对象的json字符串
     * @return
     */
    public String formatJsonStr() {
        return JSON.toJSONString(this);
    }
}
