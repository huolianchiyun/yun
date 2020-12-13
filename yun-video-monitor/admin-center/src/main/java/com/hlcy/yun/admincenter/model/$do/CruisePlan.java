package com.hlcy.yun.admincenter.model.$do;

import com.hlcy.yun.common.model.BaseDO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 巡航计划表
 * 表 t_vm_cruise_plan
 * @author ASUS
 * @date 2020-12-11 21:00:52
 */
@Getter
@Setter
public class CruisePlan extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 描述信息
     */
    private String description;

    /**
     * 巡航计划详情
     */
    private String plan;

}
