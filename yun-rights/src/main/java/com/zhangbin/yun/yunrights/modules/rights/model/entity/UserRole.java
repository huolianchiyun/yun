package com.zhangbin.yun.yunrights.modules.rights.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_user_role
 * @author ASUS
 * @date 2020-07-27 21:58:22
 */
@Data
public class UserRole implements Serializable {
    /**
     */
    private Long userId;

    /**
     */
    private Long roleId;

    private static final long serialVersionUID = 1L;
}