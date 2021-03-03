package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.gb28181.service.params.player.DownloadParams;
import com.hlcy.yun.gb28181.service.params.player.PlayParams;
import com.hlcy.yun.gb28181.service.params.player.PlaybackParams;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.Player;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import static com.hlcy.yun.common.web.response.ResponseUtil.success;


@Slf4j
@Api(tags = "GB28181：播放 API")
@RequiredArgsConstructor
@RequestMapping("/yun/player")
@RestController
public class PlayerController {

    private final Player player;

    @PostMapping("/play")
    @ApiOperation("点播")
    public DeferredResult<ResponseEntity<ResponseData>> play(@RequestBody PlayParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_PLAY + params.getChannelId()).getCallbackKey(), result);
        player.play(params);
        return result;
    }

    @PostMapping("/stop/{ssrc}")
    @ApiOperation("点播或历史回放停止（流媒体无人观看停止专用接口)")
    public ResponseEntity<ResponseData<Void>> stop(@PathVariable String ssrc) {
        player.stop(ssrc);
        return success();
    }

    @ApiOperation("历史回放")
    @PostMapping("/playback")
    public DeferredResult<ResponseEntity<ResponseData>> playback(@RequestBody PlaybackParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_PLAYBACK + params.getChannelId()).getCallbackKey(), result);
        player.playback(params);
        return result;
    }

    @ApiOperation("历史回放快进或回退")
    @PostMapping("/playback/scale/{scale}/{ssrc}")
    public ResponseEntity<ResponseData<Void>> playbackForwardOrkBack(@PathVariable String ssrc, @PathVariable double scale) {
        player.playbackForwardOrkBack(ssrc, scale);
        return success();
    }

    @ApiOperation("历史回放进度条拖拽")
    @PostMapping("/playback/drag/{ssrc}/{range}")
    public ResponseEntity<ResponseData<Void>> playbackDrag(@PathVariable String ssrc, @PathVariable int range) {
        player.playbackDrag(ssrc, range);
        return success();
    }

    @ApiOperation("历史回放暂停")
    @PostMapping("/playback/pause/{ssrc}")
    public ResponseEntity<ResponseData<Void>> playbackPause(@PathVariable String ssrc) {
        player.playbackPause(ssrc);
        return success();
    }

    @ApiOperation("历史回放暂停后重播")
    @PostMapping("/playback/replay/{ssrc}")
    public ResponseEntity<ResponseData<Void>> playbackReplay(@PathVariable String ssrc) {
        player.playbackReplay(ssrc);
        return success();
    }

    @ApiOperation("历史视频下载")
    @PostMapping("/playback/download")
    public DeferredResult<ResponseEntity<ResponseData>> downloadHistoryVideo(@RequestBody DownloadParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_PLAYBACK_DOWNLOAD + params.getChannelId()).getCallbackKey(), result);
        player.downloadHistoryVideo(params);
        return result;
    }
}
