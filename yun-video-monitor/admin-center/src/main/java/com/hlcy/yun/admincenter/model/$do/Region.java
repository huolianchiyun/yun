package com.hlcy.yun.admincenter.model.$do;

import com.hlcy.yun.common.model.BaseDO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 行政区域表
 * 表 t_vm_region
 * @author ASUS
 * @date 2020-12-11 21:00:52
 */
@Getter
@Setter
public class Region extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 父节点ID
     */
    private Long pid;

    /**
     * 行政区域中文名
     */
    private String name;

    /**
     * 行政区域编码
     */
    private String code;

}
