package com.hlcy.yun.gb28181.service.params;

import lombok.Getter;
import lombok.Setter;

/**
 * 报警布防/撤防 API 参数
 */
@Getter
@Setter
public class GuardParams extends DeviceParams {
    private GuardOperate operate;

    public enum GuardOperate {
        SetGuard, ResetGuard
    }
}
