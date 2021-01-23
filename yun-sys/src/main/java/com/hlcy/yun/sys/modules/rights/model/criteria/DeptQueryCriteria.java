package com.hlcy.yun.sys.modules.rights.model.criteria;

import com.hlcy.yun.common.mybatis.page.AbstractQueryPage;
import com.hlcy.yun.sys.modules.rights.model.validation.ValidateDateRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ValidateDateRange(message = "开始时间不能大于结束时间！")
public class DeptQueryCriteria extends AbstractQueryPage implements Serializable {

    private String deptName;

    /**
     * 搜索范围：开始时间 （创建时间）
     */
    @ApiModelProperty("格式：2020-08-22")
    protected String startTime;

    /**
     * 搜索范围：结束时间 （创建时间）
     */
    @ApiModelProperty("格式：2020-08-25")
    protected String endTime;
}
