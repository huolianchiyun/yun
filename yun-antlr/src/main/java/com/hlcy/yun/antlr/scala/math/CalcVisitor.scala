package com.hlcy.yun.antlr.scala.math

import scala.collection.mutable

/**
 * 实现计算功能的访问器类
 */
class CalcVisitor extends MathBaseVisitor[Int] {
    // 模拟计算器的内存，存放 "变量名->值" 的映射，即在赋值时候往这里写
    private val memory = new mutable.HashMap[String, Int]()

    // 访问表达式语句：expr NEWLINE
    override def visitPrintExpr(ctx: MathParser.PrintExprContext): Int = {
        val value: Int = visit(ctx.expr()) // 对表达式访问求值
        println(value)
        0 // 反正用不到这个返回值，这里返回假值
    }

    // 访问赋值语句：ID '=' expr NEWLINE
    override def visitAssign(ctx: MathParser.AssignContext): Int = {
        val id: String = ctx.ID().getText // 获取左值标识符
        val value: Int = visit(ctx.expr()) // 对右值表达式访问求值
        memory.put(id, value)
        value
    }

    // 访问表达式加括号：'(' expr ')'
    override def visitParens(ctx: MathParser.ParensContext): Int = visit(ctx.expr) // 其实就是把括号里表达式的值算出来返回

    // 访问乘除法表达式：expr op=('*'|'/') expr
    override def visitMulDiv(ctx: MathParser.MulDivContext): Int = {
        val left: Int = visit(ctx.expr(0)) // 被除数，或乘法因子1
        val right: Int = visit(ctx.expr(1)) // 除数，或乘法因子2
        if (ctx.op.getType == MathParser.MUL) {
            left * right
        } else {
            left / right
        }
    }

    // 访问加减法表达式：expr op=('+'|'-') expr
    override def visitAddSub(ctx: MathParser.AddSubContext): Int = {
        val left: Int = visit(ctx.expr(0))
        val right: Int = visit(ctx.expr(1))
        if (ctx.op.getType == MathParser.ADD) {
            left + right
        } else {
            left - right
        }
    }

    // 访问单个标识符构成的表达式：ID
    override def visitId(ctx: MathParser.IdContext): Int = {
        val id: String = ctx.ID.getText // 获取标识符名字
        memory.getOrElse(id, 0)
    }

    // 访问单个整数构成的表达式：INT
    override def visitInt(ctx: MathParser.IntContext): Int = Integer.valueOf(ctx.INT().getText)
}

