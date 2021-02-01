package com.hlcy.yun.gb28181.service.params.query;

import com.hlcy.yun.gb28181.service.params.DeviceParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryParams extends DeviceParams {
    @ApiModelProperty(hidden = true)
    protected String cmdType;

    public QueryParams(String cmdType) {
        this.cmdType = cmdType;
    }
}
