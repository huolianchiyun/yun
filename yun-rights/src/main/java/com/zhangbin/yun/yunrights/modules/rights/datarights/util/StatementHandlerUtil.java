package com.zhangbin.yun.yunrights.modules.rights.datarights.util;

import com.zhangbin.yun.yunrights.modules.rights.datarights.RuleManager;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.Dialect;
import com.zhangbin.yun.yunrights.modules.rights.datarights.exception.DataRightsException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Field;

public abstract class StatementHandlerUtil {

    /**
     * 权限查询
     *
     * @param dialect  /
     * @param boundSql /
     */
    public static void dataRightsQuery(Dialect dialect, BoundSql boundSql, MappedStatement ms) {
        if (dialect.beforeRightsQuery(ms)) {
            String rightsSql = dialect.getPermissionSqlForSelect(boundSql, RuleManager.getRulesForCurrentUser());
            setRightsSql(boundSql, rightsSql);
        }
    }

    public static void dataRightsUpdate(Dialect dialect, BoundSql boundSql, MappedStatement ms) {
        if (dialect.beforeRightsQuery(ms)) {
            //获取到原始sql语句
            String rightsSql = dialect.getPermissionSqlForUpdate(boundSql, RuleManager.getRulesForCurrentUser());
            setRightsSql(boundSql, rightsSql);
        }
    }

    private static void setRightsSql(BoundSql boundSql, String rightsSql) {
        try {
            Field field = boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, rightsSql);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new DataRightsException("", e);
        }
    }
}
