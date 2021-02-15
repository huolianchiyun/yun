package com.hlcy.yun.gb28181.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class RecordInfo {

    private String deviceId;

    private String name;

    private int sumNum;

    private List<RecordItem> recordList;

}
