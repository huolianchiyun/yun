package com.zhangbin.yun.yunrights.modules.rights.datarights;

import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.AbstractDialect;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.Dialect;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Mybatis - 通用数据权限拦截器<br/>
 */
public class DataRightsHelper implements Dialect {
    private DataRightsAutoDialect autoDialect;

    private Map<String, List<PermissionRuleDO>> groupCodePermissionsMap;

    @Override
    public boolean skip(MappedStatement ms, Object parameterObject) {
        if (groupCodePermissionsMap == null) {
            return true;
        } else {
            autoDialect.initDelegateDialect(ms);
            return false;
        }
    }

    @Override
    public Object processParameterObject(MappedStatement ms, Object parameterObject, BoundSql boundSql, CacheKey pageKey) {
        return autoDialect.getDelegate().processParameterObject(ms, parameterObject, boundSql, pageKey);
    }

    @Override
    public boolean beforeRightsQuery(MappedStatement ms, Object parameterObject) {
        return autoDialect.getDelegate().beforeRightsQuery(ms, parameterObject);
    }

    @Override
    public String getPermissionSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, CacheKey cacheKey) {
        return autoDialect.getDelegate().getPermissionSql(ms, boundSql, parameterObject, cacheKey);
    }

    @Override
    public Object afterRightsQuery(List rightsList, Object parameterObject) {
        //这个方法即使不分页也会被执行，所以要判断 null
        AbstractDialect delegate = autoDialect.getDelegate();
        if (delegate != null) {
            return delegate.afterRightsQuery(rightsList, parameterObject);
        }
        return rightsList;
    }

    @Override
    public void afterAll() {
        // 这个方法即使不数据权限查询也会被执行，所以要判断 null
        AbstractDialect delegate = autoDialect.getDelegate();
        if (delegate != null) {
            delegate.afterAll();
            autoDialect.clearDelegate();
        }
    }

    @Override
    public void setProperties(Properties properties) {
        autoDialect = new DataRightsAutoDialect();
        autoDialect.setProperties(properties);
    }

}
