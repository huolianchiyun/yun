package com.hlcy.yun.sip.gb28181.message.factory;

import com.hlcy.yun.sip.gb28181.SipLayer;
import com.hlcy.yun.sip.gb28181.bean.Device;
import com.hlcy.yun.sip.gb28181.config.GB28181Properties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.sip.InvalidArgumentException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Slf4j
public final class SipRequestFactory {

    private static final String CONTENT_TYPE = "Application";

    private static final String CONTENT_SUBTYPE_SDP = "SDP";

    private static final String CONTENT_SUBTYPE_MANSCDP = "MANSCDP+xml";

    private static SipFactory sipFactory;

    public static void setSipFactory(SipFactory sipFactory) {
        SipRequestFactory.sipFactory = sipFactory;
    }

    public static Request getMessageRequest(To to, From from, String subject, String transport, byte[] content) {
        return createRequest(to, from, subject, CONTENT_TYPE, CONTENT_SUBTYPE_MANSCDP, Request.MESSAGE, transport, content);
    }

    public static Request getInviteRequest(To to, From from, String subject, String transport, byte[] content) {
        return createRequest(to, from, subject, CONTENT_TYPE, CONTENT_SUBTYPE_SDP, Request.INVITE, transport, content);
    }

    /**
     * Create a message request
     *
     * @param to
     * @param from
     * @param subject
     * @param contentType
     * @param contentSubType
     * @param method
     * @param content
     * @return {@link Request}
     */
    private static Request createRequest(To to, From from, String subject, String contentType, String contentSubType, String method, String transport, byte[] content) {
        try {
            final AddressFactory addressFactory = SipLayer.getAddressFactory();
            // sip uri
            SipURI requestURI = addressFactory.createSipURI(to.user, to.host.toAddress());

            HeaderFactory headerFactory = SipLayer.getHeaderFactory();

            // To
            SipURI toSipURI = addressFactory.createSipURI(to.user, to.host.toAddress());
            Address toAddress = addressFactory.createAddress(toSipURI);
            ToHeader toHeader = headerFactory.createToHeader(toAddress, to.tag);

            // Content-Length:消息实体的字节长度
            final ContentLengthHeader contentLengthHeader = headerFactory.createContentLengthHeader(content.length);

            // Contact
            Address concatAddress = addressFactory.createAddress(addressFactory.createSipURI(from.user, from.host.toAddress()));
            final ContactHeader contactHeader = headerFactory.createContactHeader(concatAddress);

            // CSeq
            CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1L, method);

            // Call-ID
            CallIdHeader callIdHeader = SipLayer.getSipProvider(SipLayer.getTransport(transport)).getNewCallId();

            // Via
            ViaHeader viaHeader = headerFactory.createViaHeader(from.host.ip, from.host.port, transport, "");
            viaHeader.setRPort();
            List<ViaHeader> viaHeaders = Collections.singletonList(viaHeader);

            // From
            SipURI fromSipURI = addressFactory.createSipURI(from.user, from.host.toAddress());
            Address fromAddress = addressFactory.createAddress(fromSipURI);
            FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, from.tag);

            // Subject
            final SubjectHeader subjectHeader = headerFactory.createSubjectHeader(subject);

            // Content-Type
            ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader(contentType, contentSubType);

            // Max-Forwards
            MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);

            final Request request = sipFactory.createMessageFactory().createRequest(requestURI, method, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards, contentTypeHeader, content);
            request.setContentLength(contentLengthHeader);
            request.addHeader(contactHeader);
            request.addHeader(subjectHeader);

            return request;
        } catch (ParseException | PeerUnavailableException | InvalidArgumentException e) {
            log.error("Create a request exception, cause: {} \ncreate params: \nto:{} \nfrom:{} \nsubject:{} \ncontentType:{} \ncontentSubType:{} \nmethod:{} \ntransport:{} \ncontent:{}.",
                    e.getMessage(), to, from, subject, contentType, contentSubType, method, transport, new String(content));
            e.printStackTrace();
            throw new RuntimeException("Create a request exception.", e);
        }
    }

    @Getter
    class To {
        private String user;
        private Host host;
        private String tag;

        public To(String user, Host host, String tag) {
            this.user = user;
            this.host = host;
            this.tag = tag;
        }
    }

    @Getter
    class From {
        // SIP服务 or 设备 or 流媒体的编码
        private String user;
        private Host host;
        private String tag;

        public From(String user, Host host, String tag) {
            this.user = user;
            this.host = host;
            this.tag = tag;
        }
    }

    @Getter
    class Host {
        private String ip;
        private int port;

        public Host(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        private String toAddress() {
            return ip + ":" + port;
        }
    }
}
