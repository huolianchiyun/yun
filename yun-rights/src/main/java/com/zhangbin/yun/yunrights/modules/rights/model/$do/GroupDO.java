package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.beans.Transient;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDO;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import com.zhangbin.yun.yunrights.modules.rights.model.dto.DeptDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * 表 t_sys_group
 *
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */

@Getter
@Setter
@JsonIgnoreProperties("handler")
public class GroupDO extends BaseDO implements Comparable<GroupDO>, CollectChildren.ChildrenSupport<GroupDO>, ExcelSupport, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 父组id
     */
    @ApiModelProperty(required = true)
    private Long pid;

    /**
     * 组编码，生成规则："父编号:子组ID"
     */
    @ApiModelProperty(required = true)
    private String groupCode;

    /**
     * 组类型
     */
    @ApiModelProperty(notes = "组的类型，非必填字段，作用：可以将相同类型的组归为一类")
    private String groupType;

    @ApiModelProperty(required = true)
    private String groupName;

    private String description;

    private Integer groupSort;

    /**
     * 组长，群主
     */
    @ApiModelProperty(required = true)
    private String groupMaster;

    /**
     * API 权限
     */
    private String apiRights;

    /**
     * 非表字段
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long oldPid;

    /**
     * 非表字段
     */
    private Set<UserDO> users;

    /**
     * 非表字段
     */
    protected Set<MenuDO> menus;

    @Transient
    public Long getOldPid() {
        return oldPid;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<GroupDO> children;

    public Long getPid() {
        if (null == pid) {
            pid = 0L;
        }
        return pid;
    }

    public DeptDTO toDept() {
        DeptDTO deptDTO = new DeptDTO();
        deptDTO.setId(id);
        deptDTO.setPid(pid);
        deptDTO.setDeptCode(groupCode);
        deptDTO.setDeptName(groupName);
        deptDTO.setDeptSort(groupSort);
        deptDTO.setDescription(description);
        deptDTO.setApiRights(apiRights);
        deptDTO.setCreator(creator);
        deptDTO.setUpdater(updater);
        deptDTO.setCreateTime(createTime);
        deptDTO.setUpdateTime(updateTime);
        if (CollectionUtil.isNotEmpty(children)) {
            deptDTO.setChildren(children.stream().map(GroupDO::toDept).collect(Collectors.toList()));
        }
        return deptDTO;
    }

    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("组名称", groupName);
        map.put("组长", groupMaster);
        map.put("API权限", groupMaster);
        map.put("创建人", creator);
        map.put("创建日期", createTime);
        return map;
    }

    @Override
    public int compareTo(GroupDO o) {
        return Integer.compare(groupSort == null ? 0 : groupSort, o.groupSort == null ? 0 : o.groupSort);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupDO)) return false;
        GroupDO other = (GroupDO) o;
        return groupCode.equals(other.groupCode) || id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupCode, id);
    }
}
