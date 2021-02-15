package com.hlcy.yun.gb28181.service.command.notify;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.notify.NotifyParams;
import com.hlcy.yun.gb28181.service.command.Command;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import lombok.RequiredArgsConstructor;

import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.*;

@RequiredArgsConstructor
public abstract class AbstractNotifyCmd<T extends NotifyParams> implements Command<T> {
    protected final GB28181Properties properties;
    protected final String CMD = "${Cmd}";
    protected final String CMD_TYPE = "${CmdType}";

    protected abstract String buildCmdXML(T t);

    public void execute(T t) {
        final String cmd = getCmdTemplate(t).replace(CMD, buildCmdXML(t)).replace(CMD_TYPE, t.getCmdType());

        Request request = SipRequestFactory.getMessageRequest(
                createTo(t.getChannelId(), t.getDeviceIp(), t.getDevicePort()),
                createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                t.getDeviceTransport(),
                cmd.getBytes(StandardCharsets.UTF_8));
        RequestSender.sendRequest(request);
    }

    protected String getCmdTemplate(T t) {
        return new StringBuilder(200)
                .append("<?xml version=\"1.0\" ?>")
                .append("<Notify>")
                .append("<CmdType>").append(CMD_TYPE).append("</CmdType>")
                .append("<SN>").append((int) ((Math.random() * 9 + 1) * 100000)).append("</SN>")
                .append("<DeviceID>").append(t.getChannelId()).append("</DeviceID>")
                .append(CMD)
                .append("</Notify>")
                .toString();
    }
}
