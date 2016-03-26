package com.zhidian.dsp.util;

import com.zhidian.dsp.constant.DspConstant;
import com.zhidian3g.common.mail.MailService;
import com.zhidian3g.common.util.CommonLoggerUtil;
import com.zhidian3g.common.util.Utils;

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
		MailService.send(DspConstant.IP_ADRESS + " Dsp适配器系统solr异常", "sorl异常=============<br>" + exception);
		Utils.sleepTime(10);
		if(solrExceptionCount == 3) {
			System.exit(0);
		}
	}
}
