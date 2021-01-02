package com.hlcy.yun.gb28181.operation.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

// TODO 考虑缓存中的垃圾清理
public final class FlowContextCache {
    private final static TimedCache<String, FlowContext> CONTEXT_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);

    public static void put(String key, FlowContext context) {
        CONTEXT_CACHE.put(key, context);
    }

    public static void setNewKey(String oldKey, String newKey) {
        final FlowContext context = CONTEXT_CACHE.get(oldKey);
        CONTEXT_CACHE.put(newKey, context);
        CONTEXT_CACHE.remove(oldKey);
    }

    public static FlowContext get(String key) {
        return CONTEXT_CACHE.get(key);
    }

    public static void remove(String key) {
        CONTEXT_CACHE.remove(key);
    }
}
