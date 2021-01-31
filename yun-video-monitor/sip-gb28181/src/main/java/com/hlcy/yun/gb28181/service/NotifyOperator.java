package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.command.notify.AbstractNotifyCmd;
import com.hlcy.yun.gb28181.service.command.notify.VoiceBroadcastNotifyCmd;
import com.hlcy.yun.gb28181.service.params.notify.NotifyParams;
import com.hlcy.yun.gb28181.service.params.notify.VoiceBroadcastNotifyParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备通知操作器
 */
@Service
@RequiredArgsConstructor
public class NotifyOperator<T extends NotifyParams> implements InitializingBean {
    private Map<Class, AbstractNotifyCmd> cmdFactory = new HashMap<>(5);
    private final GB28181Properties properties;

    @SuppressWarnings("unchecked")
    public void operate(T operateParam) {
        final AbstractNotifyCmd cmd = cmdFactory.get(operateParam.getClass());
        cmd.execute(operateParam);
    }

    @Override
    public void afterPropertiesSet() {
        cmdFactory.put(VoiceBroadcastNotifyParams.class, new VoiceBroadcastNotifyCmd(properties));
    }

    public void stopVoiceBroadcast(String ssrc) {
        final VoiceBroadcastNotifyCmd cmd = (VoiceBroadcastNotifyCmd) cmdFactory.get(VoiceBroadcastNotifyParams.class);
        cmd.stop(ssrc);
    }
}
