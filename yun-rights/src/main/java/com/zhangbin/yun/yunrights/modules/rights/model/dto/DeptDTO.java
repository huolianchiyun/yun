package com.zhangbin.yun.yunrights.modules.rights.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class DeptDTO extends BaseDo implements Serializable {

    private Long id;

    private String name;

    private Boolean enabled;

    private Integer deptSort;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptDTO> children;

    private Long pid;

    private Integer subCount;

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return name;
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
                Objects.equals(name, deptDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
