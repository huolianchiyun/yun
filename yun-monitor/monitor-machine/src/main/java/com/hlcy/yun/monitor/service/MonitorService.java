package com.hlcy.yun.monitor.service;

import com.hlcy.yun.monitor.model.vo.MachineInfo;

/**
 * 监控服务部署机器接口
 */
public interface MonitorService {

    /**
     * 获取本地机器信息
     * @return MachineInfo
     */
    MachineInfo getLocalMachineInfo();

}
