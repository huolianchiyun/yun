package com.zhangbin.yun.yunrights.modules.rights.model.criteria;

import com.zhangbin.yun.yunrights.modules.rights.common.constant.RightsConstants;
import lombok.Data;

@Data
public class DeptQueryCriteria extends GroupQueryCriteria {

    private String deptName;

    public void setDeptName(String deptName) {
        this.deptName = deptName;
        super.setGroupName(deptName);
    }

    @Override
    public void setGroupType(String groupType) {
        super.setGroupType(RightsConstants.GROUP_TYPE);
    }

}
