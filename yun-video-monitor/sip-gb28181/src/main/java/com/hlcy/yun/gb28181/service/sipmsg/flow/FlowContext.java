package com.hlcy.yun.gb28181.service.sipmsg.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.gb28181.service.params.DeviceParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.sip.client.RequestProcessor;
import com.hlcy.yun.gb28181.sip.client.ResponseProcessor;
import com.hlcy.yun.gb28181.sip.javax.RecoveredClientTransaction;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;

import javax.sip.ClientTransaction;
import java.io.Serializable;
import java.util.Iterator;

public class FlowContext extends MessageContext implements Serializable {
    private static final long serialVersionUID = 1L;

    private static GB28181Properties properties;

    private final TimedCache<Enum, ClientTransaction> SESSION_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);

    private boolean isRecovered;

    private final Operation operation;

    private DeviceParams operationalParams;

    private String ssrc;

    public FlowContext(Operation operation, DeviceParams operationalParams) {
        this(operation);
        this.operationalParams = operationalParams;
    }

    //TODO 后续优化
    private FlowContext(Operation operation) {
        this.operation = operation;
        if (operation == Operation.GUARD || operation == Operation.HOME_POSITION || operation == Operation.KEEPALIVE
                || operation == Operation.RECORD || operation == Operation.RESET_ALARM) {
            this.currentRequestProcessor = FlowPipelineFactory.getRequestFlowPipeline(operation).first();
        } else if (operation == Operation.PLAY || operation == Operation.PLAYBACK) {
            this.currentResponseProcessor = FlowPipelineFactory.getResponseFlowPipeline(operation).first();
        }
    }

    public ResponseProcessor getResponseProcessor() {
        return currentResponseProcessor;
    }

    public RequestProcessor getRequestProcessor() {
        return currentRequestProcessor;
    }

    /**
     * Switch current response processor to the next response processor.
     */
    public void setCurrentRequestProcessor2next() {
        this.currentRequestProcessor = this.currentRequestProcessor.getNextProcessor();
    }

    @Override
    public void setCurrentResponseProcessor2next() {
        this.currentResponseProcessor = this.currentResponseProcessor.getNextProcessor();
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

    public ClientTransaction get(Enum key) {
        final ClientTransaction clientTransaction = SESSION_CACHE.get(key);
        if (isRecovered) {
            return new RecoveredClientTransaction(clientTransaction);
        }
        return clientTransaction;
    }

    public void put(Enum key, ClientTransaction transaction) {
        SESSION_CACHE.put(key, transaction);
    }

    public Iterator<ClientTransaction> iterator() {
        return SESSION_CACHE.iterator();
    }

    public void clearSessionCache() {
        SESSION_CACHE.clear();
    }

}
