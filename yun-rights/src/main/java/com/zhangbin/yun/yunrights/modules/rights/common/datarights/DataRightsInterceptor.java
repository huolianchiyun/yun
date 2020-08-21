package com.zhangbin.yun.yunrights.modules.rights.common.datarights;

import com.zhangbin.yun.yunrights.modules.common.exception.BadConfigurationException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StringUtils;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 数据权限控制
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class DataRightsInterceptor implements Interceptor {

    private Dialect dialect;
    private String default_dialect_class = DataRightsHelper.class.getName();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {







        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialect");
        if (StringUtils.isEmpty(dialectClass)) {
            dialectClass = default_dialect_class;
        }
        try {
            Class<?> aClass = Class.forName(dialectClass);
            dialect = (Dialect) aClass.newInstance();
        } catch (Exception e) {
            throw new BadConfigurationException(e);
        }
    }

}
