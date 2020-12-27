package com.hlcy.yun.sip.gb28181.client;

import com.alibaba.fastjson.JSON;
import com.hlcy.yun.sip.gb28181.SipLayer;
import lombok.extern.slf4j.Slf4j;
import javax.sip.*;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

@Slf4j
public final class RequestSender {

    public static ClientTransaction sendRequest(Request request) {
        final SipProvider sipProvider = SipLayer.getSipProvider(SipLayer.getTransport(((ViaHeader) request.getHeader(ViaHeader.NAME)).getTransport()));
        try {
            final ClientTransaction clientTransaction = sipProvider.getNewClientTransaction(request);
            clientTransaction.sendRequest();
            return clientTransaction;
        } catch (SipException e) {
            log.error("Send a request({}) message failed, \ncause: {}", JSON.toJSONString(request), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
