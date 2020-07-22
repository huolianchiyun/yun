package com.zhangbin.yun.yunrights.modules.rights.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_user_group
 * @author ASUS
 * @date 2020-07-21 21:51:01
 */
@Data
public class UserGroup implements Serializable {
    /**
     */
    private Long userId;

    /**
     */
    private Long tGroupId;

    private static final long serialVersionUID = 1L;
}