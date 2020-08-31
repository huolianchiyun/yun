package com.zhangbin.yun.yunrights.modules.rights.datarights;

import cn.hutool.core.collection.CollectionUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.logging.mapper.LogMapper;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.AbstractDialect;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.Dialect;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.aspectj.weaver.ast.Not;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

/**
 * Mybatis - 通用数据权限拦截器<br/>
 */
public class DataRightsHelper implements Dialect {
    private DataRightsAutoDialect autoDialect;


    @Override
    public boolean skip(MappedStatement ms) {
        if ("admin".equals(SecurityUtils.getCurrentUsername()) || isNotPermission(ms)
                || CollectionUtil.isEmpty(RuleManager.getRulesForCurrentUser())) {
            return true;
        } else {
            autoDialect.initDelegateDialect(ms);
            return false;
        }
    }

    @Override
    public boolean skipForUpdate(MappedStatement ms) {
        return "admin".equals(SecurityUtils.getCurrentUsername()) || isNotPermission(ms);
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
    public String getPermissionSql(BoundSql boundSql, Object parameterObject) {
        return autoDialect.getDelegate().getPermissionSql(boundSql, parameterObject);
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

    private boolean isNotPermission(MappedStatement ms) {
        try {
            Class<?> clazz = Class.forName(ms.getId().substring(0, ms.getId().lastIndexOf(".")));
            if(clazz.isAnnotationPresent(NotPermission.class)) return true;
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(NotPermission.class)) return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
