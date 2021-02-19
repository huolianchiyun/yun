package com.hlcy.yun.gb28181.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class RecordInfo {

    private String deviceId;

    private String name;

    private int sumNum;

    private List<RecordItem> recordList;

    public RecordInfo toSort() {
        if (recordList != null && !recordList.isEmpty()) {
            Collections.sort(recordList);
        }
        return this;
    }
}
