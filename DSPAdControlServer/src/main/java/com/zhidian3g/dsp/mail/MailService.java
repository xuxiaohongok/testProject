package com.zhidian3g.dsp.mail;

import com.zhidian3g.dsp.util.PropertiesUtil;

public class MailService {
	private final static String HOST = PropertiesUtil.getInstance().getValue("mail.smtp");
	private final static String USERNAME = PropertiesUtil.getInstance().getValue("mail.username");
	private final static String PASSWORD = PropertiesUtil.getInstance().getValue("mail.password");
	private final static String SENDADDRESS = PropertiesUtil.getInstance().getValue("mail.send.address");
	
	
	public static void send(String title, String message){
		startMailSender(title, message, SENDADDRESS);
	}
	
	public static void send( String address, String title, String message){
		startMailSender(title, message, address);
	}
	
	private static void startMailSender(String title, String message, String to){
		//生成邮件对象
		Mail mail = new Mail(title, message, USERNAME,to);
		//生成并设置发送服务器
		ServerSetting mailSetting = new ServerSetting();
		mailSetting.setSmtpService(HOST, USERNAME, PASSWORD);
		mailSetting.setMailMessage(mail);
		//发送邮件
		mailSetting.sendByAsyn();
	}

}
