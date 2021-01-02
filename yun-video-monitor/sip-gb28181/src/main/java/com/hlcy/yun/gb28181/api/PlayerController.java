package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.gb28181.operation.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.Player;
import com.hlcy.yun.gb28181.bean.Device;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;


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

    @PostMapping("/stop/{deviceId}/{ssrc}")
    public DeferredResult<ResponseEntity<ResponseData>> playStop(@PathVariable String deviceId, @PathVariable String ssrc) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_STOP + deviceId, result);
        player.stop(ssrc);
        return result;
    }
}
