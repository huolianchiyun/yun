package com.hlcy.yun.sys.modules.security.cache;

import com.hlcy.yun.common.utils.str.StringUtils;
import com.hlcy.yun.sys.modules.security.model.dto.MyUserDetails;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class UserInfoMemoryCache implements UserInfoCache {
    private static final Map<String, MyUserDetails> cache = new ConcurrentHashMap<>();

    @Override
    public MyUserDetails put(String key, MyUserDetails user) {
        return cache.put(key, user);
    }

    @Override
    public MyUserDetails get(String key) {
        return cache.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }


    /**
     * 清理特定用户缓存信息<br>
     * 用户信息变更时
     *
     * @param userName /
     */
    @Override
    public void cleanCacheByUsername(String userName) {
        if (StringUtils.isNotEmpty(userName)) {
            cache.remove(userName);
        }
    }

    /**
     * 清理所有用户的缓存信息<br>
     * 如发生角色授权信息变化，可以简便的全部失效缓存
     */
    public void cleanAll() {
        cache.clear();
    }
}
