package com.zhidian.ad.domain;

import java.io.Serializable;

public class AdTargetsMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String allValues;

	public String getAllValues() {
		return allValues;
	}

	public void setAllValues(String allValues) {
		this.allValues = allValues;
	}
	
}
