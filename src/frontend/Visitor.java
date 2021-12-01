package frontend;

import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.Pair;

import java.math.BigInteger;
import java.util.*;

public class Visitor extends sysyBaseVisitor<Void> {
    private int sonAns;//计算表达式用于子树求值
    private String sonRam;//子树存储器，参考上方
    private int num=0;//getMpId的自增长ID
    private int funcNow;
    private int now;//当前visit作用域的ID
    private int nowConst=0;//constArray当前可用编号
    private boolean sonIsRam;
    private boolean isConst;
    private boolean fromCond;
    private boolean inFuncDef;
    private String sonBr;//son块结束后跳转到sonBr，显然只需要跳到上一层父亲
    private List<HashMap<Integer,Integer>> fa = new ArrayList<>();// 注意-1，存放每个函数中的作用域父子关系
    private HashMap<String,Integer> getMpId = new HashMap<>();//注意-1 ，每个函数对应HashMap在下面两个List的位置
    private List<HashMap<String, String>> idToAd = new ArrayList<>();//标识符到寄存器的映射
    private ArrayList<ArrayList<String>> tmpRam= new ArrayList<>();//每个函数的所有寄存器
    private ArrayList<ArrayList<ArrayList<String>>> needAllocaRam = new ArrayList<>();//每个block需要alloca的寄存器
    private ArrayList<ArrayList<ArrayList<String>>> irList = new ArrayList<>();//每个block的所有ir
    private ArrayList<HashMap<String,Integer>> constList = new ArrayList<>();//存放常量
    private ArrayList<HashMap<String,String>> randRam=new ArrayList<>();
    private HashMap<String,Integer> backConstArray = new HashMap<>();//对于每个block，存放更新的const，便于回滚
    private HashMap<String,Integer> constAtoId = new HashMap<>();//存放当前对应常量命名的数组在下面两个list的序号，记录pre并更新便于求值
    private ArrayList<ArrayList<Integer>> constArray = new ArrayList<>();//每个多维数组被视作是一维，直接塞就好
    private ArrayList<ArrayList<Integer>> constArraySize = new ArrayList<>();//记录多维数组每维尺寸，几维直接查size就好
    private HashMap<Integer,Integer> blockId=new HashMap<>();//每个函数的当前可分配最小block编号
    private ArrayList<ArrayList<ArrayList<Integer>>> sonList = new ArrayList<>();
    private HashMap<String,Integer> typeMap = new HashMap<>();//每个变量or常量调用什么,1arrToAd 2constArrToAd 3 varAtoId 4 constAtoId
    private HashMap<String,Integer> vis = new HashMap<>();
    private HashMap<String,Integer> globalVar = new HashMap<>();
    private HashMap<String,Integer> globalConst = new HashMap<>();
    private List<HashMap<String, String>> arrToAd = new ArrayList<>();//变量数组到寄存器的映射
    private List<HashMap<String, String>> constArrToAd = new ArrayList<>();//常量数组到寄存器的映射
    private HashMap<String,Integer> varAtoId = new HashMap<>();//存放当前对应常量命名的数组在下面两个list的序号，记录pre并更新便于求值
    private ArrayList<ArrayList<String>> varArray = new ArrayList<>();//每个多维数组被视作是一维，直接塞就好,因为是变量，所以会有寄存器
    private ArrayList<ArrayList<Integer>> varArraySize = new ArrayList<>();//记录多维数组每维尺寸，几维直接查size就好
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
                    "declare void @putch(i32)\ndeclare void @memset(i32*, i32, i32)");
            visitChildren(ctx);

        }catch (RecognitionException re){
            System.exit(-1);
        }
        return null;
    }
    private void printBlock(int x) {
        ArrayList<String> t = needAllocaRam.get(funcNow - 1).get(x);
        ArrayList<String> tmps = irList.get(funcNow-1).get(x);
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
            }else {
                int len=s.length();
                System.out.print(s);
            }
        }
    }
    @Override public Void visitFuncDef(sysyParser.FuncDefContext ctx) {
        inFuncDef=true;
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
            arrToAd.add(new HashMap<>());
            constArrToAd.add(new HashMap<>());
            randRam.add(new HashMap<String,String>());
            constList.add(new HashMap<String,Integer>());
            fa.add(new HashMap<Integer,Integer>());
            sonList.add(new ArrayList<>());
            blockId.put(funcNow,1);
        }
        try {
            visitFuncType(ctx.funcType());
        }catch (RecognitionException re){
            System.exit(-1);
        }
        System.out.print(s+'@'+ctx.IDENT().getText()+"(){\n");
        try {
            needAllocaRam.get(funcNow-1).add(new ArrayList<>());
            sonList.get(funcNow-1).add(new ArrayList<>());
            irList.get(funcNow-1).add(new ArrayList<>());
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
        funcNow=pre;
        System.out.print("}\n");
        inFuncDef=false;
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
        vis.clear();
        int nowBlock = now + 1;
        int tmp = blockId.get(funcNow);
        nowBlock = tmp;
        tmp++;
        blockId.replace(funcNow, tmp);
        irList.get(funcNow - 1).add(new ArrayList<>());
        sonList.get(funcNow - 1).add(new ArrayList<>());
        sonList.get(funcNow - 1).get(now).add(nowBlock);
        needAllocaRam.get(funcNow-1).add(new ArrayList<>());
        fa.get(funcNow - 1).put(nowBlock, now);
        HashMap<String,Integer> tmpConst = new HashMap<>();
        HashMap<String ,String> tmpVar = new HashMap<>();
        HashMap<String,String> tmpVarArr = new HashMap<>();
        HashMap<String,String> tmpConstVarArr = new HashMap<>();
        HashMap<String,Integer> tmpVarMap = new HashMap<>();
        HashMap<String,Integer> tmpVr = new HashMap<>();
        HashMap<String,Integer> tmpCa = new HashMap<>();
        boolean lbr=true;
        try {
            int pre = now;
            addIR("visitSon" + nowBlock);
            for(String s:arrToAd.get(funcNow-1).keySet())tmpVarArr.put(s,arrToAd.get(funcNow-1).get(s));
            for(String s:constArrToAd.get(funcNow-1).keySet())tmpConstVarArr.put(s,constArrToAd.get(funcNow-1).get(s));
            for(String s:idToAd.get(funcNow-1).keySet())tmpVar.put(s,idToAd.get(funcNow-1).get(s));
            for(String s:constList.get(funcNow-1).keySet())tmpConst.put(s,constList.get(funcNow-1).get(s));
            for(String s:typeMap.keySet())tmpVarMap.put(s,typeMap.get(s));
            for(String s:constAtoId.keySet())tmpCa.put(s,constAtoId.get(s));
            for(String s:varAtoId.keySet())tmpVr.put(s,varAtoId.get(s));
            now = nowBlock;
            visitChildren(ctx);
            now = pre;
        } catch (RecognitionException re) {
            System.exit(-1);
        }
        vis.clear();
        idToAd.get(funcNow-1).clear();
        arrToAd.get(funcNow-1).clear();
        constArrToAd.get(funcNow-1).clear();
        constList.get(funcNow-1).clear();
        typeMap.clear();
        varAtoId.clear();
        constAtoId.clear();
        for(String s:tmpConst.keySet())constList.get(funcNow-1).put(s,tmpConst.get(s));
        for(String s:tmpVar.keySet())idToAd.get(funcNow-1).put(s,tmpVar.get(s));
        for(String s:tmpVarArr.keySet())arrToAd.get(funcNow-1).put(s,tmpVarArr.get(s));
        for(String s:tmpConstVarArr.keySet())constArrToAd.get(funcNow-1).put(s,tmpConstVarArr.get(s));
        for(String s:tmpVarMap.keySet())typeMap.put(s,tmpVarMap.get(s));
        for(String s:tmpCa.keySet())constAtoId.put(s,tmpCa.get(s));
        for(String s:tmpVr.keySet())varAtoId.put(s,tmpVr.get(s));
        for(String s:constList.get(funcNow-1).keySet()){
            vis.put(s,1);
        }
        for(String s:idToAd.get(funcNow-1).keySet()){
            vis.put(s,1);
        }
        return null;
    }
    private boolean inArrayDef = false;
    private int nowConstId;
    private int posNow;
    private int depNow;
    private int prePos;
    private int nowVarId=0;
    private HashMap<Integer,Integer> visitAss = new HashMap<>();
    @Override public Void visitConstInitVal(sysyParser.ConstInitValContext ctx) {
        sonIsRam=false;
        if(ctx.L_BRACE()!=null){
            depNow++;
            if(visitAss.containsKey(depNow)){
                int tmpp=visitAss.get(depNow);
                visitAss.replace(depNow,tmpp+1);
            }else visitAss.put(depNow,0);
            if(ctx.constInitVal().isEmpty()) {
                depNow--;
                return null;
            }
            int tmp=1;
            for(int i=depNow-1;i<constArray.get(nowConstId).size();i++){
                tmp*=constArraySize.get(nowConstId).get(i);
            }
            posNow+=tmp* visitAss.get(depNow);
            if(ctx.constInitVal(0).L_BRACE()==null) {
                for (int i = prePos; i < posNow; i++) constArray.get(nowConstId).add(0);
                prePos=posNow+ctx.constInitVal().size();
            }
            for(int i=0;i<ctx.constInitVal().size();i++)visitConstInitVal(ctx.constInitVal(i));
            posNow-=tmp*visitAss.get(depNow);
            depNow--;
        }else{
            if(inArrayDef){
                visitChildren(ctx);
                if(sonIsRam)System.exit(-123540);
                constArray.get(nowConstId).add(sonAns);
            }else{
                visitChildren(ctx);
                if(sonIsRam)System.exit(-123541);
            }
        }
        return null;
    }
    @Override public Void visitConstDef(sysyParser.ConstDefContext ctx) {
        sonIsRam=false;
        if(ctx.L_BRACKT().isEmpty()) {
            visitConstInitVal(ctx.constInitVal());
            if (sonIsRam) System.exit(-10);
            else {
                if (inFuncDef) {
                    Integer tmp = sonAns;
                    if (vis.containsKey(ctx.IDENT().getText())) System.exit(-22);
                    if (idToAd.get(funcNow - 1).containsKey(ctx.IDENT().getText())) System.exit(-26);
                    if (arrToAd.get(funcNow - 1).containsKey(ctx.IDENT().getText())) System.exit(-26);
                    if (globalConst.containsKey(ctx.IDENT().getText())) System.exit(-236);
                    if (globalVar.containsKey(ctx.IDENT().getText())) System.exit(-569);
                    constList.get(funcNow - 1).put(ctx.IDENT().getText(), sonAns);
                    typeMap.put(ctx.IDENT().getText(),6);
                    vis.put(ctx.IDENT().getText(), 1);
                } else {
                    if (globalConst.containsKey(ctx.IDENT().getText())) System.exit(-236);
                    if (globalVar.containsKey(ctx.IDENT().getText())) System.exit(-569);
                    globalConst.put(ctx.IDENT().getText(), sonAns);
                    typeMap.put(ctx.IDENT().getText(),8);
                }
            }
        }else{
            int n=ctx.L_BRACKT().size();
            sonIsRam=false;
            String na=ctx.IDENT().getText();
            if(!inFuncDef){
                if (globalConst.containsKey(ctx.IDENT().getText())) System.exit(-236);
                if (globalVar.containsKey(ctx.IDENT().getText())) System.exit(-569);
                constArray.add(new ArrayList<>());
                constArraySize.add(new ArrayList<>());
                if(!constAtoId.containsKey(na))constAtoId.put(na,nowConst);
                else System.exit(-12354);
                typeMap.put(ctx.IDENT().getText(),4);
            }else{
                if (vis.containsKey(ctx.IDENT().getText())) System.exit(-22);
                if (idToAd.get(funcNow - 1).containsKey(ctx.IDENT().getText())) System.exit(-26);
                if (arrToAd.get(funcNow - 1).containsKey(ctx.IDENT().getText())) System.exit(-26);
                if (globalConst.containsKey(ctx.IDENT().getText())) System.exit(-236);
                if (globalVar.containsKey(ctx.IDENT().getText())) System.exit(-569);
                constArray.add(new ArrayList<>());
                constArraySize.add(new ArrayList<>());
                if(!constAtoId.containsKey(na))constAtoId.put(na,nowConst);
                else constAtoId.replace(na,nowConst);
                typeMap.put(ctx.IDENT().getText(),2);
            }
            nowConst++;
            int nowId=constAtoId.get(na);
            for(int i=0;i<n;i++){
                sonIsRam=false;
                visitConstExp(ctx.constExp(i));
                if(sonIsRam)System.exit(-1937);
                constArraySize.get(nowId).add(sonAns);
                if(sonAns<0)System.exit(-121315);
            }
            int m=constArraySize.get(nowId).size();
            inArrayDef=true;
            nowConstId=nowId;
            visitAss.clear();
            depNow=posNow=prePos=0;
            visitConstInitVal(ctx.constInitVal());
            int mul=1;
            for(int i:constArraySize.get(nowId)){
                mul*=i;
            }
            int nn=constArray.get(nowId).size();
            for(int i=0;i<mul-nn;i++){
                constArray.get(nowId).add(0);
            }
            if(!inFuncDef) {
                System.out.print("@" + na + " = dso_local constant [ " + mul + " x i32]");
                System.out.print("[");
                n=constArray.get(nowId).size();
                for(int i=0;i<n;i++){
                    System.out.print("i32 "+constArray.get(nowId).get(i));
                    if(i<n-1)System.out.print(",");
                }
                System.out.println("]");
            }else{
                String newRam = randomRam();
                if (!constArrToAd.get(funcNow - 1).containsKey(na)) constArrToAd.get(funcNow - 1).put(na, newRam);
                else constArrToAd.get(funcNow - 1).replace(na, newRam);
                String iniRam = randomRam();
                addIR(iniRam+" = getelementptr ["+mul+" x i32], ["+mul+" x i32]* "+newRam+" , i32 0, i32 0\n");
                addIR("call void @memset(i32* "+iniRam+", i32 0, i32 "+mul*4+")\n");
                int nowpos=0;
                for(int i:constArray.get(nowId)){
                    String nowRam = randomRam();
                    addIR(nowRam+" = getelementptr i32, i32* "+iniRam+", i32 "+nowpos+"\n");
                    addIR("store i32 "+i+", i32* "+nowRam+"\n");
                    nowpos++;
                }
            }
            inArrayDef=false;
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
    @Override public Void visitInitVal(sysyParser.InitValContext ctx) {
        sonIsRam=false;
        if(ctx.L_BRACE()!=null) {
            depNow++;
            if (visitAss.containsKey(depNow)) {
                int tmpp = visitAss.get(depNow);
                visitAss.replace(depNow, tmpp + 1);
            } else {
                visitAss.put(depNow, 0);
                int tmpp = visitAss.get(depNow);
            }
            if(ctx.initVal().isEmpty()){
                depNow--;
                return null;
            }
            int tmp = 1;
            for (int i = depNow - 1; i < varArraySize.get(nowConstId).size(); i++) {
                tmp *= varArraySize.get(nowConstId).get(i);
            }
            posNow += tmp * visitAss.get(depNow);
            if (ctx.initVal(0).L_BRACE() == null) {
                for (int i = prePos; i < posNow; i++) varArray.get(nowConstId).add("0");
                prePos = posNow+ctx.initVal().size();
            }
            for (int i = 0; i < ctx.initVal().size(); i++) visitInitVal(ctx.initVal(i));
            posNow -= tmp * visitAss.get(depNow);
            depNow--;
        }else{
            if(inArrayDef){
                visitChildren(ctx);
                if(sonIsRam) {
                    if(!inFuncDef)System.exit(-123515);
                    varArray.get(nowConstId).add(sonRam);
                }else{
                    Integer tmp = sonAns;
                    varArray.get(nowConstId).add(tmp.toString());
                }
            } else visitExp(ctx.exp());
        }
        return null;
    }
    @Override public Void visitVarDef(sysyParser.VarDefContext ctx) {
        String nowVar = ctx.IDENT().getText();
        if(ctx.L_BRACKT().isEmpty()) {
            if (inFuncDef) {
                String newRam = randomRam();
                if (globalConst.containsKey(nowVar)) System.exit(-365);
                if (constList.get(funcNow - 1).containsKey(nowVar)) System.exit(-212);
                if (vis.containsKey(nowVar)) System.exit(-23);
                vis.put(nowVar, 1);
                if (!idToAd.get(funcNow - 1).containsKey(nowVar)) idToAd.get(funcNow - 1).put(nowVar, newRam);
                else idToAd.get(funcNow - 1).replace(nowVar, newRam);
                needAllocaRam.get(funcNow - 1).get(now).add(newRam);
                if (ctx.ASSIGN() != null) {
                    visitInitVal(ctx.initVal());
                    if (sonIsRam) {
                        addIR("store i32 " + sonRam + " , i32 *" + newRam + "\n");
                    } else {
                        addIR("store i32 " + sonAns + " , i32 *" + newRam + "\n");
                    }
                }
                typeMap.put(ctx.IDENT().getText(),5);
            } else {
                if (globalConst.containsKey(nowVar)) System.exit(-365);
                if (globalVar.containsKey(nowVar)) System.exit(-65);
                if(constAtoId.containsKey(nowVar)) System.exit(-13);
                if(varAtoId.containsKey(nowVar)) System.exit(-13);
                if (ctx.ASSIGN() != null) {
                    visitInitVal(ctx.initVal());
                    if (sonIsRam) System.exit(-648);
                    else System.out.println("@" + nowVar + " = dso_local global i32 " + sonAns);
                    globalVar.put(nowVar, sonAns);
                } else {
                    System.out.println("@" + nowVar + " = dso_local global i32 0");
                    globalVar.put(nowVar, 0);
                }
                typeMap.put(ctx.IDENT().getText(),7);
            }
        }else{
            inArrayDef=true;
            String na = ctx.IDENT().getText();
            if (inFuncDef) {
                if (constList.get(funcNow - 1).containsKey(nowVar)) System.exit(-212);
                if (vis.containsKey(nowVar)) System.exit(-23);
                varArray.add(new ArrayList<>());
                varArraySize.add(new ArrayList<>());
                if(!varAtoId.containsKey(na))varAtoId.put(na,nowVarId);
                else varAtoId.replace(na,nowVarId);
                nowVarId++;
                int nowId = varAtoId.get(na);
                int n=ctx.constExp().size();
                int mul=1;
                for(int i=0;i<n;i++){
                    sonIsRam=false;
                    visitConstExp(ctx.constExp(i));
                    if(sonIsRam)System.exit(-1937);
                    varArraySize.get(nowId).add(sonAns);
                    mul*=sonAns;
                    if(sonAns<0)System.exit(-121315);
                }
                String newRam = randomRam();
                if (!arrToAd.get(funcNow - 1).containsKey(na)) arrToAd.get(funcNow - 1).put(na, newRam);
                else arrToAd.get(funcNow - 1).replace(na, newRam);
                addIR(newRam+" = alloca ["+mul+" x i32]\n");
                if(ctx.ASSIGN()!=null) {
                    nowConstId = nowId;
                    visitAss.clear();
                    depNow = posNow = prePos = 0;
                    visitInitVal(ctx.initVal());
                }
                int nn=varArray.get(nowId).size();
                for(int i=0;i<mul-nn;i++){
                    varArray.get(nowId).add("0");
                }
//              %3 = getelementptr [20 x i32], [20 x i32]* @a, i32 0, i32 %2 ; %3 类型为 i32*
                String iniRam = randomRam();
                addIR(iniRam+" = getelementptr ["+mul+" x i32], ["+mul+" x i32]* "+newRam+" , i32 0, i32 0\n");
                addIR("call void @memset(i32* "+iniRam+", i32 0, i32 "+mul*4+")\n");
                int nowpos=0;
                for(String i:varArray.get(nowId)){
                    String nowRam = randomRam();
                    addIR(nowRam+" = getelementptr i32, i32* "+iniRam+", i32 "+nowpos+"\n");
                    addIR("store i32 "+i+", i32* "+nowRam+"\n");
                    nowpos++;
                }
                typeMap.put(ctx.IDENT().getText(),1);
            } else {
                if (globalConst.containsKey(nowVar)) System.exit(-365);
                if (globalVar.containsKey(nowVar)) System.exit(-65);
                varArray.add(new ArrayList<>());
                varArraySize.add(new ArrayList<>());
                if(!varAtoId.containsKey(na))varAtoId.put(na,nowVarId);
                else System.exit(-12354);
                nowVarId++;
                int nowId = varAtoId.get(na);
                int n=ctx.constExp().size();
                int mul=1;
                for(int i=0;i<n;i++){
                    sonIsRam=false;
                    visitConstExp(ctx.constExp(i));
                    if(sonIsRam)System.exit(-1937);
                    varArraySize.get(nowId).add(sonAns);
                    mul*=sonAns;
                    if(sonAns<0)System.exit(-121315);
                }
                if(ctx.ASSIGN()!=null) {
                    nowConstId = nowId;
                    visitAss.clear();
                    depNow = posNow = prePos = 0;
                    visitInitVal(ctx.initVal());
                    int nn=varArray.get(nowId).size();
                    for(int i=0;i<mul-nn;i++){
                        varArray.get(nowId).add("0");
                    }
                    System.out.print("@" + na + " = dso_local global [ " + mul + " x i32]");
                    System.out.print("[");
                    n = varArray.get(nowId).size();
                    for (int i = 0; i < n; i++) {
                        System.out.print("i32 " + varArray.get(nowId).get(i));
                        if (i < n - 1) System.out.print(",");
                    }
                    System.out.println("]");
                }else{
                    System.out.println("@"+na+" = dso_local global ["+mul+" x i32] zeroinitializer");
                }
                typeMap.put(ctx.IDENT().getText(),3);
            }
            inArrayDef=false;
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
        }else if(globalVar.containsKey(nowVar)){
            visitExp(ctx.exp());
            if(sonIsRam){
                addIR("store i32 "+sonRam+" , i32 * @"+nowVar+"\n");
            }else{
                addIR("store i32 "+sonAns+" , i32 * @"+nowVar+"\n");
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
                    anoRam+="7k7k";
                    addIR(anoRam+"= zext i1 "+newRam+" to i32\n");
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
            visitLVal(ctx.lVal());
        }else if(ctx.number()!=null){
            sonIsRam=false;
            visitNumber(ctx.number());
        }else{
            visitChildren(ctx);
        }
        return null;
    }
    private String nxtLabel;
    private String exeLabel;
    private String outLabel;
    private String backLabel;
    private String condLabel;
    private boolean sonRet=false;
    @Override public Void visitConditionStmt(sysyParser.ConditionStmtContext ctx) {
        exeLabel=randomBlock();
        outLabel=randomBlock();
        boolean sonret=false;
        boolean sonret2=false;
        String ss=backLabel;
        String ee=exeLabel,oo=outLabel;
        visitCond(ctx.cond());
        addIR(ee+": \n");
        fromCond=true;
        sonRet=false;
        visitStmt(ctx.stmt(0));
        if(!sonRet)addIR("br label "+"%"+ ss+"\n");
        sonret=sonRet;
        addIR(oo+": \n");
        sonRet=false;
        if(ctx.stmt().size()>1){
            fromCond=true;
            visitStmt(ctx.stmt(1));
        }else sonRet=false;
        fromCond=false;
        if(!sonRet)addIR("br label "+"%"+ ss+"\n");
        sonret2=sonRet;
        sonRet=sonret&sonret2;
        exeLabel=ee;
        outLabel=oo;
        return null;
    }
    private List<Pair<String,String>> addList=new ArrayList<>();//son,sonLabel
    @Override public Void visitLOrExp(sysyParser.LOrExpContext ctx){
        int n=ctx.lAndExp().size();
        for(int i=0;i<n;i++){
            addList.clear();
            visitLAndExp(ctx.lAndExp(i));
            nxtLabel=randomBlock();
            if(i!=n-1){
                for(Pair p:addList){
                    addIR(p.b+":\n");
                    addIR("br i1 "+p.a+" , label "+"%"+exeLabel+" , label "+"%"+nxtLabel+'\n');
                }
                addIR(nxtLabel+":\n");
            }else{
                for(Pair p:addList){
                    addIR(p.b+":\n");
                    addIR("br i1 "+p.a+" , label "+"%"+exeLabel+" , label "+"%"+outLabel+'\n');
                }

            }
        }
        return null;
    }
    @Override public Void visitLAndExp(sysyParser.LAndExpContext ctx) {
        int n=ctx.eqExp().size();
        for(int i=0;i<n;i++){
            visitEqExp(ctx.eqExp(i));
            nxtLabel=randomBlock();
            String addLabel=randomBlock();
            String newRam=randomRam();
            addIR(newRam+" = icmp ne i32 "+sonRam+" , 0\n");
            if(i!=n-1){
                addIR("br i1 "+newRam+" , label "+"%"+nxtLabel+" , label "+"%"+addLabel+'\n');
                addIR(nxtLabel+":\n");
                sonRam=newRam;
            }else{
                sonRam=newRam;
                addIR("br "+ "label "+"%"+addLabel+'\n');
            }
            addList.add(new Pair(sonRam,addLabel));
        }
        return null;
    }
    private boolean fromI1;//儿子是i1还是i32
    @Override public Void visitRelExp(sysyParser.RelExpContext ctx) {
        int n=ctx.addExp().size();
        String preRam="";
        for(int i=0;i<n;i++){
            sonIsRam=false;
            visitAddExp(ctx.addExp(i));
            if(!sonIsRam){
                sonRam=randomRam();
                addIR(sonRam+"= add i32 "+sonAns+" , 0 \n");
                sonIsRam=true;
            }
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
            sonIsRam=false;
            visitRelExp(ctx.relExp(i));
            if(i!=0){
                String newRam=randomRam();
                String ss=ctx.eqOp().get(i-1).getText();
                if(ctx.eqOp().get(i-1).EQ()!=null){
                    ss="eq";
                }else ss="ne";
                addIR(newRam+" = icmp "+ss+" i32 "+preRam+" ,  "+sonRam+"\n");
                String anoRam = randomRam();
                anoRam+="9877";
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
            String ss=backLabel;
            visitConditionStmt(ctx.conditionStmt());
            if(!sonRet) addIR(ss+": \n");
        }else{
            if(ctx.returnStmt()!=null)sonRet=true;
            visitChildren(ctx);
        }
        return null;
    }
    private String nCondLabel;
    private String nOutLabel;
    @Override public Void visitWhileStmt(sysyParser.WhileStmtContext ctx) {
        exeLabel=randomBlock();
        outLabel=randomBlock();
        boolean sonret=false;
        boolean sonret2=false;
        String ss=backLabel;
        String ee=exeLabel,oo=outLabel;
        condLabel = randomBlock();
        condLabel+="49812";
        nOutLabel=outLabel;
        nCondLabel=condLabel;
        String preNO=nOutLabel,preNC=nCondLabel;
        String condd=condLabel;
        addIR("br label %"+condd+"\n");
        addIR(condd+":\n");
        visitCond(ctx.cond());
        addIR(ee+":\n");
        visitStmt(ctx.stmt());
        addIR("br label %"+condd+"\n");
        addIR(oo+":\n");
        exeLabel=ee;
        outLabel=oo;
        condLabel=condd;
        nOutLabel=preNO;
        nCondLabel=preNC;
        return null;
    }
    @Override public Void visitContinueStmt(sysyParser.ContinueStmtContext ctx) {
        sonRet=true;
        addIR("br label %"+nCondLabel+"\n");
        return null;
    }
    @Override public Void visitBreakStmt(sysyParser.BreakStmtContext ctx) {
        sonRet=true;
        addIR("br label %"+nOutLabel+"\n");
        return null;
    }
    @Override public Void visitLVal(sysyParser.LValContext ctx) {
        if(ctx.L_BRACKT().isEmpty()) {
            if(typeMap.get(ctx.IDENT().getText())<4)System.exit(-12354);
            if (inFuncDef) {
                if (idToAd.get(funcNow - 1).containsKey(ctx.IDENT().getText())) {
                    if (isConst) System.exit(-54);
                    String tmpRam = idToAd.get(funcNow - 1).get(ctx.IDENT().getText());
                    String newRam = randomRam();
                    addIR(newRam + " = load i32, i32* " + tmpRam + "\n");
                    sonRam = newRam;
                    sonIsRam = true;
                } else {
                    String nowVar = ctx.IDENT().getText();
                    if (!constList.get(funcNow - 1).containsKey(ctx.IDENT().getText())) {
                        if (globalConst.containsKey(nowVar)) {
                            sonIsRam = false;
                            sonAns = globalConst.get(nowVar);
                        } else if (globalVar.containsKey(nowVar)) {
                            String tmpRam = "@" + nowVar;
                            String newRam = randomRam();
                            addIR(newRam + " = load i32, i32* " + tmpRam + "\n");
                            sonRam = newRam;
                            sonIsRam = true;
                        } else System.exit(-32565);
                    } else {
                        sonIsRam = false;
                        sonAns = constList.get(funcNow - 1).get(ctx.IDENT().getText());
                    }
                }
            } else {
                String nowVar = ctx.IDENT().getText();
                if (globalVar.containsKey(nowVar)) {
                    sonIsRam = true;
                    System.exit(-31232);
                } else if (globalConst.containsKey(nowVar)) {
                    sonIsRam = false;
                    sonAns = globalConst.get(nowVar);
                }else System.exit(-1254);
            }
        }else{
            if(typeMap.get(ctx.IDENT().getText())>4)System.exit(-12354);
            String nowId=ctx.IDENT().getText();
            if(varAtoId.containsKey(nowId)){
                int pos=varAtoId.get(nowId);
                if(ctx.L_BRACKT().size()!=varArraySize.get(pos).size())System.exit(-5486);
                else{
                    int n=ctx.exp().size();
                    String preRam = randomRam();
                    for(int i=0;i<n;i++){
                        sonIsRam=false;
                        visitExp(ctx.exp(i));
                        if(i==0){
                            if(sonIsRam) addIR("store i32 " + sonRam + " , i32 *" + preRam + "\n");
                            else addIR("store i32 " + sonAns + " , i32 *" + preRam + "\n");
                            String tmpram;
                            String newRam=randomRam();
                            addIR(newRam+" = load i32, i32* "+preRam+"\n");
                            for(int j=i+1;j<n;j++){
                                tmpram=randomRam();
                                addIR(tmpram+" = mul i32 "+newRam+" , "+varArraySize.get(pos).get(j)+"\n");
                                newRam=tmpram;
                            }
                            preRam=newRam;
                            continue;
                        }
                        if(sonIsRam){
                            String newRam=sonRam;
                            String tmpram;
                            for(int j=i+1;j<n;j++){
                                tmpram=randomRam();
                                addIR(tmpram+" = mul i32 "+newRam+" , "+varArraySize.get(pos).get(j)+"\n");
                                newRam=tmpram;
                            }
                            tmpram = randomRam();
                            addIR(tmpram+" = add i32 "+preRam+" , "+newRam+"\n");
                            preRam=tmpram;
                        }else{
                            String newRam=randomRam();
                            String tmpram;
                            addIR("store i32 "+sonAns+" , i32 * "+newRam+"\n");
                            String anoRam = randomRam();
                            addIR(anoRam +" = load i32,i32 * "+newRam+ "\n");
                            newRam=anoRam;
                            for(int j=i+1;j<n;j++){
                                tmpram=randomRam();
                                addIR(tmpram+" = mul i32 "+newRam+" , "+varArraySize.get(pos).get(j)+"\n");
                                newRam=tmpram;
                            }
                            tmpram = randomRam();
                            addIR(tmpram+" = add i32 "+preRam+" , "+newRam+"\n");
                            preRam=tmpram;
                        }
                    }
                    sonIsRam=true;
                    int mul=1;
                    for(int i=0;i<varArraySize.get(pos).size();i++){
                        mul*=varArraySize.get(pos).get(i);
                    }
                    String iniRam=randomRam();
                    String anoRam;
                    if(arrToAd.get(funcNow-1).containsKey(nowId)){
                        anoRam=arrToAd.get(funcNow-1).get(nowId);
                    }else{
                        anoRam="@"+nowId;
                    }
                    addIR(iniRam+" = getelementptr ["+mul+" x i32], ["+mul+" x i32]* "+anoRam+" , i32 0, i32 0\n");
                    String nowRam = randomRam();
                    addIR(nowRam+" = getelementptr i32, i32* "+iniRam+", i32 "+preRam+"\n");
                    String retRam =randomRam();
                    addIR(retRam+" = load i32,i32 * "+nowRam+"\n");
                    sonRam=retRam;
                }
            }else if(constAtoId.containsKey(nowId)){
                int pos=varAtoId.get(nowId);
                if(ctx.L_BRACKT().size()!=constArraySize.get(pos).size())System.exit(-5486);
                else{
                    int n=ctx.exp().size();
                    String preRam = randomRam();
                    for(int i=0;i<n;i++){
                        sonIsRam=false;
                        visitExp(ctx.exp(i));
                        if(i==0){
                            if(sonIsRam) addIR("store i32 " + sonRam + " , i32 *" + preRam + "\n");
                            else addIR("store i32 " + sonAns + " , i32 *" + preRam + "\n");
                            String tmpram;
                            String newRam=randomRam();
                            addIR(newRam+" = load i32, i32* "+preRam+"\n");
                            for(int j=i+1;j<n;j++){
                                tmpram=randomRam();
                                addIR(tmpram+" = mul i32 "+newRam+" , "+constArraySize.get(pos).get(j)+"\n");
                                newRam=tmpram;
                            }
                            preRam=newRam;
                            continue;
                        }
                        if(sonIsRam){
                            String newRam=sonRam;
                            String tmpram;
                            for(int j=i+1;j<n;j++){
                                tmpram=randomRam();
                                addIR(tmpram+" = mul i32 "+newRam+" , "+constArraySize.get(pos).get(j)+"\n");
                                newRam=tmpram;
                            }
                            tmpram = randomRam();
                            addIR(tmpram+" = add i32 "+preRam+" , "+newRam+"\n");
                            preRam=tmpram;
                        }else{
                            String newRam = randomRam();
                            String tmpram;
                            addIR("store i32 "+sonAns+" , i32 * "+newRam+"\n");
                            String anoRam = randomRam();
                            addIR(anoRam +" = load i32,i32 * "+newRam+ "\n");
                            newRam=anoRam;
                            for(int j=i+1;j<n;j++){
                                tmpram=randomRam();
                                addIR(tmpram+" = mul i32 "+newRam+" , "+constArraySize.get(pos).get(j)+"\n");
                                newRam=tmpram;
                            }
                            tmpram = randomRam();
                            addIR(tmpram+" = add i32 "+preRam+" , "+newRam+"\n");
                            preRam=tmpram;
                        }
                    }
                    sonIsRam=true;
                    int mul=1;
                    for(int i=0;i<constArraySize.get(pos).size();i++){
                        mul*=constArraySize.get(pos).get(i);
                    }
                    String iniRam=randomRam();
                    String anoRam;
                    if(constArrToAd.get(funcNow-1).containsKey(nowId)){
                        anoRam=constArrToAd.get(funcNow-1).get(nowId);
                    }else{
                        anoRam="@"+nowId;
                    }
                    addIR(iniRam+" = getelementptr ["+mul+" x i32], ["+mul+" x i32]* "+anoRam+" , i32 0, i32 0\n");
                    String nowRam = randomRam();
                    addIR(nowRam+" = getelementptr i32, i32* "+iniRam+", i32 "+preRam+"\n");
                    String retRam =randomRam();
                    addIR(retRam+" = load i32,i32 * "+nowRam+ "\n");
                    sonRam=retRam;
                }
            }else System.exit(-1256);
        }
        return null;
    }
}
