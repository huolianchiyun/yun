package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.bean.Device;
import com.hlcy.yun.gb28181.bean.PlaybackInfo;

public interface Player {

    /**
     * 点播
     *
     * @param device /
     */
    void play(Device device);

    /**
     * 点播停止
     *
     * @param ssrc /
     */
    void playStop(String ssrc);

    /**
     * 回放播放
     * @param playbackInfo /
     */
    void playback(PlaybackInfo playbackInfo);

    /**
     * 点播停止
     *
     * @param ssrc /
     */
    void playbackStop(String ssrc);

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
