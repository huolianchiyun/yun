package com.hlcy.yun.gb28181.service.params.control;

import lombok.Getter;
import lombok.Setter;

/**
 * 报警布防/撤防 API 参数
 */
@Getter
@Setter
public class GuardControlParams extends ControlParams {
    private GuardOperate operate;

    public enum GuardOperate {
        SetGuard, ResetGuard
    }
}
