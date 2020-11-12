package com.yun.sys.modules.rights.datarights.dialect;


import com.yun.sys.modules.rights.model.$do.PermissionRuleDO;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Properties;
import java.util.Set;

/**
 * 数据库方言，针对不同数据库进行实现
 */
public interface Dialect {

    /**
     * 跳过数据权限查询
     *
     * @param ms               MappedStatement
     * @param statementHandler StatementHandler
     * @return true 跳过，false 执行权限查询
     */
    default boolean skip(MappedStatement ms, StatementHandler statementHandler) {
        return true;
    }

    /**
     * 跳过数据权限修改
     *
     * @param ms               MappedStatement
     * @param statementHandler StatementHandler
     * @return true 跳过，false 执行权限
     */
    default boolean skipForUpdate(MappedStatement ms, StatementHandler statementHandler) {
        return true;
    }

    /**
     * 执行数据权限前
     *
     * @param ms MappedStatement
     * @return 返回 true 会进行数据权限操作
     */
    default boolean beforeExecuteDataRights(MappedStatement ms) {
        return true;
    }

    /**
     * 生成权限 select sql
     *
     * @param boundSql 绑定 SQL 对象
     * @param rules    当前用户数据权限规则
     * @return rights select sql
     */
    String getPermissionSqlForSelect(BoundSql boundSql, Set<PermissionRuleDO> rules);

    /**
     * 生成权限 update|delete sql
     *
     * @param boundSql 绑定 SQL 对象
     * @param rules    当前用户数据权限规则
     * @return rights update|delete sql
     */
    String getPermissionSqlForUpdate(BoundSql boundSql, Set<PermissionRuleDO> rules);

    /**
     * 完成所有任务后
     */
    void afterAll();

    /**
     * 设置参数
     *
     * @param properties 插件属性
     */
    void setProperties(Properties properties);

}
