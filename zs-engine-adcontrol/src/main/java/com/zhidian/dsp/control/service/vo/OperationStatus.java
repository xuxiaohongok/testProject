package com.zhidian.dsp.control.service.vo;

import com.zhidian.common.util.JsonUtil;

/**
 * 
 * 操作状态信息
 * @author Administrator
 *
 */
public class OperationStatus {
	
	private OperationStatusMessage statusMessage;
	private int status;
	
	public enum OperationMark {
		SUCCESS(1), ERR(0);
		 private int isSuccess;
		 private OperationMark(int isSuccess) {
			 this.isSuccess = isSuccess;
		 }
		public int getIsSuccess() {
			return isSuccess;
		}
		public void setIsSuccess(int isSuccess) {
			this.isSuccess = isSuccess;
		}
	}
	
	private OperationStatus(){};
	
	public OperationStatus(OperationMark operationMark) {
		this.status = operationMark.isSuccess;
	}

	/**
	 * 操作状态信息
	 * @author Administrator
	 */
	public class OperationStatusMessage {
		
		private int statusCode;
		private String statusMessage;
		
		public OperationStatusMessage(int statusCode, String statusMessage) {
			super();
			this.statusCode = statusCode;
			this.statusMessage = statusMessage;
		}
		public int getStatusCode() {
			return statusCode;
		}
		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}
		public String getStatusMessage() {
			return statusMessage;
		}
		public void setStatusMessage(String statusMessage) {
			this.statusMessage = statusMessage;
		}
	}

	
	public OperationStatusMessage getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(OperationStatusMessage statusMessage) {
		this.statusMessage = statusMessage;
	}

	public int getStatus() {
		return status;
	}

	public static void main(String[] args) {
		System.out.println(setOperationMessage(OperationMark.SUCCESS, 1, "ok"));
	}

	public static String setOperationMessage(OperationMark operationMark, Integer statusCode, String message) {
		OperationStatus operationStatus = new OperationStatus(operationMark);
		OperationStatus.OperationStatusMessage operationStatusMessage = operationStatus.new OperationStatusMessage(statusCode, message);
		operationStatus.setStatusMessage(operationStatusMessage);
		return JsonUtil.toJson(operationStatus);
	}
	
}
