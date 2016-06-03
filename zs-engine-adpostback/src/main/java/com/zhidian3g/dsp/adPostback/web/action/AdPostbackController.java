package com.zhidian3g.dsp.adPostback.web.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian3g.dsp.adPostback.service.AdPostBackHandlerService;
import com.zhidian3g.dsp.adPostback.web.vo.AdPostBackParments;

/**
 * 
 */
@Controller
public class AdPostbackController {

	@Resource
	private AdPostBackHandlerService adPostBackHandlerService;

    /**
     * 广告竞价成功回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/aw.shtml")
    public void adWin(HttpServletRequest request,HttpServletResponse response,  AdPostBackParments adPostBackParments) {
    	try {
			adPostBackHandlerService.adWinHandler(adPostBackParments);
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
		}
    	response.setStatus(200);
    }

    /**
     * 广告曝光回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/as.shtml")
    public void adShow(HttpServletRequest request,HttpServletResponse response, AdPostBackParments adPostBackParments) {
    	try {
			adPostBackHandlerService.adShowHandler(adPostBackParments);
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
		}
    	response.setStatus(200);
    }

    /**
     * 广告点击回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/ac.shtml")
    public void adClick(HttpServletRequest request,HttpServletResponse response, AdPostBackParments adPostBackParments) {
    	try {
    		adPostBackHandlerService.adClickHandler(adPostBackParments);
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
		}
    	
    	response.setStatus(200);
    }

}
