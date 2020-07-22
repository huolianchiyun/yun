package com.zhangbin.yun.yunrights.modules.rights.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_menu
 * @author ASUS
 * @date 2020-07-21 21:51:01
 */
@Data
public class Menu extends BaseEntity implements Serializable {
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

    private static final long serialVersionUID = 1L;
}