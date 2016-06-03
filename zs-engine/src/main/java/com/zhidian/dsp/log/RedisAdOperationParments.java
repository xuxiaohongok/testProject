package com.zhidian.dsp.log;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RedisAdOperationParments {
	
	//媒体请求唯一标识
	private String rId;
	
	//媒体唯一用户标识
	private String uId;
	
	private String hws;
	
	//广告id
	private Long aId ;
	
	//广告创意id
	private Long cId;
	
	//素材id
	private Long mId;
	
	 //落地页id
	private Long lId;
	
	//渠道id
	private Integer chId;
	
	//媒体id
	private String mdId;
	
	//计费类型
	private Integer fT;
	
	//媒体出的低价
	private Long mP;
	
	//竞价价格
	private Long bP;
	
	//渠道媒体的广告位标识
	private String abId;
	
	//渠道发送广告竞价请求时间
	private String rat;
	
	//区域
	private String aa;
	
	//请求ip
	private String ip;
	
	private Long acId;
	
	private Integer os;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ToStringBuilder.reflectionToString(this);
	}

	public String getrId() {
		return rId;
	}

	public void setrId(String rId) {
		this.rId = rId;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getHws() {
		return hws;
	}

	public void setHws(String hws) {
		this.hws = hws;
	}

	public Long getaId() {
		return aId;
	}

	public void setaId(Long aId) {
		this.aId = aId;
	}

	public Long getcId() {
		return cId;
	}

	public void setcId(Long cId) {
		this.cId = cId;
	}

	public Long getmId() {
		return mId;
	}

	public void setmId(Long mId) {
		this.mId = mId;
	}

	public Long getlId() {
		return lId;
	}

	public void setlId(Long lId) {
		this.lId = lId;
	}

	public Integer getChId() {
		return chId;
	}

	public void setChId(Integer chId) {
		this.chId = chId;
	}

	public String getMdId() {
		return mdId;
	}

	public void setMdId(String mdId) {
		this.mdId = mdId;
	}

	public Integer getfT() {
		return fT;
	}

	public void setfT(Integer fT) {
		this.fT = fT;
	}

	public Long getmP() {
		return mP;
	}

	public void setmP(Long mP) {
		this.mP = mP;
	}

	public Long getbP() {
		return bP;
	}

	public void setbP(Long bP) {
		this.bP = bP;
	}

	public String getAbId() {
		return abId;
	}

	public void setAbId(String abId) {
		this.abId = abId;
	}

	public String getRat() {
		return rat;
	}

	public void setRat(String rat) {
		this.rat = rat;
	}

	public String getAa() {
		return aa;
	}

	public void setAa(String aa) {
		this.aa = aa;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getAcId() {
		return acId;
	}

	public void setAcId(Long acId) {
		this.acId = acId;
	}

	public Integer getOs() {
		return os;
	}

	public void setOs(Integer os) {
		this.os = os;
	}
}
