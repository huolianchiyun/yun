package com.zhangbin.yun.yunrights.modules.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Meta {
    private Status status;
    private String message;

    public static Meta ok() {
        return new Meta(Status.OK);
    }

    public static Meta Error(String errMsg) {
        return new Meta(Status.Error, errMsg);
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
        OK(200), Error(500);
        @JsonProperty
        @JSONField
        private int code;

        Status(int code) {
            this.code = code;
        }
    }
}
