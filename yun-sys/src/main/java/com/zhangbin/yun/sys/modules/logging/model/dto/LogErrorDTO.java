package com.zhangbin.yun.sys.modules.logging.model.dto;

import com.zhangbin.yun.sys.modules.logging.model.$do.LogDO;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LogErrorDTO implements Serializable {
    public LogErrorDTO() {}
    public LogErrorDTO(LogDO logDo) {
        this.id = logDo.getId();
        this.userName = logDo.getOperator();
        this.operationDesc = logDo.getOperationDesc();
        this.requestMethod = logDo.getRequestMethod();
        this.requestParams = logDo.getRequestParams();
        this.browser = logDo.getBrowser();
        this.clientIp = logDo.getClientIp();
        this.address = logDo.getAddress();
        this.createTime = logDo.getCreateTime();
    }

    private Long id;

    private String userName;

    private String operationDesc;

    private String requestMethod;

    private String requestParams;

    private String browser;

    private String clientIp;

    private String address;

    private LocalDateTime createTime;

}
