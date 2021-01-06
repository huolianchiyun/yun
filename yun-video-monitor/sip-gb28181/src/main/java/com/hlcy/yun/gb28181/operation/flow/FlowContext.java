package com.hlcy.yun.gb28181.operation.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.gb28181.bean.Device;
import com.hlcy.yun.gb28181.bean.PlaybackInfo;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.operation.Operation;
import com.hlcy.yun.gb28181.operation.ResponseProcessor;

import javax.sip.ClientTransaction;
import java.util.Iterator;

public class FlowContext {
    private final TimedCache<Enum, ClientTransaction> SESSION_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);
    private final Operation operation;
    private ResponseProcessor currentProcessor;
    private GB28181Properties properties;
    private Device device;
    private PlaybackInfo playbackInfo;
    private String ssrc;

    public FlowContext(Operation operation, Device device, GB28181Properties properties) {
        this.operation = operation;
        this.device = device;
        this.properties = properties;
        this.currentProcessor = FlowPipelineFactory.getFlowPipeline(operation).first();
    }

    public FlowContext(Operation operation, PlaybackInfo playbackInfo, GB28181Properties properties) {
        this.operation = operation;
        this.playbackInfo = playbackInfo;
        this.properties = properties;
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

    public Operation getOperation() {
        return operation;
    }

    public Device getDevice() {
        return device;
    }

    public PlaybackInfo getPlaybackInfo() {
        return playbackInfo;
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
