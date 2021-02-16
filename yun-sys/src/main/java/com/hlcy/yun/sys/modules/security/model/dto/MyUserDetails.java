package com.hlcy.yun.sys.modules.security.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hlcy.yun.sys.modules.rights.model.$do.UserDO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class MyUserDetails implements UserDetails {
    public MyUserDetails(UserDO user, List<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    private UserDO user;

    @JsonIgnore
    private List<GrantedAuthority> authorities;

    /**
     * 提供给前端校验该用户有无访问 api权限用
     *
     * @return 返回该用户拥有的所有能访问的 api权限集合
     */
    public Set<String> getUrlRights() {
        return authorities == null ? new HashSet<>(0) : authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getUsername();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return user.getStatus();
    }
}
