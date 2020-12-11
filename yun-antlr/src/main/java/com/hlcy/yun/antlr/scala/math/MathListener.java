// Generated from E:/workspace/yun/yun-antlr/src/main/resources\Math.g4 by ANTLR 4.8
package com.hlcy.yun.antlr.scala.math;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MathParser}.
 */
public interface MathListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MathParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(MathParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(MathParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code printExpr}
	 * labeled alternative in {@link MathParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterPrintExpr(MathParser.PrintExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printExpr}
	 * labeled alternative in {@link MathParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitPrintExpr(MathParser.PrintExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assign}
	 * labeled alternative in {@link MathParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssign(MathParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link MathParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssign(MathParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blank}
	 * labeled alternative in {@link MathParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterBlank(MathParser.BlankContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blank}
	 * labeled alternative in {@link MathParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitBlank(MathParser.BlankContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parens}
	 * labeled alternative in {@link MathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParens(MathParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link MathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParens(MathParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link MathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(MathParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link MathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(MathParser.MulDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link MathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(MathParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link MathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(MathParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code id}
	 * labeled alternative in {@link MathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterId(MathParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code id}
	 * labeled alternative in {@link MathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitId(MathParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code int}
	 * labeled alternative in {@link MathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInt(MathParser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code int}
	 * labeled alternative in {@link MathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInt(MathParser.IntContext ctx);
}
