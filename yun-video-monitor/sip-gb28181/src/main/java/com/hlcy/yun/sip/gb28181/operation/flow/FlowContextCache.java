package com.hlcy.yun.sip.gb28181.operation.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

public final class FlowContextCache {
    private final static TimedCache<String, FlowContext> CONTEXT_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);

    public static void put(String key, FlowContext context) {
        CONTEXT_CACHE.put(key, context);
    }

    public static FlowContext get(String key) {
        return CONTEXT_CACHE.get(key);
    }

    public static void remove(String key) {
        CONTEXT_CACHE.remove(key);
    }
}
