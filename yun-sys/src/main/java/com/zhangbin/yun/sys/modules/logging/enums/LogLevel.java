package com.zhangbin.yun.sys.modules.logging.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LogLevel {
    ERROR("Error"),
    WARN("WARN"),
    INFO("INFO"),
    DEBUG("DEBUG");

    @JsonValue
    private String level;

    LogLevel(String level) {
        this.level = level;
    }

    @JsonCreator
    public LogLevel build(String level) {
        LogLevel[] values = values();
        for (LogLevel logLevel : values) {
            if (logLevel.level == level) {
                return logLevel;
            }
        }
        return null;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
