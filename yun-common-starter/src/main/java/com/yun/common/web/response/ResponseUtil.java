package com.yun.common.web.response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseUtil {

    /**
     * controller api 无返回值时用
     */
    public static ResponseEntity<ResponseData<Void>> success() {
        return success(null);
    }
    /**
     * controller api 无返回值时用
     */
    @Deprecated
    public static ResponseEntity<ResponseData> success0() {
        return success0(null);
    }

    public static <T> org.springframework.http.ResponseEntity<ResponseData<T>> success(T data) {
        return new org.springframework.http.ResponseEntity<>(new ResponseData<>(data, Meta.ok()), HttpStatus.OK);
    }

    @Deprecated
    public static <T> org.springframework.http.ResponseEntity<ResponseData> success0(T data) {
        return new org.springframework.http.ResponseEntity<>(new ResponseData<>(data, Meta.ok()), HttpStatus.OK);
    }

    public static org.springframework.http.ResponseEntity<ResponseData> error(String errMsg) {
        return new org.springframework.http.ResponseEntity<>(new ResponseData(Meta.error(errMsg)), HttpStatus.OK);
    }
    public static org.springframework.http.ResponseEntity<ResponseData> requestError(String errMsg) {
        return new org.springframework.http.ResponseEntity<>(new ResponseData(Meta.requestError(errMsg)), HttpStatus.OK);
    }

    public static org.springframework.http.ResponseEntity<ResponseData> noApiRights(String errMsg) {
        return new org.springframework.http.ResponseEntity<>(new ResponseData(Meta.noApiRights(errMsg)), HttpStatus.UNAUTHORIZED);
    }

}
