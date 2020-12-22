package com.hlcy.yun.sip.gb28181.bean;

import lombok.Getter;

import java.util.Map;

@Getter
public class Device {

    /**
     * 设备Id
     */
    private String deviceId;

    /**
     * 设备名
     */
    private String name;

    /**
     * 设备联网IP
     */
    private String ip;

    /**
     * 设备联网端口
     */
    private int port;

    /**
     * 设备安装地址
     */
    private String address;

    /**
     * 生产厂商
     */
    private String manufacturer;

    /**
     * 型号
     */
    private String model;

    /**
     * 固件版本
     */
    private String firmware;

    /**
     * 传输协议：UDP/TCP
     */
    private String transport;

    /**
     * 在线
     */
    private int online;

    /**
     * 设备通道列表
     */
    private Map<String, DeviceChannel> channelMap;

    public Device(String deviceId) {
        this.deviceId = deviceId;
    }

    public Device setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public Device setName(String name) {
        this.name = name;
        return this;
    }

    public Device setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Device setPort(int port) {
        this.port = port;
        return this;
    }

    public Device setAddress(String address) {
        this.address = address;
        return this;
    }

    public Device setTransport(String transport) {
        this.transport = transport;
        return this;
    }

    public Device setChannelMap(Map<String, DeviceChannel> channelMap) {
        this.channelMap = channelMap;
        return this;
    }


    public Device setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }


    public Device setModel(String model) {
        this.model = model;
        return this;
    }


    public Device setFirmware(String firmware) {
        this.firmware = firmware;
        return this;
    }

    public Device setOnline(int online) {
        this.online = online;
        return this;
    }
}
