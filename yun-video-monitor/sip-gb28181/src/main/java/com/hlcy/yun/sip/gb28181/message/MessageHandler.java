package com.hlcy.yun.sip.gb28181.message;


import javax.sdp.SdpFactory;
import javax.sdp.SdpParseException;
import javax.sdp.SessionDescription;

public abstract class MessageHandler<E> {
    private final SdpFactory sdpFactory = SdpFactory.getInstance();

    protected MessageHandler<E> prev;
    protected MessageHandler<E> next;

    public abstract void handle(E event);

    /**
     * Get session description
     * @param sdpBody the sdp message
     * @return {@link SessionDescription}
     */
    protected SessionDescription getSessionDescription(String sdpBody) throws SdpParseException {
        return sdpFactory.createSessionDescription(sdpBody);
    }
}
