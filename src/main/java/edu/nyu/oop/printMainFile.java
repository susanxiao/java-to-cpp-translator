package edu.nyu.oop;

import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.out;

/**
 * Created by Garrett on 10/21/16.
 */
public class printMainFile extends Visitor {


    private printMainFile.printMainFileSummary summary = new printMainFile.printMainFileSummary();
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Runtime runtime;
    private AstTraversal.AstTraversalSummary summaryTraversal;

    StringBuilder mainImplementation = new StringBuilder();

    // visitXXX methods
    public void visitClassDeclaration(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                GNode currentNode = (GNode) o;
                if (currentNode.getName().equals("ClassBody")) {
                    visitClassBody(currentNode);
                }
            }
        }
    }

    public void visitClassBody(GNode n) {
        GNode methodMain = (GNode) n.getNode(0);
        visitMethodDeclaration(methodMain);
    }

    public void visitMethodDeclaration(GNode n) {
        GNode block = (GNode) n.getNode(7);
        for (Object o : block) {
            if (o instanceof Node) {
                GNode currentNode = (GNode) o;
                if (currentNode.getName().equals("FieldDeclaration")) {
                    visitFieldDeclaration(currentNode);
                } else if (currentNode.getName().equals("ExpressionStatement")) {
                    visitExpressionStatement(currentNode);
                }
            }
        }
    }

    public void visitFieldDeclaration(GNode n) {
        String fieldDeclaration = "\t";

        String type = n.getNode(1).getNode(0).getString(0);
        fieldDeclaration += type + " ";

        if (n.getNode(2).getName().equals("Declarators")) {
            Node declaratorsNode = (Node) n.getNode(2);
            for (Object o : declaratorsNode) {
                if (o instanceof Node) {
                    Node currentDeclarator = (Node) o;
                    fieldDeclaration += currentDeclarator.getString(0) + " ";
                    if (currentDeclarator.getNode(2).getName().equals("NewClassExpression")) {
                        fieldDeclaration += " = new ";
                        fieldDeclaration += currentDeclarator.getNode(2).getNode(2).getString(0);
                        Node declaratorArgs = currentDeclarator.getNode(2).getNode(3);
                        if (declaratorArgs.size() > 0) {

                        } else {
                            fieldDeclaration += "();\n";
                        }
                    }
                }
            }
        }
        mainImplementation.append(fieldDeclaration + "\n");
    }

    public void visitExpressionStatement(GNode n) {
        String expressionStatement = "\t";
        if (n.getNode(0).getName().equals("CallExpression")) {
            if (n.getNode(0).getNode(0).getName().equals("SelectionExpression")) {
                n.getNode(0).getNode(0).getNode(0).getName().equals("PrimaryIdentifier");
                expressionStatement += "cout << ";
                if (n.getNode(0).getNode(0).getNode(0).getString(0).equals("cout")) {
                    Node arguments = n.getNode(0).getNode(3);
                    for (Object o : arguments) {
                        if (o instanceof Node) {
                            Node currentNode = (Node) o;
                            if (currentNode.getName().equals("CallExpression")) {
                                expressionStatement += currentNode.getNode(0).getString(0);
                                String method = currentNode.getString(2);
                                expressionStatement += "->__vptr->" + method + "(";
                                Node args = currentNode.getNode(3);
                                int argSize = args.size();
                                if (args.size() > 0) {
                                    for (Object o1 : args) {
                                        argSize--;
                                        if(argSize > 0){
                                            expressionStatement+= o1.toString() + ",";
                                        }else{
                                            expressionStatement += o1.toString();
                                        }
                                    }
                                }
                                expressionStatement += ")";
                                if(method.equals("toString")){
                                    expressionStatement += "->data ";
                                }
                            } else if (currentNode.getName().equals("StringLiteral")) {
                                expressionStatement += currentNode.getString(0) + " ";
                            }
                        }
                    }
                }
                expressionStatement += "<< endl;";
            }
        }
        mainImplementation.append(expressionStatement + "\n\n");
    }

    // visitMethod


    public printMainFile(Runtime runtime, AstTraversal.AstTraversalSummary summaryTraversal) {
        this.runtime = runtime;
        this.summaryTraversal = summaryTraversal;
    }

    static class printMainFileSummary {
        String currentClassName;
        String filePrinted;
    }

    public printMainFile.printMainFileSummary getSummary(GNode n) {
        StringBuilder s1 = new StringBuilder();

        s1.append("\n//------------------\n\n");

        s1.append("#include <iostream>\n#include <sstream>\n");
        s1.append("#include \"java_lang.h\"\n\n");
        s1.append("#include \"output.h\"\n");
        s1.append("\n" +
                "using namespace java::lang;\n" +
                "using namespace std;\n");
        s1.append("using namespace ");

        for(Object o : n){
            if(((Node)o).getName().equals("PackageDeclaration")){
                Node currentNode = (Node) o;
                int size = currentNode.getNode(1).size();
                for(Object o1 : currentNode.getNode(1)){
                    size--;
                    if(size > 0) {
                        s1.append(o1.toString() + "::");
                    }else{
                        s1.append(o1.toString());
                    }
                }
            }
        }

        s1.append(";\n\n");
        s1.append("int main(void)\n{\n\n");

        //  visit the main method
        for (Object o : n) {
            Node currentClass = (Node) o;
            if (currentClass.getName().equals("ClassDeclaration")) {
                if (currentClass.getString(1).contains("Test")) {
                    visitClassDeclaration((GNode) currentClass);
                }
            }
        }

        s1.append(mainImplementation);


        // append the return statement
        s1.append("\treturn 0;\n");
        s1.append("}");

        s1.append("\n\n//------------------\n\n");

        //out.println(s1.toString());

        summary.filePrinted = s1.toString();

        return summary;
    }


    public static void main(String[] args) {

        //LoadFileImplementations.prettyPrintAst(node);
        for (int i = 0; i < 21; i++) {
            if (i != 1) {
                continue;
            }
            String test = "./src/test/java/inputs/";
            String test1 = "";
            String test2 = "";
            if (i < 10) {
                test1 = "test00" + i;
                test2 = "Test00" + i;
            } else {
                test1 = "test0" + i;
                test2 = "Test0" + i;
            }
            test += test1;
            test += "/" + test2 + ".java";
            try {
                PrintWriter printerMain;

                File main;

                GNode node = (GNode) LoadFileImplementations.loadTestFile(test);

                // get the summary traversal (class implementations)
                AstTraversal visitorTraversal = new AstTraversal(LoadFileImplementations.newRuntime());
                AstTraversal.AstTraversalSummary summaryTraversal = visitorTraversal.getTraversal(node);

                // get the mutated tree
                AstMutator visitor1 = new AstMutator(LoadFileImplementations.newRuntime());
                AstMutator.AstMutatorSummary summary = visitor1.getMutator(node);

                // get the summary of the cpp implementations
                printMainFile visitor = new printMainFile(LoadFileImplementations.newRuntime(), summaryTraversal);
                printMainFile.printMainFileSummary summaryMain = visitor.getSummary(node);
                LoadFileImplementations.prettyPrintAst(node);

                String mainFile = "";
                mainFile += summaryMain.filePrinted;

                main = new File("testOutputs/mainFileOutputs", test2);
                main.createNewFile();

                printerMain = new PrintWriter(main);
                printerMain.println(mainFile);
                printerMain.flush();
                printerMain.close();
                out.println("main " + i + " printed\n");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
