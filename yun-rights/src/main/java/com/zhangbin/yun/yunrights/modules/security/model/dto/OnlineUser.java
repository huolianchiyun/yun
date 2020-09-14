package com.zhangbin.yun.yunrights.modules.security.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 在线用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 岗位
     */
    private String dept;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * IP
     */
    private String ip;

    /**
     * 地址
     */
    private String address;

    /**
     * token
     */
    private String key;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date loginTime;


}
