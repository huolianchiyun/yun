package com.zhangbin.yun.sys.modules.logging.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class LogSmallDTO implements Serializable {

    private String operationDesc;

    private String clientIp;

    private Long requestTimeConsuming;

    private String address;

    private String browser;

    private Timestamp createTime;
}
