package frontend;

import org.antlr.v4.runtime.RecognitionException;

import java.math.BigInteger;
import java.util.*;

public class Visitor extends sysyBaseVisitor<Void> {
    private int sonAns;//计算表达式用于子树求值
    private String sonRam;//子树存储器，参考上方
    private int num=0;//getMpId的自增长ID
    private int now;//当前visit函数的ID
    private boolean sonIsRam;
    private HashMap<String,Integer> iniAd = new HashMap<>();//每个函数的目前可用寄存器，自增长
    private HashMap<String,Integer> getMpId = new HashMap<>();//注意-1 ，每个函数对应HashMap在下面两个List的位置
    private List<HashMap<String, String>> idToAd = new ArrayList<>();//标识符到寄存器的映射
    private List<HashMap<Integer,Integer>> adToVal = new ArrayList<>();//寄存器的value
    private ArrayList<ArrayList<String>> tmpRam= new ArrayList<ArrayList<String>>();//每个函数的所有寄存器
    private ArrayList<ArrayList<String>> needAllocaRam = new ArrayList<ArrayList<String>>();//每个函数需要alloca的寄存器
    private ArrayList<ArrayList<String>> irList = new ArrayList<ArrayList<String>>();//每个函数的所有ir
    private void addIR(String s){
        int tmp=now-1;
        irList.get(tmp).add(s);
    }//偷懒写个函数
    private String randomRam(){
        Random df = new Random();
        int n=df.nextInt(10)+1;
        String str = "%";
        for (int i = 0; i < n; i++) {
            str = str + (char)(Math.random()*26+'a');
        }
        Integer i=n;
        return str+i.toString();
    }
    @Override public Void visitCompUnit(sysyParser.CompUnitContext ctx) {
        try {
            visitChildren(ctx);
            Iterator ite = getMpId.entrySet().iterator();
            while(ite.hasNext()){
                HashMap.Entry entry = (HashMap.Entry) ite.next();
                Integer tmpi= (Integer) entry.getValue();
                ArrayList<String> t=needAllocaRam.get(tmpi.intValue()-1);
                for(String s:t){
                    System.out.println(s+" = alloca i32");
                }
                ArrayList<String> tmps=irList.get(tmpi.intValue()-1);
                for(String s:tmps){
                    System.out.print(s);
                }
            }
        }catch (RecognitionException re){
            System.exit(-1);
        }
        return null;
    }
    @Override public Void visitFuncType(sysyParser.FuncTypeContext ctx) {
        String s = "define dso_local ";
        if(ctx.INT_KW()!=null){
            s+="i32 ";
        }else if(ctx.VOID_KW()!=null){
            s+="i1 ";
        }
        addIR(s);
        return null;
    }
    @Override public Void visitFuncDef(sysyParser.FuncDefContext ctx) {
        String tmp = ctx.IDENT().getText();
        int pre=now;
        if(getMpId.containsKey(tmp)){
            now=getMpId.get(tmp).intValue();
        }else{
            getMpId.put(tmp,Integer.valueOf(++num));
            now=getMpId.get(tmp).intValue();
            tmpRam.add(new ArrayList<String>());
            needAllocaRam.add(new ArrayList<String>());
            irList.add(new ArrayList<String>());
        }
        try {
            visitFuncType(ctx.funcType());
        }catch (RecognitionException re){
            System.exit(-1);
        }
        addIR('@'+ctx.IDENT().getText()+"()");
        try {
            visitBlock(ctx.block());
        }catch (RecognitionException re){
            System.exit(-1);
        }
        now=pre;
        return null;
    }
    @Override public Void visitIntConst(sysyParser.IntConstContext ctx) {
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
        return null;
    }
    @Override public Void visitBlock(sysyParser.BlockContext ctx) {
        if(ctx.L_BRACE()!=null) {
            addIR("{\n");
        }
        try {
            visitChildren(ctx);
        }catch (RecognitionException re){
            System.exit(-1);
        }
        if(ctx.R_BRACE()!=null) {
            addIR("}\n");
        }
        return null;
    }
    @Override public Void visitStmt(sysyParser.StmtContext ctx) {

        return super.visitStmt(ctx);
    }
    @Override public Void visitReturnStmt(sysyParser.ReturnStmtContext ctx) {
        //System.out.print("ret i32 ");
        visitExp(ctx.exp());
        //System.out.print(sonAns);
        addIR("ret i32 ");
        if(sonIsRam)addIR(sonRam+'\n');
        else addIR(sonAns+"\n");
        return null;
    }
    @Override public Void visitAddExp(sysyParser.AddExpContext ctx) {
        int n=ctx.mulExp().size();
        int tmp=0;
        int fl=0;
        int fls=0;
        String preSon="null";
        for (int i=0;i<n;i++) {
            sonIsRam = false;
            visitMulExp(ctx.mulExp(i));
            if (!sonIsRam) {
                if (fl == 0) {
                    fl = 1;
                    tmp = sonAns;
                }
            } else {
                if (fls == 0) {
                    fls = 1;
                    preSon = sonRam;
                }
            }
            if (i == 0) continue;
            if (ctx.addOp(i - 1).MINUS() != null) {
                if (sonIsRam) {
                    if(preSon=="null")System.exit(-1);
                    String newSon=randomRam();
                    addIR(newSon+" = sub i32 "+preSon+sonRam);
                    preSon=newSon;
                } else {
                    if(preSon!="null"){
                        String newSon=randomRam();
                        addIR(newSon+" = sub i32 "+preSon+sonAns);
                        preSon=newSon;
                    }else tmp = tmp - sonAns;
                }
            } else if (ctx.addOp(i - 1).PLUS() != null) {
                if (sonIsRam) {
                    if(preSon=="null")System.exit(-1);
                    String newSon=randomRam();
                    addIR(newSon+" = add i32 "+preSon+sonRam);
                    preSon=newSon;
                } else {
                    if(preSon!="null"){
                        String newSon=randomRam();
                        addIR(newSon+" = add i32 "+preSon+sonAns);
                        preSon=newSon;
                    }else tmp = tmp + sonAns;
                }
            } else {
                System.exit(-1);
            }
        }
        if(preSon!="null"){
            sonRam=preSon;
            sonIsRam=true;
            return null;
        }
        sonAns=tmp;
        return null;
    }
    @Override public Void visitMulExp(sysyParser.MulExpContext ctx) {
        int n = ctx.unaryExp().size();
        int tmp=0;
        int fl=0;
        int fls=0;
        String preSon="null";
        for(int i=0;i<n;i++) {
            sonIsRam = false;
            visitUnaryExp(ctx.unaryExp(i));
            if(!sonIsRam){
                if(fl==0){
                    fl=1;
                    tmp=sonAns;
                }
            }else{
                if(fls==0){
                    fls=1;
                    preSon=sonRam;
                }
            }
            if(i==0)continue;
            if (ctx.mulOp(i - 1).DIV() != null) {
                if (sonIsRam) {
                    String newSon=randomRam();
                    addIR(newSon+" = sdiv i32 "+preSon+sonRam);
                    preSon=newSon;
                } else {
                    if(preSon!="null"){
                        String newSon=randomRam();
                        addIR(newSon+" = sdiv i32 "+preSon+sonAns);
                        preSon=newSon;
                    }else tmp = tmp / sonAns;
                }
            } else if (ctx.mulOp(i - 1).MOD() != null) {
                if (sonIsRam) {
                    String newSon=randomRam();
                    addIR(newSon+" = srem i32 "+preSon+sonRam);
                    preSon=newSon;
                } else {
                    if(preSon!="null"){
                        String newSon=randomRam();
                        addIR(newSon+" = srem i32 "+preSon+sonAns);
                        preSon=newSon;
                    }else tmp = tmp % sonAns;
                }
            } else if (ctx.mulOp(i - 1).MUL() != null) {
                if (sonIsRam) {
                    String newSon=randomRam();
                    addIR(newSon+" = smul i32 "+preSon+sonRam);
                    preSon=newSon;
                } else {
                    if(preSon!="null"){
                        String newSon=randomRam();
                        addIR(newSon+" = smul i32 "+preSon+sonAns);
                        preSon=newSon;
                    }else tmp = tmp * sonAns;
                }
            } else {
                System.exit(-1);
            }
        }
        if(preSon!="null"){
            sonRam=preSon;
            sonIsRam=true;
            return null;
        }
        sonAns=tmp;
        return null;
    }
    @Override public Void visitUnaryExp(sysyParser.UnaryExpContext ctx) {
        if(ctx.unaryOp()!=null){
            visitUnaryExp(ctx.unaryExp());
            if(ctx.unaryOp().MINUS()!=null){
                if(!sonIsRam)sonAns=-sonAns;
                else{
                    String newRam = randomRam();
                    addIR(newRam+" = smul i32 -1, "+sonRam);
                    sonRam=newRam;
                }
            }
        }else if(ctx.primaryExp()!=null){
            visitPrimaryExp(ctx.primaryExp());
        }else if(ctx.callee()!=null)visitCallee(ctx.callee());
        return null;
    }

    @Override public Void visitPrimaryExp(sysyParser.PrimaryExpContext ctx){
        if(ctx.lVal()!=null){
            if(idToAd.get(now-1).containsKey(ctx.lVal().IDENT().getText())){
                sonRam=idToAd.get(now-1).get(ctx.lVal().IDENT().getText());
                if(!adToVal.get(now-1).containsKey(sonRam))System.exit(-1);
                sonIsRam=true;
            }else{
                /*sonRam=randomRam();
                idToAd.get(now).put(ctx.lVal().getText(),sonRam);*/
                System.exit(-1);
            }
        }else if(ctx.number()!=null){
            sonIsRam=false;
            visitNumber(ctx.number());
        }else{
            visitChildren(ctx);
        }
        return null;
    }

}
