package com.hlcy.yun.gb28181.service.sipmsg.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.CacheObj;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.common.spring.SpringContextHolder;
import com.hlcy.yun.common.spring.redis.RedisUtils;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.player.PlayParams;
import com.hlcy.yun.gb28181.sip.biz.MessageContextCache;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import com.hlcy.yun.gb28181.ssrc.SSRCManger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.sip.header.CallIdHeader;
import javax.sip.message.Message;
import java.util.*;
import java.util.stream.Collectors;

// TODO 考虑缓存中的垃圾清理
@Slf4j
public final class FlowContextCacheUtil {
    private static String KEY;
    private static volatile FlowContextCache flowContextCache;
    private static RedisUtils redisUtils;

    public static void init() {
        // 后续初始化预留
        redisUtils = SpringContextHolder.getBean(RedisUtils.class);
        final GB28181Properties properties = SpringContextHolder.getBean(GB28181Properties.class);
        KEY = properties.getSsrcFlowContextInRedisKey();
        new FlowContextCache().init();

        final Set<String> ssrcSet = redisUtils.hkeys(KEY).stream().map(Object::toString).collect(Collectors.toSet());
        SSRCManger.init(properties.getSipDomain().substring(3, 8), ssrcSet);
    }

    public static FlowContext get(String key) {
        return flowContextCache.get(key);
    }

    public static void put(String key, FlowContext context) {
        flowContextCache.put(key, context);
    }

    public static void putSerialize(String key, FlowContext context) {
        redisUtils.hset(KEY, key, context);
    }

    public static void setNewKey(String oldKey, String newKey) {
        flowContextCache.setNewKey(oldKey, newKey);
    }

    public static void remove(String key) {
        flowContextCache.remove(key);
        redisUtils.hdel(KEY, key);
    }

    public static List<FlowContext> findFlowContextByPlayParams(PlayParams params) {
        // 先不考虑，本地缓存找不到，再去redis获取，因为ssrc 对应不看时，流媒体回调stop释放ssrc，
        // 但极端情况有问题，设备通道已达最大值，且ssrc全存储redis，在获取通道，设备将响应繁忙。这也做的目的提升性能
        return flowContextCache.findFlowContextByPlayParams(params);
    }

    public static Optional<FlowContext> findFlowContextBySsrc(String ssrc) {
        final Optional<FlowContext> context = flowContextCache.findFlowContextBySsrc(ssrc);
        if (!context.isPresent()) {
            FlowContext flowContext = (FlowContext) redisUtils.hget(KEY, ssrc);
            if (flowContext != null) {
                flowContext.setFromDeserialization(true);
                flowContextCache.put(ssrc, flowContext);
                return Optional.of(flowContext);
            }
        }
        return context;
    }

    static class FlowContextCache extends MessageContextCache {
        private final TimedCache<String, FlowContext> CONTEXT_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);

        void init() {
            flowContextCache = this;
        }

        public void put(String key, FlowContext context) {
            CONTEXT_CACHE.put(key, context);
        }

        public void setNewKey(String oldKey, String newKey) {
            final FlowContext context = CONTEXT_CACHE.get(oldKey);
            CONTEXT_CACHE.put(newKey, context);
            CONTEXT_CACHE.remove(oldKey);
        }

        public FlowContext get(String key) {
            return CONTEXT_CACHE.get(key);
        }

        public void remove(String key) {
            CONTEXT_CACHE.remove(key);
        }

        @Override
        public MessageContext get(Message message) {
            return get(getCallId(message));
        }

        private String getCallId(Message message) {
            return ((CallIdHeader) message.getHeader(CallIdHeader.NAME)).getCallId();
        }

        List<FlowContext> findFlowContextByPlayParams(PlayParams params) {
            List<FlowContext> result = new ArrayList<>();
            CONTEXT_CACHE.forEach(context -> {
                if (params.getPlay() == context.getOperation()
                        && context.getOperationalParams().getChannelId().equals(params.getChannelId())
                        && !StringUtils.isEmpty(params.getFormat()) == context.isMediaMakeDevicePushStream()) {
                    result.add(context);
                }
            });
            return result;
        }

        Optional<FlowContext> findFlowContextBySsrc(String ssrc) {
            Optional<FlowContext> optional = Optional.ofNullable(FlowContextCacheUtil.get(ssrc));
            if (!optional.isPresent()) {
                final Iterator<CacheObj<String, FlowContext>> cacheObjIterator = CONTEXT_CACHE.cacheObjIterator();
                while (cacheObjIterator.hasNext()) {
                    final FlowContext context = cacheObjIterator.next().getValue();
                    if (!StringUtils.isEmpty(context.getSsrc()) && context.getSsrc().equals(ssrc)) {
                        return Optional.of(context);
                    }
                }
            }
            return optional;
        }
    }
}
