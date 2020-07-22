package com.zhangbin.yun.yunrights.modules.rights.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_group
 * @author ASUS
 * @date 2020-07-21 21:51:01
 */
@Data
public class Group extends BaseEntity implements Serializable {
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

    private static final long serialVersionUID = 1L;
}