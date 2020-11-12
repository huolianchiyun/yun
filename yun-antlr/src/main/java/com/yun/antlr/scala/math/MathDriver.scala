package com.yun.antlr.scala.math

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree

object MathDriver {
    def main(args: Array[String]): Unit = {
//        val input: CharStream = CharStreams.fromString("12*2+12\r\n")
        val input: CharStream = CharStreams.fromString("a=2*(3+4)-5\nb=2\na+b\n")
        // 词法分析->Token流->生成语法分析器对象
        val lexer: MathLexer = new MathLexer(input)
        val tokens: CommonTokenStream = new CommonTokenStream(lexer)
        val parser: MathParser = new MathParser(tokens)
        // 启动语法分析，获取语法树(根节点)
        val tree: ParseTree = parser.prog()
        System.out.println(tree.toStringTree(parser))
        val visitor = new CalcVisitor()
        // 访问这棵语法树，在访问同时即可进行计算获取结果
        visitor.visit(tree);
    }
}
