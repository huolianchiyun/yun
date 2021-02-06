package com.hlcy.yun.gb28181.service.sipmsg.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.gb28181.service.params.DeviceParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.sip.biz.RequestProcessor;
import com.hlcy.yun.gb28181.sip.biz.ResponseProcessor;
import com.hlcy.yun.gb28181.sip.javax.RecoveredClientTransaction;
import com.hlcy.yun.gb28181.sip.message.Pipeline;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import java.io.Serializable;
import java.util.Iterator;

public class FlowContext implements Serializable, MessageContext {
    private static final long serialVersionUID = 1L;
    private static GB28181Properties properties;

    private final TimedCache<Enum, ClientTransaction> CLIENT_SESSION_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);
    private final TimedCache<Enum, ServerTransaction> SERVER_SESSION_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);

    private RequestProcessor currentRequestProcessor;
    private ResponseProcessor currentResponseProcessor;
    private final Operation operation;
    private DeviceParams operationalParams;
    private boolean mediaPullStream;
    private boolean isRecovered;
    private String ssrc;

    public FlowContext(Operation operation, DeviceParams operationalParams, boolean mediaPullStream) {
        this(operation);
        this.operationalParams = operationalParams;
        this.mediaPullStream = mediaPullStream;
    }

    public FlowContext(Operation operation, DeviceParams operationalParams) {
        this(operation);
        this.operationalParams = operationalParams;
    }

    private FlowContext(Operation operation) {
        this.operation = operation;
        if (operation == Operation.GUARD || operation == Operation.HOME_POSITION || operation == Operation.KEEPALIVE
                || operation == Operation.RECORD || operation == Operation.RESET_ALARM) {
            this.currentRequestProcessor = FlowPipelineFactory.getRequestFlowPipeline(operation).first();
        } else if (operation == Operation.PLAY || operation == Operation.PLAYBACK) {
            this.currentResponseProcessor = FlowPipelineFactory.getResponseFlowPipeline(operation).first();
        } else if (operation == Operation.BROADCAST) {
            this.currentRequestProcessor = FlowPipelineFactory.getRequestFlowPipeline(operation).first();
            this.currentResponseProcessor = FlowPipelineFactory.getResponseFlowPipeline(operation).first();
        }
    }

    public ResponseProcessor responseProcessor() {
        return currentResponseProcessor;
    }

    public RequestProcessor requestProcessor() {
        return currentRequestProcessor;
    }

    /**
     * Switch current response processor to the next response processor.
     */
    public void switchResponseProcessor2next() {
        this.currentResponseProcessor = this.currentResponseProcessor.getNextProcessor();
    }

    @Override
    public Pipeline pipeline() {
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

    void setRecovered(boolean recovered) {
        isRecovered = recovered;
    }

    public boolean isMediaPullStream() {
        return mediaPullStream;
    }

    public ClientTransaction getClientTransaction(Enum key) {
        final ClientTransaction clientTransaction = CLIENT_SESSION_CACHE.get(key);
        if (isRecovered) {
            return new RecoveredClientTransaction(clientTransaction);
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
        CLIENT_SESSION_CACHE.clear();
    }

}
