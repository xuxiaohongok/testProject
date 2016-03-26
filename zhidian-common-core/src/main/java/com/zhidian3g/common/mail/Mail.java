package com.zhidian3g.common.mail;


public class Mail {
	
	private String subject;
	
	private String text;
	
	private String from;
	
	private String to;
	
	public Mail(){
		subject = new String();
		text = new String();
		from = new String();
		to = new String();
	}

	public Mail(String subject, String text, String from, String to) {
		this.subject = subject;
		this.text = text;
		this.from = from;
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
