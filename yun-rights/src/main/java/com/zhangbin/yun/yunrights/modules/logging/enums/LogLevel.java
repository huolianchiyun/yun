package com.zhangbin.yun.yunrights.modules.logging.enums;

public enum LogLevel {
    ERROR("Error"),
    WARN("WARN"),
    INFO("INFO"),
    DEBUG("DEBUG");

    private String level;

    LogLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
