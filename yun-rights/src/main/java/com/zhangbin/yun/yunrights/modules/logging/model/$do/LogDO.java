package com.zhangbin.yun.yunrights.modules.logging.model.$do;

import java.io.Serializable;
import java.util.LinkedHashMap;

import cn.hutool.core.util.ObjectUtil;
import com.zhangbin.yun.common.mybatis.audit.annotation.CreatedBy;
import com.zhangbin.yun.common.constant.Constants;
import com.zhangbin.yun.common.model.BaseDO;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import lombok.*;

/**
 * 系统日志
 * 表 t_sys_log
 *
 * @author ASUS
 * @date 2020-07-31 10:50:00
 */

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LogDO extends BaseDO implements ExcelSupport, Serializable {

    private static final long serialVersionUID = 1L;

    public LogDO(String logLevel, Long requestTimeConsuming) {
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
    @CreatedBy
    private String operator;

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


    @Override
    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("用户名", operator);
        map.put("IP", clientIp);
        map.put("IP来源", address);
        map.put("描述", operationDesc);
        map.put("浏览器", browser);
        map.put("请求耗时/毫秒", requestTimeConsuming);
        map.put("异常详情", new String(ObjectUtil.isNotNull(exceptionDetail) ? exceptionDetail : Constants.EMPTY_STR.getBytes()));
        map.put("创建日期", createTime);
        return map;
    }
}
