package com.zhidian.base;

public class CareateReource {
	private String id;
	private int createId;
	private int meterialId;
	private int meterialType;
	private String imageHWs;
	private String title;
	private String detail;
	
	public CareateReource(String id, int createId, int meterialId, int meterialType,
			String imageHWs, String title, String detail) {
		super();
		this.id = id;
		this.createId = createId;
		this.meterialId = meterialId;
		this.meterialType = meterialType;
		this.imageHWs = imageHWs;
		this.title = title;
		this.detail = detail;
	}

	public int getCreateId() {
		return createId;
	}

	public int getMeterialId() {
		return meterialId;
	}

	public int getMeterialType() {
		return meterialType;
	}

	public String getImageHWs() {
		return imageHWs;
	}

	public String getTitle() {
		return title;
	}

	public String getDetail() {
		return detail;
	}
	
	
}
