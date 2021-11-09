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
    private HashMap<String,Integer> getMpId = new HashMap<>();//注意-1 ，每个函数对应HashMap在下面两个List的位置
    private List<HashMap<String, String>> idToAd = new ArrayList<>();//标识符到寄存器的映射
    private ArrayList<ArrayList<String>> tmpRam= new ArrayList<>();//每个函数的所有寄存器
    private ArrayList<ArrayList<String>> needAllocaRam = new ArrayList<>();//每个函数需要alloca的寄存器
    private ArrayList<ArrayList<String>> irList = new ArrayList<>();//每个函数的所有ir
    private ArrayList<ArrayList<String>> defList = new ArrayList<>();//每个block
    private ArrayList<HashMap<String,String>> randRam=new ArrayList<>();
    private void addIR(String s){
        int tmp=now-1;
        irList.get(tmp).add(s);
    }//偷懒写个函数
    private String getRandRam(){
        Random df = new Random();
        int n=df.nextInt(10)+1;
        String str = "%";
        for (int i = 0; i < n; i++) {
            str = str + (char)(Math.random()*26+'a');
        }
        df = new Random();
        n=df.nextInt(20)+1;
        Integer i=n;
        return str+i.toString();
    }
    private String randomRam(){
        String tmps=getRandRam();
        while((randRam.get(now-1).containsKey(tmps))){
            tmps=getRandRam();
        }
        randRam.get(now-1).put(tmps,"null");
        return tmps;
    }
    @Override public Void visitCompUnit(sysyParser.CompUnitContext ctx) {
        try {
            visitChildren(ctx);
            Iterator ite = getMpId.entrySet().iterator();
            while(ite.hasNext()){
                HashMap.Entry entry = (HashMap.Entry) ite.next();
                Integer tmpi= (Integer) entry.getValue();
                ArrayList<String> t=needAllocaRam.get(tmpi.intValue()-1);
                ArrayList<String> tmps=irList.get(tmpi.intValue()-1);
                System.out.print(tmps.get(0));
                System.out.print(tmps.get(1));
                for(String s:t){
                    System.out.println(s+" = alloca i32");
                }
                for(int i=2;i<tmps.size();i++){
                    String s=tmps.get(i);
                    System.out.print(s);
                }
            }
        }catch (RecognitionException re){
            System.exit(-1);
        }
        return null;
    }
    @Override public Void visitFuncDef(sysyParser.FuncDefContext ctx) {
        String s = "define dso_local ";
        if(ctx.funcType().INT_KW()!=null){
            s+="i32 ";
        }else if(ctx.funcType().VOID_KW()!=null){
            s+="i1 ";
        }
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
            idToAd.add(new HashMap<String,String>());
            randRam.add(new HashMap<String,String>());
        }
        try {
            visitFuncType(ctx.funcType());
        }catch (RecognitionException re){
            System.exit(-1);
        }
        addIR(s+'@'+ctx.IDENT().getText()+"()");
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
    @Override public Void visitVarDecl(sysyParser.VarDeclContext ctx){
        int n=ctx.varDef().size();
        for(int i=0;i<n;i++){
            visitVarDef(ctx.varDef(i));
        }
        return null;
    }
    @Override public Void visitVarDef(sysyParser.VarDefContext ctx) {
        String nowVar=ctx.IDENT().getText();
        HashMap<String,String> nowMap=idToAd.get(now-1);
        if(nowMap.containsKey(nowVar));//System.exit(-1);
        else{
            String newRam = randomRam();
            nowMap.put(nowVar,newRam);
            idToAd.set(now-1,nowMap);
            needAllocaRam.get(now-1).add(newRam);
            if(ctx.ASSIGN()!=null){
                visitInitVal(ctx.initVal());
                if(sonIsRam){
                    addIR("store i32 "+sonRam+" , i32 *"+newRam+"\n");
                }else{
                    addIR("store i32 "+sonAns+" , i32 *"+newRam+"\n");
                }
            }
        }
        return null;
    }
    @Override public Void visitAssignStmt(sysyParser.AssignStmtContext ctx) {
        String nowVar = ctx.lVal().IDENT().getText();
        if(idToAd.get(now-1).containsKey(nowVar)){
            String nowRam=idToAd.get(now-1).get(nowVar);
            visitExp(ctx.exp());
            if(sonIsRam){
                addIR("store i32 "+sonRam+" , i32 *"+nowRam+"\n");
            }else{
                addIR("store i32 "+sonAns+" , i32 *"+nowRam+"\n");
            }
        }
        return null;
    }
    @Override public Void visitReturnStmt(sysyParser.ReturnStmtContext ctx) {
        //System.out.print("ret i32 ");
        visitExp(ctx.exp());
        //System.out.print(sonAns);
        addIR("ret i32 ");
        Integer tmp=sonAns;
        if(sonIsRam)addIR(sonRam+"\n");
        else addIR(tmp.toString()+"\n");
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
            }
            if (i == 0) continue;
            if (ctx.addOp(i - 1).MINUS() != null) {
                if (sonIsRam) {
                    String newSon=randomRam();
                    if(fls==0){
                        fls=1;
                        preSon=sonRam;
                        addIR(newSon+" = sub i32 "+tmp+" , "+sonRam+"\n");
                        continue;
                    }
                    addIR(newSon+" = sub i32 "+preSon+" , "+sonRam+"\n");
                    preSon=newSon;
                } else {
                    if(preSon!="null"){
                        String newSon=randomRam();
                        addIR(newSon+" = sub i32 "+preSon+" , "+sonAns+"\n");
                        preSon=newSon;
                    }else tmp = tmp - sonAns;
                }
            } else if (ctx.addOp(i - 1).PLUS() != null) {
                if (sonIsRam) {
                    String newSon=randomRam();
                    if(fls==0){
                        fls=1;
                        preSon=sonRam;
                        addIR(newSon+" = add i32 "+tmp+" , "+sonRam+"\n");
                        continue;
                    }
                    addIR(newSon+" = add i32 "+preSon+" , "+sonRam+"\n");
                    preSon=newSon;
                } else {
                    if(preSon!="null"){
                        String newSon=randomRam();
                        addIR(newSon+" = add i32 "+preSon+" , "+sonAns+"\n");
                        preSon=newSon;
                    }else tmp = tmp + sonAns;
                }
            } else {
               // System.exit(-1);
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
            }
            if(i==0)continue;
            if (ctx.mulOp(i - 1).DIV() != null) {
                if (sonIsRam) {
                    String newSon=randomRam();
                    if(fls==0){
                        fls=1;
                        preSon=sonRam;
                        addIR(newSon+" = sdiv i32 "+tmp+" , "+sonRam+"\n");
                        continue;
                    }
                    addIR(newSon+" = sdiv i32 "+preSon+" , "+sonRam+"\n");
                    preSon=newSon;
                } else {
                    if(preSon!="null"){
                        String newSon=randomRam();
                        addIR(newSon+" = sdiv i32 "+preSon+" , "+sonAns+"\n");
                        preSon=newSon;
                    }else tmp = tmp / sonAns;
                }
            } else if (ctx.mulOp(i - 1).MOD() != null) {
                if (sonIsRam) {
                    String newSon=randomRam();
                    if(fls==0){
                        fls=1;
                        preSon=sonRam;
                        addIR(newSon+" = srem i32 "+tmp+" , "+sonRam+"\n");
                        continue;
                    }
                    addIR(newSon+" = srem i32 "+preSon+" , "+sonRam+"\n");
                    preSon=newSon;
                } else {
                    if(preSon!="null"){
                        String newSon=randomRam();
                        addIR(newSon+" = srem i32 "+preSon+" , "+sonAns+"\n");
                        preSon=newSon;
                    }else tmp = tmp % sonAns;
                }
            } else if (ctx.mulOp(i - 1).MUL() != null) {
                if (sonIsRam) {
                    String newSon=randomRam();
                    if(fls==0){
                        fls=1;
                        preSon=sonRam;
                        addIR(newSon+" = mul i32 "+tmp+" , "+sonRam+"\n");
                        continue;
                    }
                    addIR(newSon+" = mul i32 "+preSon+" , "+sonRam+"\n");
                    preSon=newSon;
                } else {
                    if(preSon!="null"){
                        String newSon=randomRam();
                        addIR(newSon+" = mul i32 "+preSon+" , "+sonAns+"\n");
                        preSon=newSon;
                    }else tmp = tmp * sonAns;
                }
            } else {
                //System.exit(-1);
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
                    addIR(newRam+" = mul i32 -1, "+sonRam+"\n");
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
                String tmpRam=idToAd.get(now-1).get(ctx.lVal().IDENT().getText());
                String newRam =randomRam();
                addIR(newRam+" = load i32, i32* "+tmpRam+"\n");
                sonRam=newRam;
                sonIsRam=true;
            }else{
                /*sonRam=randomRam();
                idToAd.get(now).put(ctx.lVal().getText(),sonRam);*/
               // System.exit(-1);
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
