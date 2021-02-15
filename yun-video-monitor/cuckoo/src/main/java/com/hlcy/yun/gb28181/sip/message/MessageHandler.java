package com.hlcy.yun.gb28181.sip.message;

import javax.sdp.SdpFactory;
import javax.sdp.SdpParseException;
import javax.sdp.SessionDescription;
import javax.sip.message.Message;
import java.nio.charset.StandardCharsets;

public abstract class MessageHandler<E> {
    private final SdpFactory sdpFactory = SdpFactory.getInstance();
    protected Pipeline<? extends MessageHandler> pipeline;
    protected MessageHandler<E> prev;
    protected MessageHandler<E> next;

    public abstract void handle(E event);

    /**
     * Get session description
     *
     * @param sdpMessageBody the sdp message
     * @return {@link SessionDescription}
     */
    protected SessionDescription getSessionDescription(String sdpMessageBody) throws SdpParseException {
        return sdpFactory.createSessionDescription(sdpMessageBody);
    }

    protected String getMessageBodyByStr(Message message) {
        return new String(message.getRawContent());
    }

    protected byte[] getMessageBodyByByteArr(Message event) {
        return new String(event.getRawContent()).getBytes(StandardCharsets.UTF_8);
    }

    public Pipeline<? extends MessageHandler> getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline<? extends MessageHandler> pipeline) {
        this.pipeline = pipeline;
    }
}
