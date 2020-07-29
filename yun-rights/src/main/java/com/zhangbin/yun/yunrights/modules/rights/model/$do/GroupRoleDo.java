package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_group_role
 * @author ASUS
 * @date 2020-07-29 22:13:33
 */
@Data
public class GroupRoleDo implements Serializable {
    /**
     */
    private Long groupId;

    /**
     */
    private Long roleId;

    private static final long serialVersionUID = 1L;
}