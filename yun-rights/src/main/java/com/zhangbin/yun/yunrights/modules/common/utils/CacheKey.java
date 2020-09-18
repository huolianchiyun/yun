package com.zhangbin.yun.yunrights.modules.common.utils;

/**
 * 关于缓存的Key集合
 */
public interface CacheKey {

    /**
     * 内置 用户、部门、组、菜单 相关key
     */
    String USER_MODIFY_TIME_KEY = "user:modify:time:key:";
    String DEPT_MODIFY_TIME_KEY = "dept:modify:time:key:";
    String GROUP_MODIFY_TIME_KEY = "group:modify:time:key:";
    String MENU_MODIFY_TIME_KEY = "menu:modify:time:key:";

    /**
     * 用户
     */
    String USER_ID = "user::id:";
    String USER_NAME = "user::username:";
    /**
     * 数据
     */
    String DATE_USER = "data::user:";
    /**
     * 菜单
     */
    String MENU_USER = "menu::user:";
    /**
     * 组授权
     */
    String GROUP_AUTH = "group::auth:";

    /**
     * 组授权
     */
    String GROUP_MENU = "menu::group:";
    /**
     * 组信息
     */
    String GROUP_ID = "group::id:";
}
