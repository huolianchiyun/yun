package com.zhangbin.yun.yunrights.modules.rights.model.criteria;

import com.zhangbin.yun.yunrights.modules.common.page.AbstractQueryPage;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 組公共查询类
 */
@Data
public class GroupQueryCriteria extends AbstractQueryPage implements Serializable {

    protected Long pid;

    protected String groupType;

    private String groupName;

    protected List<LocalDateTime> createTimes;


}
