package com.zhangbin.yun.yunrights.modules.rights.model.criteria;

import com.zhangbin.yun.common.mybatis.page.AbstractQueryPage;
import com.zhangbin.yun.yunrights.modules.rights.model.validation.ValidateDateRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 組公共查询类
 */
@Data
@ValidateDateRange(message = "开始时间不能大于结束时间！")
public class GroupQueryCriteria extends AbstractQueryPage implements Serializable {

    protected Long pid;

    private String groupType;

    private String groupName;

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
