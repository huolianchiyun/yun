package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_menu
 * @author ASUS
 * @date 2020-07-29 22:13:33
 */
@Data
public class MenuDo extends BaseDo implements Serializable {
    /**
     */
    private String menuTitle;

    /**
     * 菜单类型
     */
    private Integer menuType;

    /**
     */
    private String description;

    /**
     * 父菜单id
     */
    private Long pid;

    /**
     * Spring Security使用Spring EL表达式对URL或方法进行权限控制
     */
    private String permission;

    private static final long serialVersionUID = 1L;
}