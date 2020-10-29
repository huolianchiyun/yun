package com.zhangbin.yun.sys.modules.monitor.service;

import com.zhangbin.yun.sys.modules.monitor.model.vo.MachineInfo;

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
