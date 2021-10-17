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
	 * Visit a parse tree produced by {@link sysyParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(sysyParser.DeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#constDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDecl(sysyParser.ConstDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#bType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBType(sysyParser.BTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#constDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDef(sysyParser.ConstDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#constInitVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstInitVal(sysyParser.ConstInitValContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(sysyParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#varDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDef(sysyParser.VarDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#initVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitVal(sysyParser.InitValContext ctx);
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
	 * Visit a parse tree produced by {@link sysyParser#funcFParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncFParams(sysyParser.FuncFParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#funcFParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncFParam(sysyParser.FuncFParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(sysyParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#blockItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockItem(sysyParser.BlockItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(sysyParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#assignStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStmt(sysyParser.AssignStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#expStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpStmt(sysyParser.ExpStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#conditionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionStmt(sysyParser.ConditionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#whileStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(sysyParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#breakStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(sysyParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#continueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStmt(sysyParser.ContinueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#returnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(sysyParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp(sysyParser.ExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(sysyParser.CondContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#lVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLVal(sysyParser.LValContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExp(sysyParser.PrimaryExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(sysyParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#intConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntConst(sysyParser.IntConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#unaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExp(sysyParser.UnaryExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#callee}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallee(sysyParser.CalleeContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#unaryOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(sysyParser.UnaryOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#funcRParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncRParams(sysyParser.FuncRParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(sysyParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#mulExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExp(sysyParser.MulExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#mulOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulOp(sysyParser.MulOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#addExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExp(sysyParser.AddExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#addOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddOp(sysyParser.AddOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#relExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelExp(sysyParser.RelExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#relOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelOp(sysyParser.RelOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#eqExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExp(sysyParser.EqExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#eqOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqOp(sysyParser.EqOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#lAndExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLAndExp(sysyParser.LAndExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#lOrExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLOrExp(sysyParser.LOrExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link sysyParser#constExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstExp(sysyParser.ConstExpContext ctx);
}