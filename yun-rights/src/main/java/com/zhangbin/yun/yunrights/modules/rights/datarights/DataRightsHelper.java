package com.zhangbin.yun.yunrights.modules.rights.datarights;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.AbstractDialect;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.Dialect;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Mybatis - 通用数据权限拦截器<br/>
 * DataRightsInterceptor.dialect = DataRightsHelper
 */
public class DataRightsHelper implements Dialect {

    private DataRightsAutoDialect autoDialect;

    @Override
    public boolean skip(MappedStatement ms) {
        if (/*"admin".equals(SecurityUtils.getCurrentUsername()) || */hasNotPermissionAnnotation(ms)
            /*|| CollectionUtil.isEmpty(RuleManager.getRulesForCurrentUser())*/) {
            return true;
        } else {
            autoDialect.initDelegateDialect(ms);
            return false;
        }
    }

    @Override
    public boolean skipForUpdate(MappedStatement ms) {
        return "admin".equals(SecurityUtils.getCurrentUsername()) || hasNotPermissionAnnotation(ms);
    }

    @Override
    public boolean beforeExecuteDataRights(MappedStatement ms) {
        return autoDialect.getDelegate().beforeExecuteDataRights(ms);
    }

    @Override
    public String getPermissionSqlForSelect(BoundSql boundSql, Set<PermissionRuleDO> rules) {
        return autoDialect.getDelegate().getPermissionSqlForSelect(boundSql, rules);
    }

    @Override
    public String getPermissionSqlForUpdate(BoundSql boundSql, Set<PermissionRuleDO> rules) {
        return autoDialect.getDelegate().getPermissionSqlForUpdate(boundSql, rules);
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

    private boolean hasNotPermissionAnnotation(MappedStatement ms) {
//        try {
//            List<String> splitMethodId = Arrays.asList(ms.getId().split("\\."));
//            Class<?> clazz = Class.forName(String.join(".", splitMethodId.subList(0, splitMethodId.size() - 1)));
//            if (clazz.isAnnotationPresent(NotPermission.class)) return true;
//
//            String methodName = splitMethodId.get(splitMethodId.size() - 1);
//            Class<?> type = ms.getParameterMap().getType();
////            Class<?>[] parameterTypes = type == null ? new Class<?>[0] : ArrayUtil.a;
//            Method targetMethod = ClassUtil.getDeclaredMethod(clazz, methodName, parameterTypes);
//            if (targetMethod != null && targetMethod.isAnnotationPresent(NotPermission.class)) return true;
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return false;
        return true;
    }

}
