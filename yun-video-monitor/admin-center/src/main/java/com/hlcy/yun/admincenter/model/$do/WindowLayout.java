package com.hlcy.yun.admincenter.model.$do;

import com.hlcy.yun.common.model.BaseDO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 布局表
 * 表 t_vm_window_layout
 * @author ASUS
 * @date 2020-12-11 21:00:52
 */
@Getter
@Setter
public class WindowLayout extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 布局名称
     */
    private String name;

    /**
     * 横向格子数
     */
    private Integer hNum;

    /**
     * 纵向格子数
     */
    private Integer vNum;

    /**
     * 设备编号序列
     */
    private String devices;

    /**
     * 最后一次使用标记
     */
    private Boolean lastedUse;

}
