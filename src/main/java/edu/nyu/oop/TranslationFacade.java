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

    PrintHeaderFile.headerFileSummary headerFileSummary = null;
    AstTraversal.AstTraversalSummary summaryTraversal = null;


    private String filePath = null;
    private String printPath = null;
    private GNode root = null;

    public HeaderComponent(String filePath, String printPath, GNode root) {
        this.filePath = filePath;
        this.printPath = printPath;
        this.root = root;
    }

    public void updateHeader() {
        AstTraversal visitor1 = new AstTraversal(newRuntime());
        // PHASE 1: obtain the traversal summary
        this.summaryTraversal = visitor1.getTraversal(root);
        // PHASE 2: construct the c++ header Ast
        GNode headerNode = HeaderAst.getHeaderAst(summaryTraversal).parent;
        // PHASE 3: create the header code
        headerFileSummary = new PrintHeaderFile(newRuntime(), summaryTraversal).getSummary(headerNode);
    }

    public void printHeader() {
        String headerPath = printPath + "output.h";
        File header = new File(headerPath);

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

    public void execute() {
        this.updateHeader();
        this.printHeader();
    }
}

class CppComponent {
    private String printPath = null;
    private GNode root = null;
    private PrintCppFile.cppFileSummary cppSummary = null;
    AstTraversal.AstTraversalSummary summaryTraversal = null;

    public CppComponent(String printPath, GNode root) {
        this.printPath = printPath;
        this.root = root;
    }

    public GNode getRoot(){
        return this.root;
    }

    public void setTraversal(AstTraversal.AstTraversalSummary summaryTraversal) {
        this.summaryTraversal = summaryTraversal;
    }

    public void updateCpp() {
        // PHASE 4: mutate the traversed tree
        new AstMutator(newRuntime()).mutate(root);
        // PHASE 5: create the cpp code
        cppSummary = new PrintCppFile(newRuntime(), summaryTraversal).getSummary(root);
    }

    public void printCpp() {
        File output = new File(printPath + "output.cpp");
        output.getParentFile().mkdirs();
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

    public void execute() {
        this.updateCpp();
        this.printCpp();
    }

}

class MainComponent {

    private String printPath = null;
    private GNode root = null;
    private PrintMainFile.printMainFileSummary mainSummary = null;
    private AstTraversal.AstTraversalSummary summaryTraversal = null;


    public MainComponent(String printPath, GNode root) {
        this.printPath = printPath;
        this.root = root;
    }

    public void setTraversal(AstTraversal.AstTraversalSummary summaryTraversal) {
        this.summaryTraversal = summaryTraversal;
    }

    public void setRoot(GNode root){
        this.root = root;
    }

    public void updateMain() {
        mainSummary = new PrintMainFile(newRuntime(), this.summaryTraversal).getSummary(this.root);
    }

    public void printMain() {
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

    public void execute(){
        this.updateMain();
        this.printMain();
    }

}

public class TranslationFacade {

    private HeaderComponent headerController;
    private CppComponent cppController;
    private MainComponent mainController;

    public TranslationFacade(String filePath, String path) {
        this.headerController = new HeaderComponent(filePath, path, (GNode) loadTestFile(filePath));
        this.cppController = new CppComponent(path, (GNode) loadTestFile(filePath));
        this.mainController = new MainComponent(path, (GNode) loadTestFile(filePath));
    }

    public void translate() {
        headerController.execute();
        cppController.setTraversal(headerController.summaryTraversal);
        cppController.execute();
        mainController.setTraversal(cppController.summaryTraversal);
        mainController.setRoot(cppController.getRoot());
        mainController.execute();
    }

}
