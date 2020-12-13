package com.hlcy.yun.sys.modules.rights.model.$do;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.hlcy.yun.common.model.BaseDO;
import com.hlcy.yun.common.utils.date.DateUtil;
import com.hlcy.yun.common.utils.download.excel.ExcelSupport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

import static com.hlcy.yun.common.utils.validator.ValidationUtil.REGEX_MOBILE;

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

    private String comeFrom;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(hidden = true)
    private LocalDateTime pwdResetTime;

    // UserDO 和 GroupDO 之间存在相互引用， 生成swagger api 文档时会报错：Ambiguous models equality when conditions is empty.
    // swagger 问题记录： https://github.com/sitmun/sitmun-backend-core/issues/16
    // 临时解决方案：  @ApiModelProperty(hidden = true) 忽略相互引用属性
    @ApiModelProperty(hidden = true)
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

    public UserDO(String username, String pwd, LocalDateTime pwdResetTime) {
        this.username = username;
        this.pwd = pwd;
        this.pwdResetTime = pwdResetTime;
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
        map.put("所属部门", dept == null ? "" : dept.getGroupName());
        map.put("手机号码", phone);
        map.put("邮箱", email);
        map.put("状态", status ? "已激活" : "未激活");
        map.put("是否删除", deleted ? "已删除" : "未删除");
        map.put("用户来源", comeFrom);
        map.put("修改密码时间", pwdResetTime == null ? "" :  DateUtil.format2MdHms(pwdResetTime));
        map.put("创建日期", createTime == null ? "" : DateUtil.format2MdHms(createTime));
        return map;
    }
}
