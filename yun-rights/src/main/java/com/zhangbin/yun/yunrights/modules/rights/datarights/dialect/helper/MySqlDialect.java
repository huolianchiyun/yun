package com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.helper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.AbstractDialect;
import org.apache.ibatis.cache.CacheKey;

import java.util.Set;

public class MySqlDialect extends AbstractDialect {

    @Override
    protected String getPermissionSql(String sql, Set<PermissionRuleDO> rules) {
        // TODO mysql 数据权限处理逻辑  根据权限修改原有sql
        return null;
    }
}
