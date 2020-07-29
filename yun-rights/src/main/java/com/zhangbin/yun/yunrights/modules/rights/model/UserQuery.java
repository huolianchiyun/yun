package com.zhangbin.yun.yunrights.modules.rights.model;

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户公共查询类
 */
@Data
public class UserQuery implements Serializable {

    private Long id;

    private Set<Long> deptIds = new HashSet<>();

    private String blurry;

    private Boolean enabled;

    private Long deptId;

    private List<Timestamp> createTime;
}
