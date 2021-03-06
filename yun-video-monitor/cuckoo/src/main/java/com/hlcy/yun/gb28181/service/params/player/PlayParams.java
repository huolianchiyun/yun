package com.hlcy.yun.gb28181.service.params.player;

import com.hlcy.yun.gb28181.service.params.DeviceParams;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 点播 API 参数
 */
@Getter
@Setter
public class PlayParams extends DeviceParams {
    @ApiModelProperty(hidden = true)
    private Operation play = Operation.PLAY;
    /**
     * 视频码率
     */
    private String format;

    /**
     * 是否获取新流
     */
    private boolean newStream;
}
