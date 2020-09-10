package com.zhangbin.yun.yunrights.modules.rights.datarights;

import com.zhangbin.yun.yunrights.modules.common.exception.BadConfigurationException;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.Dialect;
import com.zhangbin.yun.yunrights.modules.rights.datarights.util.StatementHandlerUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.util.StringUtils;
import java.sql.Connection;
import java.util.Properties;

/**
 * 数据权限控制
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataRightsInterceptor implements Interceptor {

    private Dialect dialect;
    private final String default_dialect_class = DataRightsHelper.class.getName();


    /**
     * 数据权限控制逻辑
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MappedStatement mappedStatement = getMappedStatement(statementHandler);
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            if (SqlCommandType.SELECT.equals(sqlCommandType)) {
                processForSelectSql(statementHandler, mappedStatement);
            } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                // TODO 先不做处理，update权限在rights业务代码中控制，无法是控制系统权限修改等操作，真正业务暂不考虑
                // processForUpdateOrDeleteSql(statementHandler, mappedStatement);
            }
            return invocation.proceed();
        } finally {
            if (dialect != null) {
                dialect.afterAll();
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler || target instanceof Executor) {
            return Plugin.wrap(target, this); // 添加拦截代理
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
        dialect.setProperties(properties);
    }

    /**
     * Spring bean 方式配置时，如果没有配置属性就不会执行下面的 setProperties 方法，就不会初始化
     * <p>
     * 因此这里会出现 null 的情况 fixed #26
     */
    private void checkDialectExists() {
        if (dialect == null) {
            synchronized (default_dialect_class) {
                if (dialect == null) {
                    setProperties(new Properties());
                }
            }
        }
    }

    /**
     * 数据查询权限
     *
     * @param statementHandler /
     * @param ms               /
     */
    private void processForSelectSql(StatementHandler statementHandler, MappedStatement ms) {
        checkDialectExists();
        if (!dialect.skip(ms)) {
            StatementHandlerUtil.dataRightsQuery(dialect, statementHandler.getBoundSql(), ms);
        }
    }

    /**
     * 非数据创建本人和管理员则不能对数据进行 update、delete操作
     *
     * @param statementHandler /
     * @param ms  /
     */
    private void processForUpdateOrDeleteSql(StatementHandler statementHandler, MappedStatement ms) {
        if (dialect.skipForUpdate(ms)) {
            StatementHandlerUtil.dataRightsUpdate(dialect, statementHandler.getBoundSql(), ms);
        }
    }

    private MappedStatement getMappedStatement(StatementHandler statementHandler) {
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        return (MappedStatement) metaObject.getValue("delegate.mappedStatement");
    }
}
