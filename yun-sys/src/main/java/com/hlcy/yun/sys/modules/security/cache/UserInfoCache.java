package com.hlcy.yun.sys.modules.security.cache;

import com.hlcy.yun.sys.modules.security.model.dto.MyUserDetails;

public interface UserInfoCache {

    /**
     * 缓存用户信息
     * @param username 全局唯一
     * @param user 用户信息
     * @return MyUserDetails
     */
    MyUserDetails put(String username, MyUserDetails user);

    MyUserDetails get(String username);

    boolean containsKey(String username);

    /**
     * 清理指定用户缓存信息<br>
     * 用户信息变更时，可调用改方法清理
     *
     * @param username /
     */
    void cleanCacheByUsername(String username);


    /**
     * 清理所有用户的缓存信息<br>
     * 如发生角色授权信息变化，可以简便的全部失效缓存
     */
    void cleanAll();
}
