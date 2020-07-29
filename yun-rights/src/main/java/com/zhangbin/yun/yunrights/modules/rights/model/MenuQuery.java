package com.zhangbin.yun.yunrights.modules.rights.model;

import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

/**
 * 菜单公共查询类
 */
@Data
public class MenuQuery {

    // 模块搜索条件
    private String blurry;

    private List<Timestamp> createTime;

    private Long pid;
}
