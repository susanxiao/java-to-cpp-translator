package edu.nyu.oop;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import xtc.tree.GNode;

import static org.junit.Assert.assertEquals;

/**
 * Created by Garrett on 10/12/16.
 */
public class AstTraversalTest {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Executing AstTraversal");
    }

    @Test
    public void test000() {
        System.out.println("*********************** Test 000 ***********************");
        GNode node;
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test000/Test000.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 1);
        assert (summary.classMethodCounts.get("Test000").equals(1));
    }

    @Test
    public void test001() {
        System.out.println("*********************** Test 001 ***********************");
        GNode node;
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test001/Test001.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 2);
        assert (summary.classMethodCounts.get("A").equals(1));
        assert (summary.classMethodCounts.get("Test001").equals(1));
    }

    @Test
    public void test002() {
        System.out.println("*********************** Test 002 ***********************");
        GNode node;
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test002/Test002.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 2);
        assert (summary.classMethodCounts.get("A").equals(1));
        assert (summary.classMethodCounts.get("Test002").equals(1));
    }

    @Test
    public void test003() {
        System.out.println("*********************** Test 003 ***********************");
        GNode node;
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test003/Test003.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 2);
        assert (summary.classMethodCounts.get("A").equals(1));
        assert (summary.classMethodCounts.get("Test003").equals(1));
    }

    @Test
    public void test004() {
        System.out.println("*********************** Test 004 ***********************");
        GNode node;
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test004/Test004.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 2);
        assert (summary.classMethodCounts.get("A").equals(1));
        assert (summary.classMethodCounts.get("Test004").equals(1));
    }

    @Test
    public void test005() {
        System.out.println("*********************** Test 005 ***********************");
        GNode node;
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test005/Test005.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 3);
        assert (summary.classMethodCounts.get("A").equals(1));
        assert (summary.classMethodCounts.get("B").equals(1));
        assert (summary.classMethodCounts.get("Test005").equals(1));
    }

    @Test
    public void test006() {
        System.out.println("*********************** Test 006 ***********************");
        GNode node;
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test006/Test006.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 2);
        assert (summary.classMethodCounts.get("A").equals(3));
        assert (summary.classMethodCounts.get("Test006").equals(1));
    }

    @Test
    public void test007() {
        System.out.println("*********************** Test 007 ***********************");
        GNode node;
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test007/Test007.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 3);
        assert (summary.classMethodCounts.get("A").equals(0));
        assert (summary.classMethodCounts.get("B").equals(0));
        assert (summary.classMethodCounts.get("Test007").equals(1));
    }

    @Test
    public void test008() {
        System.out.println("*********************** Test 008 ***********************");
        GNode node;
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test008/Test008.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 3);
        assert (summary.classMethodCounts.get("A").equals(0));
        assert (summary.classMethodCounts.get("B").equals(0));
        assert (summary.classMethodCounts.get("Test008").equals(1));
    }

    @Test
    public void test009() {
        System.out.println("*********************** Test 009 ***********************");
        GNode node;
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test009/Test009.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 2);
        assert (summary.classMethodCounts.get("A").equals(0));
        assert (summary.classMethodCounts.get("Test009").equals(1));
    }

    @Test
    public void test010() {
        System.out.println("*********************** Test 010 ***********************");
        GNode node;
        node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test010/Test010.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 5);
        assert (summary.classMethodCounts.get("A").equals(3));
        assert (summary.classMethodCounts.get("B1").equals(0));
        assert (summary.classMethodCounts.get("B2").equals(0));
        assert (summary.classMethodCounts.get("C").equals(0));
        assert (summary.classMethodCounts.get("Test010").equals(1));
    }

    @Ignore
    @Test
    public void test011() {
        System.out.println("*********************** Test 011 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test011/Test011.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 5);
        assert (summary.classMethodCounts.get("A").equals(3));
        assert (summary.classMethodCounts.get("B1").equals(0));
        assert (summary.classMethodCounts.get("B2").equals(0));
        assert (summary.classMethodCounts.get("C").equals(0));
        assert (summary.classMethodCounts.get("Test011").equals(1));
    }

    @Test
    public void test012() {
        System.out.println("*********************** Test 012 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test012/Test012.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 5);
        assert (summary.classMethodCounts.get("A").equals(3));
        assert (summary.classMethodCounts.get("B1").equals(0));
        assert (summary.classMethodCounts.get("B2").equals(0));
        assert (summary.classMethodCounts.get("C").equals(1));
        assert (summary.classMethodCounts.get("Test012").equals(1));
    }

    @Test
    public void test013() {
        System.out.println("*********************** Test 013 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test013/Test013.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 2);
        assert (summary.classMethodCounts.get("A").equals(2));
        assert (summary.classMethodCounts.get("Test013").equals(1));
    }

    @Test
    public void test014() {
        System.out.println("*********************** Test 014 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test014/Test014.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 2);
        assert (summary.classMethodCounts.get("A").equals(1));
        assert (summary.classMethodCounts.get("Test014").equals(1));
    }

    @Test
    public void test015() {
        System.out.println("*********************** Test 015 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test015/Test015.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 3);
        assert (summary.classMethodCounts.get("A").equals(1));
        assert (summary.classMethodCounts.get("B").equals(2));
        assert (summary.classMethodCounts.get("Test015").equals(1));
    }

    @Test
    public void test016() {
        System.out.println("*********************** Test 016 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test016/Test016.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 3);
        assert (summary.classMethodCounts.get("A").equals(1));
        assert (summary.classMethodCounts.get("B").equals(2));
        assert (summary.classMethodCounts.get("Test016").equals(1));
    }

    @Test
    public void test017() {
        System.out.println("*********************** Test 017 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test017/Test017.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
        assert (summary.classCount == 2);
        assert (summary.classMethodCounts.get("A").equals(2));
        assert (summary.classMethodCounts.get("Test017").equals(1));
    }

    @Ignore
    @Test
    public void test018() {
        System.out.println("*********************** Test 018 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test018/Test018.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
    }

    @Ignore
    @Test
    public void test019() {
        System.out.println("*********************** Test 019 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test019/Test019.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
    }

    @Ignore
    @Test
    public void test020() {
        System.out.println("*********************** Test 020 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test020/Test020.java");
        AstTraversal visitor = new AstTraversal(XtcTestUtils.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);
    }
}