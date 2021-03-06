package com.hlcy.yun.gb28181.service.sipmsg.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.gb28181.service.params.DeviceParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.sip.biz.RequestProcessor;
import com.hlcy.yun.gb28181.sip.biz.ResponseProcessor;
import com.hlcy.yun.gb28181.sip.javax.DeserializeClientTransaction;
import com.hlcy.yun.gb28181.sip.message.MessageHandler;
import com.hlcy.yun.gb28181.sip.message.Pipeline;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import lombok.NoArgsConstructor;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static com.hlcy.yun.gb28181.sip.message.handler.MessageContext.PipelineType.REQUEST;
import static com.hlcy.yun.gb28181.sip.message.handler.MessageContext.PipelineType.RESPONSE;

@NoArgsConstructor
public class FlowContext implements MessageContext, Serializable {
    private static final long serialVersionUID = 1L;
    private boolean fromDeserialization;
    private static GB28181Properties properties;

    /**
     * context cleanup state
     */
    private volatile AtomicInteger cleanup = new AtomicInteger(0);

    private final TimedCache<Enum, ClientTransaction> CLIENT_SESSION_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);
    private transient final TimedCache<Enum, ServerTransaction> SERVER_SESSION_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);

    private RequestProcessor currentRequestProcessor;
    private ResponseProcessor currentResponseProcessor;

    private Operation operation;
    private DeviceParams operationalParams;
    private boolean mediaMakeDevicePushStream;
    private long downloadFileSize;
    private String ssrc;

    public FlowContext(Operation operation, DeviceParams operationalParams, String ssrc, boolean mediaMakeDevicePushStream) {
        this(operation);
        this.ssrc = ssrc;
        this.operationalParams = operationalParams;
        this.mediaMakeDevicePushStream = mediaMakeDevicePushStream;
    }

    /**
     * FlowContext constructor
     *
     * @param operation         /
     * @param operationalParams /
     * @param requestProcessor  Current request processor for FlowContext
     * @param responseProcessor Current response processor for FlowContext
     */
    public FlowContext(Operation operation, DeviceParams operationalParams, RequestProcessor requestProcessor, ResponseProcessor responseProcessor) {
        this(operation, operationalParams, responseProcessor);
        this.currentRequestProcessor = requestProcessor;
    }

    /**
     * FlowContext constructor
     *
     * @param operation         /
     * @param operationalParams /
     * @param responseProcessor Current response processor for FlowContext
     */
    public FlowContext(Operation operation, DeviceParams operationalParams, ResponseProcessor responseProcessor) {
        this.operation = operation;
        this.operationalParams = operationalParams;
        this.currentResponseProcessor = responseProcessor;
    }

    public FlowContext(Operation operation, DeviceParams operationalParams) {
        this(operation);
        this.operationalParams = operationalParams;
    }

    private FlowContext(Operation operation) {
        this.operation = operation;
        if (operation == Operation.KEEPALIVE) {
            this.currentRequestProcessor = FlowPipelineFactory.getRequestFlowPipeline(operation).first();
        } else if (operation == Operation.PLAY || operation == Operation.PLAYBACK
                || operation == Operation.DOWNLOAD || operation == Operation.SUBSCRIBE) {
            this.currentResponseProcessor = FlowPipelineFactory.getResponseFlowPipeline(operation).first();
        } else if (operation == Operation.BROADCAST) {
            this.currentRequestProcessor = FlowPipelineFactory.getRequestFlowPipeline(operation).first();
            this.currentResponseProcessor = FlowPipelineFactory.getResponseFlowPipeline(operation).first();
        }
    }

    @Override
    public ResponseProcessor responseProcessor() {
        return currentResponseProcessor;
    }


    @Override
    public RequestProcessor requestProcessor() {
        return currentRequestProcessor;
    }

    /**
     * Switch current response processor to the next response processor.
     */
    @Override
    public void switchResponseProcessor2next() {
        if (this.currentResponseProcessor != null) {
            this.currentResponseProcessor = this.currentResponseProcessor.getNextProcessor();
        }
    }

    @Override
    public Pipeline<? extends MessageHandler> pipeline(PipelineType type) {
        if (REQUEST == type) {
            return FlowPipelineFactory.getRequestFlowPipeline(operation);
        } else if (RESPONSE == type) {
            return FlowPipelineFactory.getResponseFlowPipeline(operation);
        }
        return null;
    }

    void setCurrentProcessorToFirstByeProcessor() {
        this.currentResponseProcessor = FlowPipelineFactory.getResponseFlowPipeline(operation).get(operation.name());
    }

    public static void setProperties(GB28181Properties properties) {
        FlowContext.properties = properties;
    }

    public Operation getOperation() {
        return operation;
    }

    public DeviceParams getOperationalParams() {
        return operationalParams;
    }

    public GB28181Properties getProperties() {
        return properties;
    }

    public String getSsrc() {
        return ssrc;
    }

    public void setSsrc(String ssrc) {
        this.ssrc = ssrc;
    }

    public boolean isMediaMakeDevicePushStream() {
        return mediaMakeDevicePushStream;
    }

    public ClientTransaction getClientTransaction(Enum key) {
        final ClientTransaction clientTransaction = CLIENT_SESSION_CACHE.get(key);
        if (fromDeserialization) {
            return new DeserializeClientTransaction(clientTransaction);
        }
        return clientTransaction;
    }

    public ServerTransaction getServerTransaction(Enum key) {
        return SERVER_SESSION_CACHE.get(key);
    }

    public void put(Enum key, ClientTransaction transaction) {
        CLIENT_SESSION_CACHE.put(key, transaction);
    }

    public void put(Enum key, ServerTransaction transaction) {
        SERVER_SESSION_CACHE.put(key, transaction);
    }

    public Iterator<ClientTransaction> iterator() {
        return CLIENT_SESSION_CACHE.iterator();
    }

    public void clearSessionCache() {
        if (CLIENT_SESSION_CACHE != null) {
            CLIENT_SESSION_CACHE.clear();
        }
        if (SERVER_SESSION_CACHE != null) {
            SERVER_SESSION_CACHE.clear();
        }
    }

    /**
     * context is in clean up state
     *
     * @return true, if it greater than one, it is in the cleanup state, otherwise false, it not in the cleanup state.
     */
    public boolean isCleanup() {
        return cleanup.incrementAndGet() > 1;
    }

    /**
     * Check whether Flow Context is expired
     *
     * @return true expires, false does not expire
     */
    public boolean expire() {
        return cleanup.get() > 1;
    }

    public boolean isFromDeserialization() {
        return fromDeserialization;
    }

    void setFromDeserialization(boolean fromDeserialization) {
        this.fromDeserialization = fromDeserialization;
    }

    public void setCurrentResponseProcessor(ResponseProcessor currentResponseProcessor) {
        this.currentResponseProcessor = currentResponseProcessor;
    }

    public long getDownloadFileSize() {
        return downloadFileSize;
    }

    public void setDownloadFileSize(long downloadFileSize) {
        this.downloadFileSize = downloadFileSize;
    }
}
