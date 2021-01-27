package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.IFameParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 强制关键帧命令,设备收到此命令应立刻发送一个IDR帧(可选)
 */
public class IFameCmd extends AbstractControlCmd<IFameParams> {

    public IFameCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(IFameParams iFameParams) {
        return "<IFameCmd>Send</IFameCmd>";
    }

}
