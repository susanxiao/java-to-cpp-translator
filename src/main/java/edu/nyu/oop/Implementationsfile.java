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
        GNode parentNode = HeaderAst.ConstructHeaderAst(summaryTraversal);

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

            header = new File("output", "output.h");
            File header1 = new File("testOutputs/translationOutputs", s1.toString() + "output.h");
            header.createNewFile();
            header1.createNewFile();

            printerHeader = new PrintWriter(header);
            PrintWriter printerHeader1 = new PrintWriter(header1);

            printerHeader.println(summary.code.toString());
            printerHeader.flush();
            printerHeader.close();

            printerHeader1.println(summary.code.toString());
            printerHeader1.flush();
            printerHeader1.close();

            out.println("output.h printed\n");

            // get the mutated tree
            AstMutator visitorMutator = new AstMutator(LoadFileImplementations.newRuntime());
            AstMutator.AstMutatorSummary summaryMutator = visitorMutator.getMutator(node);

            // get the summary of the cpp implementations
            printCppFile visitorCpp = new printCppFile(LoadFileImplementations.newRuntime(), summaryTraversal);
            printCppFile.printCppFileSummary summaryCpp = visitorCpp.getSummary(node);
            String cppFile = summaryCpp.filePrinted;
            String cppFile1 = cppFile;

            output = new File("output", "output.cpp");
            output.createNewFile();


            File output1 = new File("testOutputs/translationOutputs", s1.toString() + "output.cpp");
            output1.createNewFile();

            PrintWriter printWriterCppFile1 = new PrintWriter(output1);
            printWriterCppFile1.println(cppFile1);
            printWriterCppFile1.flush();
            printWriterCppFile1.close();

            printerCppFile = new PrintWriter(output);
            printerCppFile.println(cppFile);
            printerCppFile.flush();
            printerCppFile.close();
            out.println("output.cpp printed\n");

            // get the summary of the main implementations
            printMainFile visitorMain = new printMainFile(LoadFileImplementations.newRuntime(), summaryTraversal);
            printMainFile.printMainFileSummary summaryMain = visitorMain.getSummary(node);
            String mainFile = summaryMain.filePrinted;

            String mainFile1 = mainFile;

            main = new File("output", "main.cpp");
            main.createNewFile();

            File main1 = new File("testOutputs/translationOutputs", s1.toString() + "main.cpp");

            PrintWriter printerMainFile1 = new PrintWriter(main1);
            printerMainFile1.println(mainFile1);
            printerMainFile1.flush();
            printerMainFile1.close();

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

    public static int getInteger(String val) {
        try {
            int value = Integer.parseInt(val);
            if (value >= 0 && value <= 20)
                return value;
            else return -1;
        }
        catch (Exception e) {
            return -1;
        }
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
