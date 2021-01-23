package com.hlcy.yun.gb28181.bean.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class QueryParams extends DeviceParams {
    protected String cmdType;
}
