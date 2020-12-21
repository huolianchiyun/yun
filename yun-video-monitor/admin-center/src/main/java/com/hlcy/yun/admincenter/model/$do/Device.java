package com.hlcy.yun.admincenter.model.$do;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hlcy.yun.common.mybatis.audit.annotation.CreatedBy;
import com.hlcy.yun.common.mybatis.audit.annotation.CreatedDate;
import com.hlcy.yun.common.mybatis.audit.annotation.LastModifiedBy;
import com.hlcy.yun.common.mybatis.audit.annotation.LastModifiedDate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备表
 * 表 t_vm_device
 *
 * @author ASUS
 * @date 2020-12-11 21:22:52
 */
@Getter
@Setter
public class Device implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    private String id;

    /**
     * 父设备的设备编号
     */
    private String parentId;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 通道ID
     */
    private String channelId;

    /**
     * 制造商
     */
    private String manufacturer;

    /**
     * 型号
     */
    private String model;

    /**
     * 设备所属
     */
    private String owner;

    /**
     * 是否存在子设备
     */
    private Boolean parental;

    /**
     * 区域编码
     */
    private String civilCode;

    /**
     * 安装地址
     */
    private String address;

    /**
     * 注册方式:1符合IETF RFC 3261标准的认证注册模式(默认) 2基于口令的双向认证注册模式 3基于数字证书的双向认证注册模式
     */
    private String registerWay;

    /**
     * 涉密属性:0: 不涉密(默认) 1: 涉密
     */
    private Boolean secrecy;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 端口号
     */
    private String port;

    /**
     * 传输协议
     */
    private String transport;

    /**
     * 状态
     */
    private Byte state;

    /**
     * 扩展信息
     */
    private String extraInfo;

    @CreatedBy
    @ApiModelProperty(hidden = true)
    protected String creator;

    @LastModifiedBy
    @ApiModelProperty(hidden = true)
    protected String updater;

    @CreatedDate
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    protected LocalDateTime createTime;

    @LastModifiedDate
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    protected LocalDateTime updateTime;
}
