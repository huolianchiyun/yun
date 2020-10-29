package com.zhangbin.yun.sys.modules.security.cache;

import com.zhangbin.yun.common.utils.str.StringUtils;
import com.zhangbin.yun.sys.modules.security.model.dto.MyUserDetails;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserInfoCache {
    /**
     * 用户信息缓存
     */
    public static volatile Map<String, MyUserDetails> userInfoMap = new ConcurrentHashMap<>();


    public static MyUserDetails put(String key, MyUserDetails user) {
        return userInfoMap.put(key, user);
    }

    public static boolean containsKey(String key) {
        return userInfoMap.containsKey(key);
    }

    public static MyUserDetails get(String key) {
        return userInfoMap.get(key);
    }

    /**
     * 清理特定用户缓存信息<br>
     * 用户信息变更时
     *
     * @param userName /
     */
    public static void cleanCacheFor(String userName) {
        if (StringUtils.isNotEmpty(userName)) {
            userInfoMap.remove(userName);
        }
    }

    /**
     * 清理所有用户的缓存信息<br>
     * 如发生角色授权信息变化，可以简便的全部失效缓存
     */
    public static void cleanAll() {
        userInfoMap.clear();
    }
}
