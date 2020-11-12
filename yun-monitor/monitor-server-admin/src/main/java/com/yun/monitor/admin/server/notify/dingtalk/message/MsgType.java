package com.yun.monitor.admin.server.notify.dingtalk.message;


public enum MsgType {
    Text("text"), Link("link"), MarkDown("markdown");
    private String value;

    MsgType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
