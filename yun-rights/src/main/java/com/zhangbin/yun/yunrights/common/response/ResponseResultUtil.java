package com.zhangbin.yun.yunrights.common.response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseResultUtil {

    public static <T> ResponseEntity success() {
        return new ResponseEntity(new ResponseResult(null, new Meta(200)), HttpStatus.OK);
    }

    public static <T> ResponseEntity success(T data) {
        return new ResponseEntity(new ResponseResult(data, new Meta(200)), HttpStatus.OK);
    }

    public static ResponseEntity error(String errMsg) {
        return new ResponseEntity(new ResponseResult(new Meta(500, errMsg)), HttpStatus.OK);
    }

}
