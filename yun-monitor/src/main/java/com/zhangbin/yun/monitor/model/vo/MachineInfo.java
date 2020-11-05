package com.zhangbin.yun.monitor.model.vo;


import java.util.Map;

public class MachineInfo {
    private Map<String, Object> system;
    private Map<String, Object> cpu;
    private Map<String, Object> memory;
    private Map<String, Object> swap;
    private Map<String, Object> disk;

    public Map<String, Object> getSystem() {
        return system;
    }

    public MachineInfo setSystem(Map<String, Object> system) {
        this.system = system;
        return this;
    }

    public Map<String, Object> getCpu() {
        return cpu;
    }

    public MachineInfo setCpu(Map<String, Object> cpu) {
        this.cpu = cpu;
        return this;
    }

    public Map<String, Object> getMemory() {
        return memory;
    }

    public MachineInfo setMemory(Map<String, Object> memory) {
        this.memory = memory;
        return this;
    }

    public Map<String, Object> getSwap() {
        return swap;
    }

    public MachineInfo setSwap(Map<String, Object> swap) {
        this.swap = swap;
        return this;
    }

    public Map<String, Object> getDisk() {
        return disk;
    }

    public MachineInfo setDisk(Map<String, Object> disk) {
        this.disk = disk;
        return this;
    }
}
