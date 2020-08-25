package com.zhangbin.yun.yunrights.modules.rights.model.criteria;

import com.zhangbin.yun.yunrights.modules.common.page.AbstractQueryPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 組公共查询类
 */
@Data
public class GroupQueryCriteria extends AbstractQueryPage implements Serializable {

    protected Long pid;

    protected String groupType;

    private String groupName;

    /**
     * 搜索范围：开始时间 （创建时间）
     */
    @ApiModelProperty("格式：2020-08-22")
    private String startTime;

    /**
     * 搜索范围：结束时间 （创建时间）
     */
    @ApiModelProperty("格式：2020-08-25")
    private String endTime;
}
