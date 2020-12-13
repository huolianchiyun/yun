package com.hlcy.yun.admincenter.model.$do;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.hlcy.yun.common.model.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 报警表
 * 表 t_vm_alarm
 * @author ASUS
 * @date 2020-12-11 21:00:52
 */
@Getter
@Setter
public class Alarm  extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 报警信息
     */
    private String info;

    /**
     * 报警等级
     */
    private String level;

    /**
     * 报警状态
     */
    private Byte state;

    /**
     * 设备编号
     */
    private String deviceId;

}
