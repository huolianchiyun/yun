package com.hlcy.yun.antlr.scala.sql.mysql.druid;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

public class SQLParser {
    public static void main(String[] args) {
        test();
    }
    public static SQLStatement parser(String sql, String dbType) throws SQLSyntaxErrorException {
        List<SQLStatement> list = SQLUtils.parseStatements(sql, dbType);
        if (list.size() > 1) {
            throw new SQLSyntaxErrorException("MultiQueries is not supported,use single query instead ");
        }
        return list.get(0);
    }

    private static List<SQLStatement> getSQLStatementList(String sql) {
        String dbType = JdbcConstants.MYSQL;
        return SQLUtils.parseStatements(sql, dbType);
    }

    private static void test() {
        String sql = "select age a,name n from student s inner join (select id,name from score where sex='女') temp on sex='男' " +
                "and temp.id in(select id from score where sex='男') where student.name='zhangsan' group by student.age order by student.id ASC;";
        System.out.println("SQL语句为：" + sql);
        //格式化输出
        String result = SQLUtils.format(sql, JdbcConstants.MYSQL);
        System.out.println("格式化后输出：\n" + result);
        System.out.println("*********************");
        List<SQLStatement> sqlStatementList = getSQLStatementList(sql);
        //默认为一条sql语句
        SQLStatement stmt = sqlStatementList.get(0);
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        stmt.accept(visitor);
        System.out.println("数据库类型\t\t" + visitor.getDbType());

        //获取字段名称
        System.out.println("查询的字段\t\t" + visitor.getColumns());

        //获取表名称
        System.out.println("表名\t\t\t" + visitor.getTables().keySet());

        System.out.println("条件\t\t\t" + visitor.getConditions());

        System.out.println("group by\t\t" + visitor.getGroupByColumns());

        System.out.println("order by\t\t" + visitor.getOrderByColumns());
    }

    public static void test1() throws SQLSyntaxErrorException {
        String sql = "select name, id from acct where id =10";
        String dbType = "mysql";
        System.out.println("原始SQL 为 ： " + sql);
        SQLSelectStatement statement = (SQLSelectStatement) parser(sql, dbType);
        SQLSelect select = statement.getSelect();
        SQLSelectQueryBlock query = (SQLSelectQueryBlock) select.getQuery();
        SQLExprTableSource tableSource = (SQLExprTableSource) query.getFrom();
        String tableName = tableSource.getExpr().toString();
        System.out.println("获取的表名为  tableName ：" + tableName);
        //修改表名为acct_1
        tableSource.setExpr("acct_1");
        System.out.println("修改表名后的SQL 为 ： [" + statement.toString() + "]");
    }
}

