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
	 * Enter a parse tree produced by {@link sysyParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(sysyParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(sysyParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#constDecl}.
	 * @param ctx the parse tree
	 */
	void enterConstDecl(sysyParser.ConstDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#constDecl}.
	 * @param ctx the parse tree
	 */
	void exitConstDecl(sysyParser.ConstDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#bType}.
	 * @param ctx the parse tree
	 */
	void enterBType(sysyParser.BTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#bType}.
	 * @param ctx the parse tree
	 */
	void exitBType(sysyParser.BTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#constDef}.
	 * @param ctx the parse tree
	 */
	void enterConstDef(sysyParser.ConstDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#constDef}.
	 * @param ctx the parse tree
	 */
	void exitConstDef(sysyParser.ConstDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#constInitVal}.
	 * @param ctx the parse tree
	 */
	void enterConstInitVal(sysyParser.ConstInitValContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#constInitVal}.
	 * @param ctx the parse tree
	 */
	void exitConstInitVal(sysyParser.ConstInitValContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(sysyParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(sysyParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#varDef}.
	 * @param ctx the parse tree
	 */
	void enterVarDef(sysyParser.VarDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#varDef}.
	 * @param ctx the parse tree
	 */
	void exitVarDef(sysyParser.VarDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#initVal}.
	 * @param ctx the parse tree
	 */
	void enterInitVal(sysyParser.InitValContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#initVal}.
	 * @param ctx the parse tree
	 */
	void exitInitVal(sysyParser.InitValContext ctx);
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
	 * Enter a parse tree produced by {@link sysyParser#funcFParams}.
	 * @param ctx the parse tree
	 */
	void enterFuncFParams(sysyParser.FuncFParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#funcFParams}.
	 * @param ctx the parse tree
	 */
	void exitFuncFParams(sysyParser.FuncFParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#funcFParam}.
	 * @param ctx the parse tree
	 */
	void enterFuncFParam(sysyParser.FuncFParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#funcFParam}.
	 * @param ctx the parse tree
	 */
	void exitFuncFParam(sysyParser.FuncFParamContext ctx);
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
	 * Enter a parse tree produced by {@link sysyParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void enterBlockItem(sysyParser.BlockItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void exitBlockItem(sysyParser.BlockItemContext ctx);
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
	 * Enter a parse tree produced by {@link sysyParser#assignStmt}.
	 * @param ctx the parse tree
	 */
	void enterAssignStmt(sysyParser.AssignStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#assignStmt}.
	 * @param ctx the parse tree
	 */
	void exitAssignStmt(sysyParser.AssignStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#expStmt}.
	 * @param ctx the parse tree
	 */
	void enterExpStmt(sysyParser.ExpStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#expStmt}.
	 * @param ctx the parse tree
	 */
	void exitExpStmt(sysyParser.ExpStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#conditionStmt}.
	 * @param ctx the parse tree
	 */
	void enterConditionStmt(sysyParser.ConditionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#conditionStmt}.
	 * @param ctx the parse tree
	 */
	void exitConditionStmt(sysyParser.ConditionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(sysyParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(sysyParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#breakStmt}.
	 * @param ctx the parse tree
	 */
	void enterBreakStmt(sysyParser.BreakStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#breakStmt}.
	 * @param ctx the parse tree
	 */
	void exitBreakStmt(sysyParser.BreakStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#continueStmt}.
	 * @param ctx the parse tree
	 */
	void enterContinueStmt(sysyParser.ContinueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#continueStmt}.
	 * @param ctx the parse tree
	 */
	void exitContinueStmt(sysyParser.ContinueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(sysyParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(sysyParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp(sysyParser.ExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp(sysyParser.ExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(sysyParser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(sysyParser.CondContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#lVal}.
	 * @param ctx the parse tree
	 */
	void enterLVal(sysyParser.LValContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#lVal}.
	 * @param ctx the parse tree
	 */
	void exitLVal(sysyParser.LValContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExp(sysyParser.PrimaryExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExp(sysyParser.PrimaryExpContext ctx);
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
	/**
	 * Enter a parse tree produced by {@link sysyParser#intConst}.
	 * @param ctx the parse tree
	 */
	void enterIntConst(sysyParser.IntConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#intConst}.
	 * @param ctx the parse tree
	 */
	void exitIntConst(sysyParser.IntConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#unaryExp}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExp(sysyParser.UnaryExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#unaryExp}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExp(sysyParser.UnaryExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#callee}.
	 * @param ctx the parse tree
	 */
	void enterCallee(sysyParser.CalleeContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#callee}.
	 * @param ctx the parse tree
	 */
	void exitCallee(sysyParser.CalleeContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#unaryOp}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOp(sysyParser.UnaryOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#unaryOp}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOp(sysyParser.UnaryOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#funcRParams}.
	 * @param ctx the parse tree
	 */
	void enterFuncRParams(sysyParser.FuncRParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#funcRParams}.
	 * @param ctx the parse tree
	 */
	void exitFuncRParams(sysyParser.FuncRParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(sysyParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(sysyParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#mulExp}.
	 * @param ctx the parse tree
	 */
	void enterMulExp(sysyParser.MulExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#mulExp}.
	 * @param ctx the parse tree
	 */
	void exitMulExp(sysyParser.MulExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#mulOp}.
	 * @param ctx the parse tree
	 */
	void enterMulOp(sysyParser.MulOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#mulOp}.
	 * @param ctx the parse tree
	 */
	void exitMulOp(sysyParser.MulOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#addExp}.
	 * @param ctx the parse tree
	 */
	void enterAddExp(sysyParser.AddExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#addExp}.
	 * @param ctx the parse tree
	 */
	void exitAddExp(sysyParser.AddExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#addOp}.
	 * @param ctx the parse tree
	 */
	void enterAddOp(sysyParser.AddOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#addOp}.
	 * @param ctx the parse tree
	 */
	void exitAddOp(sysyParser.AddOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#relExp}.
	 * @param ctx the parse tree
	 */
	void enterRelExp(sysyParser.RelExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#relExp}.
	 * @param ctx the parse tree
	 */
	void exitRelExp(sysyParser.RelExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#relOp}.
	 * @param ctx the parse tree
	 */
	void enterRelOp(sysyParser.RelOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#relOp}.
	 * @param ctx the parse tree
	 */
	void exitRelOp(sysyParser.RelOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#eqExp}.
	 * @param ctx the parse tree
	 */
	void enterEqExp(sysyParser.EqExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#eqExp}.
	 * @param ctx the parse tree
	 */
	void exitEqExp(sysyParser.EqExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#eqOp}.
	 * @param ctx the parse tree
	 */
	void enterEqOp(sysyParser.EqOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#eqOp}.
	 * @param ctx the parse tree
	 */
	void exitEqOp(sysyParser.EqOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#lAndExp}.
	 * @param ctx the parse tree
	 */
	void enterLAndExp(sysyParser.LAndExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#lAndExp}.
	 * @param ctx the parse tree
	 */
	void exitLAndExp(sysyParser.LAndExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#lOrExp}.
	 * @param ctx the parse tree
	 */
	void enterLOrExp(sysyParser.LOrExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#lOrExp}.
	 * @param ctx the parse tree
	 */
	void exitLOrExp(sysyParser.LOrExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link sysyParser#constExp}.
	 * @param ctx the parse tree
	 */
	void enterConstExp(sysyParser.ConstExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link sysyParser#constExp}.
	 * @param ctx the parse tree
	 */
	void exitConstExp(sysyParser.ConstExpContext ctx);
}