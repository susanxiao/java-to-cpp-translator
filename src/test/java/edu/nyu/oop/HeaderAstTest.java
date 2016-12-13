package edu.nyu.oop;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import xtc.tree.GNode;

import java.io.File;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;

/**
 * Created by Garrett on 10/24/16.
 */
public class HeaderAstTest {


    @BeforeClass
    public static void beforeClass() {
        out.println("Executing HeaderAst");
    }

    @Test
    public void test000() {
        out.println("*********************** Test 000 ***********************");
        String path = "./src/test/java/inputs/test000/Test000.java";
        GNode node = (GNode) XtcTestUtils.loadTestFile(path);
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assertEquals(n.numberClasses,0);
    }

    @Test
    public void test001() {
        out.println("*********************** Test 001 ***********************");
        String path = "./src/test/java/inputs/test001/Test001.java";
        GNode node = (GNode) XtcTestUtils.loadTestFile(path);
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);
    }

    @Test
    public void test002() {
        out.println("*********************** Test 002 ***********************");
        String path = "./src/test/java/inputs/test002/Test002.java";
        GNode node = (GNode) XtcTestUtils.loadTestFile(path);
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);
    }

    @Test
    public void test003() {
        System.out.println("*********************** Test 003 ***********************");
        String path = "./src/test/java/inputs/test003/Test003.java";
        GNode node = (GNode) XtcTestUtils.loadTestFile(path);
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);;
    }

    @Test
    public void test004() {
        System.out.println("*********************** Test 004 ***********************");
        String path = "./src/test/java/inputs/test004/Test004.java";
        GNode node = (GNode) XtcTestUtils.loadTestFile(path);
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);
        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);
    }

    @Test
    public void test005() {
        System.out.println("*********************** Test 005 ***********************");
        String path = "./src/test/java/inputs/test005/Test005.java";
        GNode node = (GNode) XtcTestUtils.loadTestFile(path);
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,2);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);
        int countB = n.classMethodCounts.get("B");
        assertEquals(countB,1);
    }

    @Test
    public void test006() {
        System.out.println("*********************** Test 006 ***********************");
        String path = "./src/test/java/inputs/test006/Test006.java";
        GNode node = (GNode) XtcTestUtils.loadTestFile(path);
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,3);
    }

    @Test
    public void test007() {
        System.out.println("*********************** Test 007 ***********************");
        String path = "./src/test/java/inputs/test007/Test007.java";
        GNode node = (GNode) XtcTestUtils.loadTestFile(path);
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,2);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,0);
        int countB = n.classMethodCounts.get("B");
        assertEquals(countB,0);

    }

    @Test
    public void test008() {
        System.out.println("*********************** Test 008 ***********************");

        String path = "./src/test/java/inputs/test008/Test008.java";
        GNode node = (GNode) XtcTestUtils.loadTestFile(path);
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,2);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,0);
        int countB = n.classMethodCounts.get("B");
        assertEquals(countB,0);
    }

    @Test
    public void test009() {
        System.out.println("*********************** Test 009 ***********************");
        String path = "./src/test/java/inputs/test009/Test009.java";
        GNode node = (GNode) XtcTestUtils.loadTestFile(path);
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,0);
    }

    @Test
    public void test010() {
        System.out.println("*********************** Test 010 ***********************");
        String path = "./src/test/java/inputs/test010/Test010.java";
        GNode node = (GNode) XtcTestUtils.loadTestFile(path);
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,4);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,3);
        int countB1 = n.classMethodCounts.get("B1");
        assertEquals(countB1,0);
        int countB2 = n.classMethodCounts.get("B2");
        assertEquals(countB2,0);
        int countC = n.classMethodCounts.get("C");
        assertEquals(countC,0);

    }

    @Test
    public void test011() {
        System.out.println("*********************** Test 011 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test011/Test011.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,4);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,3);
        int countB1 = n.classMethodCounts.get("B1");
        assertEquals(countB1,0);
        int countB2 = n.classMethodCounts.get("B2");
        assertEquals(countB2,0);
        int countC = n.classMethodCounts.get("C");
        assertEquals(countC,0);
    }

    @Test
    public void test012() {
        System.out.println("*********************** Test 012 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test012/Test012.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,4);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,3);
        int countB1 = n.classMethodCounts.get("B1");
        assertEquals(countB1,0);
        int countB2 = n.classMethodCounts.get("B2");
        assertEquals(countB2,0);
        int countC = n.classMethodCounts.get("C");
        assertEquals(countC,1);
    }

    @Test
    public void test013() {
        System.out.println("*********************** Test 013 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test013/Test013.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,2);

    }

    @Test
    public void test014() {
        System.out.println("*********************** Test 014 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test014/Test014.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);

    }

    @Test
    public void test015() {
        System.out.println("*********************** Test 015 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test015/Test015.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,2);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);
        int countB = n.classMethodCounts.get("B");
        assertEquals(countB,2);
    }

    @Test
    public void test016() {
        System.out.println("*********************** Test 016 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test016/Test016.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,2);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);
        int countB = n.classMethodCounts.get("B");
        assertEquals(countB,2);

    }

    @Test
    public void test017() {
        System.out.println("*********************** Test 017 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test017/Test017.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);

    }

    @Test
    public void test018() {
        System.out.println("*********************** Test 018 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test018/Test018.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,0);

    }

    @Test
    public void test019() {
        System.out.println("*********************** Test 019 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test019/Test019.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,0);

    }

    @Test
    public void test020() {
        System.out.println("*********************** Test 020 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test020/Test020.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);

    }

    @Test
    public void test021() {
        System.out.println("*********************** Test 021 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test021/Test021.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,0);

    }

    @Test
    public void test022() {
        System.out.println("*********************** Test 022 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test022/Test022.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,0);

    }

    @Test
    public void test023() {
        System.out.println("*********************** Test 023 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test023/Test023.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,0);
    }

    @Test
    public void test024() {
        System.out.println("*********************** Test 024 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test024/Test024.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);

    }

    @Test
    public void test025() {
        System.out.println("*********************** Test 025 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test025/Test025.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,2);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);
        int countB = n.classMethodCounts.get("B");
        assertEquals(countB,1);
    }

    @Test
    public void test026() {
        System.out.println("*********************** Test 026 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test026/Test026.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,2);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);
        int countB = n.classMethodCounts.get("B");
        assertEquals(countB,1);
    }

    @Test
    public void test027() {
        System.out.println("*********************** Test 027 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test027/Test027.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);

    }

    @Test
    public void test028() {
        System.out.println("*********************** Test 028 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test028/Test028.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);

    }

    @Test
    public void test029() {
        System.out.println("*********************** Test 029 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test029/Test029.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);
    }

    @Test
    public void test030() {
        System.out.println("*********************** Test 030 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test030/Test030.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA,1);
    }

    @Test
    public void test031() {
        System.out.println("*********************** Test 031 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test031/Test031.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,0);
    }

    @Test
    public void test032() {
        System.out.println("*********************** Test 032 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test032/Test032.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA, 6);

    }

    @Test
    public void test033() {
        System.out.println("*********************** Test 033 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test033/Test033.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA, 6);

    }

    @Test
    public void test034() {
        System.out.println("*********************** Test 034 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test034/Test034.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA, 3);

    }

    @Test
    public void test035() {
        System.out.println("*********************** Test 035 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test035/Test035.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        HeaderAst.HeaderAstSummary n = HeaderAst.getHeaderAst(summary);

        assertEquals(n.numberClasses,1);
        int countA = n.classMethodCounts.get("A");
        assertEquals(countA, 2);

    }

}
