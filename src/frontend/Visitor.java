package frontend;

import org.antlr.v4.runtime.RecognitionException;

import java.math.BigInteger;
import java.util.*;

public class Visitor extends sysyBaseVisitor<Void> {
    private int sonAns;//计算表达式用于子树求值
    private String sonRam;//子树存储器，参考上方
    private int num=0;//getMpId的自增长ID
    private int funcNow;
    private int now;//当前visit作用域的ID
    private boolean sonIsRam;
    private boolean isConst;
    private boolean fromCond;
    private String sonBr;//son块结束后跳转到sonBr，显然只需要跳到上一层父亲
    private List<HashMap<Integer,Integer>> fa = new ArrayList<>();// 注意-1，存放每个函数中的作用域父子关系
    private HashMap<String,Integer> getMpId = new HashMap<>();//注意-1 ，每个函数对应HashMap在下面两个List的位置
    private List<HashMap<String, String>> idToAd = new ArrayList<>();//标识符到寄存器的映射
    private ArrayList<ArrayList<String>> tmpRam= new ArrayList<>();//每个函数的所有寄存器
    private ArrayList<ArrayList<ArrayList<String>>> needAllocaRam = new ArrayList<>();//每个block需要alloca的寄存器
    private ArrayList<ArrayList<ArrayList<String>>> irList = new ArrayList<>();//每个block的所有ir
    private ArrayList<HashMap<String,Integer>> constList = new ArrayList<>();//存放常量
    private ArrayList<HashMap<String,String>> randRam=new ArrayList<>();
    private HashMap<Integer,Integer> blockId=new HashMap<>();//每个函数的当前可分配最小block编号
    private List<List<List<String>>> ownId = new ArrayList<>();//每个block的独有变量，visit结束后删除
    private List<List<List<String>>> ownConst = new ArrayList<>();//每个block的独有变量，visit结束后删除
    private ArrayList<ArrayList<ArrayList<Integer>>> sonList = new ArrayList<>();
    private void addIR(String s){irList.get(funcNow-1).get(now).add(s);}//偷懒写个函数
    private String getRandRam(){
        Random df = new Random();
        int n=df.nextInt(10)+1;
        StringBuilder str = new StringBuilder("%");
        for (int i = 0; i < n; i++) {
            str.append((char) (Math.random() * 26 + 'a'));
        }
        df = new Random();
        n=df.nextInt(20)+1;
        int i=n;
        return str.toString() + i;
    }
    private String randomRam(){
        String tmps=getRandRam();
        while((randRam.get(funcNow-1).containsKey(tmps))){
            tmps=getRandRam();
        }
        randRam.get(funcNow-1).put(tmps,"null");
        return tmps;
    }
    private String randomBlock(){
        Random df = new Random();
        int n=df.nextInt(20)+1;
        StringBuilder str = new StringBuilder("");
        for (int i = 0; i < n; i++) {
            if(i>0&&i<n-1){
                int tmpp=new Random().nextInt(5);
                if((tmpp&1)!=0)str.append('_');
            }
            str.append((char) (Math.random() * 26 + 'a'));
        }
        return str.toString();
    }
    @Override public Void visitCompUnit(sysyParser.CompUnitContext ctx) {
        try {
            System.out.println("declare i32 @getint()\n" +
                    "declare void @putint(i32)\ndeclare i32 @getch()\n" +
                    "declare void @putch(i32)");
            visitChildren(ctx);

        }catch (RecognitionException re){
            System.exit(-1);
        }
        return null;
    }
    private void printBlock(int x) {
        ArrayList<String> t = needAllocaRam.get(funcNow - 1).get(x);
        ArrayList<String> tmps = irList.get(funcNow-1).get(x);
        /*System.out.print(tmps.get(0));
        System.out.print(tmps.get(1));*/
        for (String s : t) {
            System.out.println(s + " = alloca i32");
        }
        for (int i = 0; i < tmps.size(); i++) {
            String s = tmps.get(i);
            if(s.length()>=8&&s.substring(0,8).equals("visitSon")){
                int len=s.length();
                String ss=s.substring(8,len);
                int num=Integer.parseInt(ss);
                printBlock(num);
            }else System.out.print(s);
        }
    }
    @Override public Void visitFuncDef(sysyParser.FuncDefContext ctx) {
        String s = "define dso_local ";
        if(ctx.funcType().INT_KW()!=null){
            s+="i32 ";
        }else if(ctx.funcType().VOID_KW()!=null){
            s+="i1 ";
        }
        String tmp = ctx.IDENT().getText();
        int pre=funcNow;
        if(getMpId.containsKey(tmp)){
            funcNow= getMpId.get(tmp);
        }else{
            getMpId.put(tmp, ++num);
            funcNow= getMpId.get(tmp);
            tmpRam.add(new ArrayList<String>());
            needAllocaRam.add(new ArrayList<>());
            irList.add(new ArrayList<>());
            idToAd.add(new HashMap<String,String>());
            randRam.add(new HashMap<String,String>());
            constList.add(new HashMap<String,Integer>());
            fa.add(new HashMap<Integer,Integer>());
            sonList.add(new ArrayList<>());
            blockId.put(funcNow,1);
            ownId.add(new ArrayList<>());ownConst.add(new ArrayList<>());
        }
        try {
            visitFuncType(ctx.funcType());
        }catch (RecognitionException re){
            System.exit(-1);
        }
        System.out.print(s+'@'+ctx.IDENT().getText()+"()");
        try {
            needAllocaRam.get(funcNow-1).add(new ArrayList<>());
            sonList.get(funcNow-1).add(new ArrayList<>());
            irList.get(funcNow-1).add(new ArrayList<>());
            ownId.get(funcNow-1).add(new ArrayList<>());
            ownConst.get(funcNow-1).add(new ArrayList<>());
            fromCond=false;
            visitBlock(ctx.block());
            int num = blockId.get(funcNow);
            for(int i=1;i<num;i++){
                if(!fa.get(funcNow - 1).containsKey(i)){
                    fa.get(funcNow-1).put(i,0);
                    sonList.get(funcNow-1).get(0).add(i);
                    irList.get(funcNow-1).get(0).add("visitSon"+i);
                }
            }
            printBlock(0);
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
        int nowBlock = now + 1;
        int tmp = blockId.get(funcNow);
        nowBlock = tmp;
        tmp++;
        blockId.replace(funcNow, tmp);
        irList.get(funcNow - 1).add(new ArrayList<>());
        sonList.get(funcNow - 1).add(new ArrayList<>());
        sonList.get(funcNow - 1).get(now).add(nowBlock);
        ownId.get(funcNow-1).add(new ArrayList<>());ownConst.get(funcNow-1).add(new ArrayList<>());
        needAllocaRam.get(funcNow-1).add(new ArrayList<>());
        fa.get(funcNow - 1).put(nowBlock, now);
        if (ctx.L_BRACE() != null) {
            if(!fromCond)addIR("{\n");
        }
        try {
            int pre = now;
            addIR("visitSon" + nowBlock);//todo 记得加id
            now = nowBlock;
            visitChildren(ctx);
            for(String s:ownId.get(funcNow-1).get(now))idToAd.get(funcNow-1).remove(s);
            for(String s:ownConst.get(funcNow-1).get(now))constList.get(funcNow-1).remove(s);
            now = pre;
        } catch (RecognitionException re) {
            System.exit(-1);
        }
        if (ctx.R_BRACE() != null) {
            if(!fromCond)addIR("}\n");
        }
        return null;
    }
   @Override public Void visitConstDef(sysyParser.ConstDefContext ctx) {
        sonIsRam=false;
        visitConstInitVal(ctx.constInitVal());
        if(sonIsRam)System.exit(-10);
        else{
            ownConst.get(funcNow-1).get(now).add(ctx.IDENT().getText());
            Integer tmp=sonAns;
            constList.get(funcNow-1).put(ctx.IDENT().getText(),sonAns);
        }
       return null;
   }
    @Override public Void visitCallee(sysyParser.CalleeContext ctx) {
        if(Objects.equals(ctx.IDENT().getText(), "getint") || Objects.equals(ctx.IDENT().getText(), "getch")){
            if(ctx.funcRParams()!=null)System.exit(-123);
            String newRam=randomRam();
            addIR(newRam+" = call i32 @"+ctx.IDENT().getText()+"()\n");
            sonIsRam=true;
            sonRam=newRam;
        }else if(Objects.equals(ctx.IDENT().getText(), "putint") || Objects.equals(ctx.IDENT().getText(), "putch")){
            if(ctx.funcRParams()==null)System.exit(-128);
            if(ctx.funcRParams().param().size()!=1)System.exit(-124);
            if(ctx.funcRParams().param(0).STRING()!=null){
                System.exit(-128);
                String s=ctx.funcRParams().param(0).STRING().getText();
                addIR("call void @"+ctx.IDENT().getText()+"(i32 "+s+")\n");
            }else{
                sonIsRam=false;
                visitExp(ctx.funcRParams().param(0).exp());
                if(sonIsRam) addIR("call void @"+ctx.IDENT().getText()+"(i32 "+sonRam+")\n");
                else addIR("call void @"+ctx.IDENT().getText()+"(i32 "+sonAns+")\n");
            }
        }else{
            System.exit(-1235);
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
        HashMap<String,String> nowMap=idToAd.get(funcNow-1);
        if(nowMap.containsKey(nowVar));//System.exit(-1);
        else{
            ownId.get(funcNow-1).get(now).add(ctx.IDENT().getText());
            String newRam = randomRam();
            nowMap.put(nowVar,newRam);
            idToAd.set(funcNow-1,nowMap);
            needAllocaRam.get(funcNow-1).get(now).add(newRam);
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
        if(idToAd.get(funcNow-1).containsKey(nowVar)){
            String nowRam=idToAd.get(funcNow-1).get(nowVar);
            visitExp(ctx.exp());
            if(sonIsRam){
                addIR("store i32 "+sonRam+" , i32 *"+nowRam+"\n");
            }else{
                addIR("store i32 "+sonAns+" , i32 *"+nowRam+"\n");
            }
        }else System.exit(-2);
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
            }else {
                if (fls == 0) preSon = sonRam;
                fls++;
            }
            if (i == 0) continue;
            if (ctx.addOp(i - 1).MINUS() != null) {
                if (sonIsRam) {
                    String newSon=randomRam();
                    if(fls==1){
                        addIR(newSon+" = sub i32 "+tmp+" , "+sonRam+"\n");
                        preSon=newSon;
                        fls++;
                        continue;
                    }
                    addIR(newSon+" = sub i32 "+preSon+" , "+sonRam+"\n");
                    preSon=newSon;
                } else {
                    if(!preSon.equals("null")){
                        String newSon=randomRam();
                        addIR(newSon+" = sub i32 "+preSon+" , "+sonAns+"\n");
                        preSon=newSon;
                    }else tmp = tmp - sonAns;
                }
            } else if (ctx.addOp(i - 1).PLUS() != null) {
                if (sonIsRam) {
                    String newSon=randomRam();
                    if(fls==1){
                        addIR(newSon+" = add i32 "+tmp+" , "+sonRam+"\n");
                        preSon=newSon;
                        fls++;
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
                System.exit(-5);
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
                if(fls==0)preSon=sonRam;
                fls++;
            }
            if(i==0)continue;
            if (ctx.mulOp(i - 1).DIV() != null) {
                if (sonIsRam) {
                    String newSon=randomRam();
                    if(fls==1){
                        addIR(newSon+" = sdiv i32 "+tmp+" , "+sonRam+"\n");
                        preSon=newSon;
                        fls++;
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
                    if(fls==1){
                        addIR(newSon+" = srem i32 "+tmp+" , "+sonRam+"\n");
                        preSon=newSon;
                        fls++;
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
                    if(fls==1){
                        addIR(newSon+" = mul i32 "+tmp+" , "+sonRam+"\n");
                        preSon=newSon;
                        fls++;
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
                System.exit(-6);
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
                    addIR(newRam+" = sub i32 0, "+sonRam+"\n");
                    sonRam=newRam;
                }
            }
            if(ctx.unaryOp().NOT()!=null){
                if(!sonIsRam){
                  if(sonAns!=0){
                      sonAns=1;
                  }else sonAns=0;
                } else{
                    String newRam = randomRam();
                    addIR(newRam+" = icmp eq i32 "+sonRam+" , 0\n");
                    String anoRam=randomRam();
                    addIR(anoRam+"= zext i1 "+newRam+" to i32");
                    sonRam=anoRam;
                }
            }
        }else if(ctx.primaryExp()!=null){
            visitPrimaryExp(ctx.primaryExp());
        }else if(ctx.callee()!=null)visitCallee(ctx.callee());
        return null;
    }

    @Override public Void visitPrimaryExp(sysyParser.PrimaryExpContext ctx){
        if(ctx.lVal()!=null){
            if(idToAd.get(funcNow-1).containsKey(ctx.lVal().IDENT().getText())){
                if(isConst)System.exit(-54);
                String tmpRam=idToAd.get(funcNow-1).get(ctx.lVal().IDENT().getText());
                String newRam =randomRam();
                addIR(newRam+" = load i32, i32* "+tmpRam+"\n");
                sonRam=newRam;
                sonIsRam=true;
            }else{
                if(!constList.get(funcNow- 1).containsKey(ctx.lVal().IDENT().getText()))
                    System.exit(-3);
                else{
                    sonIsRam=false;
                    sonAns=constList.get(funcNow-1).get(ctx.lVal().IDENT().getText()).intValue();
                }
            }
        }else if(ctx.number()!=null){
            sonIsRam=false;
            visitNumber(ctx.number());
        }else{
            visitChildren(ctx);
        }
        return null;
    }
    private String nxtLabel;
    private String endLabel;
    private String exeLabel;
    private String outLabel;
    private String backLabel;
    @Override public Void visitConditionStmt(sysyParser.ConditionStmtContext ctx) {
        exeLabel=randomBlock();
        outLabel=randomBlock();
        visitCond(ctx.cond());
        addIR(exeLabel+": \n");
        fromCond=true;
        visitStmt(ctx.stmt(0));
        addIR("br label "+"%"+ backLabel+"\n");
        addIR(outLabel+": \n");
        if(ctx.stmt().size()>1){
            fromCond=true;
            visitStmt(ctx.stmt(1));
        }
        fromCond=false;
        addIR("br label "+"%"+ backLabel+"\n");
        return null;
    }
    @Override public Void visitLOrExp(sysyParser.LOrExpContext ctx){
        int n=ctx.lAndExp().size();
        for(int i=0;i<n;i++){
            endLabel=randomBlock();
            visitLAndExp(ctx.lAndExp(i));
            addIR(endLabel+": \n");
            nxtLabel=randomBlock();
            if(i!=n-1){
                addIR("br i1 "+sonRam+" , label "+"%"+exeLabel+" , label "+"%"+nxtLabel+'\n');
                addIR(nxtLabel+" :\n");
            }else{
                addIR("br i1 "+sonRam+" , label "+"%"+exeLabel+" , label "+"%"+outLabel+'\n');
            }
        }
        return null;
    }
    @Override public Void visitLAndExp(sysyParser.LAndExpContext ctx) {
        int n=ctx.eqExp().size();
        for(int i=0;i<n;i++){
            visitEqExp(ctx.eqExp(i));
            nxtLabel=randomBlock();
            if(i!=n-1){
                addIR("br i1 "+sonRam+" , label "+"%"+nxtLabel+" , label "+"%"+endLabel+'\n');
                addIR(nxtLabel+" :\n");
            }else{
                addIR("br "+ "label "+"%"+endLabel+'\n');
            }
        }
        return null;
    }
    private boolean fromI1;//儿子是i1还是i32
    @Override public Void visitRelExp(sysyParser.RelExpContext ctx) {
        int n=ctx.addExp().size();
        String preRam="";
        for(int i=0;i<n;i++){
            visitAddExp(ctx.addExp(i));
            if(i!=0){
                String newRam=randomRam();
                String ss=ctx.relOp().get(i-1).getText();
                if(ctx.relOp().get(i-1).LT()!=null){
                    ss="slt";
                }else if(ctx.relOp().get(i-1).GT()!=null) {
                    ss = "sgt";
                }else if(ctx.relOp().get(i-1).LE()!=null){
                    ss = "sle";
                }else{
                    ss="sge";
                }
                addIR(newRam+" = icmp "+ss+" i32 "+preRam+" , "+sonRam+"\n");
                String anoRam = randomRam();
                addIR(anoRam+" = zext i1 "+newRam+" to i32\n");
                preRam=anoRam;
            } else preRam=sonRam;
        }
        sonRam=preRam;
        return null;
    }

    @Override public Void visitEqExp(sysyParser.EqExpContext ctx) {
        int n=ctx.relExp().size();
        String preRam="";
        for(int i=0;i<n;i++){
            visitRelExp(ctx.relExp(i));
            if(i!=0){
                String newRam=randomRam();
                String ss=ctx.eqOp().get(i-1).getText();
                if(ctx.eqOp().get(i-1).EQ()!=null){
                    ss="eq";
                }else ss="ne";
                addIR(newRam+" = icmp "+ss+" i32 "+preRam+" ,  "+sonRam+"\n");
                String anoRam = randomRam();
                addIR(anoRam+" = zext i1 "+newRam+" to i32\n");
                preRam=anoRam;
            } else preRam=sonRam;
        }
        sonRam=preRam;
        return null;
    }
    @Override public Void visitStmt(sysyParser.StmtContext ctx) {
        if(ctx.conditionStmt()!=null){
            backLabel=randomBlock();
            visitConditionStmt(ctx.conditionStmt());
            addIR(backLabel+": \n");
        }else{
            visitChildren(ctx);
        }
        return null;
    }
}
