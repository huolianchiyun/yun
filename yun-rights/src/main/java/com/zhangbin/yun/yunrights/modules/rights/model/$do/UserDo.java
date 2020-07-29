package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 表 t_sys_user
 * @author ASUS
 * @date 2020-07-29 22:13:33
 */
@Data
public class UserDo extends BaseDo implements Serializable {
    /**
     */
    private String userName;

    /**
     */
    private String loginName;

    /**
     * 性別：1 男， 2 女
     */
    private Byte gender;

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