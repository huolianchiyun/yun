package com.hlcy.yun.sys.modules.security.cache;

import com.hlcy.yun.common.spring.redis.RedisUtils;
import com.hlcy.yun.common.utils.str.StringUtils;
import com.hlcy.yun.sys.modules.security.model.dto.MyUserDetails;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserInfoRedisCache implements UserInfoCache {
    private static final String KEY = "H::UserInfoCache";
    private final RedisUtils redisUtils;

    @Override
    public MyUserDetails put(String username, MyUserDetails user) {
        return redisUtils.hset(KEY, username, user) ? user : null;
    }

    @Override
    public MyUserDetails get(String username) {
        return (MyUserDetails) redisUtils.hget(KEY, username);
    }

    @Override
    public boolean containsKey(String username) {
        return redisUtils.hHasKey(KEY, username);
    }

    /**
     * 清理特定用户缓存信息<br>
     * 用户信息变更时
     *
     * @param username /
     */
    @Override
    public void cleanCacheByUsername(String username) {
        if (StringUtils.isNotEmpty(username)) {
            redisUtils.hdel(KEY, username);
        }
    }

    /**
     * 清理所有用户的缓存信息<br>
     * 如发生角色授权信息变化，可以简便的全部失效缓存
     */
    public void cleanAll() {
        redisUtils.del(KEY);
    }
}
