package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 表 t_sys_user_role
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Getter
@Setter
public class UserRoleDO implements Serializable {
    /**
     */
    private Long userId;

    /**
     */
    private Long roleId;

    private static final long serialVersionUID = 1L;
}
