package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import lombok.Data;

/**
 * 表 t_sys_user
 *
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Data
public class UserDo extends BaseDo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String userName;

    /**
     *
     */
    private String nickName;

    /**
     * 性別：1 男， 2 女
     */
    private Byte gender;

    /**
     *
     */
    private String pwd;

    /**
     *
     */
    private String phone;

    private String email;

    /**
     *
     */
    private Long deptId;

    /**
     * 用户状态：0 禁用，1 启用
     */
    private Boolean enabled;

    /**
     *
     */
    private LocalDateTime pwdResetTime;

    private Set<RoleDo> roles;

    private GroupDo dept;

    public UserDo() {
    }

    public UserDo(String userName, String pwd, LocalDateTime pwdResetTime) {
        this.userName = userName;
        this.pwd = pwd;
        this.pwdResetTime = pwdResetTime;
    }

    public UserDo(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDo other = (UserDo) o;
        return Objects.equals(id, other.id) || (Objects.equals(userName, other.userName) && Objects.equals(pwd, other.pwd));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, pwd);
    }
}
