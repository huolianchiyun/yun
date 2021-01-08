package com.hlcy.yun.gb28181.sip.message.factory;

import com.hlcy.yun.gb28181.sip.SipLayer;
import com.hlcy.yun.gb28181.util.UUIDUtil;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.sip.*;
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

    private static final byte[] EMPTY_CONTENT = new byte[0];

    private static SipFactory sipFactory;

    public static void setSipFactory(SipFactory sipFactory) {
        SipRequestFactory.sipFactory = sipFactory;
    }

    public static Request getMessageRequest(To to, From from, String transport) {
        return createRequest(to, from, null, null, CONTENT_TYPE, CONTENT_SUBTYPE_MANSCDP, Request.MESSAGE, transport, EMPTY_CONTENT);
    }

    public static Request getMessageRequest(To to, From from, String transport, byte[] content) {
        return createRequest(to, from, null, null, CONTENT_TYPE, CONTENT_SUBTYPE_MANSCDP, Request.MESSAGE, transport, content);
    }

    public static Request getMessageRequest(To to, From from, String subject, String viaBranch, String transport, byte[] content) {
        return createRequest(to, from, subject, viaBranch, CONTENT_TYPE, CONTENT_SUBTYPE_MANSCDP, Request.MESSAGE, transport, content);
    }

    public static Request getInviteRequest(To to, From from, String transport) {
        return createRequest(to, from, null, null, CONTENT_TYPE, CONTENT_SUBTYPE_SDP, Request.INVITE, transport, EMPTY_CONTENT);
    }

    public static Request getInviteRequest(To to, From from, String transport, byte[] content) {
        return createRequest(to, from, null, null, CONTENT_TYPE, CONTENT_SUBTYPE_SDP, Request.INVITE, transport, content);
    }

    public static Request getInviteRequest(To to, From from, String subject, String viaBranch, String transport, byte[] content) {
        return createRequest(to, from, subject, viaBranch, CONTENT_TYPE, CONTENT_SUBTYPE_SDP, Request.INVITE, transport, content);
    }

    public static Request getAckRequest(ClientTransaction clientTransaction) {
        return getAckRequest(clientTransaction, EMPTY_CONTENT);
    }

    public static Request getAckRequest(ClientTransaction clientTransaction, byte[] content) {
        final Dialog dialog = clientTransaction.getDialog();
        try {
            final Request ack = dialog.createAck(1L);
            HeaderFactory headerFactory = SipLayer.getHeaderFactory();
            ack.setContentLength(headerFactory.createContentLengthHeader(content.length));
            ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader(CONTENT_TYPE, CONTENT_SUBTYPE_SDP);
            ack.setContent(content, contentTypeHeader);
            return ack;
        } catch (InvalidArgumentException | SipException | ParseException e) {
            log.error("Create a ack request exception, cause: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Request getInfoRequest(ClientTransaction clientTransaction, byte[] content) {
        final Dialog dialog = clientTransaction.getDialog();
        try {
            final Request info = dialog.createRequest(Request.INFO);
            HeaderFactory headerFactory = SipLayer.getHeaderFactory();
            info.setContentLength(headerFactory.createContentLengthHeader(content.length));
            ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader(CONTENT_TYPE, CONTENT_SUBTYPE_SDP);
            info.setContent(content, contentTypeHeader);
            return info;
        } catch (InvalidArgumentException | SipException | ParseException e) {
            log.error("Create a info request exception, cause: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Request getByeRequest(ClientTransaction clientTransaction) {
        final Dialog dialog = clientTransaction.getDialog();
        try {
            return dialog.createRequest(Request.BYE);
        } catch (SipException e) {
            log.error("Create a bye request exception, cause: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String getCallId(Request request) {
        return ((SIPRequest) request).getCallId().getCallId();
    }

    public static To createTo(String user, String ip, int port, String tag) {
        return new To(user, new Host(ip, port), tag);
    }

    public static To createTo(String user, String ip, int port) {
        return new To(user, new Host(ip, port));
    }

    public static From createFrom(String user, String ip, int port, String tag) {
        return new From(user, new Host(ip, port), tag);
    }

    public static From createFrom(String user, String ip, int port) {
        return new From(user, new Host(ip, port));
    }

    /**
     * Create a sip request
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
    private static Request createRequest(To to, From from, String subject, String viaBranch, String contentType, String contentSubType,
                                         String method, String transport, byte[] content) {
        try {
            final AddressFactory addressFactory = SipLayer.getAddressFactory();
            // sip uri
            SipURI requestURI = addressFactory.createSipURI(to.user, to.host.toAddress());

            HeaderFactory headerFactory = SipLayer.getHeaderFactory();

            // To
            SipURI toSipURI = addressFactory.createSipURI(to.user, to.host.toAddress());
            Address toAddress = addressFactory.createAddress(toSipURI);
            ToHeader toHeader = headerFactory.createToHeader(toAddress, to.tag);

            // CSeq
            CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1L, method);

            // Call-ID
            CallIdHeader callIdHeader = SipLayer.getSipProvider(SipLayer.getTransport(transport)).getNewCallId();

            // Via
            ViaHeader viaHeader = headerFactory.createViaHeader(from.host.ip, from.host.port, transport, viaBranch); // viaBranch may be null, but not empty.
            viaHeader.setRPort();
            List<ViaHeader> viaHeaders = Collections.singletonList(viaHeader);

            // From
            SipURI fromSipURI = addressFactory.createSipURI(from.user, from.host.toAddress());
            Address fromAddress = addressFactory.createAddress(fromSipURI);
            FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, from.tag);

            // Content-Type
            ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader(contentType, contentSubType);

            // Max-Forwards
            MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);

            final Request request = sipFactory.createMessageFactory().createRequest(requestURI, method, callIdHeader, cSeqHeader, fromHeader, toHeader,
                    viaHeaders, maxForwards, contentTypeHeader, content);

            // Content-Length:消息实体的字节长度
            final ContentLengthHeader contentLengthHeader = headerFactory.createContentLengthHeader(content.length);
            request.setContentLength(contentLengthHeader);

            // Contact
            Address concatAddress = addressFactory.createAddress(addressFactory.createSipURI(from.user, from.host.toAddress()));
            final ContactHeader contactHeader = headerFactory.createContactHeader(concatAddress);
            request.addHeader(contactHeader);

            // Subject
            if (subject != null && !subject.isEmpty()) {
                final SubjectHeader subjectHeader = headerFactory.createSubjectHeader(subject);
                request.addHeader(subjectHeader);
            }

            return request;
        } catch (ParseException | PeerUnavailableException | InvalidArgumentException e) {
            log.error("Create a request exception, cause: {} \ncreate params: \nto:{} \nfrom:{} \nsubject:{} \ncontentType:{} \ncontentSubType:{} \nmethod:{} \ntransport:{} \ncontent:{}.",
                    e.getMessage(), to, from, subject, contentType, contentSubType, method, transport, new String(content));
            throw new RuntimeException("Create a request exception.", e);
        }
    }

    @Getter
    static class To {
        private String user;
        private Host host;
        private String tag;

        /**
         * To constructor
         *
         * @param user
         * @param host
         * @param tag  this value may be null, but not empty.
         */
        To(String user, Host host, String tag) {
            this(user, host);
            this.tag = tag;
        }

        To(String user, Host host) {
            this.user = user;
            this.host = host;
        }
    }

    @Getter
    static class From {
        // SIP服务 or 设备 or 流媒体的编码
        private String user;
        private Host host;
        private String tag;

        /**
         * From constructor
         *
         * @param user
         * @param host
         * @param tag  this value may be null, but not empty.
         */
        From(String user, Host host, String tag) {
            this(user, host);
            this.tag = tag;
        }

        From(String user, Host host) {
            this.user = user;
            this.host = host;
            this.tag = UUIDUtil.getUUID();
        }
    }

    @Getter
    static class Host {
        private String ip;
        private int port;

        Host(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        private String toAddress() {
            return ip + ":" + port;
        }
    }
}
