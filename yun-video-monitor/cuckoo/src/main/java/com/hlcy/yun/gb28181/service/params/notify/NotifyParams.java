package com.hlcy.yun.gb28181.service.params.notify;

import com.hlcy.yun.gb28181.service.params.DeviceParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifyParams extends DeviceParams {
    @ApiModelProperty(hidden = true)
    protected String cmdType;

    public NotifyParams(String cmdType) {
        this.cmdType = cmdType;
    }
}
