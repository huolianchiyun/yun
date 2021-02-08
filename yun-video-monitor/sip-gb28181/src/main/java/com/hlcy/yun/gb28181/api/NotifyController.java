package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.gb28181.service.NotifyOperator;
import com.hlcy.yun.gb28181.service.command.notify.VoiceBroadcastNotifyCmd;
import com.hlcy.yun.gb28181.service.params.notify.NotifyParams;
import com.hlcy.yun.gb28181.service.params.notify.VoiceBroadcastNotifyParams;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import static com.hlcy.yun.common.web.response.ResponseUtil.success;

@Slf4j
@Api(tags = "GB28181：设备通知 API")
@RequiredArgsConstructor
@RequestMapping("/yun/notify")
@RestController
public class NotifyController {
    private final NotifyOperator<NotifyParams> operator;

    @GetMapping("/voice/broadcast")
    @ApiOperation("语音广播通知")
    public DeferredResult<ResponseEntity<ResponseData>> notifyVoiceBroadcast(VoiceBroadcastNotifyParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_VOICE + params.getChannelId(), result);
        operator.operate(params);
        return result;
    }

    @PostMapping("/stop/{ssrc}")
    @ApiOperation("语音广播停止通知")
    public ResponseEntity<ResponseData<Void>> stop(@PathVariable String ssrc) {
        operator.stopVoiceBroadcast(ssrc);
        return success();
    }
}
