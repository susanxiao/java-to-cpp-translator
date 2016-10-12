package edu.nyu.oop;

import org.junit.BeforeClass;
import org.junit.Test;
import xtc.tree.GNode;

/**
 * Created by Garrett on 10/12/16.
 */
public class AstBuilderTest {
    private static GNode node = null;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Executing AstBuilder");
        node = (GNode) XtcTestUtils.loadTestFile("/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test001/Test001.java");
        // XtcTestUtils.prettyPrintAst(node);
    }

    @Test
    public void testClassesSummary() {
        AstBuilder visitor = new AstBuilder(XtcTestUtils.newRuntime());
        AstBuilder.AstBuilderSummary summary = visitor.getPackageDeclaration(node);
        AstBuilder.AstBuilderSummary summary1 = visitor.getClassDeclarations(node);

    }
}
