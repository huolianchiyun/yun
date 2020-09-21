package com.zhangbin.yun.yunrights.modules.security.security;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.service.GroupService;
import com.zhangbin.yun.yunrights.modules.rights.service.UserService;
import com.zhangbin.yun.yunrights.modules.security.config.bean.LoginProperties;
import com.zhangbin.yun.yunrights.modules.security.model.dto.MyUserDetails;
import com.zhangbin.yun.yunrights.modules.security.cache.UserInfoCache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final GroupService groupService;

    private final LoginProperties loginProperties;

    public void setEnableCache(boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }

    @Override
    public MyUserDetails loadUserByUsername(String username) {
        MyUserDetails myUserDetails = null;
        if (loginProperties.isCacheEnable() && UserInfoCache.containsKey(username)) {
            myUserDetails = UserInfoCache.get(username);
        }
        if (Objects.isNull(myUserDetails)) {
            UserDO user = userService.queryByUsername(username);
            Assert.notNull(user, "*** 用户不存 ***");
            Assert.isTrue(user.getStatus(), "*** 账号未激活 ***");
            myUserDetails = new MyUserDetails(user, groupService.getGrantedAuthorities(user));
            UserInfoCache.put(username, myUserDetails);
        }
        return myUserDetails;
    }
}
