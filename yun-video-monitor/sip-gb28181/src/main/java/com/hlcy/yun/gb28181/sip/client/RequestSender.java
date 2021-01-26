package com.hlcy.yun.gb28181.sip.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hlcy.yun.gb28181.sip.javax.RecoveredClientTransaction;
import com.hlcy.yun.gb28181.sip.SipLayer;
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
            log.error("Send a request({}) message failed, \ncause: {}", JSON.toJSONString(request, SerializerFeature.IgnoreNonFieldGetter), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void sendAckRequest(Request ack, ClientTransaction transaction) {
        try {
            transaction.getDialog().sendAck(ack);
        } catch (SipException e) {
            log.error("Send a ack request({}) message failed, \ncause: {}", JSON.toJSONString(ack, SerializerFeature.IgnoreNonFieldGetter), e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendByeRequest(Request bye, ClientTransaction transaction) {
        if (transaction.getClass().isAssignableFrom(RecoveredClientTransaction.class)) {
            sendRequest(bye);
            return;
        }
        final SipProvider sipProvider = SipLayer.getSipProvider(SipLayer.getTransport(((ViaHeader) bye.getHeader(ViaHeader.NAME)).getTransport()));
        try {
            transaction.getDialog().sendRequest(sipProvider.getNewClientTransaction(bye));
        } catch (SipException e) {
            log.error("Send a bye request({}) message failed, \ncause: {}", JSON.toJSONString(bye, SerializerFeature.IgnoreNonFieldGetter), e.getMessage());
            e.printStackTrace();
        }
    }
}
