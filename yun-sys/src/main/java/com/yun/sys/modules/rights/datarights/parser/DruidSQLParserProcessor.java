package com.yun.sys.modules.rights.datarights.parser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

public final class DruidSQLParserProcessor {

    public static String addConditionForSingleTableSQL(String sql, String condition, String dialect) {
        if(StringUtils.hasText(condition)){
            return SQLUtils.addCondition(sql, condition, dialect);
        }
        return sql;
    }

    public static String[] getTableNamesForm(String sql, String dialect) {
        List<SQLStatement> sqlStatementList = getSQLStatementList(SQLUtils.format(sql, dialect));
        //默认为一条sql语句
        SQLStatement stmt = sqlStatementList.get(0);
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        stmt.accept(visitor);
        Set<TableStat.Name> tableNames = visitor.getTables().keySet();
        return tableNames.stream().map(TableStat.Name::getName).toArray(String[]::new);
    }

    private static List<SQLStatement> getSQLStatementList(String sql) {
        String dbType = JdbcConstants.MYSQL;
        return SQLUtils.parseStatements(sql, dbType);
    }
}
