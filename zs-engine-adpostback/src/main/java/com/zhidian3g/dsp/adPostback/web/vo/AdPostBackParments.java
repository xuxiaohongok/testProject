package com.zhidian3g.dsp.adPostback.web.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AdPostBackParments {
	
	private Integer at;
	private String rid;
	private String cp;
	private String rat;
	private String rt;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getAt() {
		return at;
	}

	public void setAt(Integer at) {
		this.at = at;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
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
	
}
