package com.zhangbin.yun.yunrights.modules.logging.model.dto;

import com.zhangbin.yun.yunrights.modules.logging.enums.LogLevel;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 日志查询类
 */
@Data
public final class LogQueryCriteria extends QueryPage{


    private String blurry;

    private BlurryType blurryType;

    private LogLevel logLevel;

    private List<LocalDateTime> createTimes;

    public LogQueryCriteria setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public LogQueryCriteria setBlurry(String blurry) {
        this.blurry = blurry;
        return this;
    }

    public LogQueryCriteria setCreateTimes(List<LocalDateTime> createTime) {
        this.createTimes = createTime;
        return this;
    }

    public LogQueryCriteria setBlurryType(BlurryType blurryType) {
        this.blurryType = blurryType;
        return this;
    }

    public enum BlurryType{
        USER_NAME(1, "userName"),
        OPERATION_DESC(2, "operationDesc"),
        ADDRESS(3, "address"),
        CLIENT_IP(4, "clientIp"),
        REQUEST_METHOD(5, "requestMethod"),
        REQUEST_PARAMS(6, "requestParams");

        private int code;
        private String name;

        BlurryType(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }
}
