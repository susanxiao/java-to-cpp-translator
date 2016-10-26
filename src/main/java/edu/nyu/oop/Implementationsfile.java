package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.lang.JavaEntities;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.util.Runtime;

import java.io.File;
import java.util.ArrayList;

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

        //GNode node = (GNode) LoadFileImplementations.loadTestFile("./src/test/java/inputs/test000/Test000.java");
        //GNode node = (GNode) LoadFileImplementations.loadTestFile("./src/test/java/inputs/test001/Test001.java");
        //GNode node = (GNode) LoadFileImplementations.loadTestFile("./src/test/java/inputs/test002/Test002.java");
        //GNode node = (GNode) LoadFileImplementations.loadTestFile("./src/test/java/inputs/test003/Test003.java");
        //GNode node = (GNode) LoadFileImplementations.loadTestFile("./src/test/java/inputs/test004/Test004.java");
        //GNode node = (GNode) LoadFileImplementations.loadTestFile("./src/test/java/inputs/test005/Test005.java");
        GNode node = (GNode) LoadFileImplementations.loadTestFile("./src/test/java/inputs/test006/Test006.java");



        AstTraversal visitor = new AstTraversal(LoadFileImplementations.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
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

    public static Node loadTestFile(String filename) {
        File file = new File(filename);
        return NodeUtil.parseJavaFile(file);
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

}
