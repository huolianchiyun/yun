package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Objects;

@Getter
@Setter
public class PermissionRuleDO extends BaseDo implements ExcelSupport, Serializable {

    private String ruleName;

    private String groupCodes;

    private String fromTable;

    private String ruleExps;

    private String description;

    private Boolean enabled;

    @Override
    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("规则名", ruleName);
        map.put("适用的组", groupCodes);
        map.put("适用表", fromTable);
        map.put("规则表达式", ruleExps);
        map.put("规则描述", description);
        map.put("是否可用", enabled ? "可用" : "不可用");
        map.put("创建人", creator);
        map.put("创建日期", createTime);
        map.put("修改人", updater);
        map.put("修改时间", updateTime);
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionRuleDO)) return false;
        PermissionRuleDO that = (PermissionRuleDO) o;
        return Objects.equals(groupCodes, that.groupCodes) &&
                Objects.equals(fromTable, that.fromTable) &&
                Objects.equals(ruleExps, that.ruleExps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupCodes, fromTable, ruleExps);
    }
}
