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
public class ImplementationUtil {

    /**
     * Main method runs the program
     */

    public static void main(String[] args) {
        //TO RUN: run-main edu.nyu.oop.ImplementationUtil ***
        // *** a number 0-20, or nothing to run all test cases
        int start = 0;
        int end = 0;

        start = end = 12;

        if (args.length > 0) {
            int value = ImplementationUtil.getInteger(args[0]);
            if (value >= 0) {
                start = value;
                end = value;
            }
        }

        for (int i = start; i <= end; i++) {
            String filePath = String.format("./src/test/java/inputs/test%03d/Test%03d.java", i, i);
            GNode root = (GNode) loadTestFile(filePath);
            AstTraversal visitor1 = new AstTraversal(newRuntime());

            // PHASE 1: obtain the traversal summary
            AstTraversal.AstTraversalSummary summaryTraversal = visitor1.getTraversal(root);

            // PHASE 2: construct the c++ header Ast
            GNode headerNode = HeaderAst.getHeaderAst(summaryTraversal).parent;
            //prettyPrintAst(headerNode);

            // PHASE 3: create the header code
            //TODO: check this one
            PrintHeaderFile.headerFileSummary headerFileSummary = new PrintHeaderFile(newRuntime(), summaryTraversal).getSummary(headerNode);

            // PHASE 4: mutate the traversed tree
            new AstMutator(newRuntime()).mutate(root);

            // PHASE 5: create the cpp code
            //TODO: this one
            PrintCppFile.cppFileSummary cppSummary = new PrintCppFile(newRuntime(), summaryTraversal).getSummary(root);

            // PHASE 5: create the main code
            //TODO: finish this one
            PrintMainFile.printMainFileSummary mainSummary = new PrintMainFile(newRuntime(), summaryTraversal).getSummary(root);

            try {
                String headerPath = String.format("./testOutputs/translationOutputs/test%03d/output.h", i);
                String outputPath = String.format("./testOutputs/translationOutputs/test%03d/output.cpp", i);
                String mainPath = String.format("./testOutputs/translationOutputs//test%03d/main.cpp", i);

                if (start == end) {
                    //running a single file will place it in output folder
                    headerPath = "./output/output.h";
                    outputPath = "./output/output.cpp";
                    mainPath = "./output/main.cpp";
                }
                File header = new File(headerPath);
                File output = new File(outputPath);
                File main = new File(mainPath);

                main.getParentFile().mkdirs();

                FileWriter printHeader = new FileWriter(header);
                printHeader.write(headerFileSummary.code.toString());
                printHeader.flush();
                printHeader.close();
                out.println("Printed "+header.getPath());

                FileWriter printOutput = new FileWriter(output);
                printOutput.write(cppSummary.code.toString());
                printOutput.flush();
                printOutput.close();
                out.println("Printed "+output.getPath());

                FileWriter printMain = new FileWriter(main);
                printMain.write(mainSummary.filePrinted);
                printMain.flush();
                printMain.close();
                out.println("Printed "+main.getPath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
}
