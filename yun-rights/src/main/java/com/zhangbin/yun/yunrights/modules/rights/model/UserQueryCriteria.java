package com.zhangbin.yun.yunrights.modules.rights.model;

import com.zhangbin.yun.yunrights.modules.logging.model.dto.QueryPage;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户公共查询类
 */
@Data
public class UserQueryCriteria extends QueryPage implements Serializable {

    private Long id;

    private Set<Long> deptIds = new HashSet<>();

    private String blurry;

    private Boolean enabled;

    private Long deptId;

    private List<LocalDateTime> createTimes;
}
