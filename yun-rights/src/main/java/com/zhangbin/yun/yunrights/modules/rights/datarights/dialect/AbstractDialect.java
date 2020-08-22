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
     * @param parameterObject 方法参数
     * @return
     */
    @Override
    public boolean skip(MappedStatement ms, Object parameterObject) {
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
    public String getPermissionSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, CacheKey cacheKey) {
        String sql = boundSql.getSql();
        // 获取数据权限规则对象
        Set<PermissionRuleDO> rules = null;

        return getPermissionSql(sql, rules, cacheKey);
    }

    /**
     * 单独处理数据权限部分
     *
     * @param sql
     * @param rules
     * @param cacheKey
     * @return
     */
    protected abstract String getPermissionSql(String sql, Set<PermissionRuleDO> rules, CacheKey cacheKey);

    @Override
    public Object afterRightsQuery(List rightsList, Object parameterObject) {
        return rightsList;
    }

    @Override
    public void afterAll() {

    }

    @Override
    public void setProperties(Properties properties) {
    }
}
