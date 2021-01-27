package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.AlarmParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;

/**
 * 告警复位命令
 */
public class ResetAlarmCmd extends AbstractControlCmd<AlarmParams> {
    public ResetAlarmCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(AlarmParams alarmParams) {
        return "<AlarmCmd>" + "ResetAlarm" + "</AlarmCmd>";
    }
}