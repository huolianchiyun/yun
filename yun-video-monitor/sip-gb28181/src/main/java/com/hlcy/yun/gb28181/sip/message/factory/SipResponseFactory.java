package com.hlcy.yun.gb28181.sip.message.factory;

import com.hlcy.yun.gb28181.sip.SipLayer;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.extern.slf4j.Slf4j;

import javax.sip.header.ContentTypeHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;


@Slf4j
public final class SipResponseFactory {
    /**
     * Creates a new Response message of type specified by the statusCode
     * parameter, based on a specific Request message. This new Response does
     * not contain a body.
     *
     * @param statusCode -
     *                   the new integer of the statusCode value of this Message.
     * @param request    -
     *                   the received Request object upon which to base the Response.
     */
    public static Response buildResponse(int statusCode, Request request) {
        try {
            return SipLayer.getMessageFactory().createResponse(statusCode, request);
        } catch (ParseException e) {
            throw new IllegalArgumentException("An error has been reached unexpectedly while parsing the statusCode", e);
        }
    }

    /**
     * Creates a new Response message of type specified by the statusCode
     * paramater, based on a specific Request with a new body in the form of a
     * Java object and the body content type.
     *
     * @param statusCode  -
     *                    the new integer of the statusCode value of this Message.
     * @param request     -
     *                    the received Reqest object upon which to base the Response.
     * @param content     -
     *                    the new Object of the body content value of this Message.
     * @param contentType -
     *                    the new ContentTypeHeader object of the content type value of
     *                    this Message.
     */
    public static Response buildResponse(int statusCode, Request request, ContentTypeHeader contentType, Object content) {
        try {
            return SipLayer.getMessageFactory().createResponse(statusCode, request, contentType, content);
        } catch (ParseException e) {
            throw new IllegalArgumentException("An error has been reached unexpectedly while parsing the statusCode", e);
        }
    }
}

