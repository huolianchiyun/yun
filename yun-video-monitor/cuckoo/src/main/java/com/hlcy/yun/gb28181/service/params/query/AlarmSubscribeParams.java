package com.hlcy.yun.gb28181.service.params.query;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlarmSubscribeParams extends QueryParams {
    public AlarmSubscribeParams() {
        super("Alarm");
    }

    /**
     * 报警起始级别(可选), 0为全部, 1为一级警情, 2为二级警情, 3为三级警情, 4为四级警情
     */
    private int startAlarmPriority;

    /**
     * 报警终止级别(可选), 0为全部, 1为一级警情, 2为二级警情, 3为三级警情, 4为四级警情
     */
    private int endAlarmPriority;

    /**
     * 报警方式条件(可选),
     * 取值 0为全部, 1为电话报警, 2为设备报警, 3为短信报警, 4为GPS报警, 5为视频报警, 6为设备故障报警, 7其他报警;
     * 可以为直接组合如12为电话报警或设备报警
     */
    private int alarmMethod;

    /**
     * 报警类型
     */
    private String alarmType;

    /**
     * 报警发生开始时间(可选)
     */
    private LocalDateTime startAlarmTime;

    /**
     * 报警发生结束时间(可选)
     */
    private LocalDateTime endAlarmTime;
}
