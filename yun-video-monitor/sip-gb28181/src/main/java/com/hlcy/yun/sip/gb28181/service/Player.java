package com.hlcy.yun.sip.gb28181.service;

import com.hlcy.yun.sip.gb28181.bean.Device;

public interface Player {

    /**
     * 点播或直播
     *
     * @param device /
     */
    void play(Device device);

    /**
     * 停止播放
     * @param ssrc /
     */
    void stop(String ssrc);

//    void fastForward();
}
