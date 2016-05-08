package com.zhidian3g.dsp.vo.adcontrol;


/**
 * 落地页信息
 * @author Administrator
 *
 */
public class AdLandingPageMessage {

	/**落地页id*/
	private Long id;
	
	/**落地页地址****/
	private String landingPageUrl;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getLandingPageUrl() {
		return landingPageUrl;
	}

	public void setLandingPageUrl(String landingPageUrl) {
		this.landingPageUrl = landingPageUrl;
	}
}
