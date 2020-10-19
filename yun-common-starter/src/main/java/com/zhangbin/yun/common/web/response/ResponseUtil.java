package com.zhangbin.yun.common.web.response;


import org.springframework.http.HttpStatus;

public final class ResponseUtil {

    public static org.springframework.http.ResponseEntity<ResponseData> success() {
        return success(null);
    }

    public static <T> org.springframework.http.ResponseEntity<ResponseData> success(T data) {
        return new org.springframework.http.ResponseEntity<>(new ResponseData<>(data, Meta.ok()), HttpStatus.OK);
    }

    public static org.springframework.http.ResponseEntity<ResponseData> error(String errMsg) {
        return new org.springframework.http.ResponseEntity<>(new ResponseData(Meta.error(errMsg)), HttpStatus.OK);
    }
    public static org.springframework.http.ResponseEntity<ResponseData> requestError(String errMsg) {
        return new org.springframework.http.ResponseEntity<>(new ResponseData(Meta.requestError(errMsg)), HttpStatus.OK);
    }

    public static org.springframework.http.ResponseEntity<ResponseData> noApiRights(String errMsg) {
        return new org.springframework.http.ResponseEntity<>(new ResponseData(Meta.noApiRights(errMsg)), HttpStatus.OK);
    }

}
