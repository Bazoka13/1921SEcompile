// Generated from D:/2021SEwork/Compile/src/frontend\sysy.g4 by ANTLR 4.9.1
package frontend;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link sysyParser}.
 */
public interface sysyListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link sysyParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(sysyParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(sysyParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#compUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompUnit(sysyParser.CompUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#compUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompUnit(sysyParser.CompUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void enterFuncDef(sysyParser.FuncDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void exitFuncDef(sysyParser.FuncDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#funcType}.
	 * @param ctx the parse tree
	 */
	void enterFuncType(sysyParser.FuncTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#funcType}.
	 * @param ctx the parse tree
	 */
	void exitFuncType(sysyParser.FuncTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(sysyParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(sysyParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(sysyParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(sysyParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(sysyParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(sysyParser.NumberContext ctx);
}