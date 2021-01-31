package com.hlcy.yun.gb28181.sip.message.factory;

import com.hlcy.yun.gb28181.sip.SipLayer;
import lombok.extern.slf4j.Slf4j;

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
}

