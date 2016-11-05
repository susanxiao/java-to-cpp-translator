package edu.nyu.oop;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import xtc.tree.GNode;

import java.io.File;

/**
 * Created by Garrett on 10/12/16.
 */
public class AstMutatorTest {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Executing AstMutator");
        // node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test005/Test005.java");
        // XtcTestUtils.prettyPrintAst(node);
    }

    @Test
    public void test000() {
        System.out.println("*********************** Test 000 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test000/Test000.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test000.txt"), node);
    }

    @Test
    public void test001() {
        System.out.println("*********************** Test 001 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test001/Test001.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test001.txt"), node);
    }

    @Ignore
    @Test
    public void test002() {
        System.out.println("*********************** Test 002 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test002/Test002.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test002.txt"), node);
        XtcTestUtils.prettyPrintAst(node);
    }

    @Test
    public void test003() {
        System.out.println("*********************** Test 003 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test003/Test003.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test003.txt"), node);
    }

    @Test
    public void test004() {
        System.out.println("*********************** Test 004 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test004/Test004.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test004.txt"), node);
    }

    @Test
    public void test005() {
        System.out.println("*********************** Test 005 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test005/Test005.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test005.txt"), node);
    }

    @Test
    public void test006() {
        System.out.println("*********************** Test 006 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test006/Test006.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test006.txt"), node);
    }

    @Test
    public void test007() {
        System.out.println("*********************** Test 007 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test007/Test007.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test007.txt"), node);
    }

    @Test
    public void test008() {
        System.out.println("*********************** Test 008 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test008/Test008.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test008.txt"), node);
    }

    @Test
    public void test009() {
        System.out.println("*********************** Test 009 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test009/Test009.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test009.txt"), node);
    }

    @Test
    public void test010() {
        System.out.println("*********************** Test 010 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test010/Test010.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test010.txt"), node);
    }

    @Test
    public void test011() {
        System.out.println("*********************** Test 011 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test011/Test011.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test011.txt"), node);
    }

    @Test
    public void test012() {
        System.out.println("*********************** Test 012 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test012/Test012.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test012.txt"), node);
    }

    @Test
    public void test013() {
        System.out.println("*********************** Test 013 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test013/Test013.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test013.txt"), node);
    }

    @Test
    public void test014() {
        System.out.println("*********************** Test 014 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test014/Test014.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test014.txt"), node);
    }

    @Test
    public void test015() {
        System.out.println("*********************** Test 015 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test015/Test015.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test015.txt"), node);
    }

    @Test
    public void test016() {
        System.out.println("*********************** Test 016 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test016/Test016.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test016.txt"), node);
    }

    @Test
    public void test017() {
        System.out.println("*********************** Test 017 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test017/Test017.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test017.txt"), node);
    }

    @Test
    public void test018() {
        System.out.println("*********************** Test 018 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test018/Test018.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test018.txt"), node);
    }

    @Test
    public void test019() {
        System.out.println("*********************** Test 019 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test019/Test019.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test019.txt"), node);
    }

    @Test
    public void test020() {
        System.out.println("*********************** Test 020 ***********************");
        GNode node = (GNode) XtcTestUtils.loadTestFile("./src/test/java/inputs/test020/Test020.java");
        AstMutator visitor = new AstMutator(XtcTestUtils.newRuntime());
        visitor.mutate(node);
        ImplementationUtil.prettyPrintToFile(new File("testOutputs/mutatedAstOutputs/test020.txt"), node);
    }
}