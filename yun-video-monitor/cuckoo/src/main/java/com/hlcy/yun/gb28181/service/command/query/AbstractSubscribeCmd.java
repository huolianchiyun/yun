package com.hlcy.yun.gb28181.service.command.query;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.query.QueryParams;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;

import static com.hlcy.yun.gb28181.service.sipmsg.flow.Operation.SUBSCRIBE;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.*;

public abstract class AbstractSubscribeCmd<T extends QueryParams> extends AbstractQueryCmd<T> {

    AbstractSubscribeCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    public void execute(T t) {
        final String cmd = getFinalCmd(t);

        Request request = SipRequestFactory.getSubscribeRequest(
                createTo(t.getChannelId(), t.getDeviceIp(), t.getDevicePort()),
                createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                t.getDeviceTransport(),
                cmd.getBytes(StandardCharsets.UTF_8));
        RequestSender.sendRequest(request);
        FlowContextCacheUtil.put(getCallId(request), new FlowContext(SUBSCRIBE, t));
    }
}
