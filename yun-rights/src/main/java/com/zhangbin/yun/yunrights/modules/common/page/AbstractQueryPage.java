package com.zhangbin.yun.yunrights.modules.common.page;

import lombok.Data;

/**
 * 查询分页条件基类
 * 设为抽象类，目的在于不让 new
 */
@Data
public abstract class AbstractQueryPage {
    protected Integer pageNum;
    protected Integer pageSize;
}
