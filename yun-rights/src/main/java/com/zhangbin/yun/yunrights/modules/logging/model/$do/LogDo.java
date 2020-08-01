package com.zhangbin.yun.yunrights.modules.logging.model.$do;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import lombok.Data;

/**
 * 系统日志
 * 表 t_sys_log
 *
 * @author ASUS
 * @date 2020-07-31 10:50:00
 */
@Data
public class LogDo extends BaseDo implements Serializable {
    public LogDo(String logLevel, Long requestTimeConsuming) {
        this.logLevel = logLevel;
        this.requestTimeConsuming = requestTimeConsuming;
    }

    /**
     * ID
     */
    private Long id;

    /**
     * 请求操作描述
     */
    private String operationDesc;

    /**
     * 日志等级
     */
    private String logLevel;

    /**
     * 请求的方法
     */
    private String requestMethod;

    /**
     * 方法入参
     */
    private String requestParams;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 请求耗时
     */
    private Long requestTimeConsuming;

    /**
     * 发请求的用户
     */
    private String userName;

    /**
     * 内网、外网
     */
    private String address;

    /**
     * 客户端浏览器类型
     */
    private String browser;

    /**
     * 操作异常信息详情
     */
    private byte[] exceptionDetail;

    /**
     * 日志记录创建时间
     */
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}
