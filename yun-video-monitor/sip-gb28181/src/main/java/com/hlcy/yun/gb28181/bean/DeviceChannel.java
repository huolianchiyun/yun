package com.hlcy.yun.gb28181.bean;

import lombok.Getter;

@Getter
public class DeviceChannel {

    /**
     * 通道id
     */
    private String channelId;

    /**
     * 通道名
     */
    private String name;

    /**
     * 生产厂商
     */
    private String manufacture;

    /**
     * 型号
     */
    private String model;

    /**
     * 设备归属
     */
    private String owner;

    /**
     * 行政区域
     */
    private String civilCode;

    /**
     * 警区
     */
    private String block;

    /**
     * 安装地址
     */
    private String address;

    /**
     * 是否有子设备 1有, 0没有
     */
    private int parental;

    /**
     * 父级 id
     */
    private String parentId;

    /**
     * 信令安全模式  缺省为 0; 0:不采用; 2: S/MIME签名方式; 3: S/ MIME加密签名同时采用方式; 4:数字摘要方式
     */
    private int safetyWay;

    /**
     * 注册方式 缺省为 1; 1:符合 IETFRFC 3261 标准的认证注册模式; 2:基于口令的双向认证注册模式; 3:基于数字证书的双向认证注册模式
     */
    private int registerWay;

    /**
     * 证书序列号
     */
    private String certNum;

    /**
     * 证书有效标识 缺省为 0;证书有效标识: 0:无效 1: 有效
     */
    private int certifiable;

    /**
     * 证书无效原因码
     */
    private int errCode;

    /**
     * 证书终止有效期
     */
    private String endTime;

    /**
     * 保密属性 缺省为 0; 0:不涉密, 1:涉密
     */
    private String secrecy;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 端口号
     */
    private int port;

    /**
     * 密码
     */
    private String password;

    /**
     * 在线/离线
     * 1：在线, 0：离线
     * 默认在线
     * 信令:
     * <Status>ON</Status>
     * <Status>OFF</Status>
     * 遇到过 NVR下的 IPC下发信令可以推流， 但是 Status 响应 OFF
     */
    private int status;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    public DeviceChannel setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public DeviceChannel setName(String name) {
        this.name = name;
        return this;
    }

    public DeviceChannel setManufacture(String manufacture) {
        this.manufacture = manufacture;
        return this;
    }

    public DeviceChannel setModel(String model) {
        this.model = model;
        return this;
    }

    public DeviceChannel setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public DeviceChannel setCivilCode(String civilCode) {
        this.civilCode = civilCode;
        return this;
    }

    public DeviceChannel setBlock(String block) {
        this.block = block;
        return this;
    }

    public DeviceChannel setAddress(String address) {
        this.address = address;
        return this;
    }

    public DeviceChannel setParental(int parental) {
        this.parental = parental;
        return this;
    }

    public DeviceChannel setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public DeviceChannel setSafetyWay(int safetyWay) {
        this.safetyWay = safetyWay;
        return this;
    }

    public DeviceChannel setRegisterWay(int registerWay) {
        this.registerWay = registerWay;
        return this;
    }

    public DeviceChannel setCertNum(String certNum) {
        this.certNum = certNum;
        return this;
    }

    public DeviceChannel setCertifiable(int certifiable) {
        this.certifiable = certifiable;
        return this;
    }

    public DeviceChannel setErrCode(int errCode) {
        this.errCode = errCode;
        return this;
    }

    public DeviceChannel setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public DeviceChannel setSecrecy(String secrecy) {
        this.secrecy = secrecy;
        return this;
    }

    public DeviceChannel setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public DeviceChannel setPort(int port) {
        this.port = port;
        return this;
    }

    public DeviceChannel setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * 设置状态
     * @param status 1：在线, 0：离线
     * @return /
     */
    public DeviceChannel setStatus(int status) {
        this.status = status;
        return this;
    }

    public DeviceChannel setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public DeviceChannel setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }
}
