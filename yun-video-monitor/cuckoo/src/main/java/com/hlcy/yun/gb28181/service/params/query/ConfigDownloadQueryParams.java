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
     * SVAC编码配置: SVACEncodeConfig, SVAC解码配置: SVACDecodeConfig。
     * 可同时查询多个配置类型,各类型以“/”分隔
     */
    private String configType = ConfigType.BasicParam.name();

    public ConfigDownloadQueryParams() {
        super("ConfigDownload");
    }

    enum ConfigType {
        BasicParam,
        VideoParamOpt,
        SVACEncodeConfig,
        SVACDecodeConfig;
    }
}
