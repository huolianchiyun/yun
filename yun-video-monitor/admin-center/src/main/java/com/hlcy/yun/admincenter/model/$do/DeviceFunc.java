package com.hlcy.yun.admincenter.model.$do;

import com.hlcy.yun.common.model.BaseDO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备能力标签表
 * 表 t_vm_device_func
 * @author ASUS
 * @date 2020-12-11 21:00:52
 */
@Getter
@Setter
public class DeviceFunc extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 能力名称
     */
    private String name;

    /**
     * 数值
     */
    private String value;

}
