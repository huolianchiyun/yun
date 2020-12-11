package com.hlcy.yun.monitor.service.impl;


import com.hlcy.yun.common.utils.date.DateUtil;
import com.hlcy.yun.monitor.service.MonitorService;
import com.hlcy.yun.monitor.utils.MachineUtil;
import com.hlcy.yun.monitor.model.vo.MachineInfo;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.time.LocalDateTime;


@Service
public class MonitorServiceImpl implements MonitorService {
    @Override
    public MachineInfo getLocalMachineInfo() {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        return new MachineInfo().setSystem(MachineUtil.getSystemInfo(os))
                .setCpu(MachineUtil.getCpuInfo(hal.getProcessor()))
                .setMemory(MachineUtil.getMemoryInfo(hal.getMemory()))
                .setSwap(MachineUtil.getSwapInfo(hal.getMemory()))
                .setDisk(MachineUtil.getDiskInfo(os))
                .setTime(DateUtil.format(LocalDateTime.now(), "HH:mm:ss"));
    }
}
