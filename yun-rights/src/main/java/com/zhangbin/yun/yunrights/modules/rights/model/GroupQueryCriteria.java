package com.zhangbin.yun.yunrights.modules.rights.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色公共查询类
 */
@Data
public class GroupQueryCriteria {

    private String blurry;

    private List<LocalDateTime> createTimes;
}
