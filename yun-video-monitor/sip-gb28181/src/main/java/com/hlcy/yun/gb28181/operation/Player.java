package com.hlcy.yun.gb28181.operation;

import com.hlcy.yun.gb28181.operation.params.PlayParams;
import com.hlcy.yun.gb28181.operation.params.PlaybackParams;

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
     * @param ssrc media stream id
     */
    void stop(String ssrc);

    /**
     * 回放播放
     *
     * @param playbackParams /
     */
    void playback(PlaybackParams playbackParams);

    /**
     * 回放快进或倒放
     *
     * @param ssrc  media stream id
     * @param scale 头应支持的基本取值为 0.25、0.5、1、2、4
     */
    void playbackForwardOrkBack(String ssrc, double scale);

    /**
     * 回放拖动
     *
     * @param ssrc  media stream id
     * @param range 头的值为播放录像起点的相对值,取值范围为 0 到播放录像的终点时间,参数以 s 为单位,不能为负值。
     *              如Range头的值为0,则表示从起点开始播放,Range头的值为100,则表示从录像起点后的100s处开始播放,
     *              Range头的取值为now表示从当前位置开始播放
     */
    void playbackDrag(String ssrc, int range);

    /**
     * 回放暂停
     *
     * @param ssrc media stream id
     */
    void playbackPause(String ssrc);

    /**
     * 回放重新播放
     *
     * @param ssrc media stream id
     */
    void playbackReplay(String ssrc);
}
