package com.zhangbin.yun.yunrights.modules.logging.model.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zhangbin.yun.common.mybatis.page.AbstractQueryPage;
import com.zhangbin.yun.yunrights.modules.logging.enums.LogLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 日志查询类
 */
@Data
public final class LogQueryCriteria extends AbstractQueryPage {


    private String blurry;

    private BlurryType blurryType;

    private LogLevel logLevel;

    /**
     * 搜索范围：开始时间 （创建时间）
     */
    @ApiModelProperty("格式：2020-08-22")
    private String startTime;

    /**
     * 搜索范围：结束时间 （创建时间）
     */
    @ApiModelProperty("格式：2020-08-25")
    private String endTime;

    public LogQueryCriteria setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public LogQueryCriteria setBlurry(String blurry) {
        this.blurry = blurry;
        return this;
    }

    public LogQueryCriteria setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public LogQueryCriteria setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public enum BlurryType {
        OPERATOR(1, "operator"),
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
