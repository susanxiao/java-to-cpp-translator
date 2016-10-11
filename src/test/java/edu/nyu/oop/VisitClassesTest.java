package edu.nyu.oop;

import org.junit.*;
import xtc.tree.GNode;

import static org.junit.Assert.*;

public class VisitClassesTest {

    private static GNode node = null;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Executing visitClasssesTest");
        node = (GNode) XtcTestUtils.loadTestFile("/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test000/Test000.java");
        // XtcTestUtils.prettyPrintAst(node);
    }

    @Test
    public void testClassesSummary() {
        VisitClasses visitor = new VisitClasses(XtcTestUtils.newRuntime());
        VisitClasses.VisitClassesSummary summary = visitor.getSummary(node);
        System.out.println(summary.ClassNames);
        System.out.println(summary.ClassesCount);
    }

}
