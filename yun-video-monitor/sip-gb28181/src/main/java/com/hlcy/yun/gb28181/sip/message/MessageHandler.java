package com.hlcy.yun.gb28181.sip.message;


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
     * @param sdpMessageBody the sdp message
     * @return {@link SessionDescription}
     */
    protected SessionDescription getSessionDescription(String sdpMessageBody) throws SdpParseException {
        return sdpFactory.createSessionDescription(sdpMessageBody);
    }

}
