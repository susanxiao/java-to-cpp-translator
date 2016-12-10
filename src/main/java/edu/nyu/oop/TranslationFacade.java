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

<<<<<<< HEAD
    private PrintHeaderFile.headerFileSummary headerFileSummary = null;
    private AstTraversal.AstTraversalSummary summaryTraversal = null;
=======
    PrintHeaderFile.headerFileSummary headerFileSummary = null;
    AstTraversal.AstTraversalSummary summaryTraversal = null;


>>>>>>> master
    private String filePath = null;
    private String printPath = null;
    private GNode root = null;

    public HeaderComponent(String filePath, String printPath, GNode root) {
        this.filePath = filePath;
        this.printPath = printPath;
        this.root = root;
    }

<<<<<<< HEAD
    public AstTraversal.AstTraversalSummary getSummaryTraversal() {
        return this.summaryTraversal;
    }

=======
>>>>>>> master
    public void updateHeader() {
        AstTraversal visitor1 = new AstTraversal(newRuntime());
        // PHASE 1: obtain the traversal summary
        this.summaryTraversal = visitor1.getTraversal(root);
        // PHASE 2: construct the c++ header Ast
        GNode headerNode = HeaderAst.getHeaderAst(summaryTraversal).parent;
        // PHASE 3: create the header code
<<<<<<< HEAD
        this.headerFileSummary = new PrintHeaderFile(newRuntime(), summaryTraversal).getSummary(headerNode);
    }

    public void printHeader() {
        File header = new File(printPath + "output.h");
        header.getParentFile().mkdirs();
=======
        headerFileSummary = new PrintHeaderFile(newRuntime(), summaryTraversal).getSummary(headerNode);
    }

    public void printHeader() {
        String headerPath = printPath + "output.h";
        File header = new File(headerPath);
>>>>>>> master

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
<<<<<<< HEAD
    private AstTraversal.AstTraversalSummary summaryTraversal = null;
=======
    AstTraversal.AstTraversalSummary summaryTraversal = null;
>>>>>>> master

    public CppComponent(String printPath, GNode root) {
        this.printPath = printPath;
        this.root = root;
    }

<<<<<<< HEAD
    public GNode getRoot() {
=======
    public GNode getRoot(){
>>>>>>> master
        return this.root;
    }

    public void setTraversal(AstTraversal.AstTraversalSummary summaryTraversal) {
        this.summaryTraversal = summaryTraversal;
    }

<<<<<<< HEAD
    public AstTraversal.AstTraversalSummary getSummaryTraversal() {
        return this.summaryTraversal;
    }

    public PrintCppFile.cppFileSummary getCppSummary() {
        return this.cppSummary;
    }


=======
>>>>>>> master
    public void updateCpp() {
        // PHASE 4: mutate the traversed tree
        new AstMutator(newRuntime()).mutate(root);
        // PHASE 5: create the cpp code
<<<<<<< HEAD
        this.cppSummary = new PrintCppFile(newRuntime(), summaryTraversal).getSummary(root);
=======
        cppSummary = new PrintCppFile(newRuntime(), summaryTraversal).getSummary(root);
>>>>>>> master
    }

    public void printCpp() {
        File output = new File(printPath + "output.cpp");
<<<<<<< HEAD
=======
        output.getParentFile().mkdirs();
>>>>>>> master
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

<<<<<<< HEAD
    public void setRoot(GNode root) {
=======
    public void setRoot(GNode root){
>>>>>>> master
        this.root = root;
    }

    public void updateMain() {
<<<<<<< HEAD
        this.mainSummary = new PrintMainFile(newRuntime(), summaryTraversal).getSummary(root);
=======
        mainSummary = new PrintMainFile(newRuntime(), this.summaryTraversal).getSummary(this.root);
>>>>>>> master
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

<<<<<<< HEAD
    public void execute() {
=======
    public void execute(){
>>>>>>> master
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
<<<<<<< HEAD
        cppController.setTraversal(headerController.getSummaryTraversal());
        cppController.execute();
        mainController.setTraversal(cppController.getSummaryTraversal());
=======
        cppController.setTraversal(headerController.summaryTraversal);
        cppController.execute();
        mainController.setTraversal(cppController.summaryTraversal);
>>>>>>> master
        mainController.setRoot(cppController.getRoot());
        mainController.execute();
    }

}
