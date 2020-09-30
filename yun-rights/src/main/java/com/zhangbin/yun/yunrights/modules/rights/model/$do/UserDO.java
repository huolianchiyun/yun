package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDO;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
import static com.zhangbin.yun.yunrights.modules.common.utils.ValidationUtil.REGEX_MOBILE;

/**
 * 表 t_sys_user
 *
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Getter
@Setter
@JsonIgnoreProperties("handler")
public class UserDO extends BaseDO implements ExcelSupport, Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(required = true)
    @NotBlank(groups = {Create.class, Update.class}, message = "username 不能为空！")
    private String username;

    @NotBlank(groups = Create.class, message = "nickname 不能为空！")
    private String nickname;

    /**
     * 性別：1 男， 2 女
     */
    @ApiModelProperty(required = true, notes = "性別：1 -> 男， 2 -> 女", example = "2")
    @Size(max = 2, min = 1, message = "1 -> 男， 2 -> 女")
    @NotNull(groups = Create.class, message = "gender 不能为空！")
    private Byte gender;

    @ApiModelProperty(required = true)
    @Null(groups = Update.class, message = "此接口不允许修改密码！")
    private String pwd;

    @Pattern(regexp = REGEX_MOBILE, message = "电话号码格式错误！")
    private String phone;

    @Email
    private String email;

    private Long deptId;

    /**
     * 用户状态：0 禁用，1 启用
     */
    @ApiModelProperty(required = true)
    private Boolean status;

    /**
     * 是否已删除：0：未删除，1：已删除
     */
    @ApiModelProperty(required = true)
    private Boolean deleted;

    /**
     * 是否是管理员，管理员全局唯一，即系统中只有一个
     */
    private Boolean admin;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(hidden = true)
    private LocalDateTime pwdResetTime;

    private Set<GroupDO> groups;

    @ApiModelProperty(hidden = true)
    private GroupDO dept;

    public UserDO() {
    }

    public UserDO(Long id, String pwd, LocalDateTime pwdResetTime) {
        this.id = id;
        this.pwd = pwd;
        this.pwdResetTime = pwdResetTime;
    }

    public UserDO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDO other = (UserDO) o;
        return Objects.equals(id, other.id) || (Objects.equals(username, other.username) && Objects.equals(pwd, other.pwd));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, pwd);
    }

    @Override
    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        List<String> groups = this.groups.stream().map(GroupDO::getGroupName).collect(Collectors.toList());
        map.put("显示名", nickname);
        map.put("用户名", username);
        map.put("所属组", String.join(",", groups));
        map.put("所属部门", dept.getGroupName());
        map.put("手机号码", phone);
        map.put("邮箱", email);
        map.put("状态", status ? "已激活" : "未激活");
        map.put("是否删除", deleted ? "已删除" : "未删除");
        map.put("修改密码时间", pwdResetTime);
        map.put("创建日期", createTime);
        return map;
    }
}
