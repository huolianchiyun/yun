package com.hlcy.yun.gb28181.service.command.control;

import com.hlcy.yun.gb28181.service.params.control.CruiseControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.DefaultMessageResponseProcessor;

import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getCallId;

/**
 * 巡航指令
 */
public class CruiseCmd extends AbstractControlCmd<CruiseControlParams> {
    public CruiseCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(CruiseControlParams presetParams) {
        return "<PTZCmd>" + buildCruiseCmd(presetParams) + "</PTZCmd>";
    }

    private String buildCruiseCmd(CruiseControlParams params) {
        final StringBuilder builder = getBit123CmdTemplate();
        // 字节4
        int bit4 = params.getType().getBit4();
        builder.append(String.format("%02X", params.getType().getBit4()), 0, 2);
        // 字节5
        int bit5 = params.getGroupNum();
        builder.append(String.format("%02X", bit5), 0, 2);

        final CruiseControlParams.CruiseType type = params.getType();
        // 字节6
        int bit6 = CruiseControlParams.CruiseType.START_CRUISE == type ? 0x00 : params.getPresetIndex();
        if (CruiseControlParams.CruiseType.SET_CRUISE_SPEED == type || CruiseControlParams.CruiseType.SET_CRUISE_DURATION == type) {
            bit6 = params.getSpeedOrDuration();
        }

        builder.append(String.format("%02X", bit6), 0, 2);
        // 字节7  TODO time error
        final int bit7 = (CruiseControlParams.CruiseType.SET_CRUISE_SPEED == type || CruiseControlParams.CruiseType.SET_CRUISE_DURATION == type
                ? params.getSpeedOrDuration() : 0x00) & 0xF0 >> 4;

        builder.append(String.format("%02X", bit7), 0, 2);

        // 字节8:校验码,为前面的第1~7字节的算术和的低8位,即算术和对256取模后的结果。
        // 字节8=(字节1+字节2+字节3+字节4+字节5+字节6+字节7)%256。
        int checkCode = (bit1 + bit2 + bit3 + bit4 + bit5 + bit6 + bit7) % 0X100;
        builder.append(String.format("%02X", checkCode), 0, 2);
        return builder.toString();
    }

    @Override
    protected void cacheFlowContext(CruiseControlParams cruiseControlParams, Request request) {
        final FlowContext flowContext = new FlowContext(Operation.CRUISE, cruiseControlParams, new DefaultMessageResponseProcessor());
        FlowContextCacheUtil.put(getCallId(request), flowContext);
    }
}
