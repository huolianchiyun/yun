package com.hlcy.yun.sys.modules.common.xcache;

/**
 * 关于缓存的Key集合
 */
public interface CacheKey {
    String API_RIGHTS = "api-rights";

    /**
     * 部门信息
     */
    String DEPT_ID = "dept::id:";
    String DEPT_PID = "dept::pid:";

    /**
     * 组信息
     */
    String GROUP_ID = "group::id:";
    String GROUP_PID = "group::pid:";
    String GROUP_BIND_MENU = "menu::group:";

    /**
     * 菜单信息
     */
    String MENU_ID = "menu::id:";
    String MENU_PID = "menu::pid:";

    // cacheNames
    String USER = "user";
    String MENU = "menu";
    String GROUP = "group";
    String RULE = "rule";

    // 与用户相关的缓存，采用 hash存储。用戶相关指该用户登录后加载的相关数据。
    // redis key 中包含 BIND_USER_FLAG 说明 适合用户相关的采用 hash存储，且 hash key: BIND_USER_HASH_KEY_PREFIX + username
    String HSet = "+HSet:";
    String BIND_USER = HSet+ "+U:";
    String BIND_USER_HASH_KEY_PREFIX0 = "H:USER";
    String BIND_USER_HASH_KEY_PREFIX = BIND_USER_HASH_KEY_PREFIX0 + "::";
    /**
     * 用户
     */
    String UserId = "userId";
    String UserIdKey = UserId + ":";
    String USERNAME = "username";
    String UsernameKey = USERNAME + ":";
}
