package com.hlcy.yun.gb28181.service.command.query;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.query.AlarmQueryParams;
import com.hlcy.yun.gb28181.service.params.query.AlarmSubscribeParams;

import java.time.LocalDateTime;

public class AlarmSubscribeCmd extends AbstractSubscribeCmd<AlarmSubscribeParams> {

    public AlarmSubscribeCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(AlarmSubscribeParams params) {
        final StringBuilder cmd = new StringBuilder(200)
                .append("<StartAlarmPriority>").append(params.getStartAlarmPriority()).append("</StartAlarmPriority>")
                .append("<EndAlarmPriority>").append(params.getEndAlarmPriority()).append("</EndAlarmPriority>")
                .append("<AlarmMethod>").append(params.getAlarmMethod()).append("</AlarmMethod>");
        final LocalDateTime startAlarmTime = params.getStartAlarmTime();
        if (startAlarmTime != null) {
            cmd.append("<StartAlarmTime>").append(startAlarmTime).append("</StartAlarmTime>");
        }
        final LocalDateTime endAlarmTime = params.getEndAlarmTime();
        if (endAlarmTime != null) {
            cmd.append("<EndAlarmTime>").append(endAlarmTime).append("</EndAlarmTime>");
        }
        return cmd.toString();
    }
}
