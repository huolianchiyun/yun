package com.zhangbin.yun.monitor.service.impl;


import static com.zhangbin.yun.monitor.utils.MachineUtil.*;

import com.zhangbin.yun.monitor.model.vo.MachineInfo;
import com.zhangbin.yun.monitor.service.MonitorService;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;


@Service
public class MonitorServiceImpl implements MonitorService {
    @Override
    public MachineInfo getLocalMachineInfo() {
        final MachineInfo machineInfo = new MachineInfo();
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        machineInfo.setSystem(getSystemInfo(os))
                .setCpu(getCpuInfo(hal.getProcessor()))
                .setMemory(getMemoryInfo(hal.getMemory()))
                .setSwap(getSwapInfo(hal.getMemory()))
                .setDisk(getDiskInfo(os));
        return machineInfo;
    }
}