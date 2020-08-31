package com.zhangbin.yun.yunrights.modules.rights.datarights.dialect;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public abstract class AbstractDialect implements Dialect {
    /**
     * 该方法不会被调用
     * 查看 {@link com.zhangbin.yun.yunrights.modules.rights.datarights.DataRightsHelper#skip}
     *
     * @param ms              MappedStatement
     * @return
     */
    @Override
    public boolean skip(MappedStatement ms) {
        return true;
    }

    @Override
    public boolean skipForUpdate(MappedStatement ms) {
        return true;
    }

    @Override
    public Object processParameterObject(MappedStatement ms, Object parameterObject, BoundSql boundSql, CacheKey pageKey) {
        return parameterObject;
    }

    @Override
    public boolean beforeRightsQuery(MappedStatement ms, Object parameterObject) {
        return true;
    }

    @Override
    public String getPermissionSql(BoundSql boundSql, Object parameterObject) {
        String sql = boundSql.getSql();
        // 获取数据权限规则对象
        Set<PermissionRuleDO> rules = null;

        return getPermissionSql(sql, rules);
    }

    /**
     * 单独处理数据权限部分
     *
     * @param sql
     * @param rules
     * @return
     */
    protected abstract String getPermissionSql(String sql, Set<PermissionRuleDO> rules);

    @Override
    public void afterAll() {

    }

    @Override
    public void setProperties(Properties properties) {
    }
}
