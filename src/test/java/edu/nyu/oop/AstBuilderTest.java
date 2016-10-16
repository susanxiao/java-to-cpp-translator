package edu.nyu.oop;

import org.junit.BeforeClass;
import org.junit.Test;
import xtc.tree.GNode;
import static java.lang.System.out;

import static org.junit.Assert.assertEquals;

/**
 * Created by Garrett on 10/12/16.
 */
public class AstBuilderTest {
    private static GNode node = null;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Executing AstBuilder");
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test001/Test001.java");
        // XtcTestUtils.prettyPrintAst(node);
    }

    @Test
    public void testInput001() {
        AstBuilder visitor = new AstBuilder(XtcTestUtils.newRuntime());
        AstBuilder.AstBuilderSummary summary = visitor.getClassDeclarations(node);


    }
}
