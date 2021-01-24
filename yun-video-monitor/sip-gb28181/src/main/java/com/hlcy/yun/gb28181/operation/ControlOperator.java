package com.hlcy.yun.gb28181.operation;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.operation.params.*;
import com.hlcy.yun.gb28181.operation.command.control.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 设备控制操作器
 */
@Service
@RequiredArgsConstructor
public class ControlOperator<T extends DeviceParams> implements InitializingBean {
    private Map<Class, AbstractControlCmd> cmdFactory;
    private final GB28181Properties properties;

    @SuppressWarnings("unchecked")
    public void operate(T operateParam) {
        final AbstractControlCmd abstractControlCmd = cmdFactory.get(operateParam.getClass());
        abstractControlCmd.execute(operateParam);
    }

    @Override
    public void afterPropertiesSet() {
        cmdFactory.put(PtzParams.class, new PtzCmd(properties));
        cmdFactory.put(TeleBootParams.class, new TeleBootCmd(properties));
        cmdFactory.put(RecordParams.class, new RecordCmd(properties));
        cmdFactory.put(IFameParams.class, new IFameCmd(properties));
        cmdFactory.put(HomePositionParams.class, new HomePositionCmd(properties));
        cmdFactory.put(GuardParams.class, new GuardCmd(properties));
        cmdFactory.put(DragZoomParams.class, new DragZoomCmd(properties));
        cmdFactory.put(AlarmParams.class, new ResetAlarmCmd(properties));
        cmdFactory.put(FIParams.class, new PresetCmd(properties));
        cmdFactory.put(PresetParams.class, new PresetCmd(properties));
        cmdFactory.put(CruiseParams.class, new CruiseCmd(properties));
        cmdFactory.put(ScanParams.class, new ScanCmd(properties));
        cmdFactory.put(AuxilSwitchParams.class, new AuxilSwitchCmd(properties));
    }
}
