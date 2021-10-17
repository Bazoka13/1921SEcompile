package frontend;

import org.antlr.v4.runtime.RecognitionException;

import java.math.BigInteger;

public class Visitor extends sysyBaseVisitor<Void> {
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
        String s = "i32 ";
        if(ctx.DECIMAL_CONST()!=null){
            int tmp = (new BigInteger(ctx.DECIMAL_CONST().getText(),10).intValue());
            //System.out.print(tmp);
            s+=tmp;
        }else if(ctx.OCTAL_CONST()!=null){
            int tmp = (new BigInteger(ctx.OCTAL_CONST().getText(),8).intValue());
            s+=tmp;
            //System.out.print(tmp);
        }else if(ctx.HEXADECIMAL_CONST()!=null){
            int tmp = (new BigInteger(ctx.HEXADECIMAL_CONST().getText().substring(2),16).intValue());
            s+=tmp;
            //System.out.print(tmp);
        }
        System.out.print(s);
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
        return null;
    }



}
