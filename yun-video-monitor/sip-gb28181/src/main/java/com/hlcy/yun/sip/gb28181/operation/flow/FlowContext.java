package com.hlcy.yun.sip.gb28181.operation.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.sip.gb28181.bean.Device;
import com.hlcy.yun.sip.gb28181.operation.Operation;
import com.hlcy.yun.sip.gb28181.operation.ResponseProcessor;

import javax.sip.ClientTransaction;

import static com.hlcy.yun.sip.gb28181.operation.flow.FlowPipelineFactory.getFlowPipeline;

public class FlowContext {
    private final TimedCache<String, ClientTransaction> SESSION_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);
    private final Operation operation;
    private ResponseProcessor currentProcessor;
    private Device device;

    public FlowContext(Operation operation, Device device) {
        this.operation = operation;
        this.device = device;
        this.currentProcessor = getFlowPipeline(operation).first();
    }

    public ResponseProcessor getProcessor() {
        return currentProcessor;
    }

    /**
     * Switch current processor to the next processor
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

    public void clearSessionCache() {
        SESSION_CACHE.clear();
    }

}
