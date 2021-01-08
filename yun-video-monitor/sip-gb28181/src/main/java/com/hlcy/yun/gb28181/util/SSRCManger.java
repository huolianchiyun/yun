package com.hlcy.yun.gb28181.util;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.hlcy.yun.gb28181.exception.SSRCException;

import java.util.Set;

/**
 * SIP信令中的 SSRC工具类
 * SSRC值由 10 位十进制整数组成的字符串，
 * 第一位为 0 代表实况，为1则代表回放；
 * 第二位至第六位由监控域 ID的第 4 位到第 8 位组成；最后 4 位为不可重复的 4 个整数.
 */
public final class SSRCManger {

    private static String ssrc4to8bit;

    private final static Set<String> inuseLast4bitSet = new ConcurrentHashSet<>();

    private final static Set<String> unusedLast4bitSet;

    static {
        unusedLast4bitSet = new ConcurrentHashSet<>(9999);
        for (int i = 1; i < 10000; i++) {
            if (i < 10) {
                unusedLast4bitSet.add("000" + i);
            } else if (i < 100) {
                unusedLast4bitSet.add("00" + i);
            } else if (i < 1000) {
                unusedLast4bitSet.add("0" + i);
            } else {
                unusedLast4bitSet.add(String.valueOf(i));
            }
        }
    }

    /**
     * 获取视频预览的SSRC值,第一位固定为0
     */
    public static String getPlaySSRC() throws SSRCException {
        return "0" + ssrc4to8bit + getLast4bit();
    }

    /**
     * 获取录像回放的SSRC值,第一位固定为1
     */
    public static String getPlayBackSSRC() throws SSRCException {
        return "1" + ssrc4to8bit + getLast4bit();
    }

    /**
     * 释放ssrc，主要用完的ssrc一定要释放，否则会耗尽
     */
    public static void releaseSSRC(String ssrc) {
        String last4bit = ssrc.substring(6);
        inuseLast4bitSet.remove(last4bit);
        unusedLast4bitSet.add(last4bit);
    }

    public static void setSsrc4to8bit(String ssrc4to8bit) {
        SSRCManger.ssrc4to8bit = ssrc4to8bit;
    }

    /**
     * 获取后四位数随机数
     */
    private static String getLast4bit() throws SSRCException {
        final String last4bit = unusedLast4bitSet.stream().findAny().orElseThrow(() -> new SSRCException("*** No SSRC available ***"));
        unusedLast4bitSet.remove(last4bit);
        inuseLast4bitSet.add(last4bit);
        return last4bit;
    }
}
