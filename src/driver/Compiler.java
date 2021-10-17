package driver;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import frontend.*;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import io.*;

public class Compiler {
    public static void solve(String[] code) throws IOException {
        //String s = IOUtils.readFromStream(System.in);
        String s = new String("int main(){\n" +
                "    return 0x32;\n" +
                "}");
        CharStream input = CharStreams.fromString(s);
        errorListener el = new errorListener();
        sysyLexer lexer = new sysyLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(el);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        sysyParser parser = new sysyParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(el);
        ParseTree tree = parser.program();
        Visitor vis = new Visitor();
        vis.visit(tree);
    }
}
