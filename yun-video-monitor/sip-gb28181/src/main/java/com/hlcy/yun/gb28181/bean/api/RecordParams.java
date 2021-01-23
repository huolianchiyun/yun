package com.hlcy.yun.gb28181.bean.api;

import lombok.Getter;
import lombok.Setter;

/**
 * 录像控制 API 参数
 */
@Getter
@Setter
public class RecordParams extends DeviceParams {
    private RecordOperate operate;


    public enum RecordOperate {
        Record, StopRecord
    }
}
