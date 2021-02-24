package com.hlcy.yun.gb28181.service.params.player;

import com.hlcy.yun.gb28181.service.params.DeviceParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * History video download API 参数
 */
@Getter
@Setter
public class DownloadParams extends DeviceParams {
    private int downloadType;

    @ApiModelProperty(required = true)
    private long startTimestamp;

    @ApiModelProperty(required = true)
    private long endTimestamp;

    private int downloadSpeed;
}
