package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import com.zhangbin.yun.common.model.BaseDO;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class PermissionRuleDO extends BaseDO implements ExcelSupport, Serializable {

    @ApiModelProperty(required = true)
    private String ruleName;
    @ApiModelProperty(required = true)
    private String groupCodes;
    @ApiModelProperty(required = true)
    private String fromTable;
    @ApiModelProperty(required = true)
    private String ruleExps;

    private String description;
    @ApiModelProperty(required = true)
    private Boolean enabled;

    public PermissionRuleDO(String ruleName, String groupCodes, String fromTable, String ruleExps) {
        this.ruleName = ruleName;
        this.groupCodes = groupCodes;
        this.fromTable = fromTable;
        this.ruleExps = ruleExps;
    }

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
        return /*Objects.equals(groupCodes, that.groupCodes) &&*/
                Objects.equals(fromTable, that.fromTable) &&
                Objects.equals(ruleExps, that.ruleExps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(/*groupCodes,*/ fromTable, ruleExps);
    }

}
