package com.hlcy.yun.sys.modules.rights.model.$do;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * 表 t_sys_user_group
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Getter
@Setter
public class UserGroupDO implements Serializable {
    private static final long serialVersionUID = 1L;

    public UserGroupDO() {
    }

    public UserGroupDO(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGroupDO)) {
            return false;
        }
        UserGroupDO that = (UserGroupDO) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId);
    }

    private Long userId;

    private Long groupId;

}
