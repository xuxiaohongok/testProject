package com.zhidian.ad.service.impl;

import com.zhidian.remote.vo.request.AdConditionParam;
import com.zhidian.remote.vo.request.AdRequestParam;
import com.zhidian.remote.vo.request.AdSlotParam;
import com.zhidian.remote.vo.request.MobileParam;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjl on 2016/3/4.
 */
public class AdHelper {

    /**
     * 检查媒体API请求参数是否正确
     * @param param
     * @return
     */
    public static boolean checkRequestParam (AdRequestParam param) {
        return AdHelper.checkRequestParam(param, new ArrayList());
    }

    /**
     * 请求媒体API请求参数是否正确
     * 若存在不正确，将记录到errorMessages中
     * @param param
     * @param errorMessages
     * @return
     */
    public static boolean checkRequestParam(AdRequestParam param, List errorMessages) {
        boolean result = true;
        
        if (param == null) {
            result = false;
            errorMessages.add("请求参数为空或者参数转换为对象过程出错");
        }
        if (StringUtils.isBlank(param.getIp())) {
            result = false;
            errorMessages.add("IP地址不能为空");
        }
        if (StringUtils.isBlank(param.getSerialNumber())) {
            result = false;
            errorMessages.add("流水号不能为空");
        }
        if (param.getBidType() == null) {
            result = false;
            errorMessages.add("竞价方式不能为空");
        }
        if (param.getMediaPlatformId() == null) {
            result = false;
            errorMessages.add("媒体平台ID不能为空");
        }
        if (param.getTerminalType() == null) {
            result = false;
            errorMessages.add("终端类型不能为空");
        }

        if (param.getAdCondition() != null) {
            AdConditionParam adCondition = param.getAdCondition();
            if (adCondition.getShowType() == null) {
                result = false;
                errorMessages.add("adCondition广告要求 - showType 允许展示类型不能为空");
            }
            if (adCondition.getAdType() == null) {
                result = false;
                errorMessages.add("adCondition广告要求 - adType 允许广告创意类型不能为空");
            }
        }

        if (param.getAdSlot() != null) {
            AdSlotParam adSlot = param.getAdSlot();
            if (adSlot.getWidth() == null) {
                result = false;
                errorMessages.add("adSlot广告位信息 - width 宽不能为空");
            }
            if (adSlot.getHeight() == null) {
                result = false;
                errorMessages.add("adSlot广告位信息 - height 高不能为空");
            }
            if (adSlot.getBidMinimum() == null) {
                result = false;
                errorMessages.add("adSlot广告位信息 - bidMinimum 底价不能为空");
            }
            if (adSlot.getClickType() == null) {
                result = false;
                errorMessages.add("adSlot广告位信息 - clickType 点击交互类类型不能为空");
            }
        }

        if (param.getMobile() != null) {
            MobileParam mobile = param.getMobile();
            if (StringUtils.isBlank(mobile.getUuId())) {
                result = false;
                errorMessages.add("mobile移动设备信息 - uuId 移动设备唯一标识不能为空");
            }
            if (mobile.getDeviceOS() == null) {
                result = false;
                errorMessages.add("mobile移动设备信息 - deviceOS 设备操作系统不能为空");
            }
            if (mobile.getDeviceType() == null) {
                result = false;
                errorMessages.add("mobile移动设备信息 - deviceType 设备类型不能为空");
            }
        }
        return result;
    }
}
