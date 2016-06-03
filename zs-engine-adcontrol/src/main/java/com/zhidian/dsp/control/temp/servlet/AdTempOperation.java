package com.zhidian.dsp.control.temp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.common.util.JsonUtil;
import com.zhidian.common.util.MD5Util;
import com.zhidian.common.util.SpringContextUtil;
import com.zhidian.dsp.control.service.impl.AdHanderService;
import com.zhidian.dsp.util.AdControlLoggerUtil;

public class AdTempOperation extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * This method is called when a form has its tag value method equals to post.
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> parementMap = getParams(request);
		
		if(parementMap == null || parementMap.isEmpty() || request.getParameter("sn") == null || request.getParameter("operationCode") == null) {
			CommonLoggerUtil.addErrQuest("======parementMap=" + parementMap);
			setResponse(response, "hello word");
			return;
		} 
		
		String sn = (String)parementMap.remove("sn");
		System.out.println(JsonUtil.toJson(parementMap));
		String sign = MD5Util.encrypt(JsonUtil.toJson(parementMap));
		System.out.println(sign.equals(sign));
		System.out.println(sign);
		
		if(!sign.equals(sn)) {
			CommonLoggerUtil.addErrQuest(parementMap + "===解密出错====" + sn);
			setResponse(response, "hello word1");
			return;
		}
		
		Long dateTime = System.currentTimeMillis();
		String operationCode = request.getParameter("operationCode");
		try {
			Long dt = Long.valueOf(request.getParameter("dt"));
			long diffTime = dateTime - dt ;//间隔时间
			
			if(!(diffTime>0 && 60000>diffTime)) {//一分钟内有效
				CommonLoggerUtil.addErrQuest(parementMap + "===请求已失效====" + sn);
				setResponse(response, "hello word2");
				return;
			}
			
			AdHanderService adHanderService = (AdHanderService)SpringContextUtil.getBean("adHanderService");
			//添加广告
			if(operationCode.equals("1")) {
				AdControlLoggerUtil.addTimeLog("调用广告添加接口");
				String adId = request.getParameter("adId");
				adHanderService.addAd(Long.valueOf(adId));
				setResponse(response, "广告添加成功=======adId=" + adId);
				return;
			} else if(operationCode.equals("2")) {
				AdControlLoggerUtil.addTimeLog("调用广告删除接口");
				//暂停广告
				String adId = request.getParameter("adId");
				adHanderService.delAd(Long.valueOf(adId), 8);
				setResponse(response, "=======广告暂停成功=======adId=" + adId);
				return;
			} else if(operationCode.equals("3")) {
				//广告账户充值
				String accountId = request.getParameter("accountId");
				String fee = request.getParameter("fee");
				adHanderService.rechargeBalance(Long.valueOf(accountId), Long.valueOf(fee));
				setResponse(response, "=======广告用户充值成功=======accountId=" + accountId);
				return;
			} else if(operationCode.equals("-1")) {
				AdControlLoggerUtil.addTimeLog("====全部广告重新初始化=======");
				adHanderService.addAllAd(false);
				setResponse(response, "=======成功全部广告重新初始化=======");
				return;
			} else 	{
				setResponse(response, "没有符合操作类型");
				return;
			}
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
		}
		
		setResponse(response, "============操作异常============");
	}
	
	private TreeMap<String, Object> getParams(HttpServletRequest request) {
		TreeMap<String, Object> map = new TreeMap<String, Object>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        
        System.out.println("map=" + map);
        return map;
    }

	private void setResponse(HttpServletResponse response, String message) throws IOException {
		response.setHeader("content-type","text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(message);
		out.flush();
		out.close();
	}

}
