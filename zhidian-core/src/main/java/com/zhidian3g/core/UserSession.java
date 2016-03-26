package com.zhidian3g.core;

import java.io.Serializable;
import java.util.Map;

public class UserSession implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long uid;
	private String username;
	private String nickname;
	
	private String appId;
	private String channelId;
	private String areaId;
	private String merchantId;
	private String imei;
	private String imsi;
	private String reserved;
	
	public UserSession(){}
	
	public UserSession(Long uid,String usernaem,String nickname,@SuppressWarnings("rawtypes") Map map){
		this.uid = uid;
		this.username = usernaem;
		this.nickname = nickname;
		this.appId = (String)map.get("appId");
		this.channelId = (String)map.get("appSpreadChannelCode");
		this.merchantId= (String)map.get("merchantId");
		this.imei = (String)map.get("imei");
	}
	
	public UserSession(Long uid, String username, String nickname, String merchantid, String appid, String channelid, String imei, String imsi){
		this.uid = uid;
		this.username = username;
		this.nickname = nickname;
		this.merchantId = merchantid;
		this.appId = appid;
		this.channelId = channelid;
		this.imei = imei;
		this.imsi = imsi;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Long getUid() {
		return uid;
	}
	
	public void setUid(Long uid) {
		this.uid = uid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String userName) {
		this.username = userName;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	
}
