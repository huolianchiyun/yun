package com.hlcy.yun.gb28181.operation.command.control;


import com.hlcy.yun.gb28181.operation.params.HomePositionParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 看守位控制命令
 */
public class HomePositionCmd extends AbstractControlCmd<HomePositionParams> {

    public HomePositionCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(HomePositionParams homePositionParams) {
        return new StringBuilder(200)
                .append("<HomePosition>")
                // 看守位使能1:开启,0:关闭(必选)
                .append("<Enabled>").append(homePositionParams.getEnabled()).append("</Enabled>")
                // 自动归位时间间隔,开启看守位时使用,单位:秒(s)(可选)
                .append("<ResetTime>").append(homePositionParams.getResetTime()).append("</ResetTime>")
                // 调用预置位编号,开启看守位时使用,取值范围0~255(可选)
                .append("<PresetIndex>").append(homePositionParams.getPresetIndex()).append("</PresetIndex>")
                .append("</HomePosition>")
                .toString();
    }
}
