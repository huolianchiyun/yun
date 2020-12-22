package com.hlcy.yun.sip.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.sip.gb28181.bean.Device;
import com.hlcy.yun.sip.gb28181.operation.callback.DeferredResultHolder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class DeviceController {

    @PostMapping("/devices/{deviceId}/sync")
    public  DeferredResult<ResponseEntity<ResponseData>> devicesSync(@PathVariable String deviceId) {

        Device device;
//        cmder.catalogQuery(device);
        DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_CATALOG + deviceId, result);
        return result;
    }

}
