package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.beans.Transient;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import com.zhangbin.yun.yunrights.modules.rights.model.dto.DeptDTO;
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
public class GroupDO extends BaseDo implements Comparable<GroupDO>, CollectChildren.ChildrenSupport<GroupDO>, ExcelSupport, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 父组id
     */
    private Long pid;

    /**
     * 组类型
     */
    private String groupType;

    /**
     *
     */
    private String groupName;

    /**
     *
     */
    private String description;

    private Integer groupSort;

    /**
     * 非表字段
     */
    @JsonIgnore
    private Long oldPid;
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
        deptDTO.setDeptName(groupName);
        deptDTO.setDeptSort(groupSort);
        deptDTO.setDescription(description);
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
        map.put("创建日期", createTime);
        return map;
    }

    @Override
    public int compareTo(GroupDO o) {
        if (groupSort == null && o.groupSort == null) {
            return 0;
        } else if (groupSort != null && o.groupSort == null) {
            return 1;
        } else if (groupSort == null && o.groupSort != null) {
            return -1;
        } else {
            return Integer.compare(groupSort, o.groupSort);
        }
    }
}
