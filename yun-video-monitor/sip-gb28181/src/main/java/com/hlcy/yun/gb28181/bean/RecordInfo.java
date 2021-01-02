package com.hlcy.yun.gb28181.bean;

import lombok.Getter;

import java.util.List;

@Getter
public class RecordInfo {

	private String deviceId;

	private String name;

	private int sumNum;

	private List<RecordItem> recordList;

	public RecordInfo setDeviceId(String deviceId) {
		this.deviceId = deviceId;
		return this;
	}

	public RecordInfo setName(String name) {
		this.name = name;
		return this;
	}

	public RecordInfo setSumNum(int sumNum) {
		this.sumNum = sumNum;
		return this;
	}

	public RecordInfo setRecordList(List<RecordItem> recordList) {
		this.recordList = recordList;
		return this;
	}
}
