package com.hlcy.yun.gb28181.sip.message.factory;

import com.hlcy.yun.gb28181.sip.SipLayer;
import lombok.extern.slf4j.Slf4j;

import javax.sip.InvalidArgumentException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Slf4j
public
final class SipMessageFactoryHelper {
    private static final AddressFactory addressFactory = SipLayer.getAddressFactory();
    private static final HeaderFactory headerFactory = SipLayer.getHeaderFactory();
    private static SipFactory sipFactory;

    static Request buildRequest(URI requestURI, String method, CallIdHeader callId, CSeqHeader cSeq, FromHeader from, ToHeader to,
                                List via, MaxForwardsHeader maxForwards, ContentTypeHeader contentType, byte[] content) throws ParseException, PeerUnavailableException {
        return sipFactory.createMessageFactory().createRequest(requestURI, method, callId, cSeq, from, to,
                via, maxForwards, contentType, content);
    }

    static SubjectHeader buildSubjectHeader(String subject) throws ParseException {
        return headerFactory.createSubjectHeader(subject);
    }

    static ContactHeader buildContactHeader(com.hlcy.yun.gb28181.sip.message.factory.Address address) throws ParseException {
        Address concatAddress = addressFactory.createAddress(addressFactory.createSipURI(address.getUser(), address.getHost().toAddress()));
        return headerFactory.createContactHeader(concatAddress);
    }

    static ContactHeader buildContactHeader(Address address) {
        return headerFactory.createContactHeader(address);
    }

    static MaxForwardsHeader buildMaxForwardsHeader() throws InvalidArgumentException {
        return headerFactory.createMaxForwardsHeader(70);
    }

    static ContentTypeHeader buildContentTypeHeader(String contentType, String contentSubType) throws ParseException {
        return headerFactory.createContentTypeHeader(contentType, contentSubType);
    }

    static FromHeader buildFromHeader(From from) throws ParseException {
        SipURI fromSipURI = addressFactory.createSipURI(from.getUser(), from.getHost().toAddress());
        Address fromAddress = addressFactory.createAddress(fromSipURI);
        return headerFactory.createFromHeader(fromAddress, from.getTag());
    }

    static List<ViaHeader> buildViaHeaders(From from, String viaBranch, Transport transport) throws ParseException, InvalidArgumentException {
        // viaBranch may be null, but not empty.
        ViaHeader viaHeader = headerFactory.createViaHeader(from.getHost().getIp(), from.getHost().getPort(), transport.toString(), viaBranch);
        viaHeader.setRPort();
        return Collections.singletonList(viaHeader);
    }

    static CallIdHeader buildCallIdHeader(Transport transport) {
        return SipLayer.getSipProvider(transport).getNewCallId();
    }

    /**
     * Creates a new CSeqHeader based on the newly supplied sequence number and
     * method values.
     *
     * @param sequenceNumber the new long value of the sequence number.
     * @param method         the new string value of the method.
     * @return the newly created CSeqHeader object.
     * @throws InvalidArgumentException if supplied sequence number is less
     *                                  than zero.
     * @throws ParseException           which signals that an error has been reached
     *                                  unexpectedly while parsing the method value.
     */
    static CSeqHeader buildCSeqHeader(long sequenceNumber, String method) throws ParseException, InvalidArgumentException {
        return headerFactory.createCSeqHeader(sequenceNumber, method);
    }

    static ToHeader buildToHeader(To to) throws ParseException {
        SipURI toSipURI = buildSipURI(to);
        Address toAddress = addressFactory.createAddress(toSipURI);
        return headerFactory.createToHeader(toAddress, to.getTag());
    }

    static SipURI buildSipURI(To to) throws ParseException {
        return addressFactory.createSipURI(to.getUser(), to.getHost().toAddress());
    }

    public static void setSipFactory(SipFactory sipFactory) {
        SipMessageFactoryHelper.sipFactory = sipFactory;
    }
}
