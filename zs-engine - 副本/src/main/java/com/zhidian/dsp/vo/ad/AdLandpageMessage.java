package com.zhidian.dsp.vo.ad;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 广告基本信息
 * @author Administrator
 *
 */
public class AdLandpageMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Long id;
    
    private String name;
    
    private String landPageUrl;
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return ToStringBuilder.reflectionToString(this);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLandPageUrl() {
		return landPageUrl;
	}

	public void setLandPageUrl(String landPageUrl) {
		this.landPageUrl = landPageUrl;
	}

}