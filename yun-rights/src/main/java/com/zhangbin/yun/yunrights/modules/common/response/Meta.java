package com.zhangbin.yun.yunrights.modules.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public final class Meta {
    private Status status;
    private String message;

    static Meta ok() {
        return new Meta(Status.OK);
    }

    public static Meta Error(String errMsg) {
        return new Meta(Status.ServerError, errMsg);
    }
    public static Meta RequestError(String errMsg) {
        return new Meta(Status.RequestError, errMsg);
    }

    private Meta(Status status) {
        this.status = status;
    }

    private Meta(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum Status {
        OK(200), RequestError(400), ServerError(500);
        @JsonValue
        private int code;

        Status(int code) {
            this.code = code;
        }
    }
}
