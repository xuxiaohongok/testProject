package com.zhidian3g.dsp.adPostback.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private static MessageDigest digest = null;

	/**
	 *
	 * @param data
	 * @return
	 */
	public synchronized static final String encode(String data) {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		digest.update(data.getBytes());
		return encodeHexLowerCase(digest.digest());
	}
	
	/**
	 * 带字符编码,转化成MD5, 同时转化成小写
	 * @param data
	 * @param code
	 * @return
	 */
	public synchronized static final String encodeLowerCase(String data, String code) {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			digest.update(data.getBytes(code));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeHexLowerCase(digest.digest());
	}
	
	/**
	 * 得到正常的MD5字符串
	 * @param bytes
	 * @return
	 */
	private static final String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;
		for (i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		
		return buf.toString();
	}

	/**
	 * 得到的MD5字符串为大写
	 * @param bytes
	 * @return
	 */
	private static final String encodeHexUpperCase(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;
		for (i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		
		return buf.toString().toUpperCase();
	}
	
	/**
	 * 得到的MD5字符串为小写
	 * @param bytes
	 * @return
	 */
	private static final String encodeHexLowerCase(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;
		for (i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		
		return buf.toString().toLowerCase();
	}

	public static void main(String[] args) {
		System.out.println(MD5Util.encode("23小明meitoung_25622@163.comnextAdAPI-CX"));
		System.out.println(MD5Util.encodeLowerCase("23小明meitoung_25622@163.comnextAdAPI-CX", "UTF-8"));
	}

}
