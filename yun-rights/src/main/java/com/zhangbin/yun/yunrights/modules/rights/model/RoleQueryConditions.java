package com.zhangbin.yun.yunrights.modules.rights.model;

import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

/**
 * 角色公共查询类
 */
@Data
public class RoleQueryConditions {

    private String blurry;

    private List<Timestamp> createTime;
}
