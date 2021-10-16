// Generated from D:/2021SEwork/Compile/src/frontend\sysy.g4 by ANTLR 4.9.1
package frontend;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link sysyParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface sysyVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link sysyParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(sysyParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#compUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompUnit(sysyParser.CompUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#funcDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDef(sysyParser.FuncDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#funcType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncType(sysyParser.FuncTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(sysyParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(sysyParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(sysyParser.NumberContext ctx);
}