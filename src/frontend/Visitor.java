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
        System.out.print("define dso_local i32 ");
        try {
            visitChildren(ctx);
        }catch (RecognitionException re){
            System.exit(-1);
        }
        return null;
    }
    @Override public Void visitFuncDef(sysyParser.FuncDefContext ctx) {
        try {
            visitFuncType(ctx.funcType());
        }catch (RecognitionException re){
            System.exit(-1);
        }
        System.out.print('@'+ctx.Ident().getText()+"()");
        try {
            visitBlock(ctx.block());
        }catch (RecognitionException re){
            System.exit(-1);
        }
        return null;
    }
    @Override public Void visitNumber(sysyParser.NumberContext ctx) {
        System.out.print("i32 ");
        if(ctx.DECIMAL_CONST()!=null){
            int tmp = (new BigInteger(ctx.DECIMAL_CONST().getText(),10).intValue());
            System.out.print(tmp);
        }else if(ctx.OCTAL_CONST()!=null){
            int tmp = (new BigInteger(ctx.DECIMAL_CONST().getText(),8).intValue());
            System.out.print(tmp);
        }else{
            int tmp = (new BigInteger(ctx.DECIMAL_CONST().getText().substring(2),16).intValue());
            System.out.print(tmp);
        }
        return null;
    }
    @Override public Void visitBlock(sysyParser.BlockContext ctx) {
        System.out.print("{\n");
        try {
            visitChildren(ctx);
        }catch (RecognitionException re){
            System.exit(-1);
        }
        System.out.print("\n}");
        return null;
    }
    @Override public Void visitStmt(sysyParser.StmtContext ctx) {
        System.out.print("ret ");
        try {
            visitChildren(ctx);
        }catch (RecognitionException re){
            System.exit(-1);
        }
        return null;
    }



}
