package com.zhangbin.yun.yunrights.modules.common.config.cache;

/**
 * 关于缓存的Key集合
 */
public interface CacheKey {

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

    // cacheNames
    String USER = "user";
    String MENU = "menu";
    String GROUP = "group";
    String RULE = "rule";

    // 与用户相关的缓存，采用 hash存储。用戶相关指该用户登录后加载的相关数据。
    // redis key 中包含 BIND_USER_FLAG 说明 适合用户相关的采用 hash存储，且 hash key: BIND_USER_HASH_KEY_PREFIX + username
    String BIND_USER_FLAG = "+U:";
    String BIND_USER_HASH_KEY_PREFIX0 = "H:USER";
    String BIND_USER_HASH_KEY_PREFIX = BIND_USER_HASH_KEY_PREFIX0 + "::";
    String USERNAME_HASH_KEY_PREFIX = "H:USERNAME::";
    /**
     * 用户
     */
    String UserId = "userId";
    String UserIdKey = UserId + ":";
    String UserIdKey_BIND =  BIND_USER_FLAG + UserIdKey;
    String Username = "username";
    String UsernameKey = Username + ":";
    String UsernameKey_BIND = BIND_USER_FLAG + UsernameKey;
}
