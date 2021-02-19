package com.hlcy.yun.gb28181.service.command.control;

import com.hlcy.yun.gb28181.service.command.Command;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.control.ControlParams;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import lombok.RequiredArgsConstructor;

import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.*;

@RequiredArgsConstructor
public abstract class AbstractControlCmd<T extends ControlParams> implements Command<T> {
    private final String CMD = "${Cmd}";
    private final String CMD_TYPE = "${CmdType}";
    protected final GB28181Properties properties;
    final int bit1 = 0xA5;
    final int bit2 = 0x0F;
    final int bit3 = 0x01;

    protected abstract String buildCmdXML(T t);

    protected void cacheFlowContext(T t, Request request) {
    }

    @Override
    public void execute(T t) {
        final String cmd = getCmdTemplate(t).replace(CMD, buildCmdXML(t)).replace(CMD_TYPE, t.getCmdType());

        Request request = SipRequestFactory.getMessageRequest(
                createTo(t.getChannelId(), t.getDeviceIp(), t.getDevicePort()),
                createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                t.getDeviceTransport(),
                cmd.getBytes(StandardCharsets.UTF_8));
        RequestSender.sendRequest(request);

        cacheFlowContext(t, request);
    }

    private String getCmdTemplate(T t) {
        return new StringBuilder(200)
                .append("<?xml version=\"1.0\" ?>")
                .append("<Control>")
                .append("<CmdType>").append(CMD_TYPE).append("</CmdType>")
                .append("<SN>").append(productSN()).append("</SN>")
                .append("<DeviceID>").append(t.getChannelId()).append("</DeviceID>")
                .append(CMD)
                .append("</Control>\r\n")
                .toString();
    }

    private synchronized static String productSN() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    StringBuilder getBit123CmdTemplate() {
        return new StringBuilder().append(String.format("%02X%02X%02X", bit1, bit2, bit3), 0, 6);
    }
}
