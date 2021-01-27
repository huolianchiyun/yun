package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.gb28181.service.params.*;
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

    private final ControlOperator<DeviceParams> operator;

    @ApiOperation("云台控制")
    @PostMapping("/ptz")
    public ResponseEntity<ResponseData<Void>> ptzControl(@RequestBody PtzParams ptzParams) {
        operator.operate(ptzParams);
        return success();
    }

    @ApiOperation("手动录像")
    @PostMapping("/record")
    public DeferredResult<ResponseEntity<ResponseData>> recordControl(@RequestBody RecordParams recordParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_RECORD_INFO + recordParams.getChannelId(), result);
        operator.operate(recordParams);
        return result;
    }

    @ApiOperation("拉框放大/缩小")
    @PostMapping("/zoom")
    public ResponseEntity<ResponseData<Void>> zoomControl(@RequestBody DragZoomParams dragZoomParams) {
        operator.operate(dragZoomParams);
        return success();
    }

    @ApiOperation("远程启动控制")
    @PostMapping("/teleBoot")
    public ResponseEntity<ResponseData<Void>> teleBootControl(@RequestBody TeleBootParams teleBootParams) {
        operator.operate(teleBootParams);
        return success();
    }

    @ApiOperation("看守位控制")
    @PostMapping("/homePosition")
    public ResponseEntity<ResponseData<Void>> homePositionControl(@RequestBody HomePositionParams homePositionParams) {
        operator.operate(homePositionParams);
        return success();
    }

    @ApiOperation("告警复位")
    @PostMapping("/resetAlarm")
    public ResponseEntity<ResponseData<Void>> resetAlarmControl(@RequestBody AlarmParams AlarmParams) {
        operator.operate(AlarmParams);
        return success();
    }

    @ApiOperation("报警布防/撤防")
    @PostMapping("/guard")
    public ResponseEntity<ResponseData<Void>> guardControl(@RequestBody GuardParams guardParams) {
        operator.operate(guardParams);
        return success();
    }

    @ApiOperation("预置位")
    @PostMapping("/preset")
    public DeferredResult<ResponseEntity<ResponseData>> presetControl(@RequestBody PresetParams presetParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_PRESET + presetParams.getChannelId(), result);
        operator.operate(presetParams);
        return result;
    }

    @ApiOperation("巡航")
    @PostMapping("/cruise")
    public DeferredResult<ResponseEntity<ResponseData>> cruiseControl(@RequestBody CruiseParams cruiseParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_CRUISE + cruiseParams.getChannelId(), result);
        operator.operate(cruiseParams);
        return result;
    }

    @ApiOperation("光圈控制和聚焦控制")
    @PostMapping("/fi")
    public ResponseEntity<ResponseData<Void>> fiControl(@RequestBody FIParams fiParams) {
        operator.operate(fiParams);
        return success();
    }

    @ApiOperation("强制关键帧")
    @PostMapping("/ifame")
    public ResponseEntity<ResponseData<Void>> iframeControl(@RequestBody IFameParams iFameParams) {
        operator.operate(iFameParams);
        return success();
    }
}
