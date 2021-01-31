package com.hlcy.yun.sys.modules.rights.datarights;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.hlcy.yun.sys.modules.rights.datarights.dialect.Dialect;
import com.hlcy.yun.sys.modules.rights.datarights.rule.RuleManager;
import com.hlcy.yun.sys.modules.rights.model.$do.PermissionRuleDO;
import com.hlcy.yun.sys.modules.common.config.RightsBeanPostProcessor;
import com.hlcy.yun.common.spring.security.SecurityUtils;
import com.hlcy.yun.sys.modules.rights.datarights.dialect.AbstractDialect;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.util.*;

import static com.hlcy.yun.common.spring.security.SecurityUtils.getCurrentUsername;
import static com.hlcy.yun.sys.modules.rights.common.constant.RightsConstants.ADMINISTRATOR;

/**
 * Mybatis - 通用数据权限拦截器<br/>
 * DataRightsInterceptor.dialect = DataRightsHelper
 * <p>
 * Assign a value to variable ruleManager
 *
 * @See {@link RightsBeanPostProcessor}
 */
public class DataRightsHelper implements Dialect {
    private DataRightsAutoDialect autoDialect;
    private static RuleManager ruleManager;

    @Override
    public boolean skip(MappedStatement ms, StatementHandler statementHandler) {
        if (hasNotPermissionAnnotation(ms, statementHandler) || ADMINISTRATOR.equals(SecurityUtils.getCurrentUsername())
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
     * 获取 mapper中被代理的方法（目标方法）, 即 Mapper接口中的方法
     *
     * @param statementHandler /
     * @param splitMethodId    /
     * @param clazz            /
     * @return Method
     */
    private Method getTargetMethod(StatementHandler statementHandler, List<String> splitMethodId, Class<?> clazz) {
        String methodName = splitMethodId.get(splitMethodId.size() - 1);
        // 定位出现在执行 mapper 的哪个方法，不考虑方法重载，因为mybatis定位sql节点是根据 mapper接口完全限定名+id, 全局唯一定位
        // 但实现获取目标方法过程中已解决了方法重载问题，通过方法参数类型唯一定位
        Object paramObj = statementHandler.getParameterHandler().getParameterObject();
        Method targetMethod;
        if (paramObj instanceof MapperMethod.ParamMap) {
            targetMethod = getMethodByMethodNameAndParamTypes(clazz, methodName, (MapperMethod.ParamMap) paramObj);
            if (targetMethod == null) {
                targetMethod = getMethodByMethodName(clazz, methodName);
            }
        } else {
            targetMethod = getMethodByMethodNameAndParamType(clazz, methodName, paramObj);
        }
        return targetMethod;
    }

    private Method getMethodByMethodNameAndParamTypes(Class<?> clazz, String methodName, MapperMethod.ParamMap<Object> paramsMap) {
        Class[] parameterTypes = paramsMap.entrySet().stream()
                .filter(kv -> kv.getKey().startsWith("param") && kv.getValue() != null)
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(kv -> kv.getValue().getClass())
                .toArray(Class[]::new);

        Method targetMethod = ClassUtil.getDeclaredMethod(clazz, methodName, parameterTypes);

        if (targetMethod == null) {
            for (Class<?> anInterface : clazz.getInterfaces()) {
                targetMethod = ClassUtil.getDeclaredMethod(anInterface, methodName, parameterTypes);
                if (targetMethod != null) break;
            }
        }
        return targetMethod;
    }

    private Method getMethodByMethodName(Class<?> clazz, String methodName) {
        Method targetMethod = ReflectUtil.getMethodByName(clazz, methodName);

        if (targetMethod == null) {
            for (Class<?> anInterface : clazz.getInterfaces()) {
                targetMethod = ReflectUtil.getMethodByName(anInterface, methodName);
                if (targetMethod != null) break;
            }
        }
        return targetMethod;
    }

    private Method getMethodByMethodNameAndParamType(Class<?> clazz, String methodName, Object paramObj) {
        if (paramObj == null) {
            return ClassUtil.getDeclaredMethod(clazz, methodName, (Class<?>) null);
        }
        return ClassUtil.getDeclaredMethod(clazz, methodName, paramObj.getClass());
    }
}
