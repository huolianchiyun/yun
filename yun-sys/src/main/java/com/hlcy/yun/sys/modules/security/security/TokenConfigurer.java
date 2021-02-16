package com.hlcy.yun.sys.modules.security.security;

import com.hlcy.yun.sys.modules.security.cache.UserInfoCache;
import com.hlcy.yun.sys.modules.security.config.bean.SecurityProperties;
import com.hlcy.yun.sys.modules.security.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;
    private final UserInfoCache userInfoCache;
    private final OnlineUserService onlineUserService;

    @Override
    public void configure(HttpSecurity http) {
        TokenFilter customFilter = new TokenFilter(tokenProvider, properties, userInfoCache, onlineUserService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
