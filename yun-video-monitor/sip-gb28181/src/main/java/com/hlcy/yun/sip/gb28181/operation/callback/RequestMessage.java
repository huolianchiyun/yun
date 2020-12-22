package com.hlcy.yun.sip.gb28181.operation.callback;

public class RequestMessage {

	private String id;

	private String deviceId;

	private String type;

	private Object data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
		this.id = type + deviceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.id = type + deviceId;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
