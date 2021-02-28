package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.gb28181.service.QueryOperator;
import com.hlcy.yun.gb28181.service.params.query.AlarmQueryParams;
import com.hlcy.yun.gb28181.service.params.query.QueryParams;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@Api(tags = "GB28181： 设备消息订阅 API")
@RequiredArgsConstructor
@RequestMapping("/yun/subscribe")
@RestController
public class SubscribeController {
    private final QueryOperator<QueryParams> operator;

    @GetMapping("/alarm")
    @ApiOperation("查询设备告警")
    public DeferredResult<ResponseEntity<ResponseData>> queryAlarm(AlarmQueryParams params) {
        final DeferredResult<ResponseEntity<ResponseData>> result = new DeferredResult<>();
        DeferredResultHolder.put(DeferredResultHolder.CALLBACK_CMD_Query_ALARM + params.getChannelId(), result);
        operator.operate(params);
        return result;
    }
}
