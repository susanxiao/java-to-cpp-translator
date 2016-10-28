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
public class printCppFile extends Visitor {

    private printCppFile.printCppFileSummary summary = new printCppFile.printCppFileSummary();
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Runtime runtime;
    private AstTraversal.AstTraversalSummary summaryTraversal;

    StringBuilder cppImplementation = new StringBuilder();

    // visitXXX methods
    public void visitClassDeclaration(GNode n) {
        if (n.getString(1).contains("Test")) {
            return;
        }
        summary.currentClassName = n.getString(1);
        String vptr = "";
        vptr += "\n\t__" + summary.currentClassName + "::";
        vptr += "__" + summary.currentClassName + "(" + ")";
        vptr += "  :  __vptr(&__vtable) {}";
        cppImplementation.append(vptr + "\n\n");

        visitClassBody((GNode) n.getNode(5));
    }

    public void visitClassBody(GNode n) {
        for (Object methods : n) {
            GNode currentMethod = (GNode) methods;
            if (currentMethod.getName().equals("MethodDeclaration")) {
                visitMethodDeclaration(currentMethod);
            }
        }
    }

    public void visitMethodDeclaration(GNode n) {
        String type = n.getNode(2).getNode(0).getString(0);
        String methodName = n.getString(3);
        // check if the method contains parameters
        if (n.getNode(4).size() > 0) {

        }
        Node methodBlock = n.getNode(7);
        for (Object o : methodBlock) {
            GNode currentNode = (GNode) o;
            if (currentNode.getName().equals("ReturnStatement")) {
                visitReturnStatement(currentNode);
            }
        }
    }

    public void visitReturnStatement(GNode n) {
        String returnStatement = "";

    }

    // visitMethod
    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) {
                dispatch((Node) o);
            }
        }
    }


    public printCppFile(Runtime runtime, AstTraversal.AstTraversalSummary summaryTraversal) {
        this.runtime = runtime;
        this.summaryTraversal = summaryTraversal;
    }

    static class printCppFileSummary {
        String currentClassName;
        String filePrinted;
    }

    public printCppFileSummary getSummary(GNode n) {
        StringBuilder s1 = new StringBuilder();
        s1.append("\n------------------\n\n");
        s1.append("#include \"output.h\"\n");
        s1.append("#include <sstream>\n\n");
        s1.append("using namespace java::lang;\n\n");
        // get the namespace
        Node packageNode = n.getNode(0).getNode(1);
        String namespace = "";
        int namespaceCounter = 0;
        for (Object packageName : packageNode) {
            for (int i = 0; i < namespaceCounter; i++) {
                namespace += "\t";
            }
            namespace += packageName.toString() + "{\n";
            namespaceCounter += 1;
        }
        s1.append(namespace);


        // visit the class declarations
        for (Object o : n) {
            GNode currentClass = (GNode) o;
            if (currentClass.getName().equals("ClassDeclaration")) {
                visitClassDeclaration(currentClass);
            }
        }

        s1.append(cppImplementation);

        // close the namespace
        s1.append("\n\n");
        String closeNamespace = "";
        while (namespaceCounter > 0) {
            for (int i = 0; i < namespaceCounter - 1; i++) {
                closeNamespace += ("\t");
            }
            closeNamespace += "}\n";
            namespaceCounter--;
        }
        s1.append(closeNamespace);
        s1.append("\n------------------\n\n");
        //out.println(s1.toString());

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
                PrintWriter printerOutput;

                File output;

                // get the mutated tree
                AstMutator visitor1 = new AstMutator(LoadFileImplementations.newRuntime());
                AstMutator.AstMutatorSummary summary = visitor1.getMutator(node);

                // get the summary of the cpp implementations
                printCppFile visitor = new printCppFile(LoadFileImplementations.newRuntime(), summaryTraversal);
                printCppFileSummary summaryCpp = visitor.getSummary(node);

                String outputCppFile = "";
                outputCppFile += summaryCpp.filePrinted;

                output = new File("testOutputs/printCppFile", test2);
                output.createNewFile();

                printerOutput = new PrintWriter(output);
                printerOutput.println(outputCppFile);
                printerOutput.flush();
                printerOutput.close();
                out.println("output " + i + " printed\n");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
