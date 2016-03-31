package com.zhidian3g.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;

/**
 * Zip包处理工具类
 * @author caoyaming
 *
 */
public class ZipUtils {
	
	/**
	 * 对字符串进行压缩处理
	 * @param str 要压缩字符串
	 * @return 
	 */
	public static String gzip(String str) {
		if (StringUtils.isEmpty(str)) {   
			return "";   
		} 
		ByteArrayOutputStream out = null;
		GZIPOutputStream gzip = null;
		try {
			out = new ByteArrayOutputStream();   
			gzip = new GZIPOutputStream(out);   
			gzip.write(str.getBytes());   
			gzip.flush();
			gzip.close(); 
			gzip = null;
			return out.toString("ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (gzip != null) {
				try {
					gzip.close();
				} catch (Exception e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
		return "";
	}
	
	/**
	 * 将加密过得字符串进行解压处理
	 * @param str 要解压的字符串
	 * @return
	 */
	public static String ungzip(String str) {
		if (StringUtils.isEmpty(str)) {   
			return "";   
		} 
		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		GZIPInputStream gunzip = null;
		try {
			out = new ByteArrayOutputStream();   
			in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));   
			gunzip = new GZIPInputStream(in);   
			byte[] buffer = new byte[256];   
			int n;   
			while ((n = gunzip.read(buffer)) >= 0) {   
				out.write(buffer, 0, n);   
			}   
			//toString()使用平台默认编码，也可以显式的指定如toString("GBK")   
			return out.toString("UTF-8");   
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (gunzip != null) {
				try {
					gunzip.close();
				} catch (Exception e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
		return "";
	}
}
