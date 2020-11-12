package com.yun.monitor.service.impl;


import static com.yun.monitor.utils.MachineUtil.*;

import com.yun.common.utils.date.DateUtil;
import com.yun.monitor.model.vo.MachineInfo;
import com.yun.monitor.service.MonitorService;
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
        return new MachineInfo().setSystem(getSystemInfo(os))
                .setCpu(getCpuInfo(hal.getProcessor()))
                .setMemory(getMemoryInfo(hal.getMemory()))
                .setSwap(getSwapInfo(hal.getMemory()))
                .setDisk(getDiskInfo(os))
                .setTime(DateUtil.format(LocalDateTime.now(), "HH:mm:ss"));
    }
}
