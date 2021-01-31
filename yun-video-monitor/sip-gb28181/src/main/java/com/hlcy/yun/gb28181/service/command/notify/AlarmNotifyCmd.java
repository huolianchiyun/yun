package com.hlcy.yun.gb28181.service.command.notify;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.notify.NotifyParams;

public class AlarmNotifyCmd extends AbstractNotifyCmd<NotifyParams> {

    public AlarmNotifyCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(NotifyParams notifyParams) {
        return null;
    }
}
