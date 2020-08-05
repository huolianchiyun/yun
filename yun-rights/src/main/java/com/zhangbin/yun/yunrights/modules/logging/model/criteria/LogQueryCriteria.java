package com.zhangbin.yun.yunrights.modules.logging.model.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zhangbin.yun.yunrights.modules.common.page.QueryPage;
import com.zhangbin.yun.yunrights.modules.logging.enums.LogLevel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日志查询类
 */
@Data
public final class LogQueryCriteria extends QueryPage {


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

    public enum BlurryType {
        USER_NAME(1, "userName"),
        OPERATION_DESC(2, "operationDesc"),
        ADDRESS(3, "address"),
        CLIENT_IP(4, "clientIp"),
        REQUEST_METHOD(5, "requestMethod"),
        REQUEST_PARAMS(6, "requestParams");

        @JsonValue
        private int code;
        private String name;

        BlurryType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        @JsonCreator
        public BlurryType build(int code) {
            BlurryType[] values = values();
            for (BlurryType type : values) {
                if (type.code == code) {
                    return type;
                }
            }
            return null;
        }

        /**
         * 检测枚举 name是否与指定的name匹配
         *
         * @param name
         * @return
         */
        public boolean isMatch(String name) {
            return name.equals(this.name);
        }
    }
}
