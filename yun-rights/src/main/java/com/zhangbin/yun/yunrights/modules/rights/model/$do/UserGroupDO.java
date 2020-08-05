package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_user_group
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Data
public class UserGroupDO implements Serializable {
    /**
     */
    private Long userId;

    /**
     */
    private Long tGroupId;

    private static final long serialVersionUID = 1L;
}
