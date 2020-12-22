package com.hlcy.yun.sip.gb28181.operation.callback;

import com.hlcy.yun.common.web.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.hlcy.yun.common.web.response.ResponseUtil.*;

public final class DeferredResultHolder {
    private static final Map<String, List<DeferredResult<ResponseEntity<ResponseData>>>> RequestDeferredResultMap = new ConcurrentHashMap<>();

    public static final String CALLBACK_CMD_DEVICE_INFO = "Callback_DeviceInfo:";

    public static final String CALLBACK_CMD_CATALOG = "Callback_Catalog:";

    public static final String CALLBACK_CMD_RECORD_INFO = "Callback_RecordInfo:";


    public static void put(String requestId, DeferredResult<ResponseEntity<ResponseData>> result) {
        RequestDeferredResultMap.computeIfAbsent(requestId, k -> new ArrayList<>()).add(result);
    }

    public static List<DeferredResult<ResponseEntity<ResponseData>>> get(String requestId) {
        return RequestDeferredResultMap.get(requestId);
    }

    /**
     * 设置 Http 请求异步响应结果
     *
     * @param requestId /
     * @param data      响应数据
     */
    public synchronized static void setDeferredResultForRequest(String requestId, Object data) {
        List<DeferredResult<ResponseEntity<ResponseData>>> results = RequestDeferredResultMap.get(requestId);
        try {
            if (results != null && !results.isEmpty()) {
                results.forEach(result -> result.setResult(success1(data)));
            }
        } finally {
            RequestDeferredResultMap.remove(requestId);
        }
    }
}
