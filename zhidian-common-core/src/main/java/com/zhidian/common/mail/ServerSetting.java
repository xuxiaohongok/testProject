package com.zhidian.common.mail;

import java.security.Security;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.zhidian.common.util.CommonLoggerUtil;

public class ServerSetting {

	private MimeMessage mimeMessage;

	private MimeMessageHelper mimeMessageHelper;

	private JavaMailSenderImpl javaMailSenderImpl;

	private Properties properties;

	public ServerSetting() {
		try {
			javaMailSenderImpl = new JavaMailSenderImpl();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		try {
			properties = new Properties();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			mimeMessage = javaMailSenderImpl.createMimeMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public ServerSetting(MimeMessage mimeMessage,
			MimeMessageHelper mimeMessageHelper,
			JavaMailSenderImpl javaMailSenderImpl, Properties properties) {
		super();
		this.mimeMessage = mimeMessage;
		this.mimeMessageHelper = mimeMessageHelper;
		this.javaMailSenderImpl = javaMailSenderImpl;
		this.properties = properties;
	}

	public void setMailMessage(String from, String to, String subject,
			String text) {
			try {
				mimeMessageHelper.setFrom(from);
				mimeMessageHelper.setTo(to);
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setText(text, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void setMailMessage(Mail mail) {
		try {
			mimeMessageHelper.setFrom(mail.getFrom());
			mimeMessageHelper.setTo(mail.getTo());
			mimeMessageHelper.setSubject(mail.getSubject());
			mimeMessageHelper.setText(mail.getText(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSmtpService(String host, String username, String password) {
		javaMailSenderImpl.setHost(host);
		javaMailSenderImpl.setUsername(username);
		javaMailSenderImpl.setPassword(password);
		setProperties(host);
	}

	private void setProperties(String smtpHost) {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		properties = System.getProperties();
		properties.setProperty("mail.smtp.host", smtpHost);
		properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		properties.setProperty("mail.smtp.port", "465");
		properties.setProperty("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		javaMailSenderImpl.setJavaMailProperties(properties);
	}
	
	/**
	 * 同步发送邮件
	 */
	public void send() {
		try {
			javaMailSenderImpl.send(mimeMessage);
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
			return;
		}
	}
	
	/**
	 * 异步发送邮件
	 */
	public void sendByAsyn(){
		Thread thread = new Thread(new Runnable(){
			public void run() {
				javaMailSenderImpl.send(mimeMessage);
			}
		});
		thread.start();
	}

	public MimeMessage getMimeMessage() {
		return mimeMessage;
	}

	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public MimeMessageHelper getMimeMessageHelper() {
		return mimeMessageHelper;
	}

	public void setMimeMessageHelper(MimeMessageHelper mimeMessageHelper) {
		this.mimeMessageHelper = mimeMessageHelper;
	}

	public JavaMailSenderImpl getJavaMailSenderImpl() {
		return javaMailSenderImpl;
	}

	public void setJavaMailSenderImpl(JavaMailSenderImpl javaMailSenderImpl) {
		this.javaMailSenderImpl = javaMailSenderImpl;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}