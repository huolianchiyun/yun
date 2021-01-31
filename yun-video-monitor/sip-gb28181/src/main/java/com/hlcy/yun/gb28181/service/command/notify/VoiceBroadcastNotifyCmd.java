package com.hlcy.yun.gb28181.service.command.notify;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.notify.VoiceBroadcastNotifyParams;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast.VoiceSession;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import com.hlcy.yun.gb28181.util.SSRCManger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;

import static com.hlcy.yun.gb28181.sip.biz.RequestSender.sendByeRequest;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.*;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getCallId;

@Slf4j
public class VoiceBroadcastNotifyCmd extends AbstractNotifyCmd<VoiceBroadcastNotifyParams> {

    public VoiceBroadcastNotifyCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(VoiceBroadcastNotifyParams voiceBroadcastNotifyParams) {
        return "<SourceID>" + properties.getSipId() + "</SourceID>"
                + "<TargetID>" + voiceBroadcastNotifyParams.getChannelId() + "</TargetID>";  // 音频通道ID
    }

    protected String getCmdTemplate(VoiceBroadcastNotifyParams params) {
        return new StringBuilder(200)
                .append("<?xml version=\"1.0\" ?>")
                .append("<Notify>")
                .append("<CmdType>").append(CMD_TYPE).append("</CmdType>")
                .append("<SN>").append((int) ((Math.random() * 9 + 1) * 100000)).append("</SN>")
                .append(CMD)
                .append("</Notify>")
                .toString();
    }

    @Override
    public void execute(VoiceBroadcastNotifyParams params) {
        final String cmd = getCmdTemplate(params).replace(CMD, buildCmdXML(params)).replace(CMD_TYPE, params.getCmdType());

        Request request = SipRequestFactory.getMessageRequest(
                createTo(params.getChannelId(), params.getDeviceIp(), params.getDevicePort()),
                createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                params.getDeviceTransport(),
                cmd.getBytes(StandardCharsets.UTF_8));
        final ClientTransaction clientTransaction = RequestSender.sendRequest(request);

        final FlowContext flowContext = new FlowContext(Operation.BROADCAST, params);
        FlowContext.setProperties(properties);
        flowContext.put(VoiceSession.SIP_DEVICE_SESSION, clientTransaction);
        FlowContextCacheUtil.put(params.getChannelId(), flowContext);
    }

    public void stop(String ssrc) {
        if (isClosedVoiceBroadcast(ssrc)) {
            return;
        }

        final ServerTransaction transaction = getDeviceByeClientTransaction(ssrc);
        final Request bye = getByeRequest(transaction);
        sendByeRequest(bye, transaction);

        FlowContextCacheUtil.setNewKey(ssrc, getCallId(bye));
        SSRCManger.releaseSSRC(ssrc);
    }

    private boolean isClosedVoiceBroadcast(String ssrc) {
        final FlowContext context = FlowContextCacheUtil.get(ssrc);
        if (context == null) {
            log.info("媒体流已关闭，SSRC：{}", ssrc);
            return true;
        }
        return false;
    }

    private ServerTransaction getDeviceByeClientTransaction(String ssrc) {
        final FlowContext context = FlowContextCacheUtil.get(ssrc);
        final ServerTransaction serverTransaction = context.getServerTransaction(VoiceSession.SIP_DEVICE_SESSION);
        Assert.notNull(serverTransaction, String.format("The ServerTransaction of SIP_DEVICE_SESSION has been lost, SSRC: %s", ssrc));
        return serverTransaction;
    }
}
