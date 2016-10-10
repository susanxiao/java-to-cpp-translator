package edu.nyu.oop;

import org.junit.*;
import xtc.tree.GNode;

import static org.junit.Assert.*;

public class implementationTest {

    private static GNode node = null;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Executing implementation");
        node = (GNode) XtcTestUtils.loadTestFile("/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test000/Test000.java");
        // XtcTestUtils.prettyPrintAst(node);
    }

    @Test
    public void testMethodSummary() {
        implementation visitor = new implementation();
        implementation.ClassSummary summary = visitor.getSummary(node);
        System.out.println(summary.classNames);  //the class names
        System.out.println(summary.names); //the method names
    }

}
