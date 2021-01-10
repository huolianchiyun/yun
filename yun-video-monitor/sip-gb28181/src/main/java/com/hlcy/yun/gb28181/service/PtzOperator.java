package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.bean.api.PtzParams;
import com.hlcy.yun.gb28181.operation.control.ControlCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 云台操作器
 */
@Service
@RequiredArgsConstructor
public class PtzOperator {
    private final ControlCmd<PtzParams> command;

    public void operate(PtzParams ptzParams) {
        command.execute(ptzParams);
    }
}
