package com.hlcy.yun.gb28181.operation.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.gb28181.bean.api.PlayParams;
import com.hlcy.yun.gb28181.bean.api.PlaybackParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.operation.ResponseProcessor;

import javax.sip.ClientTransaction;
import java.io.Serializable;
import java.util.Iterator;

public class FlowContext implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean isRecovered;
    private static GB28181Properties properties;
    private final TimedCache<Enum, ClientTransaction> SESSION_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);
    private final Operation operation;
    private ResponseProcessor currentProcessor;
    private PlayParams playParams;
    private PlaybackParams playbackParams;
    private String ssrc;

    public FlowContext(Operation operation, PlayParams playParams) {
        this(operation);
        this.playParams = playParams;
    }

    public FlowContext(Operation operation, PlaybackParams playbackParams) {
        this(operation);
        this.playbackParams = playbackParams;
    }

    private FlowContext(Operation operation) {
        this.operation = operation;
        this.currentProcessor = FlowPipelineFactory.getFlowPipeline(operation).first();
    }

    public ResponseProcessor getProcessor() {
        return currentProcessor;
    }

    /**
     * Switch current processor to the next processor.
     */
    public void switch2NextProcessor() {
        this.currentProcessor = this.currentProcessor.getNextProcessor();
    }

    void setCurrentProcessorToFirstByeProcessor() {
        this.currentProcessor = FlowPipelineFactory.getFlowPipeline(operation).get(operation.name());
    }

    public static void setProperties(GB28181Properties properties) {
        FlowContext.properties = properties;
    }

    public Operation getOperation() {
        return operation;
    }

    public PlayParams getPlayParams() {
        return playParams;
    }

    public PlaybackParams getPlaybackParams() {
        return playbackParams;
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

    public boolean isRecovered() {
        return isRecovered;
    }

    public void setRecovered(boolean recovered) {
        isRecovered = recovered;
    }

    public ClientTransaction get(Enum key) {
        final ClientTransaction clientTransaction = SESSION_CACHE.get(key);
        if(isRecovered){
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
