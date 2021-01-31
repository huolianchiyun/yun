package com.hlcy.yun.gb28181.service.params.control;

import com.hlcy.yun.gb28181.service.params.DeviceParams;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ControlParams extends DeviceParams {
    protected String cmdType = "DeviceControl";

    public ControlParams(String cmdType) {
        this.cmdType = cmdType;
    }
}
