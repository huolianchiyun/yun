package com.hlcy.yun.gb28181.operation.params;

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
