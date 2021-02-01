package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.gb28181.service.params.query.CatalogQueryParams;
import com.hlcy.yun.gb28181.service.params.query.DeviceInfoQueryParams;
import com.hlcy.yun.gb28181.service.params.query.QueryParams;
import com.hlcy.yun.gb28181.service.params.query.RecordInfoQueryParams;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.QueryOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@Api(tags = "GB28181：设备查询 API")
@RequiredArgsConstructor
@RequestMapping("/yun/query")
@RestController
public class QueryController {
    private final QueryOperator<QueryParams> operator;

    @GetMapping("/catalog")
    @ApiOperation("查询设备目录")
    public DeferredResult<ResponseEntity<ResponseData>> queryCatalog(CatalogQueryParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_QUERY_CATALOG + params.getChannelId(), result);
        operator.operate(params);
        return result;
    }

    @GetMapping("/deviceInfo")
    @ApiOperation("查询设备信息")
    public DeferredResult<ResponseEntity<ResponseData>> queryDeviceInfo(DeviceInfoQueryParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_QUERY_DEVICE_INFO + params.getChannelId(), result);
        operator.operate(params);
        return result;
    }

    @GetMapping("/recordInfo")
    @ApiOperation("查询设备历史录像")
    public DeferredResult<ResponseEntity<ResponseData>> queryRecordInfo(RecordInfoQueryParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_QUERY_RECORD_INFO + params.getChannelId(), result);
        operator.operate(params);
        return result;
    }
}
