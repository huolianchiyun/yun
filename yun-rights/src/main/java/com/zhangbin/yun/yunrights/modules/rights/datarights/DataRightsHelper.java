package com.zhangbin.yun.yunrights.modules.rights.datarights;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ClassUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.AbstractDialect;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.Dialect;
import com.zhangbin.yun.yunrights.modules.rights.datarights.rule.RuleManager;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import java.lang.reflect.Method;
import java.util.*;

import static com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils.getCurrentUsername;
import static com.zhangbin.yun.yunrights.modules.rights.common.constant.RightsConstants.ADMINISTRATOR;

/**
 * Mybatis - 通用数据权限拦截器<br/>
 * DataRightsInterceptor.dialect = DataRightsHelper
 *
 * Assign a value to variable ruleManager
 * @See {@link com.zhangbin.yun.yunrights.modules.common.config.RightsBeanPostProcessor}
 */
public class DataRightsHelper implements Dialect {
    private DataRightsAutoDialect autoDialect;
    private static RuleManager ruleManager;

    @Override
    public boolean skip(MappedStatement ms, StatementHandler statementHandler) {
        if (ADMINISTRATOR.equals(SecurityUtils.getCurrentUsername()) || hasNotPermissionAnnotation(ms, statementHandler)
                || CollectionUtil.isEmpty(ruleManager.getRulesForCurrentUser(getCurrentUsername()))) {
            return true;
        } else {
            autoDialect.initDelegateDialect(ms);
            return false;
        }
    }

    @Override
    public boolean skipForUpdate(MappedStatement ms, StatementHandler statementHandler) {
        return ADMINISTRATOR.equals(SecurityUtils.getCurrentUsername()) || hasNotPermissionAnnotation(ms, statementHandler);
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

    private boolean hasNotPermissionAnnotation(MappedStatement ms, StatementHandler statementHandler) {
        try {
            List<String> splitMethodId = Arrays.asList(ms.getId().split("\\."));
            Class<?> clazz = Class.forName(String.join(".", splitMethodId.subList(0, splitMethodId.size() - 1)));
            if (clazz.isAnnotationPresent(NotPermission.class)) return true;
            Method targetMethod = getTargetMethod(statementHandler, splitMethodId, clazz);
            if (targetMethod != null && targetMethod.isAnnotationPresent(NotPermission.class)) return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取 mapper中被代理的方法
     *
     * @param statementHandler /
     * @param splitMethodId    /
     * @param clazz            /
     * @return Method
     */
    private Method getTargetMethod(StatementHandler statementHandler, List<String> splitMethodId, Class<?> clazz) {
        String methodName = splitMethodId.get(splitMethodId.size() - 1);
        // 定位出现在执行 mapper 的哪个方法，不考虑方法重载，因为mybatis定位sql节点是根据 mapper接口完全限定名+id 全局唯一定位
        // 但实现获取目标方法过程中已解决了方法重载问题，通过方法参数类型唯一定位
        Object paramObj = statementHandler.getParameterHandler().getParameterObject();
        Method targetMethod;
        if (paramObj instanceof MapperMethod.ParamMap) {
            Map<String, Object> paramsMap = (MapperMethod.ParamMap) paramObj;
            Class[] classes = paramsMap.entrySet().stream()
                    .filter(kv -> kv.getKey().startsWith("param"))
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(kv -> kv.getValue().getClass())
                    .toArray(Class[]::new);
            targetMethod = ClassUtil.getDeclaredMethod(clazz, methodName, classes);
            if (targetMethod == null) {
                for (Class<?> anInterface : clazz.getInterfaces()) {
                    targetMethod = ClassUtil.getDeclaredMethod(anInterface, methodName, classes);
                    if (targetMethod != null) break;
                }
            }
        } else {
            targetMethod = ClassUtil.getDeclaredMethod(clazz, methodName, paramObj.getClass());
        }
        return targetMethod;
    }
}
