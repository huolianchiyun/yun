package com.hlcy.yun.sys.modules.rights.model.$do;

import java.beans.Transient;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hlcy.yun.common.model.BaseDO;
import com.hlcy.yun.common.utils.date.DateUtil;
import com.hlcy.yun.common.utils.download.excel.ExcelSupport;
import com.hlcy.yun.sys.modules.rights.common.excel.CollectChildren;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 表 t_sys_dept
 *
 * @author ASUS
 * @date 2021-01-23 17:03:08
 */
@Getter
@Setter
@JsonIgnoreProperties("handler")
public class DeptDO extends BaseDO implements Comparable<DeptDO>, CollectChildren.ChildrenSupport<DeptDO>, ExcelSupport, Serializable {
    private static final long serialVersionUID = 1L;

    private Long pid;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门编码
     */
    private String deptCode;

    private Integer sort;

    private String description;

    /**
     * 部门主管
     */
    @ApiModelProperty(required = true)
    private String deptMaster;

    /**
     * 非表字段
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long oldPid;

    @Transient
    public Long getOldPid() {
        return oldPid;
    }

    /**
     * 非表字段
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptDO> children;

    @Override
    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("部门 ID", id);
        map.put("上级部门 ID", pid);
        map.put("部门名称", deptName);
        map.put("部门编码", deptCode);
        map.put("描述", description);
        map.put("创建人", creator);
        map.put("创建日期", DateUtil.format2MdHms(createTime));
        return map;
    }

    @Override
    public int compareTo(DeptDO o) {
        return Integer.compare(sort == null ? 0 : sort, o.sort == null ? 0 : o.sort);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeptDO)) {
            return false;
        }
        DeptDO other = (DeptDO) o;
        return deptCode.equals(other.deptCode) || id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptCode, id);
    }
}
