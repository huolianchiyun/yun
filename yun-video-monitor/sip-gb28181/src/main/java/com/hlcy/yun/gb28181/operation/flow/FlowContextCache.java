package com.hlcy.yun.gb28181.operation.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.CacheObj;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.common.spring.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Optional;

// TODO 考虑缓存中的垃圾清理
@Slf4j
public final class FlowContextCache {
    private static TimedCache<String, FlowContext> CONTEXT_CACHE;
    private static String SIP_FLOW_CONTEXT_REDIS_KEY;

    public static void init(String sipFlowContextRedisKey, RedisUtils redisUtils) {
        FlowContextCache.SIP_FLOW_CONTEXT_REDIS_KEY = sipFlowContextRedisKey;

        // Load data into CONTEXT_CACHE when application start up
        CONTEXT_CACHE = (TimedCache<String, FlowContext>) redisUtils.get(SIP_FLOW_CONTEXT_REDIS_KEY);
        redisUtils.del(SIP_FLOW_CONTEXT_REDIS_KEY);
        if (CONTEXT_CACHE == null) {
            CONTEXT_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);
        } else {
            log.info("*** Load data from redis to CONTEXT_CACHE when application start up, size: {} ***", CONTEXT_CACHE.size());
        }

        // Write CONTEXT_CACHE into redis when application end
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            redisUtils.set(SIP_FLOW_CONTEXT_REDIS_KEY, CONTEXT_CACHE);
            log.info("*** Write CONTEXT_CACHE into redis when application close, size: {} ***", CONTEXT_CACHE.size());
        }));
    }

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

    public static Optional<FlowContext> findFlowContextBy(String channelId) {
        final Iterator<CacheObj<String, FlowContext>> cacheObjIterator = CONTEXT_CACHE.cacheObjIterator();
        while (cacheObjIterator.hasNext()) {
            final FlowContext context = cacheObjIterator.next().getValue();
            if (context.getPlayParams().getChannelId().equals(channelId)) return Optional.ofNullable(context);
        }
        return Optional.empty();
    }
}
