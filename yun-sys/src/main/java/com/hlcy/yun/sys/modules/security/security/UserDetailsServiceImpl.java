package com.hlcy.yun.sys.modules.security.security;

import com.hlcy.yun.common.web.response.Meta;
import com.hlcy.yun.sys.modules.rights.model.$do.UserDO;
import com.hlcy.yun.sys.modules.rights.service.GroupService;
import com.hlcy.yun.sys.modules.rights.service.UserService;
import com.hlcy.yun.sys.modules.security.cache.UserInfoCache;
import com.hlcy.yun.sys.modules.security.config.bean.LoginProperties;
import com.hlcy.yun.sys.modules.security.model.dto.MyUserDetails;
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
            // 验证密码是否过期
            final Meta meta = userService.verifyPasswordExpired(username);
            Assert.isTrue(Meta.Status.NoApiRights != meta.getStatus(), meta.getMessage());
            myUserDetails = new MyUserDetails(user, groupService.getGrantedAuthorities(user));
            UserInfoCache.put(username, myUserDetails);
        }
        return myUserDetails;
    }
}
