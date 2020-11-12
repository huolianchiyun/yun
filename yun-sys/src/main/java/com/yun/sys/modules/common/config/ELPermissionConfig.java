package com.yun.sys.modules.common.config;

import com.yun.common.spring.security.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "el")
public class ELPermissionConfig {

    public Boolean check(String... permissions) {
        // 获取当前用户的所有权限
        List<String> elPermissions = SecurityUtils.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return elPermissions.contains("all") || Arrays.stream(permissions).anyMatch(elPermissions::contains);
    }
}
