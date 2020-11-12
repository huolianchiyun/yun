package com.yun.antlr.scala.sql.mysql.antlr4.parser.driver

import com.yun.antlr.scala.sql.mysql.antlr4.parser.{MySqlLexer, MySqlParser, MySqlParserBaseVisitor}
import com.zhangbin.yun.antlr.scala.sql.mysql.antlr4.parser.{MySqlLexer, MySqlParserBaseVisitor}
import org.antlr.v4.runtime.{CharStreams, CodePointCharStream, CommonTokenStream}

object MysqlParserDriver {
    def main(args: Array[String]): Unit = {
        val sql: String = "SELECT * FROM USER"
        val stream: CodePointCharStream  = CharStreams.fromString(sql)
        val lexer = new MySqlLexer(stream)
        val tokens = new CommonTokenStream(lexer)
        val parser = new MySqlParser(tokens)
        // 获取语法树根节点
        val tree: MySqlParser.RootContext = parser.root()
//        System.out.println(tree.toStringTree(parser))
        val visitor = new MySqlParserBaseVisitor[String]
        val statementContext: MySqlParser.SqlStatementContext = parser.sqlStatement()
        println(visitor.visitSqlStatement(statementContext))
    }
}
