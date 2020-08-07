package com.zhangbin.yun.yunrights.modules.rights.model.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import com.zhangbin.yun.yunrights.modules.rights.common.constant.RightsConstants;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import lombok.Data;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class DeptDTO extends BaseDo implements  Comparable<DeptDTO>, CollectChildren.ChildrenSupport<DeptDTO>, ExcelSupport, Serializable {

    private Long pid;

    private String deptName;

    private Integer deptSort;

    private String description;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptDTO> children;

    public GroupDO toGroup() {
        GroupDO groupDO = new GroupDO();
        groupDO.setId(id);
        groupDO.setPid(pid);
        groupDO.setGroupName(deptName);
        groupDO.setGroupSort(deptSort);
        groupDO.setGroupType(RightsConstants.GROUP_TYPE);
        groupDO.setCreator(creator);
        groupDO.setDescription(description);
        groupDO.setUpdater(updater);
        groupDO.setCreateTime(createTime);
        groupDO.setUpdateTime(updateTime);
        if (CollectionUtil.isNotEmpty(children)) {
            groupDO.setChildren(children.stream().map(DeptDTO::toGroup).collect(Collectors.toList()));
        }
        return groupDO;
    }

    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap map = new LinkedHashMap<>();
        map.put("部门名称", deptName);
        map.put("创建日期", createTime);
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeptDTO deptDto = (DeptDTO) o;
        return Objects.equals(id, deptDto.id) &&
                Objects.equals(deptName, deptDto.deptName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deptName);
    }

    @Override
    public int compareTo(DeptDTO o) {
        if (deptSort == null && o.deptSort == null) {
            return 0;
        } else if (deptSort != null && o.deptSort == null) {
            return 1;
        } else if (deptSort == null && o.deptSort != null) {
            return -1;
        } else {
            return Integer.compare(deptSort, o.deptSort);
        }
    }
}
