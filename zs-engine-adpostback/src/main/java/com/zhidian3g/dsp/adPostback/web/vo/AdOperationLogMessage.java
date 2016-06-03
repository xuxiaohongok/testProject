package com.zhidian3g.dsp.adPostback.web.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AdOperationLogMessage {
	
	//1.媒体请求日志 2.广告获取日志、 3.竞价成功日志 4.曝光日志  5. 点击日志
	private Integer logcode;
	
	//媒体请求唯一标识
	private String rid;
	
	//媒体唯一用户标识
	private String uid;
	
	private String hws;
	
	//广告id
	private Long aid ;
	
	//广告创意id
	private Long cid;
	
	//素材id
	private Long mid;
	
	 //落地页id
	private Long lid;
	
	//渠道id
	private Integer chid;
	
	//媒体id
	private String mdid;
	
	//计费类型
	private Integer ft;
	
	//媒体出的低价
	private Long mp;
	
	//竞价价格
	private Long bp;
	
	//结算单价
	private Long cp;
	
	//渠道媒体的广告位标识
	private String abid;
	
	//渠道发送广告竞价请求时间
	private String rat;
	
	//接口请求时间
	private String rt;
	
	//区域
	private String aa;
	
	//请求ip
	private String ip;
	
	private Integer os;
	
	//广告账户id
	private Long acid;
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ToStringBuilder.reflectionToString(this);
	}


	public Integer getLogcode() {
		return logcode;
	}


	public void setLogcode(Integer logcode) {
		this.logcode = logcode;
	}


	public String getRid() {
		return rid;
	}


	public void setRid(String rid) {
		this.rid = rid;
	}


	public String getUid() {
		return uid;
	}


	public void setUid(String uid) {
		this.uid = uid;
	}


	public String getHws() {
		return hws;
	}


	public void setHws(String hws) {
		this.hws = hws;
	}


	public Long getAid() {
		return aid;
	}


	public void setAid(Long aid) {
		this.aid = aid;
	}


	public Long getCid() {
		return cid;
	}


	public void setCid(Long cid) {
		this.cid = cid;
	}


	public Long getMid() {
		return mid;
	}


	public void setMid(Long mid) {
		this.mid = mid;
	}


	public Long getLid() {
		return lid;
	}


	public void setLid(Long lid) {
		this.lid = lid;
	}


	public Integer getChid() {
		return chid;
	}


	public void setChid(Integer chid) {
		this.chid = chid;
	}


	public String getMdid() {
		return mdid;
	}


	public void setMdid(String mdid) {
		this.mdid = mdid;
	}


	public Integer getFt() {
		return ft;
	}


	public void setFt(Integer ft) {
		this.ft = ft;
	}


	public Long getMp() {
		return mp;
	}


	public void setMp(Long mp) {
		this.mp = mp;
	}


	public Long getBp() {
		return bp;
	}

	public void setBp(Long bp) {
		this.bp = bp;
	}

	public Long getCp() {
		return cp;
	}

	public void setCp(Long cp) {
		this.cp = cp;
	}

	public String getAbid() {
		return abid;
	}

	public void setAbid(String abid) {
		this.abid = abid;
	}

	public String getRat() {
		return rat;
	}

	public void setRat(String rat) {
		this.rat = rat;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
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

	public Integer getOs() {
		return os;
	}

	public void setOs(Integer os) {
		this.os = os;
	}

	public Long getAcid() {
		return acid;
	}

	public void setAcid(Long acid) {
		this.acid = acid;
	}
	
}
