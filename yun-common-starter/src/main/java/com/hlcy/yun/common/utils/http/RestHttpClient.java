package com.hlcy.yun.common.utils.http;

import org.apache.commons.collections4.MapUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;


public final class RestHttpClient {
    private static final RestTemplate restTemplate = new RestTemplate();

    static {
        final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5 * 1000);
        factory.setReadTimeout(90 * 1000);
        restTemplate.setRequestFactory(factory);
    }

    public static <T, A> T post(String url, ParameterizedTypeReference<T> responseBodyType, A requestBody) {
        return exchange(url, HttpMethod.POST, null, responseBodyType, requestBody);
    }

    public static <T, A> T post(String url, Map<String, String> headMap, ParameterizedTypeReference<T> responseBodyType, A requestBody) {
        return exchange(url, HttpMethod.POST, headMap, responseBodyType, requestBody);
    }

    public static <T, A> T exchange(String url, HttpMethod method, ParameterizedTypeReference<T> responseBodyType, A requestBody) {
        return exchange(url, method, null, responseBodyType, requestBody);
    }

    private static <T, A> T exchange(String url, HttpMethod method, Map<String, String> headMap, ParameterizedTypeReference<T> responseBodyType, A requestBody) {
        if (HttpMethod.POST.equals(method)) {
            if (headMap == null || headMap.isEmpty()) {
                headMap = Collections.singletonMap(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            }
        }

        ResponseEntity<T> resultEntity = restTemplate.exchange(url, method, getHttpEntity(headMap, requestBody), responseBodyType);
        return resultEntity.getBody();
    }

    private static <A> HttpEntity<A> getHttpEntity(Map<String, String> headMap, A requestBody) {
        return new HttpEntity<>(requestBody, genHttpHeaders(headMap));
    }

    public static <T> T postForEntity(String url, Map<String, String> headMap, Object request, Class<T> responseType) {
        return restTemplate.postForEntity(url, getHttpEntity(headMap, request), responseType).getBody();
    }

    public static <T> T getForEntity(String url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType).getBody();
    }

    private static HttpHeaders genHttpHeaders(Map<String, String> headMap) {
        HttpHeaders headers = new HttpHeaders();
        if (MapUtils.isNotEmpty(headMap)) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(HttpHeaders.CONTENT_TYPE)) {
                    headers.setContentType(MediaType.valueOf(entry.getValue()));
                } else {
                    headers.set(entry.getKey(), entry.getValue());
                }
            }
        }
        return headers;
    }
}
