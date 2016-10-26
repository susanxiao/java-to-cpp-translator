package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.lang.JavaEntities;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.util.Runtime;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static com.sun.tools.javac.util.Constants.format;
import static java.lang.Integer.parseInt;
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

    public static Node loadTestFile(String filename) {
        File file = new File(filename);
        return NodeUtil.parseJavaFile(file);
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static void prettyPrintAst(Node node) {
        //return newRuntime().console().format(node).pln().flush();
    }
}
