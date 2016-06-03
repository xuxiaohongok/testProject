package com.zhidian.dsp.util;

import com.zhidian.dsp.constant.PropertieConstant;
import com.zhidian.common.mail.MailService;
import com.zhidian.common.util.CommonLoggerUtil;
import com.zhidian.common.util.Utils;

public class SolrUtil {
	
	/**
	 * 
	 * solr异常同时发送邮件报警
	 * @param e
	 */
	public static Integer solrExceptionCount = 0;
	public static void saveExceptionLogAndSendEmail(Exception e) {
		solrExceptionCount++;
		String exception = CommonLoggerUtil.getExceptionString(e);
		CommonLoggerUtil.addExceptionLog(e);
		MailService.send(PropertieConstant.IP_ADRESS + " Dsp适配器系统solr异常", "sorl异常=============<br>" + exception);
		Utils.sleepTime(10);
		if(solrExceptionCount == 6) {
			System.exit(0);
		}
	}
}
