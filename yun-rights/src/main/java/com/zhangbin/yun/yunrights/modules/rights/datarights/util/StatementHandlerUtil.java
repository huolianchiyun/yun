package com.zhangbin.yun.yunrights.modules.rights.datarights.util;

import com.zhangbin.yun.yunrights.modules.rights.datarights.rule.RuleManager;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.Dialect;
import com.zhangbin.yun.yunrights.modules.rights.datarights.exception.DataRightsException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Field;

import static com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils.getCurrentUsername;

/**
 *  StatementHandler 工具类
 *  Assign a value to variable ruleManager
 * @See {@link com.zhangbin.yun.yunrights.modules.common.config.RightsBeanPostProcessor}
 */
public abstract class StatementHandlerUtil {
    private static RuleManager ruleManager;

    /**
     * 权限查询
     *
     * @param dialect  /
     * @param boundSql /
     */
    public static void dataRightsQuery(Dialect dialect, BoundSql boundSql, MappedStatement ms) {
        if (dialect.beforeExecuteDataRights(ms)) {
            String rightsSql = dialect.getPermissionSqlForSelect(boundSql, ruleManager.getRulesForCurrentUser(getCurrentUsername()));
            setRightsSql(boundSql, rightsSql);
        }
    }

    public static void dataRightsUpdate(Dialect dialect, BoundSql boundSql, MappedStatement ms) {
        if (dialect.beforeExecuteDataRights(ms)) {
            //获取到原始sql语句
            String rightsSql = dialect.getPermissionSqlForUpdate(boundSql, ruleManager.getRulesForCurrentUser(getCurrentUsername()));
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
            throw new DataRightsException("Set rights sql failed.", e);
        }
    }
}
