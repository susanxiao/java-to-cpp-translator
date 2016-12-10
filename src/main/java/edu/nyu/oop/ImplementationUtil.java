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
        int end = 20;

        if (args.length > 1) {
            start = ImplementationUtil.getInteger(args[0]);
            end = ImplementationUtil.getInteger(args[1]);
        } else if (args.length > 0) {
            int value = ImplementationUtil.getInteger(args[0]);
            if (value >= 0) {
                start = value;
                end = value;
            }
        }

        TranslationFacade translator = new TranslationFacade();

        for (int i = start; i <= end; i++) {

            String filePath = String.format("./src/test/java/inputs/test%03d/Test%03d.java", i, i);
            String path = String.format("./testOutputs/translationOutputs/test%03d/", i);

            translator.setControllers(filePath, path);
            translator.translate();

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
            if (value >= 0 && value <= 50)
                return value;
            else return -1;
        } catch (Exception e) {
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
