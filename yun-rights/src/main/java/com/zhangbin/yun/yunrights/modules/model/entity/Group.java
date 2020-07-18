package com.zhangbin.yun.yunrights.modules.model.entity;

import lombok.Data;

/**
 * 表 t_sys_group
 * @author ASUS
 * @date 2020-07-18 20:23:07
 */
@Data
public class Group extends BaseEntity {
    /**
     */
    private String groupName;

    /**
     * 父组id
     */
    private Long pid;

    /**
     */
    private String description;
}