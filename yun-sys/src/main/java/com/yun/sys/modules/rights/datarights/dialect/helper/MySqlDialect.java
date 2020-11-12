package com.yun.sys.modules.rights.datarights.dialect.helper;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.druid.util.JdbcConstants;
import com.yun.sys.modules.rights.datarights.dialect.AbstractDialect;
import com.yun.sys.modules.rights.model.$do.PermissionRuleDO;
import com.yun.common.spring.security.SecurityUtils;
import com.yun.sys.modules.rights.datarights.parser.DruidSQLParserProcessor;
import org.apache.ibatis.mapping.BoundSql;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MySqlDialect extends AbstractDialect {

    @Override
    public String getPermissionSqlForSelect(BoundSql boundSql, Set<PermissionRuleDO> rules) {
        String originalSql = boundSql.getSql();
        if (CollectionUtil.isEmpty(rules)) {
            return originalSql;
        }
        Map<String, Set<PermissionRuleDO>> tableRuleMap = toTableRuleMap(rules);
        // 单表处理
        String[] tableNames = DruidSQLParserProcessor.getTableNamesForm(originalSql, JdbcConstants.MYSQL);
        if (tableNames.length == 1) {
            Set<PermissionRuleDO> set = tableRuleMap.get(tableNames[0]);
            if (CollectionUtil.isNotEmpty(set)) {
                String condition = String.join(" and ", set.stream().map(PermissionRuleDO::getRuleExps).collect(Collectors.toSet()));
                return DruidSQLParserProcessor.addConditionForSingleTableSQL(originalSql, condition, JdbcConstants.MYSQL);
            }
        }
        //TODO 非单表，复杂 SQL处理
        return originalSql;
    }


    @Override
    public String getPermissionSqlForUpdate(BoundSql boundSql, Set<PermissionRuleDO> rules) {
        //获取到原始sql语句
        String sql = boundSql.getSql();
        /*仅有创建者才能修改成功*/
        if (sql.toLowerCase().contains("where")) {
            String prefix = findColumnPrefix(sql);
            sql += " and " + prefix + "_creator = '" + SecurityUtils.getCurrentUsername() + "'";
        }
        return sql;
    }
}
