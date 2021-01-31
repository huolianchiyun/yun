package com.hlcy.yun.gb28181.service.command.query;

import com.hlcy.yun.gb28181.service.command.Command;
import com.hlcy.yun.gb28181.service.params.query.QueryParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import lombok.RequiredArgsConstructor;

import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.createFrom;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.createTo;

@RequiredArgsConstructor
public abstract class AbstractQueryCmd<T extends QueryParams> implements Command<T> {
    private final String CMD = "${Cmd}";
    private final String CMD_TYPE = "${CmdType}";
    protected final GB28181Properties properties;

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

    private String getCmdTemplate(T t) {
        return new StringBuilder(200)
                .append("<?xml version=\"1.0\" ?>")
                .append("<Query>")
                .append("<CmdType>").append(CMD_TYPE).append("</CmdType>")
                .append("<SN>").append((int) ((Math.random() * 9 + 1) * 100000)).append("</SN>")
                .append("<DeviceID>").append(t.getChannelId()).append("</DeviceID>")
                .append(CMD)
                .append("</Query>")
                .toString();
    }
}
