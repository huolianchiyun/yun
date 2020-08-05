package com.zhangbin.yun.yunrights.modules.rights.model.dto;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class RoleDTO extends BaseDo implements Serializable {

    private Long id;

    private Set<MenuDTO> menus;

    private Set<DeptDTO> depts;

    private String name;

    private String dataScope;

    private Integer level;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleDTO roleDto = (RoleDTO) o;
        return Objects.equals(id, roleDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
