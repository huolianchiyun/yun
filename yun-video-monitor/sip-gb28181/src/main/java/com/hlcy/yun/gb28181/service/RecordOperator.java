package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.bean.api.PtzParams;
import com.hlcy.yun.gb28181.bean.api.RecordParams;
import com.hlcy.yun.gb28181.operation.control.ControlCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 云台操作器
 */
@Service
@RequiredArgsConstructor
public class RecordOperator {
    private final ControlCmd<RecordParams> command;

    public void operate(RecordParams recordParams) {
        command.execute(recordParams);
    }
}
