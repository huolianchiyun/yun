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
    public ResponseEntity<ResponseData<Void>> ptzControl(@RequestBody PtzControlParams ptzControlParams) {
        operator.operate(ptzControlParams);
        return success();
    }

    @ApiOperation("远程启动控制")
    @PostMapping("/teleBoot")
    public ResponseEntity<ResponseData<Void>> teleBootControl(@RequestBody TeleBootControlParams teleBootControlParams) {
        operator.operate(teleBootControlParams);
        return success();
    }

    @ApiOperation("强制关键帧")
    @PostMapping("/ifame")
    public ResponseEntity<ResponseData<Void>> iframeControl(@RequestBody IFameControlParams iFameControlParams) {
        operator.operate(iFameControlParams);
        return success();
    }

    @ApiOperation("拉框放大/缩小")
    @PostMapping("/zoom")
    public ResponseEntity<ResponseData<Void>> zoomControl(@RequestBody DragZoomControlParams dragZoomControlParams) {
        operator.operate(dragZoomControlParams);
        return success();
    }

    @ApiOperation("手动录像")
    @PostMapping("/record")
    public DeferredResult<ResponseEntity<ResponseData>> recordControl(@RequestBody RecordControlParams recordControlParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_RECORD + recordControlParams.getChannelId(), result);
        operator.operate(recordControlParams);
        return result;
    }

    @ApiOperation("报警布防/撤防")
    @PostMapping("/guard")
    public DeferredResult<ResponseEntity<ResponseData>> guardControl(@RequestBody GuardControlParams guardControlParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_GUARD + guardControlParams.getChannelId(), result);
        operator.operate(guardControlParams);
        return result;
    }

    @ApiOperation("告警复位")
    @PostMapping("/resetAlarm")
    public DeferredResult<ResponseEntity<ResponseData>> resetAlarmControl(@RequestBody ResetAlarmControlParams ResetAlarmControlParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_RESET_ALARM + ResetAlarmControlParams.getChannelId(), result);
        operator.operate(ResetAlarmControlParams);
        return result;
    }

    @ApiOperation("看守位控制")
    @PostMapping("/homePosition")
    public DeferredResult<ResponseEntity<ResponseData>> homePositionControl(@RequestBody HomePositionControlParams homePositionControlParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_HOME_POSITION + homePositionControlParams.getChannelId(), result);
        operator.operate(homePositionControlParams);
        return result;
    }

    @ApiOperation("设备配置控制")
    @PostMapping("/deviceConfig")
    public DeferredResult<ResponseEntity<ResponseData>> deviceConfigControl(@RequestBody DeviceConfigControlParams deviceConfigControlParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_DEVICE_CONFIG + deviceConfigControlParams.getChannelId(), result);
        operator.operate(deviceConfigControlParams);
        return result;
    }

    // ++++++++++++++++++++++++++++++++++++++++
    @ApiOperation("预置位")
    @PostMapping("/preset")
    public DeferredResult<ResponseEntity<ResponseData>> presetControl(@RequestBody PresetControlParams presetControlParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_PRESET + presetControlParams.getChannelId(), result);
        operator.operate(presetControlParams);
        if (PresetControlParams.PresetType.CALL_PRESET == presetControlParams.getType()) {
            DeferredResultHolder.setDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_PRESET + presetControlParams.getChannelId(), "ok");
        }
        return result;
    }

    @ApiOperation("巡航")
    @PostMapping("/cruise")
    public DeferredResult<ResponseEntity<ResponseData>> cruiseControl(@RequestBody CruiseControlParams cruiseControlParams) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_CRUISE + cruiseControlParams.getChannelId(), result);
        operator.operate(cruiseControlParams);
        return result;
    }

    @ApiOperation("光圈控制和聚焦控制")
    @PostMapping("/fi")
    public ResponseEntity<ResponseData<Void>> fiControl(@RequestBody FIControlParams fiControlParams) {
        operator.operate(fiControlParams);
        return success();
    }

    @ApiOperation("辅助开关")
    @PostMapping("/switch")
    public ResponseEntity<ResponseData<Void>> switchControl(@RequestBody AuxilSwitchControlParams auxilSwitchControlParams) {
        operator.operate(auxilSwitchControlParams);
        return success();
    }

    @ApiOperation("水平扫描")
    @PostMapping("/scan")
    public ResponseEntity<ResponseData<Void>> scanControl(@RequestBody ScanControlParams scanControlParams) {
        operator.operate(scanControlParams);
        return success();
    }
}
