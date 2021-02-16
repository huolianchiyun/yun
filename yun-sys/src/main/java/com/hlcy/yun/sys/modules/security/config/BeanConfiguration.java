package com.hlcy.yun.sys.modules.security.config;

import com.hlcy.yun.common.spring.redis.RedisUtils;
import com.hlcy.yun.sys.modules.security.cache.UserInfoCache;
import com.hlcy.yun.sys.modules.security.cache.UserInfoMemoryCache;
import com.hlcy.yun.sys.modules.security.cache.UserInfoRedisCache;
import com.hlcy.yun.sys.modules.security.config.bean.LoginProperties;
import com.hlcy.yun.sys.modules.security.config.bean.SecurityProperties;
import com.hlcy.yun.sys.modules.security.model.dto.MyUserDetails;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "login")
    public LoginProperties loginProperties() {
        return new LoginProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "jwt")
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }

    @Bean
    public UserInfoCache userInfoCache(LoginProperties properties, RedisUtils redisUtils) {
        if (properties.isEnableCache()) {
            if (LoginProperties.CacheType.redis == properties.getTypeCache()) {
                return new UserInfoRedisCache(redisUtils);
            } else {
                return new UserInfoMemoryCache();
            }
        } else {
            // 不缓存时，返回一个没有任何实现的子类实例，目的在于不影响客户端代码，避免代码修改或空指针异常等出现
            return new UserInfoCache() {
                @Override
                public MyUserDetails put(String username, MyUserDetails user) {
                    return null;
                }

                @Override
                public MyUserDetails get(String username) {
                    return null;
                }

                @Override
                public boolean containsKey(String username) {
                    return false;
                }

                @Override
                public void cleanCacheByUsername(String username) {

                }

                @Override
                public void cleanAll() {

                }
            };
        }
    }
}
