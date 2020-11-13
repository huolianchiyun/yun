package com.yun.common.web.response;

import com.fasterxml.jackson.annotation.JsonValue;

public final class Meta {
    private Status status;
    private String message;

    public static Meta ok() {
        return new Meta(Status.OK);
    }

    public static Meta error(String errMsg) {
        return new Meta(Status.ServerError, errMsg);
    }

    public static Meta requestError(String errMsg) {
        return new Meta(Status.RequestError, errMsg);
    }

    public static Meta noApiRights(String errMsg) {
        return new Meta(Status.NoApiRights, errMsg);
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
        OK(200), RequestError(400), NoApiRights(401), ServerError(500);
        @JsonValue
        private int code;
        Status(int code) {
            this.code = code;
        }
    }
}
