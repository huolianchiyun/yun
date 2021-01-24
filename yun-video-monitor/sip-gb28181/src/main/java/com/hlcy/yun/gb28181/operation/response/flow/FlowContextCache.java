package com.hlcy.yun.gb28181.operation.response.flow;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.CacheObj;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Iterator;
import java.util.Optional;

// TODO 考虑缓存中的垃圾清理--可以尝试向设备发送消息，设备不会认为是垃圾数据，考虑清除
@Slf4j
public final class FlowContextCache {
    private final static String CONTEXT_CACHE_STORE_PATH = System.getProperty("user.dir")
            .concat(System.getProperty("file.separator"))
            .concat("CONTEXT_CACHE");
    private static TimedCache<String, FlowContext> CONTEXT_CACHE = CacheUtil.newTimedCache(Integer.MAX_VALUE);

    public static void init() {
        recoverContextCache();
        setShutdownAction();
    }

    private static void recoverContextCache() {
        final File file = new File(CONTEXT_CACHE_STORE_PATH);
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                TimedCache<String, FlowContext> temp = IoUtil.readObj(in);
                temp.forEach(e -> {
                    final String ssrc = e.getSsrc();
                    if (ssrc != null && ssrc.length() > 0){
                        e.setRecovered(true);
                        e.setCurrentProcessorToFirstByeProcessor();
                        CONTEXT_CACHE.put(ssrc, e);
                    }
                });
                temp.clear();
                log.info("*** Load data from file to CONTEXT_CACHE when application start up, size: {} ***", CONTEXT_CACHE.size());
            } catch (IOException e) {
                log.error("*** Load data from file to CONTEXT_CACHE exception  ***");
                System.exit(-1);
            }
            FileUtil.del(file);
        }
    }

    private static void setShutdownAction() {
        // Write CONTEXT_CACHE into file when application close
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                IoUtil.writeObj(new FileOutputStream(new File(CONTEXT_CACHE_STORE_PATH)), true, CONTEXT_CACHE);
                log.info("*** Write CONTEXT_CACHE to file when application close, size: {} ***", CONTEXT_CACHE.size());
            } catch (FileNotFoundException e) {
                log.error("*** Write CONTEXT_CACHE to file exception ***");
                e.printStackTrace();
            }
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
            if (context.getOperationalParams().getChannelId().equals(channelId)) return Optional.ofNullable(context);
        }
        return Optional.empty();
    }
}
