package com.hlcy.yun.sip.gb28181.bean;

public class Host {
    private String ip;
    private int port;
    private String address;

    public Host(String ip, int port, String address) {
        this.ip = ip;
        this.port = port;
        this.address = address;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
