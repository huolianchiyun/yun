package com.hlcy.yun.gb28181.service.params.query;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ConfigDownloadQueryParams extends QueryParams {
    /**
     * 查询配置参数类型(必选),
     * 可查询的配置类型包括基本参数配置: BasicParam,
     * 视频参数范围: VideoParamOpt,
     * SVAC编码配置: SVACEncodeConfig,
     * SVAC解码配置: SVACDecodeConfig。
     */
    private ConfigType configType;

    public ConfigDownloadQueryParams() {
        super("ConfigDownload");
    }

   public enum ConfigType {
        BasicParam,
        VideoParamOpt,
        SVACEncodeConfig,
        SVACDecodeConfig;
    }
}
