package edu.nyu.oop;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import xtc.tree.GNode;

import static java.lang.System.out;

/**
 * Created by Garrett on 10/24/16.
 */
public class HeaderAstTest {


    boolean debug = true;

    @BeforeClass
    public static void beforeClass() {
        out.println("Executing HeaderAst");
    }

    @Test
    public void test000() {
        out.println("*********************** Test 000 ***********************");
        String path = "/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test000/Test000.java";
        GNode node;
        if (debug) {
            node = (GNode) XtcTestUtils.loadTestFile(path);
        } else {
            node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test000/Test000.java");
        }
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assert (n.numberClasses == 0);
    }

    @Test
    public void test001() {
        out.println("*********************** Test 001 ***********************");
        String path = "/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test001/Test001.java";
        GNode node;
        if (debug) {
            node = (GNode) XtcTestUtils.loadTestFile(path);
        } else {
            node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test001/Test001.java");
        }
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assert (n.numberClasses == 1);
        assert (n.classMethodCounts.get("A") == 1);
    }

    @Ignore
    @Test
    public void test002() {
        out.println("*********************** Test 002 ***********************");
        String path = "/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test002/Test002.java";
        GNode node;
        if (debug) {
            node = (GNode) XtcTestUtils.loadTestFile(path);
        } else {
            node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test002/Test002.java");
        }
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assert (n.numberClasses == 1);
        assert (n.classMethodCounts.get("A") == 1);
    }

    @Test
    public void test003() {
        System.out.println("*********************** Test 003 ***********************");
        String path = "/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test003/Test003.java";
        GNode node;
        if (debug) {
            node = (GNode) XtcTestUtils.loadTestFile(path);
        } else {
            node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test003/Test003.java");
        }
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assert (n.numberClasses == 1);
        assert (n.classMethodCounts.get("A") == 1);
    }

    @Test
    public void test004() {
        System.out.println("*********************** Test 004 ***********************");
        String path = "/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test004/Test004.java";
        GNode node;
        if (debug) {
            node = (GNode) XtcTestUtils.loadTestFile(path);
        } else {
            node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test004/Test004.java");
        }
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assert (n.numberClasses == 1);
        assert (n.classMethodCounts.get("A") == 1);
    }

    @Test
    public void test005() {
        System.out.println("*********************** Test 005 ***********************");
        String path = "/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test005/Test005.java";
        GNode node;
        if (debug) {
            node = (GNode) XtcTestUtils.loadTestFile(path);
        } else {
            node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test005/Test005.java");
        }
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assert (n.numberClasses == 2);
        assert (n.classMethodCounts.get("A") == 1);
        assert (n.classMethodCounts.get("B") == 1);
    }

    @Test
    public void test006() {
        System.out.println("*********************** Test 006 ***********************");
        String path = "/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test006/Test006.java";
        GNode node;
        if (debug) {
            node = (GNode) XtcTestUtils.loadTestFile(path);
        } else {
            node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test006/Test006.java");
        }
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assert (n.numberClasses == 1);
        assert (n.classMethodCounts.get("A") == 3);
    }

    @Test
    public void test007() {
        System.out.println("*********************** Test 007 ***********************");
        String path = "/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test007/Test007.java";
        GNode node;
        if (debug) {
            node = (GNode) XtcTestUtils.loadTestFile(path);
        } else {
            node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test007/Test007.java");
        }
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assert (n.numberClasses == 2);
        assert (n.classMethodCounts.get("A") == 0);
        assert (n.classMethodCounts.get("B") == 0);

    }

    @Test
    public void test008() {
        System.out.println("*********************** Test 008 ***********************");
        String path = "/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test008/Test008.java";
        GNode node;
        if (debug) {
            node = (GNode) XtcTestUtils.loadTestFile(path);
        } else {
            node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test008/Test008.java");
        }
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assert (n.numberClasses == 2);
        assert (n.classMethodCounts.get("A") == 0);
        assert (n.classMethodCounts.get("B") == 0);
    }

    @Test
    public void test009() {
        System.out.println("*********************** Test 009 ***********************");
        String path = "/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test009/Test009.java";
        GNode node;
        if (debug) {
            node = (GNode) XtcTestUtils.loadTestFile(path);
        } else {
            node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test009/Test009.java");
        }
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assert (n.numberClasses == 1);
        assert (n.classMethodCounts.get("A") == 0);
    }

    @Test
    public void test010() {
        System.out.println("*********************** Test 010 ***********************");
        String path = "/Users/Garrett/Desktop/OOP/inClass/translator-5Tran/src/test/java/inputs/test010/Test010.java";
        GNode node;
        if (debug) {
            node = (GNode) XtcTestUtils.loadTestFile(path);
        } else {
            node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test010/Test010.java");
        }
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assert (n.numberClasses == 4);
        assert (n.classMethodCounts.get("A") == 3);
        assert (n.classMethodCounts.get("B1") == 0);
        assert (n.classMethodCounts.get("B2") == 0);
        assert (n.classMethodCounts.get("C") == 0);

    }

    @Ignore
    @Test
    public void test011() {
        System.out.println("*********************** Test 011 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test011/Test011.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        GNode n = HeaderAst.getHeaderAst(summary).parent;
        //XtcTestUtils.prettyPrintAst(n);
    }

    @Ignore
    @Test
    public void test012() {
        System.out.println("*********************** Test 012 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test012/Test012.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        GNode n = HeaderAst.getHeaderAst(summary).parent;
        XtcTestUtils.prettyPrintAst(n);
    }

    @Ignore
    @Test
    public void test013() {
        System.out.println("*********************** Test 013 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test013/Test013.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        GNode n = HeaderAst.getHeaderAst(summary).parent;
        //XtcTestUtils.prettyPrintAst(n);
    }

    @Ignore
    @Test
    public void test014() {
        System.out.println("*********************** Test 014 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test014/Test014.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        GNode n = HeaderAst.getHeaderAst(summary).parent;
        //XtcTestUtils.prettyPrintAst(n);
    }

    @Ignore
    @Test
    public void test015() {
        System.out.println("*********************** Test 015 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test015/Test015.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        GNode n = HeaderAst.getHeaderAst(summary).parent;
        //XtcTestUtils.prettyPrintAst(n);
    }

    @Ignore
    @Test
    public void test016() {
        System.out.println("*********************** Test 016 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test016/Test016.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        GNode n = HeaderAst.getHeaderAst(summary).parent;
        //XtcTestUtils.prettyPrintAst(n);
    }

    @Ignore
    @Test
    public void test017() {
        System.out.println("*********************** Test 017 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test017/Test017.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        GNode n = HeaderAst.getHeaderAst(summary).parent;
        //XtcTestUtils.prettyPrintAst(n);
    }

    @Ignore
    @Test
    public void test018() {
        System.out.println("*********************** Test 018 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test018/Test018.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        GNode n = HeaderAst.getHeaderAst(summary).parent;
        //XtcTestUtils.prettyPrintAst(n);
    }

    @Ignore
    @Test
    public void test019() {
        System.out.println("*********************** Test 019 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test019/Test019.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        GNode n = HeaderAst.getHeaderAst(summary).parent;
        //XtcTestUtils.prettyPrintAst(n);
    }

    @Ignore
    @Test
    public void test020() {
        System.out.println("*********************** Test 020 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test020/Test020.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        GNode n = HeaderAst.getHeaderAst(summary).parent;
        //XtcTestUtils.prettyPrintAst(n);
    }
}
