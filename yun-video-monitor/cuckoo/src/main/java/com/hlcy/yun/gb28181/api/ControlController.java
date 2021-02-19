package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.gb28181.service.params.control.*;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.ControlOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import static com.hlcy.yun.common.web.response.ResponseUtil.success;

@Slf4j
@Api(tags = "GB28181：设备控制 API")
@RequiredArgsConstructor
@RequestMapping("/yun/control")
@RestController
public class ControlController {

    private final ControlOperator<ControlParams> operator;

    @ApiOperation("云台控制")
    @PostMapping("/ptz")
    public ResponseEntity<ResponseData<Void>> ptzControl(@RequestBody PtzControlParams params) {
        operator.operate(params);
        return success();
    }

    @ApiOperation("拉框放大/缩小")
    @PostMapping("/zoom")
    public ResponseEntity<ResponseData<Void>> zoomControl(@RequestBody DragZoomControlParams params) {
        operator.operate(params);
        return success();
    }

    @ApiOperation("水平扫描")
    @PostMapping("/scan")
    public ResponseEntity<ResponseData<Void>> scanControl(@RequestBody ScanControlParams params) {
        operator.operate(params);
        return success();
    }

    @ApiOperation("远程启动控制")
    @PostMapping("/teleBoot")
    public DeferredResult<ResponseEntity<ResponseData>> teleBootControl(@RequestBody TeleBootControlParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_TELE_BOOT + params.getChannelId()).getCallbackKey(), result);
        operator.operate(params);
        return result;
    }

    @ApiOperation("强制关键帧")
    @PostMapping("/iframe")
    public DeferredResult<ResponseEntity<ResponseData>> iframeControl(@RequestBody IFrameControlParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_IFRAME + params.getChannelId()).getCallbackKey(), result);
        operator.operate(params);
        return result;
    }

    @ApiOperation("预置位")
    @PostMapping("/preset")
    public DeferredResult<ResponseEntity<ResponseData>> presetControl(@RequestBody PresetControlParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_PRESET + params.getChannelId()).getCallbackKey(), result);
        operator.operate(params);
        return result;
    }

    @ApiOperation("巡航")
    @PostMapping("/cruise")
    public DeferredResult<ResponseEntity<ResponseData>> cruiseControl(@RequestBody CruiseControlParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_CRUISE + params.getChannelId()).getCallbackKey(), result);
        operator.operate(params);
        return result;
    }

    @ApiOperation("光圈控制和聚焦控制")
    @PostMapping("/fi")
    public DeferredResult<ResponseEntity<ResponseData>> fiControl(@RequestBody FIControlParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_FI + params.getChannelId()).getCallbackKey(), result);
        operator.operate(params);
        return result;
    }

    @ApiOperation("辅助开关")
    @PostMapping("/switch")
    public DeferredResult<ResponseEntity<ResponseData>> switchControl(@RequestBody AuxilSwitchControlParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_SWITCH + params.getChannelId()).getCallbackKey(), result);
        operator.operate(params);
        return result;
    }


    @ApiOperation("看守位控制")
    @PostMapping("/homePosition")
    public DeferredResult<ResponseEntity<ResponseData>> homePositionControl(@RequestBody HomePositionControlParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_HOME_POSITION + params.getChannelId()).getCallbackKey(), result);
        operator.operate(params);
        return result;
    }

    @ApiOperation("手动录像")
    @PostMapping("/record")
    public DeferredResult<ResponseEntity<ResponseData>> recordControl(@RequestBody RecordControlParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(params.getOperate() + params.getChannelId()).getCallbackKey(), result);
        operator.operate(params);
        return result;
    }

    @ApiOperation("报警布防/撤防")
    @PostMapping("/guard")
    public DeferredResult<ResponseEntity<ResponseData>> guardControl(@RequestBody GuardControlParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_GUARD + params.getChannelId()).getCallbackKey(), result);
        operator.operate(params);
        return result;
    }

    @ApiOperation("告警复位")
    @PostMapping("/resetAlarm")
    public DeferredResult<ResponseEntity<ResponseData>> resetAlarmControl(@RequestBody ResetAlarmControlParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_RESET_ALARM + params.getChannelId()).getCallbackKey(), result);
        operator.operate(params);
        return result;
    }

    @ApiOperation("设备配置控制")
    @PostMapping("/deviceConfig")
    public DeferredResult<ResponseEntity<ResponseData>> deviceConfigControl(@RequestBody DeviceConfigControlParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(params.setCallbackKey(DeferredResultHolder.CALLBACK_CMD_DEVICE_CONFIG + params.getChannelId()).getCallbackKey(), result);
        operator.operate(params);
        return result;
    }
}
