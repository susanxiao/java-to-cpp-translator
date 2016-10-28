package edu.nyu.oop;

import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.out;

/**
 * Created by Garrett on 10/21/16.
 */
public class printMainFile extends Visitor {


    private printMainFile.printMainFileSummary summary = new printMainFile.printMainFileSummary();
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Runtime runtime;
    private AstTraversal.AstTraversalSummary summaryTraversal;

    StringBuilder mainImplementation = new StringBuilder();

    // visitXXX methods

    // visitMethod
    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) {
                dispatch((Node) o);
            }
        }
    }


    public printMainFile(Runtime runtime, AstTraversal.AstTraversalSummary summaryTraversal) {
        this.runtime = runtime;
        this.summaryTraversal = summaryTraversal;
    }

    static class printMainFileSummary {
        String currentClassName;
        String filePrinted;
    }

    public printMainFile.printMainFileSummary getSummary(GNode n) {
        StringBuilder s1 = new StringBuilder();

        s1.append("\n------------------\n\n");

        s1.append("#include <iostream>\n#include <sstream>\n");
        s1.append("#include \"java_lang.h\"\n\n");
        s1.append("#include \"output.h\"\n");
        s1.append("\n" +
                "using namespace java::lang;\n" +
                "using namespace inputs::javalang;\n" +
                "using namespace std;\n\n");
        s1.append("int main(void)\n{\n\n");



        // append the return statement
        s1.append("\treturn 0;\n");
        s1.append("}");

        s1.append("\n\n------------------\n\n");

        out.println(s1.toString());

        summary.filePrinted = s1.toString();

        return summary;
    }


    public static void main(String[] args) {
        GNode node = (GNode) LoadFileImplementations.loadTestFile("./src/test/java/inputs/test001/Test001.java");

        // get the summary traversal (class implementations)
        AstTraversal visitorTraversal = new AstTraversal(LoadFileImplementations.newRuntime());
        AstTraversal.AstTraversalSummary summaryTraversal = visitorTraversal.getTraversal(node);

        //LoadFileImplementations.prettyPrintAst(node);
        for (int i = 0; i < 2; i++) {
            String test = "./src/test/java/inputs/";
            String test1 = "";
            String test2 = "";
            if (i < 10) {
                test1 = "test00" + i;
                test2 = "Test00" + i;
            } else {
                test1 = "test0" + i;
                test2 = "Test0" + i;
            }
            test += test1;
            test += "/" + test2 + ".java";
            try {
                PrintWriter printerMain;

                File main;

                // get the mutated tree
                AstMutator visitor1 = new AstMutator(LoadFileImplementations.newRuntime());
                AstMutator.AstMutatorSummary summary = visitor1.getMutator(node);

                // get the summary of the cpp implementations
                printMainFile visitor = new printMainFile(LoadFileImplementations.newRuntime(), summaryTraversal);
                printMainFile.printMainFileSummary summaryMain = visitor.getSummary(node);

                String mainFile = "";
                mainFile += summaryMain.filePrinted;

                main = new File("testOutputs/mainFileOutputs", test2);
                main.createNewFile();

                printerMain = new PrintWriter(main);
                printerMain.println(mainFile);
                printerMain.flush();
                printerMain.close();
                out.println("main " + i + " printed\n");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
