package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.*;
import com.hlcy.yun.gb28181.service.command.control.*;
import com.hlcy.yun.gb28181.service.params.control.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备控制操作器
 */
@Service
@RequiredArgsConstructor
public class ControlOperator<T extends DeviceParams> implements InitializingBean {
    private Map<Class, AbstractControlCmd> cmdFactory = new HashMap<>(13);
    private final GB28181Properties properties;

    @SuppressWarnings("unchecked")
    public void operate(T operateParam) {
        final AbstractControlCmd cmd = cmdFactory.get(operateParam.getClass());
        cmd.execute(operateParam);
    }

    @Override
    public void afterPropertiesSet() {
        cmdFactory.put(PtzControlParams.class, new PtzCmd(properties));
        cmdFactory.put(TeleBootControlParams.class, new TeleBootCmd(properties));
        cmdFactory.put(RecordControlParams.class, new RecordCmd(properties));
        cmdFactory.put(IFameControlParams.class, new IFameCmd(properties));
        cmdFactory.put(HomePositionControlParams.class, new HomePositionCmd(properties));
        cmdFactory.put(GuardControlParams.class, new GuardCmd(properties));
        cmdFactory.put(DragZoomControlParams.class, new DragZoomCmd(properties));
        cmdFactory.put(ResetAlarmControlParams.class, new ResetAlarmCmd(properties));
        cmdFactory.put(FIControlParams.class, new FICmd(properties));
        cmdFactory.put(PresetControlParams.class, new PresetCmd(properties));
        cmdFactory.put(CruiseControlParams.class, new CruiseCmd(properties));
        cmdFactory.put(ScanControlParams.class, new ScanCmd(properties));
        cmdFactory.put(AuxilSwitchControlParams.class, new AuxilSwitchCmd(properties));
    }
}
