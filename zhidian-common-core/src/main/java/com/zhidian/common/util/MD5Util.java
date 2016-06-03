package com.zhidian.common.util;

import java.nio.charset.Charset;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * MD5加密
 * @author Administrator
 *
 */
public class MD5Util {
	
	private static final String CHARSET = "UTF-8";
	private static final String KEY = PropertiesUtil.getInstance().getValue("sy_key");

	/**
	 * 注意：需要配置文件添加sy_key秘钥
	 * md5加密
	 */
	public static String encrypt(final String str){
		try {
			HashFunction hf = Hashing.md5();
			HashCode hc = hf.newHasher().putString(KEY + ":::" + str,Charset.forName(CHARSET)).hash();
			return hc.toString();
		} catch (Exception e) {
			CommonLoggerUtil.addExceptionLog(e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(encrypt("zhidian3g"));
	}
}
