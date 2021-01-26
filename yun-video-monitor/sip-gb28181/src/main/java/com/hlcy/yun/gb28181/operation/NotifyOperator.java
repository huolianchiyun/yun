package com.hlcy.yun.gb28181.operation;

import com.hlcy.yun.common.spring.SpringContextHolder;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.operation.command.notify.AbstractNotifyCmd;
import com.hlcy.yun.gb28181.operation.params.NotifyParams;
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

    public static NotifyOperator getInstance() {
        return SpringContextHolder.getBean(NotifyOperator.class);
    }

    @SuppressWarnings("unchecked")
    public void operate(T operateParam) {
        final AbstractNotifyCmd abstractNotifyCmd = cmdFactory.get(operateParam.getClass());
        abstractNotifyCmd.execute(operateParam);
    }

    @Override
    public void afterPropertiesSet() {
        // TODO init cmdFactory
    }
}
