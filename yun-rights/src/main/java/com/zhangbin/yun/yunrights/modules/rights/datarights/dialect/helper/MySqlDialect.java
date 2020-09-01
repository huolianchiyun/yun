package com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.helper;

import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.AbstractDialect;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Set;

public class MySqlDialect extends AbstractDialect {

    @Override
    public String getPermissionSqlForSelect(BoundSql boundSql, Set<PermissionRuleDO> rules) {
        // TODO mysql 数据权限处理逻辑  根据权限修改原有sql
        return null;
    }

    @Override
    public String getPermissionSqlForUpdate(BoundSql boundSql, Set<PermissionRuleDO> rules) {
        //获取到原始sql语句
        String sql = boundSql.getSql();
        if (sql.toLowerCase().contains("where")) {
            String prefix = findColumnPrefix(sql);
            sql += " and " + prefix + "_creator = '" + SecurityUtils.getCurrentUsername() + "'";
        }
        return sql;
    }
}
