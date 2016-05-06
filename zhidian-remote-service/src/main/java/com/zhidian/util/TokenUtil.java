package com.zhidian.util;

public class TokenUtil {
	/**自己放在系统的常量类里面*************/
	private final static String ZHI_DIAN_KEY = "zhidian3g";
	
	/**
	 * 
	 * 获取广告令牌
	 * @param dxId
	 * @param mediaId
	 * @param userId
	 * @param requestId
	 * @param adId
	 * @return
	 */
	private static String getTokenString(Integer dxId, Integer mediaId, String userId, String requestId, Integer adId) {
		String at = null;
		try {
			at = MD5Util.encodeLowerCase(ZHI_DIAN_KEY + "-" + dxId + "-" + mediaId + "-" + userId + "-" + requestId + "-" + adId, "utf-8").substring(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return at;
	}
	
	public static void main(String[] args) {
		System.out.println(getTokenString(1, 2, "testUser123", "requestId123", 1));
	}
}
