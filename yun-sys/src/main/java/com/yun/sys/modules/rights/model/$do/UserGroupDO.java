package com.yun.sys.modules.rights.model.$do;

import java.io.Serializable;
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

    private Long userId;

    private Long groupId;

}
