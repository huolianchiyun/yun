package com.hlcy.yun.gb28181.service.params.control;

import lombok.Getter;
import lombok.Setter;

/**
 * 录像控制 API 参数
 */
@Getter
@Setter
public class RecordControlParams extends ControlParams {
    private RecordOperate operate;

    public enum RecordOperate {
        Record, StopRecord
    }
}
