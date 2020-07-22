package com.zhangbin.yun.yunrights.modules.security.service;

import com.zhangbin.yun.yunrights.common.exception.BadRequestException;
import com.zhangbin.yun.yunrights.common.exception.EntityNotFoundException;
import com.zhangbin.yun.yunrights.modules.security.config.bean.LoginProperties;
import com.zhangbin.yun.yunrights.modules.security.service.dto.JwtUserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
//    private final UserService userService;
//    private final RoleService roleService;
//    private final DataService dataService;
    private final LoginProperties loginProperties;
    public void setEnableCache(boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }

    /**
     * 用户信息缓存
     *
     * @see {@link UserCacheClean}
     */
    static Map<String, JwtUserWrapper> userDtoCache = new ConcurrentHashMap<>();

    @Override
    public JwtUserWrapper loadUserByUsername(String username) {
        boolean searchDb = true;
        JwtUserWrapper jwtUserDto = null;
        if (loginProperties.isCacheEnable() && userDtoCache.containsKey(username)) {
            jwtUserDto = userDtoCache.get(username);
            searchDb = false;
        }
        if (searchDb) {
            User user;
//            try {
////                user = userService.findByName(username);
//            } catch (EntityNotFoundException e) {
//                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
//                throw new UsernameNotFoundException("", e);
//            }
//            if (user == null) {
//                throw new UsernameNotFoundException("");
//            } else {
//                if (!user.getEnabled()) {
//                    throw new BadRequestException("账号未激活");
//                }
//                jwtUserDto = new JwtUserDto(
//                        user,
//                        dataService.getDeptIds(user),
//                        roleService.mapToGrantedAuthorities(user)
//                );
//                userDtoCache.put(username, jwtUserDto);
//            }
        }
        return jwtUserDto;
    }
}
