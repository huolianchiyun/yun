package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.gb28181.bean.PlaybackInfo;
import com.hlcy.yun.gb28181.operation.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.Player;
import com.hlcy.yun.gb28181.bean.Device;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import static com.hlcy.yun.common.web.response.ResponseUtil.success;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/yun/player")
@RestController
public class PlayerController {

    private final Player player;

    @PostMapping("/play")
    public DeferredResult<ResponseEntity<ResponseData>> play(@RequestBody Device device) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_PLAY + device.getDeviceId(), result);
        player.play(device);
        return result;
    }

    @PostMapping("/play/stop/{ssrc}")
    public ResponseEntity<ResponseData<Void>> playStop(@PathVariable String ssrc) {
        player.playStop(ssrc);
        return success();
    }

    @PostMapping("/playback")
    public DeferredResult<ResponseEntity<ResponseData>> playback(@RequestBody PlaybackInfo playbackInfo) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_PLAYBACK + playbackInfo.getDeviceId(), result);
        player.playback(playbackInfo);
        return result;
    }

    @PostMapping("/playback/stop/{ssrc}")
    public ResponseEntity<ResponseData<Void>> playback(@PathVariable String ssrc) {
        player.playbackStop(ssrc);
        return success();
    }

    @PostMapping("/playback/scale/{scale}/{ssrc}")
    public ResponseEntity<ResponseData<Void>> playbackForwardOrkBack(@PathVariable String ssrc, @PathVariable double scale) {
        player.playbackForwardOrkBack(ssrc, scale);
        return success();
    }

    @PostMapping("/playback/drag/{ssrc}")
    public ResponseEntity<ResponseData<Void>> playbackDrag(@PathVariable String ssrc) {
        player.playbackDrag(ssrc);
        return success();
    }

    @PostMapping("/playback/pause/{ssrc}")
    public ResponseEntity<ResponseData<Void>> playbackPause(@PathVariable String ssrc) {
        player.playbackPause(ssrc);
        return success();
    }

    @PostMapping("/playback/replay/{ssrc}")
    public ResponseEntity<ResponseData<Void>> playbackReplay(@PathVariable String ssrc) {
        player.playbackReplay(ssrc);
        return success();
    }
}
