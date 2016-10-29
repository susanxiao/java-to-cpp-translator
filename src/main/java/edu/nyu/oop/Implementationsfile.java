package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.lang.JavaEntities;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Printer;
import xtc.util.Runtime;

import java.io.*;

//import static com.sun.tools.javac.util.Constants.format;
//import static java.lang.Integer.parseInt;
import static java.lang.System.out;

/**
 * Created by Garrett on 10/17/16.
 */
public class Implementationsfile {

    /**
     * Main method runs the program
     */

    public static void main(String[] args) {

        StringBuilder s = new StringBuilder();
        s.append("./src/test/java/inputs/");
        StringBuilder s1 = new StringBuilder(args[0]);
        StringBuilder s2 = new StringBuilder();
        s2 = s1;
        s.append(s1);
        s2.setCharAt(0, 'T');
        s.append("/" + s2);
        s.append(".java");
        String filePath = s.toString();
        GNode node = (GNode) LoadFileImplementations.loadTestFile(filePath);
        AstTraversal visitor1 = new AstTraversal(LoadFileImplementations.newRuntime());

        // obtain the traversal summary
        AstTraversal.AstTraversalSummary summaryTraversal = visitor1.getTraversal(node);

        // c++ Ast
        GNode parentNode = AstC.cAst(summaryTraversal);

        try {
            PrintWriter printerHeader;
            PrintWriter printerCppFile;
            PrintWriter printerMainFile;

            File header;
            File output;
            File main;

            // printing the header file
            printHeaderFile visitor = new printHeaderFile(LoadFileImplementations.newRuntime(), summaryTraversal);
            printHeaderFile.headerFileSummary summary = visitor.getSummary(parentNode);
            String headerFile = "";
            headerFile += summary.headerGuard + summary.usingNamespace + summary.namespace;
            headerFile += summary.fowardDeclarations + summary.typeDefs + summary.structs + summary.closeNameSpace;

            header = new File("output", "output.h");
            header.createNewFile();
            printerHeader = new PrintWriter(header);
            printerHeader.println(headerFile);
            printerHeader.flush();
            printerHeader.close();
            out.println("output.h printed\n");

            // get the mutated tree
            AstMutator visitorMutator = new AstMutator(LoadFileImplementations.newRuntime());
            AstMutator.AstMutatorSummary summaryMutator = visitorMutator.getMutator(node);

            // get the summary of the cpp implementations
            printCppFile visitorCpp = new printCppFile(LoadFileImplementations.newRuntime(), summaryTraversal);
            printCppFile.printCppFileSummary summaryCpp = visitorCpp.getSummary(node);
            String cppFile = summaryCpp.filePrinted;

            output = new File("output", "output.cpp");
            output.createNewFile();
            printerCppFile = new PrintWriter(output);
            printerCppFile.println(cppFile);
            printerCppFile.flush();
            printerCppFile.close();
            out.println("output.cpp printed\n");

            // get the summary of the main implementations
            printMainFile visitorMain = new printMainFile(LoadFileImplementations.newRuntime(), summaryTraversal);
            printMainFile.printMainFileSummary summaryMain = visitorMain.getSummary(node);
            String mainFile = summaryMain.filePrinted;

            main = new File("output", "main.cpp");
            main.createNewFile();
            printerMainFile = new PrintWriter(main);
            printerMainFile.println(mainFile);
            printerMainFile.flush();
            printerMainFile.close();
            out.println("main.cpp printed\n");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class LoadFileImplementations {
    public static Runtime newRuntime() {
        Runtime runtime = new Runtime();
        runtime.initDefaultValues();
        runtime.dir("in", Runtime.INPUT_DIRECTORY, true, "");
        runtime.setValue(Runtime.INPUT_DIRECTORY, JavaEntities.TEMP_DIR);
        return runtime;
    }

    public static void prettyPrintAst(Node node) {
        newRuntime().console().format(node).pln().flush();
    }

    public static void prettyPrintToFile(File f, Node node) {
        try {
            f.getParentFile().mkdirs();
            f.createNewFile();
            Printer console = new Printer(new FileWriter(f));
            console.format(node).pln().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Node loadTestFile(String filename) {
        File file = new File(filename);
        return NodeUtil.parseJavaFile(file);
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

}
