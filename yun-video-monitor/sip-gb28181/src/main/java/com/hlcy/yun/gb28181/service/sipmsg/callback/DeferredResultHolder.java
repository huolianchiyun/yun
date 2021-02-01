package com.hlcy.yun.gb28181.service.sipmsg.callback;

import com.hlcy.yun.common.web.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.hlcy.yun.common.web.response.ResponseUtil.*;

public final class DeferredResultHolder {
    private static final Map<String, List<DeferredResult<ResponseEntity<ResponseData>>>> REQUEST_DEFERRED_RESULT_CACHE = new ConcurrentHashMap<>();

    public static final String CALLBACK_CMD_QUERY_CATALOG = "Callback_Catalog:";

    public static final String CALLBACK_CMD_QUERY_DEVICE_INFO = "Callback_DeviceInfo:";

    public static final String CALLBACK_CMD_QUERY_RECORD_INFO = "Callback_RecordInfo:";

    public static final String CALLBACK_CMD_Query_ALARM = "Callback_Alarm:";

    public static final String CALLBACK_CMD_RECORD = "Callback_Record:";

    public static final String CALLBACK_CMD_GUARD = "Callback_Guard:";

    public static final String CALLBACK_CMD_RESET_ALARM = "Callback_ResetAlarm";

    public static final String CALLBACK_CMD_HOME_POSITION = "Callback_HomePosition";

    public static final String CALLBACK_CMD_DEVICE_CONFIG = "Callback_DeviceConfig";

    public static final String CALLBACK_CMD_PRESET = "Callback_Preset:";

    public static final String CALLBACK_CMD_CRUISE = "Callback_Cruise:";

    public static final String CALLBACK_CMD_PLAY = "Callback_Play:";

    public static final String CALLBACK_CMD_PLAYBACK = "Callback_Playback:";

    public static final String CALLBACK_CMD_VOICE = "Callback_Voice:";


    public static void put(String requestId, DeferredResult<ResponseEntity<ResponseData>> result) {
        REQUEST_DEFERRED_RESULT_CACHE.computeIfAbsent(requestId, k -> new ArrayList<>()).add(result);
    }

    public static List<DeferredResult<ResponseEntity<ResponseData>>> get(String requestId) {
        return REQUEST_DEFERRED_RESULT_CACHE.get(requestId);
    }

    /**
     * 设置 Http 请求异步响应结果
     *
     * @param requestId /
     * @param data      响应数据
     */
    public synchronized static void setDeferredResultForRequest(String requestId, Object data) {
        List<DeferredResult<ResponseEntity<ResponseData>>> results = REQUEST_DEFERRED_RESULT_CACHE.get(requestId);
        try {
            if (results != null && !results.isEmpty()) {
                results.forEach(result -> result.setResult(success1(data)));
            }
        } finally {
            REQUEST_DEFERRED_RESULT_CACHE.remove(requestId);
        }
    }

    /**
     * 设置 Http 请求异步异常响应结果
     *
     * @param requestId /
     * @param message   异常信息
     */
    public synchronized static void setErrorDeferredResultForRequest(String requestId, String message) {
        List<DeferredResult<ResponseEntity<ResponseData>>> results = REQUEST_DEFERRED_RESULT_CACHE.get(requestId);
        try {
            if (results != null && !results.isEmpty()) {
                results.forEach(result -> result.setResult(error(message)));
            }
        } finally {
            REQUEST_DEFERRED_RESULT_CACHE.remove(requestId);
        }
    }
}
