package com.hlcy.yun.gb28181.service.command.notify;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.notify.VoiceBroadcastNotifyParams;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast.VoiceSession;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.sip.ServerTransaction;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;

import static com.hlcy.yun.gb28181.service.sipmsg.flow.Operation.BROADCAST;
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
        return "<SourceID>" + properties.getSipId() + "</SourceID>"  // 语音输入设备的设备编码， 摄像机注册的sip编号
                + "<TargetID>" + voiceBroadcastNotifyParams.getChannelId() + "</TargetID>";  // 语音输出设备的设备编码
    }

    @Override
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
        RequestSender.sendRequest(request);

        cacheFlowContext(params);
    }

    private void cacheFlowContext(VoiceBroadcastNotifyParams params) {
        final FlowContext flowContext = new FlowContext(BROADCAST, params);
        FlowContext.setProperties(properties);
        FlowContextCacheUtil.put(BROADCAST.name() + params.getDeviceId(), flowContext);
    }

    public void stop(String ssrc) {
        if (isClosedBroadcastOf(ssrc)) {
            return;
        }

        final ServerTransaction transaction = getDeviceByeClientTransaction(ssrc);
        final Request bye = getByeRequest(transaction);
        sendByeRequest(bye, transaction);

        FlowContextCacheUtil.setNewKey(ssrc, getCallId(bye));
    }

    private boolean isClosedBroadcastOf(String ssrc) {
        final FlowContext context = FlowContextCacheUtil.get(ssrc);
        if (context == null) {
            log.info("语音广播已关闭，SSRC：{}", ssrc);
            return true;
        }
        if (context.isCleanup()) {
            log.info("语音广播正在关闭中... ...，SSRC：{}", ssrc);
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
