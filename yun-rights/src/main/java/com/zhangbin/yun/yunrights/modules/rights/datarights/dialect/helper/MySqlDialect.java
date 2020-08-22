package com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.helper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.AbstractDialect;
import org.apache.ibatis.cache.CacheKey;

import java.util.Set;

public class MySqlDialect extends AbstractDialect {

    @Override
    protected String getPermissionSql(String sql, Set<PermissionRuleDO> rules, CacheKey cacheKey) {
        // TODO mysql 数据权限处理


        return null;
    }
}
