package com.hlcy.yun.sys.modules.rights.model.criteria;

import com.hlcy.yun.sys.modules.rights.common.constant.RightsConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeptQueryCriteria extends GroupQueryCriteria {

    private String deptName;

    public DeptQueryCriteria() {
        super.setGroupType(RightsConstants.DEPT_TYPE);
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
        super.setGroupName(deptName);
    }
}
