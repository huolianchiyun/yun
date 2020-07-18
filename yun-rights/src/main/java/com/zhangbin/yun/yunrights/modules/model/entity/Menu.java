package com.zhangbin.yun.yunrights.modules.model.entity;

import lombok.Data;

/**
 * 表 t_sys_menu
 * @author ASUS
 * @date 2020-07-18 20:23:07
 */
@Data
public class Menu extends BaseEntity {
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
}