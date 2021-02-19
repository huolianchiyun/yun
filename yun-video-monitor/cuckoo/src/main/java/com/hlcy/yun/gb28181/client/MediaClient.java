package com.hlcy.yun.gb28181.client;

import com.alibaba.fastjson.JSONObject;
import com.hlcy.yun.common.utils.http.RestHttpClient;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@Component
public class MediaClient {
    private final GB28181Properties properties;
    private String make_device_push_stream_url;
    private String test_ssrc_validity_url;

    public JSONObject mediaMakeDevicePushStream(String ssrc, String deviceIp) {
        // 是否让流媒体主动拉流
        final String url = make_device_push_stream_url.replace("${ssrc}", ssrc).replace("${deviceIp}", deviceIp);
        return RestHttpClient.exchange(url, HttpMethod.GET, new ParameterizedTypeReference<JSONObject>() {
        }, null);
    }

    public boolean isValidTestSsrc(String ssrc) {
        final String ssrcHex = "0" + Integer.toHexString(Integer.parseInt(ssrc)).toUpperCase();
        final String url = test_ssrc_validity_url.replace("${ssrc}", ssrcHex);
        final JSONObject response = RestHttpClient.exchange(url, HttpMethod.GET, new ParameterizedTypeReference<JSONObject>() {
        }, null);
        if (response.getIntValue("code") == 0 && response.getBooleanValue("exist")) {
            return true;
        }
        log.warn("*** ssrc:{}, 有效性检测为无效！", ssrc);
        return false;
    }

    @PostConstruct
    private void init() {
        make_device_push_stream_url = properties.getMediaPullStreamApi().replace("${mediaIp}", properties.getMediaIp());
        test_ssrc_validity_url = properties.getMediaPullStreamApi().replace("${mediaIp}", properties.getMediaIp());
    }
}
