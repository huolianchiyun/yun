package com.zhangbin.yun.sys.modules.rights.model.criteria;

import lombok.Getter;
import lombok.Setter;

import static com.zhangbin.yun.sys.modules.rights.common.constant.RightsConstants.DEPT_TYPE;

@Getter
@Setter
public class DeptQueryCriteria extends GroupQueryCriteria {

    private String deptName;

    public DeptQueryCriteria() {
        super.setGroupType(DEPT_TYPE);
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
        super.setGroupName(deptName);
    }
}
