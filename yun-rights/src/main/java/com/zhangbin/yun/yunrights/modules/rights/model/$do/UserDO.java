package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import lombok.Getter;
import lombok.Setter;

/**
 * 表 t_sys_user
 *
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Getter
@Setter
public class UserDO extends BaseDo implements ExcelSupport, Serializable {
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
     * 是否是管理员，管理员全局唯一，即系统中只有一个
     */
    private boolean admin;

    /**
     *
     */
    private LocalDateTime pwdResetTime;

    private Set<RoleDO> roles;

    private GroupDO dept;

    public UserDO() {
    }

    public UserDO(String userName, String pwd, LocalDateTime pwdResetTime) {
        this.userName = userName;
        this.pwd = pwd;
        this.pwdResetTime = pwdResetTime;
    }

    public UserDO(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDO other = (UserDO) o;
        return Objects.equals(id, other.id) || (Objects.equals(userName, other.userName) && Objects.equals(pwd, other.pwd));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, pwd);
    }

    @Override
    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        List<String> roles = this.roles.stream().map(RoleDO::getRoleName).collect(Collectors.toList());
        map.put("用户名", userName);
        map.put("角色", String.join(",", roles));
        map.put("部门", dept.getGroupName());
        map.put("邮箱", email);
        map.put("状态", enabled ? "启用" : "禁用");
        map.put("手机号码", phone);
        map.put("修改密码时间", pwdResetTime);
        map.put("创建日期", createTime);
        return map;
    }
}
