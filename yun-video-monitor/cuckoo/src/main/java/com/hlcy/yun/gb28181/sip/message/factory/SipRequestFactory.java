package com.hlcy.yun.gb28181.sip.message.factory;

import com.hlcy.yun.gb28181.sip.javax.DeserializeClientTransaction;
import com.hlcy.yun.gb28181.sip.SipLayer;
import gov.nist.javax.sip.Utils;
import gov.nist.javax.sip.header.*;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.extern.slf4j.Slf4j;

import javax.sip.*;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.List;

@Slf4j
public final class SipRequestFactory {

    private static final String CONTENT_TYPE = "Application";

    private static final String CONTENT_SUBTYPE_SDP = "SDP";

    private static final String CONTENT_SUBTYPE_MANSCDP = "MANSCDP+xml";

    private static final String CONTENT_SUBTYPE_MANSRTSP = "MANSRTSP";

    private static final byte[] EMPTY_CONTENT = new byte[0];


    public static Request getMessageRequest(To to, From from, Transport transport) {
        return createRequest(to, from, null, null, CONTENT_TYPE, CONTENT_SUBTYPE_MANSCDP, Request.MESSAGE, transport, EMPTY_CONTENT);
    }

    public static Request getMessageRequest(To to, From from, Transport transport, byte[] content) {
        return createRequest(to, from, null, null, CONTENT_TYPE, CONTENT_SUBTYPE_MANSCDP, Request.MESSAGE, transport, content);
    }

    public static Request getMessageRequest(To to, From from, String subject, String viaBranch, Transport transport, byte[] content) {
        return createRequest(to, from, subject, viaBranch, CONTENT_TYPE, CONTENT_SUBTYPE_MANSCDP, Request.MESSAGE, transport, content);
    }

    public static Request getSubscribeRequest(To to, From from, Transport transport, byte[] content) {
        return createRequest(to, from, null, null, CONTENT_TYPE, CONTENT_SUBTYPE_MANSCDP, Request.SUBSCRIBE, transport, content);
    }


    public static Request getInviteRequest(To to, From from, Transport transport) {
        return createRequest(to, from, null, null, CONTENT_TYPE, CONTENT_SUBTYPE_SDP, Request.INVITE, transport, EMPTY_CONTENT);
    }

    public static Request getInviteRequest(To to, From from, Transport transport, byte[] content) {
        return createRequest(to, from, null, null, CONTENT_TYPE, CONTENT_SUBTYPE_SDP, Request.INVITE, transport, content);
    }

    public static Request getInviteRequest(To to, From from, String subject, String viaBranch, Transport transport, byte[] content) {
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
            final CSeqHeader cSeqHeader = (CSeqHeader) clientTransaction.getRequest().getHeader(CSeqHeader.NAME);
            CSeqHeader infoSeqHeader = (CSeqHeader) info.getHeader(CSeqHeader.NAME);
            infoSeqHeader.setSeqNumber(cSeqHeader.getSeqNumber() + 1);

            HeaderFactory headerFactory = SipLayer.getHeaderFactory();
            info.setContentLength(headerFactory.createContentLengthHeader(content.length));
            ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader(CONTENT_TYPE, CONTENT_SUBTYPE_MANSRTSP);
            info.setContent(content, contentTypeHeader);
            return info;
        } catch (InvalidArgumentException | SipException | ParseException e) {
            log.error("Create a info request exception, cause: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Request getByeRequest(Transaction transaction) {
        Request byeRequest;
        try {
            if (isDeserializeTransaction(transaction)) {
                byeRequest = toByeRequest(transaction.getRequest());
            } else {
                byeRequest = transaction.getDialog().createRequest(Request.BYE);
            }
            CSeqHeader cSeqHeader = (CSeqHeader) byeRequest.getHeader(CSeqHeader.NAME);
            cSeqHeader.setSeqNumber(Integer.MAX_VALUE - 1);
        } catch (SipException | InvalidArgumentException e) {
            log.error("Create a bye byeRequest exception, cause: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return byeRequest;
    }

    public static Request getRequest(To to, From from, String method, Transport transport, byte[] content) {
        return createRequest(to, from, null, null, CONTENT_TYPE, CONTENT_SUBTYPE_MANSCDP, method, transport, content);
    }

    private static boolean isDeserializeTransaction(Transaction transaction) {
        return transaction.getClass().isAssignableFrom(DeserializeClientTransaction.class);
    }

    private static Request toByeRequest(Request request) {
        try {
            request.setMethod(Request.BYE);

            request.removeHeader(ContentType.NAME);
            final ContentLengthHeader contentLengthHeader = (ContentLengthHeader) request.getHeader(ContentLength.NAME);
            contentLengthHeader.setContentLength(0);
            request.removeContent();

            CSeqHeader cSeqHeader = (CSeqHeader) request.getHeader(CSeq.NAME);
            cSeqHeader.setMethod(Request.BYE);
            cSeqHeader.setSeqNumber(2L);

            ViaHeader viaHeader = (ViaHeader) request.getHeader(Via.NAME);
            viaHeader.removeParameter(Via.RPORT);
            viaHeader.removeParameter(Via.BRANCH);

            request.removeHeader(Contact.NAME);

            ToHeader toHeader = (ToHeader) request.getHeader(gov.nist.javax.sip.header.To.NAME);
            toHeader.setTag(Utils.getInstance().generateTag());
        } catch (InvalidArgumentException | ParseException e) {
            log.error("An exception occurs when convert a request to bye request, cause: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return request;
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
     * @param to             /
     * @param from           /
     * @param subject        /
     * @param contentType    /
     * @param contentSubType /
     * @param method         /
     * @param content        /
     * @return {@link Request}
     */
    private static Request createRequest(To to, From from, String subject, String viaBranch, String
            contentType, String contentSubType, String method, Transport transport, byte[] content) {
        try {
            // sip uri
            final SipURI sipURI = SipMessageFactoryHelper.buildSipURI(to);
            // To
            final ToHeader toHeader = SipMessageFactoryHelper.buildToHeader(to);
            // CSeq
            CSeqHeader cSeqHeader = SipMessageFactoryHelper.buildCSeqHeader(1L, method);
            // Call-ID
            CallIdHeader callIdHeader = SipMessageFactoryHelper.buildCallIdHeader(transport);
            // via
            final List<ViaHeader> viaHeaders = SipMessageFactoryHelper.buildViaHeaders(from, viaBranch, transport);
            // From
            final FromHeader fromHeader = SipMessageFactoryHelper.buildFromHeader(from);
            // Content-Type
            final ContentTypeHeader contentTypeHeader = SipMessageFactoryHelper.buildContentTypeHeader(contentType, contentSubType);
            // Max-Forwards
            final MaxForwardsHeader maxForwards = SipMessageFactoryHelper.buildMaxForwardsHeader();

            final Request request = SipMessageFactoryHelper.buildRequest(sipURI, method, callIdHeader, cSeqHeader, fromHeader, toHeader,
                    viaHeaders, maxForwards, contentTypeHeader, content);

            // Contact
            final ContactHeader contactHeader = SipMessageFactoryHelper.buildContactHeader(from);
            request.addHeader(contactHeader);

            // Subject
            if (subject != null && !subject.isEmpty()) {
                final SubjectHeader subjectHeader = SipMessageFactoryHelper.buildSubjectHeader(subject);
                request.addHeader(subjectHeader);
            }

            return request;
        } catch (ParseException | PeerUnavailableException | InvalidArgumentException e) {
            log.error("Create a request exception, cause: {} \ncreate params: \nto:{} \nfrom:{} \nsubject:{} \ncontentType:{} \ncontentSubType:{} \nmethod:{} \ntransport:{} \ncontent:{}.",
                    e.getMessage(), to, from, subject, contentType, contentSubType, method, transport, new String(content));
            throw new RuntimeException("Create a request exception.", e);
        }
    }
}
