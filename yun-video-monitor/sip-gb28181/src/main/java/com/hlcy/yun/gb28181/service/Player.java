package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.bean.api.PlayParams;
import com.hlcy.yun.gb28181.bean.api.PlaybackParams;

public interface Player {

    /**
     * 点播
     *
     * @param params /
     */
    void play(PlayParams params);

    /**
     * 停止点播或回放
     *
     * @param ssrc /
     */
    void stop(String ssrc);

    /**
     * 回放播放
     * @param playbackParams /
     */
    void playback(PlaybackParams playbackParams);

    /**
     * 回放快进或倒放
     */
    void playbackForwardOrkBack(String ssrc, double scale);

    /**
     * 回放拖动
     */
    void playbackDrag(String ssrc);

    /**
     * 回放暂停
     */
    void playbackPause(String ssrc);

    /**
     * 回放重新播放
     */
    void playbackReplay(String ssrc);
}
