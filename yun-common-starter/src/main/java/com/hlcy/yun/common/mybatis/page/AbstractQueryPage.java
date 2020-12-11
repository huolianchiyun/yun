package com.hlcy.yun.common.mybatis.page;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 查询分页条件基类
 * 设为抽象类，目的在于不让 new
 */
@Data
public abstract class AbstractQueryPage {
    @Min(value = 1, message = "页码必须大于等于 1 ！")
    protected Integer pageNum = 1;
    @Min(value = 5, message = "每页显示条数必须大于等于 5 ！")
    protected Integer pageSize = 10;
}
