package frontend;

import org.antlr.v4.runtime.RecognitionException;

import java.math.BigInteger;

public class Visitor extends sysyBaseVisitor<Void> {
    private int sonAns;
    @Override public Void visitCompUnit(sysyParser.CompUnitContext ctx) {
        try {
            visitChildren(ctx);
        }catch (RecognitionException re){
            System.exit(-1);
        }
        return null;
    }

    @Override public Void visitFuncType(sysyParser.FuncTypeContext ctx) {
        System.out.print("define dso_local ");
        if(ctx.INT_KW()!=null){
            System.out.print("i32 ");
        }else if(ctx.VOID_KW()!=null){
            System.out.print("i1 ");
        }
        return null;
    }
    @Override public Void visitFuncDef(sysyParser.FuncDefContext ctx) {
        try {
            visitFuncType(ctx.funcType());
        }catch (RecognitionException re){
            System.exit(-1);
        }
        System.out.print('@'+ctx.IDENT().getText()+"()");
        try {
            visitBlock(ctx.block());
        }catch (RecognitionException re){
            System.exit(-1);
        }
        return null;
    }
    @Override public Void visitIntConst(sysyParser.IntConstContext ctx) {
        //String s = "i32 ";
        if(ctx.DECIMAL_CONST()!=null){
            int tmp = (new BigInteger(ctx.DECIMAL_CONST().getText(),10).intValue());
            sonAns=tmp;
        }else if(ctx.OCTAL_CONST()!=null){
            int tmp = (new BigInteger(ctx.OCTAL_CONST().getText(),8).intValue());
            sonAns=tmp;
        }else if(ctx.HEXADECIMAL_CONST()!=null){
            int tmp = (new BigInteger(ctx.HEXADECIMAL_CONST().getText().substring(2),16).intValue());
            sonAns=tmp;
        }
        //System.out.print(s);
        return null;
    }
    @Override public Void visitBlock(sysyParser.BlockContext ctx) {
        if(ctx.L_BRACE()!=null) {
            System.out.print("{\n");
        }
        try {
            visitChildren(ctx);
        }catch (RecognitionException re){
            System.exit(-1);
        }
        if(ctx.R_BRACE()!=null) {
            System.out.print("\n}");
        }
        return null;
    }
    @Override public Void visitStmt(sysyParser.StmtContext ctx) {
        return super.visitStmt(ctx);
    }
    @Override public Void visitReturnStmt(sysyParser.ReturnStmtContext ctx) {
        System.out.print("ret ");
        visitExp(ctx.exp());
        System.out.print(sonAns);
        return null;
    }
    @Override public Void visitAddExp(sysyParser.AddExpContext ctx) {
        int n=ctx.mulExp().size();
        int tmp=0;
        for (int i=0;i<n;i++){
            visitMulExp(ctx.mulExp(i));
            if(i==0)tmp=sonAns;
            else{
                if(ctx.addOp(i-1).MINUS()!=null){
                    tmp=tmp-sonAns;
                }else if (ctx.addOp(i-1).PLUS()!=null){
                    tmp=tmp+sonAns;
                }else{
                    System.exit(-1);
                }
            }
        }
        sonAns=tmp;
        return null;
    }
    @Override public Void visitMulExp(sysyParser.MulExpContext ctx) {
        int n = ctx.unaryExp().size();
        int tmp=0;
        for(int i=0;i<n;i++){
            visitUnaryExp(ctx.unaryExp(i));
            if(i==0)tmp=sonAns;
            else{
                if(ctx.mulOp(i-1).DIV()!=null){
                    if(sonAns==0)System.exit(-1);
                    tmp=tmp/sonAns;
                }else if (ctx.mulOp(i-1).MOD()!=null){
                    if(sonAns==0)System.exit(-1);
                    tmp=tmp%sonAns;
                }else if(ctx.mulOp(i-1).MUL()!=null){
                    tmp=tmp*sonAns;
                }else{
                    System.exit(-1);
                }
            }
        }
        sonAns=tmp;
        return null;
    }
    @Override public Void visitUnaryExp(sysyParser.UnaryExpContext ctx) {
        if(ctx.unaryOp()!=null){
            visitUnaryExp(ctx.unaryExp());
            if(ctx.unaryOp().MINUS()!=null){
                sonAns=-sonAns;
            }
        }else{
            visitChildren(ctx);
        }
        return null;
    }



}
