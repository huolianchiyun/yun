package com.hlcy.yun.gb28181.ssrc;

import com.hlcy.yun.gb28181.exception.SSRCException;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * SIP信令中的 SSRC工具类
 * SSRC值由 10 位十进制整数组成的字符串，
 * 第一位为 0 代表实况，为1则代表回放；
 * 第二位至第六位由监控域 ID的第 4 位到第 8 位组成；最后 4 位为不可重复的 4 个整数.
 */
public final class SSRCManger {

    private final static Queue<String> UNUSED_LAST4BIT_PLAY_POOL;
    private final static Queue<String> UNUSED_LAST4BIT_PLAYBACK_POOL;

    private static String ssrc4to8bit;

    static {
        UNUSED_LAST4BIT_PLAY_POOL = new ConcurrentLinkedQueue<>();
        UNUSED_LAST4BIT_PLAYBACK_POOL = new ConcurrentLinkedQueue<>();
        for (int i = 1; i < 10000; i++) {
            if (i < 10) {
                UNUSED_LAST4BIT_PLAY_POOL.add("000" + i);
                UNUSED_LAST4BIT_PLAYBACK_POOL.add("000" + i);
            } else if (i < 100) {
                UNUSED_LAST4BIT_PLAY_POOL.add("00" + i);
                UNUSED_LAST4BIT_PLAYBACK_POOL.add("00" + i);
            } else if (i < 1000) {
                UNUSED_LAST4BIT_PLAY_POOL.add("0" + i);
                UNUSED_LAST4BIT_PLAYBACK_POOL.add("0" + i);
            } else {
                UNUSED_LAST4BIT_PLAY_POOL.add(String.valueOf(i));
                UNUSED_LAST4BIT_PLAYBACK_POOL.add(String.valueOf(i));
            }
        }
    }

    public static void init(String ssrc4to8bit, Set<String> inuseSSRCSet) {
        SSRCManger.ssrc4to8bit = ssrc4to8bit;
        inuseSSRCSet.forEach(SSRCManger::removeInuse);
    }

    private static void removeInuse(String ssrc) {
        String last4bit = ssrc.trim().substring(ssrc.length() - 4);
        if (ssrc.startsWith("0")) {
            UNUSED_LAST4BIT_PLAY_POOL.remove(last4bit);
        } else {
            UNUSED_LAST4BIT_PLAYBACK_POOL.remove(last4bit);
        }
    }

    /**
     * 获取视频预览的SSRC值,第一位固定为 0
     */
    public static String getPlaySSRC() throws SSRCException {
        return "0" + ssrc4to8bit + getLast4bitOfPlay();
    }

    /**
     * 获取录像回放的SSRC值,第一位固定为 1
     */
    public static String getPlayBackSSRC() throws SSRCException {
        return "1" + ssrc4to8bit + getLast4bitOfPlayBack();
    }

    /**
     * 获取后四位数随机数
     */
    private static synchronized String getLast4bitOfPlay() throws SSRCException {
        final String last4bit = UNUSED_LAST4BIT_PLAY_POOL.poll();
        if (last4bit == null) {
            throw new SSRCException("*** No SSRC available for play ***");
        }
        return last4bit;
    }

    /**
     * 获取后四位数随机数
     */
    private static synchronized String getLast4bitOfPlayBack() throws SSRCException {
        final String last4bit = UNUSED_LAST4BIT_PLAYBACK_POOL.poll();
        if (last4bit == null) {
            throw new SSRCException("*** No SSRC available for playback***");
        }
        return last4bit;
    }

    /**
     * 释放ssrc，主要用完的ssrc一定要释放，否则会耗尽
     */
    public static synchronized void releaseSSRC(String ssrc) {
        String last4bit = ssrc.trim().substring(ssrc.length() - 4);
        if (ssrc.startsWith("0")) {
            UNUSED_LAST4BIT_PLAY_POOL.add(last4bit);
        } else {
            UNUSED_LAST4BIT_PLAYBACK_POOL.add(last4bit);
        }
    }
}
