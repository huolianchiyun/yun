package com.hlcy.yun.gb28181.sip.client;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.gb28181.sip.message.handler.MessageContext;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import com.hlcy.yun.gb28181.sip.message.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MessageContextCache<C extends MessageContext> {
    protected final TimedCache<String, C> CONTEXT_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);

    public MessageContextCache() {
        ResponseHandler.setContextCache(this);
        RequestHandler.setContextCache(this);
    }

    public abstract void init();

    public abstract void recoverContextCache();

    public void put(String key, C context) {
        CONTEXT_CACHE.put(key, context);
    }

    public void setNewKey(String oldKey, String newKey) {
        final C context = CONTEXT_CACHE.get(oldKey);
        CONTEXT_CACHE.put(newKey, context);
        CONTEXT_CACHE.remove(oldKey);
    }

    public C get(String key) {
        return CONTEXT_CACHE.get(key);
    }

    public void remove(String key) {
        CONTEXT_CACHE.remove(key);
    }
}
