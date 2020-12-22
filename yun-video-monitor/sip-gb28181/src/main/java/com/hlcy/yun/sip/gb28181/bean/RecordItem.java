package com.hlcy.yun.sip.gb28181.bean;

import lombok.Getter;

@Getter
public class RecordItem {

	private String deviceId;

	private String name;

	private String filePath;

	private String address;

	private String startTime;

	private String endTime;

	private int secrecy;

	private String type;

	private String recorderId;

	public RecordItem setDeviceId(String deviceId) {
		this.deviceId = deviceId;
		return this;
	}

	public RecordItem setName(String name) {
		this.name = name;
		return this;
	}

	public RecordItem setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}

	public RecordItem setAddress(String address) {
		this.address = address;
		return this;
	}

	public RecordItem setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public RecordItem setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public RecordItem setSecrecy(int secrecy) {
		this.secrecy = secrecy;
		return this;
	}

	public RecordItem setType(String type) {
		this.type = type;
		return this;
	}

	public RecordItem setRecorderId(String recorderId) {
		this.recorderId = recorderId;
		return this;
	}
}
