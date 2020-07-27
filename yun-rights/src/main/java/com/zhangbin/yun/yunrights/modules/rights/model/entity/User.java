package com.zhangbin.yun.yunrights.modules.rights.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 表 t_sys_user
 * @author ASUS
 * @date 2020-07-27 21:58:22
 */
@Data
public class User extends BaseEntity implements Serializable {
    /**
     */
    private String userName;

    /**
     */
    private String loginName;

    /**
     * 性別：1 男， 2 女
     */
    private Integer gender;

    /**
     */
    private String pwd;

    /**
     */
    private String phone;

    /**
     */
    private Long deptId;

    /**
     * 用户状态：0 禁用，1 启用
     */
    private Boolean enabled;

    /**
     */
    private Date pwdResetTime;

    private static final long serialVersionUID = 1L;
}