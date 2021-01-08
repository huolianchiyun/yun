package com.hlcy.yun.gb28181.operation.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.gb28181.bean.api.PlayParams;
import com.hlcy.yun.gb28181.bean.api.PlaybackParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.operation.Operation;
import com.hlcy.yun.gb28181.operation.ResponseProcessor;
import javax.sip.ClientTransaction;
import java.util.Iterator;

public class FlowContext {
    private static GB28181Properties properties;
    private final TimedCache<Enum, ClientTransaction> SESSION_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);
    private final Operation operation;
    private ResponseProcessor currentProcessor;
    private PlayParams playParams;
    private PlaybackParams playbackParams;
    private String ssrc;

    public FlowContext(Operation operation, PlayParams playParams) {
        this.operation = operation;
        this.playParams = playParams;
        this.currentProcessor = FlowPipelineFactory.getFlowPipeline(operation).first();
    }

    public FlowContext(Operation operation, PlaybackParams playbackParams) {
        this.operation = operation;
        this.playbackParams = playbackParams;
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

    public static void setProperties(GB28181Properties properties){
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

    public ClientTransaction get(Enum key) {
        return SESSION_CACHE.get(key);
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
