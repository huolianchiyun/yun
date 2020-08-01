package com.zhangbin.yun.yunrights.modules.rights.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单公共查询类
 */
@Data
public class MenuQueryCriteria {

    // 模块搜索条件
    private String blurry;

    private List<LocalDateTime> createTimes;

    private Long pid;
}
