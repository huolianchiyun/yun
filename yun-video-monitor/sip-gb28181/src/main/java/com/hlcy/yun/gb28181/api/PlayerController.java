package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.gb28181.bean.api.PlayParams;
import com.hlcy.yun.gb28181.bean.api.PlaybackParams;
import com.hlcy.yun.gb28181.operation.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.Player;
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
    public DeferredResult<ResponseEntity<ResponseData>> play(@RequestBody PlayParams playParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_PLAY + playParams.getChannelId(), result);
        player.play(playParams);
        return result;
    }

    @PostMapping("/stop/{ssrc}")
    public ResponseEntity<ResponseData<Void>> playStop(@PathVariable String ssrc) {
        player.stop(ssrc);
        return success();
    }

    @PostMapping("/playback")
    public DeferredResult<ResponseEntity<ResponseData>> playback(@RequestBody PlaybackParams playbackParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_PLAYBACK + playbackParams.getChannelId(), result);
        player.playback(playbackParams);
        return result;
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
