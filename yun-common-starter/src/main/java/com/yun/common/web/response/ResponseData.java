package com.yun.common.web.response;

public class ResponseData<T> {
    private T data;
    private Meta meta;

    public ResponseData(Meta meta) {
        this.meta = meta;
    }

    public ResponseData(T data, Meta meta) {
        this.data = data;
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
