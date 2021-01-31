package com.hlcy.yun.gb28181.service.params.notify;

import com.hlcy.yun.gb28181.service.params.DeviceParams;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifyParams extends DeviceParams {
    protected String cmdType;

    public NotifyParams(String cmdType) {
        this.cmdType = cmdType;
    }
}
