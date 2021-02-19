package com.hlcy.yun.gb28181.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RecordItem implements Comparable<RecordItem> {

    private String deviceId;

    private String name;

    private String filePath;

    private String address;

    private String startTime;

    private String endTime;

    private int secrecy;

    private String type;

    private String recorderId;

    @Override
    public int compareTo(RecordItem o) {
        return startTime.compareTo(o.getStartTime());
    }
}
