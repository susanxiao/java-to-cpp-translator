package edu.nyu.oop;

import xtc.lang.JavaEntities;
import xtc.tree.GNode;
import xtc.util.Runtime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static edu.nyu.oop.ImplementationUtil.loadTestFile;
import static edu.nyu.oop.ImplementationUtil.newRuntime;
import static java.lang.System.out;

/**
 * Created by Garrett on 12/9/16.
 */
class HeaderComponent {

    private String printPath = null;
    private GNode root = null;
    private PrintHeaderFile.headerFileSummary headerFileSummary = null;
    private AstTraversal.AstTraversalSummary summaryTraversal = null;


    HeaderComponent(String printPath, GNode root) {
        this.printPath = printPath;
        this.root = root;
    }

    AstTraversal.AstTraversalSummary getSummaryTraversal() {
        return this.summaryTraversal;
    }

    private void updateHeader() {
        AstTraversal visitor1 = new AstTraversal(newRuntime());
        // PHASE 1: obtain the traversal summary
        this.summaryTraversal = visitor1.getTraversal(root);
        // PHASE 2: construct the c++ header Ast
        GNode headerNode = HeaderAst.getHeaderAst(summaryTraversal).parent;
        // PHASE 3: create the header code
        this.headerFileSummary = new PrintHeaderFile(newRuntime(), summaryTraversal).getSummary(headerNode);
    }

    private void printHeader() {
        File header = new File(printPath + "output.h");
        header.getParentFile().mkdirs();

        try {
            FileWriter printHeader = new FileWriter(header);
            printHeader.write(headerFileSummary.code.toString());
            printHeader.flush();
            printHeader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.println("Printed " + header.getPath());
    }

    void execute() {
        this.updateHeader();
        this.printHeader();
    }
}

class CppComponent {
    private String printPath = null;
    private GNode root = null;
    private PrintCppFile.cppFileSummary cppSummary = null;
    private AstTraversal.AstTraversalSummary summaryTraversal = null;

    CppComponent(String printPath, GNode root) {
        this.printPath = printPath;
        this.root = root;
    }

    GNode getRoot() {
        return this.root;
    }

    void setTraversal(AstTraversal.AstTraversalSummary summaryTraversal) {
        this.summaryTraversal = summaryTraversal;
    }

    AstTraversal.AstTraversalSummary getSummaryTraversal() {
        return this.summaryTraversal;
    }

    PrintCppFile.cppFileSummary getCppSummary() {
        return this.cppSummary;
    }


    private void updateCpp() {
        // PHASE 4: mutate the traversed tree
        new AstMutator(newRuntime()).mutate(root);
        // PHASE 5: create the cpp code
        this.cppSummary = new PrintCppFile(newRuntime(), summaryTraversal).getSummary(root);
    }

    private void printCpp() {
        File output = new File(printPath + "output.cpp");
        try {
            FileWriter printOutput = new FileWriter(output);
            printOutput.write(cppSummary.code.toString());
            printOutput.flush();
            printOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("Printed " + output.getPath());
    }

    void execute() {
        this.updateCpp();
        this.printCpp();
    }

}

class MainComponent {

    private String printPath = null;
    private GNode root = null;
    private PrintMainFile.printMainFileSummary mainSummary = null;
    private AstTraversal.AstTraversalSummary summaryTraversal = null;


    MainComponent(String printPath, GNode root) {
        this.printPath = printPath;
        this.root = root;
    }

    void setTraversal(AstTraversal.AstTraversalSummary summaryTraversal) {
        this.summaryTraversal = summaryTraversal;
    }

    void setRoot(GNode root) {
        this.root = root;
    }

    private void updateMain() {
        this.mainSummary = new PrintMainFile(newRuntime(), summaryTraversal).getSummary(root);
    }

    private void printMain() {
        File main = new File(printPath + "main.cpp");
        try {
            FileWriter printMain = new FileWriter(main);
            printMain.write(mainSummary.filePrinted);
            printMain.flush();
            printMain.close();
            out.println("Printed " + main.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void execute() {
        this.updateMain();
        this.printMain();
    }

}

public class TranslationFacade {

    private HeaderComponent headerController;
    private CppComponent cppController;
    private MainComponent mainController;

    public TranslationFacade() {

    }

    public TranslationFacade(String filePath, String path) {
        this.headerController = new HeaderComponent(path, (GNode) loadTestFile(filePath));
        this.cppController = new CppComponent(path, (GNode) loadTestFile(filePath));
        this.mainController = new MainComponent(path, (GNode) loadTestFile(filePath));
    }

    public void setControllers(String filePath, String path) {
        this.headerController = new HeaderComponent(path, (GNode) loadTestFile(filePath));
        this.cppController = new CppComponent(path, (GNode) loadTestFile(filePath));
        this.mainController = new MainComponent(path, (GNode) loadTestFile(filePath));
    }

    public void translate() {
        headerController.execute();
        cppController.setTraversal(headerController.getSummaryTraversal());
        cppController.execute();
        mainController.setTraversal(cppController.getSummaryTraversal());
        mainController.setRoot(cppController.getRoot());
        mainController.execute();
    }

}
