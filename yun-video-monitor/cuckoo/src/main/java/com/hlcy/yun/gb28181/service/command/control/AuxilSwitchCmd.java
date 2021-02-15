package com.hlcy.yun.gb28181.service.command.control;

import com.hlcy.yun.gb28181.service.params.control.AuxilSwitchControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.DefaultMessageResponseProcessor;

import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getCallId;

/**
 * 辅助开关控制指令
 */
public class AuxilSwitchCmd extends AbstractControlCmd<AuxilSwitchControlParams> {
    public AuxilSwitchCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(AuxilSwitchControlParams params) {
        return "<PTZCmd>" + buildPAuxilSwitchCmd(params) + "</PTZCmd>";
    }

    private String buildPAuxilSwitchCmd(AuxilSwitchControlParams params) {
        final StringBuilder builder = getBit123CmdTemplate();
        // 字节4
        int bit4 = params.getType().getBit4();
        builder.append(String.format("%02X", bit4), 0, 2);
        // 字节5
        int bit5 = params.getSwitchNum();
        builder.append(String.format("%02X", bit5), 0, 2);
        // 字节6
        builder.append(String.format("%02X", 0), 0, 2);
        // 字节7
        builder.append(String.format("%X", 0), 0, 1).append("0");
        // 字节8:校验码,为前面的第1~7字节的算术和的低8位,即算术和对256取模后的结果。
        // 字节8=(字节1+字节2+字节3+字节4+字节5+字节6+字节7)%256。
        int checkCode = (bit1 + bit2 + bit3 + bit4 + bit5 + 0x00 + (0x00 << 4 & 0XF0)) % 0X100;
        builder.append(String.format("%02X", checkCode), 0, 2);
        return builder.toString();
    }

    @Override
    protected void cacheFlowContext(AuxilSwitchControlParams auxilSwitchControlParams, Request request) {
        final FlowContext flowContext = new FlowContext(Operation.AUXIL_SWITCH, auxilSwitchControlParams, new DefaultMessageResponseProcessor());
        FlowContextCacheUtil.put(getCallId(request), flowContext);
    }
}
