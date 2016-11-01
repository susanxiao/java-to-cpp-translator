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
        // block node is always last child of main method node
        int blockIndex = n.size() - 1;
        GNode block = (GNode) n.getNode(blockIndex);
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
                    String variable = currentDeclarator.getString(0);
                    fieldDeclaration += variable + " ";
                    if (currentDeclarator.getNode(2).getName().equals("NewClassExpression")) {
                        fieldDeclaration += "= new ";
                        String qualifiedIdentifier = currentDeclarator.getNode(2).getNode(2).getString(0);
                        fieldDeclaration += qualifiedIdentifier;

                        // have to add the class and variable to a list so that we can use inheritance
                        summary.classNames.add(type);
                        summary.variables.add(variable);

                        Node declaratorArgs = currentDeclarator.getNode(2).getNode(3);
                        if (declaratorArgs.size() > 0) {
                            fieldDeclaration += "(";
                            for (Object arg : declaratorArgs) {
                                if (arg instanceof Node) {
                                    Node currentArg = (Node) arg;
                                    if (currentArg.getName().equals("StringLiteral")) {
                                        fieldDeclaration += "new __String(" + currentArg.getString(0) + ")";
                                    }
                                }
                            }
                            fieldDeclaration += ");";
                        } else {
                            fieldDeclaration += "();\n";
                        }
                    } else if (currentDeclarator.getNode(2).getName().equals("CastExpression")) {
                        String typeDeclarator = currentDeclarator.getNode(2).getNode(0).getNode(0).getString(0);
                        String primaryIdentifier = currentDeclarator.getNode(2).getNode(1).getString(0);
                        fieldDeclaration += "= (" + typeDeclarator + ") " + primaryIdentifier + ";\n";
                    } else if (currentDeclarator.getNode(2).getName().equals("PrimaryIdentifier")) {
                        String primaryIdentifier = currentDeclarator.getNode(2).getString(0);
                        fieldDeclaration += "= " + primaryIdentifier + ";\n";
                    } else if (currentDeclarator.getNode(2).getName().equals("SelectionExpression")) {
                        String primaryIdentifier = currentDeclarator.getNode(2).getNode(0).getString(0);
                        String field = currentDeclarator.getNode(2).getString(1);
                        fieldDeclaration += "= " + primaryIdentifier + "." + field + ";\n";
                    }
                }
            }
        }
        mainImplementation.append(fieldDeclaration + "\n");
    }

    public void visitExpressionStatement(GNode n) {
        String expressionStatement = "\t";
        if (n.getNode(0).getName().equals("CallExpression")) {
            String primaryIdentifer = "";
            if (n.getNode(0).getNode(0).getName().equals("SelectionExpression")) {
                //n.getNode(0).getNode(0).getNode(0).getName().equals("PrimaryIdentifier");
                if (n.getNode(0).getNode(0).getNode(0).getString(0).equals("cout")) {
                    expressionStatement += n.getNode(0).getNode(0).getNode(0).getString(0) + " << ";
                    primaryIdentifer = n.getNode(0).getNode(0).getNode(0).getString(0);
                    Node arguments = n.getNode(0).getNode(3);
                    for (Object o : arguments) {
                        if (o instanceof Node) {
                            Node currentNode = (Node) o;
                            if (currentNode.getName().equals("CallExpression")) {
                                if (currentNode.getNode(0).getName().equals("SelectionExpression")) {
                                    expressionStatement += currentNode.getNode(0).getNode(0).getString(0);
                                    expressionStatement += "." + currentNode.getNode(0).getString(1);
                                } else if (currentNode.getNode(0).getName().equals("CallExpression")) {
                                    expressionStatement += currentNode.getNode(0).getNode(0).getString(0);
                                    expressionStatement += "." + currentNode.getNode(0).getString(currentNode.getNode(0).size() - 2) + "()";
                                } else { expressionStatement += currentNode.getNode(0).getString(0); }
                                // expressionStatement += currentNode.getNode(0).getString(0);
                                String method = currentNode.getString(2);
                                expressionStatement += "->__vptr->" + method + "(";
                                Node args = currentNode.getNode(3);
                                int argSize = args.size();
                                if (args.size() > 0) {
                                    for (Object o1 : args) {
                                        argSize--;
                                        if (argSize > 0) {
                                            expressionStatement += o1.toString() + ",";
                                        } else {
                                            expressionStatement += o1.toString();
                                        }
                                    }
                                }
                                expressionStatement += ")";
                                if (method.equals("toString")) {
                                    expressionStatement += "->data ";
                                }else if(primaryIdentifer.equals("cout")){
                                    expressionStatement += "->data ";
                                }
                            } else if (currentNode.getName().equals("StringLiteral")) {
                                expressionStatement += currentNode.getString(0) + " ";
                            } else if (currentNode.getName().equals("SelectionExpression")) {

                                // we have to determine if the parent should be used when accessing the dataString
                                String variable = currentNode.getNode(0).getString(0);
                                String dataString = currentNode.getString(1);
                                String classVar = "";
                                boolean gateParent = true;
                                for(int i = 0; i < summary.classNames.size(); i++){
                                    out.println(summary.variables.get(i));
                                    out.println(variable);
                                    if(summary.variables.get(i).equals(variable)){
                                        classVar = summary.classNames.get(i);
                                        out.println(classVar);
                                        for(int j = 0; i < summary.classNames.size(); i++){
                                            if(summary.classNames.get(i).equals(classVar) && summary.variables.get(i).equals(dataString)){
                                                gateParent = false;
                                            }
                                        }
                                    }
                                }

                                if(gateParent) {
                                    expressionStatement += variable;
                                    expressionStatement += "->parent." + dataString + "->data";
                                }else{
                                    expressionStatement += variable;
                                    expressionStatement += "->" + dataString + "->data";
                                }
                            }
                        }
                    }
                }
                expressionStatement += " << endl;";
            } else {
                Node callExpressionNode = n.getNode(0);
                String methodName = callExpressionNode.getString(2);
                expressionStatement += callExpressionNode.getNode(0).getString(0) + "->";
                expressionStatement += callExpressionNode.getNode(1).getString(0) + "->";
                expressionStatement += methodName;
                if (callExpressionNode.getNode(3).getName().equals("Arguments")) {
                    expressionStatement += "(";
                    Node argumentsNode = (Node) callExpressionNode.getNode(3);
                    if(methodName.startsWith("set")){
                        expressionStatement += argumentsNode.getString(0) + ", ";
                    }
                    if (argumentsNode.getNode(1).getName().equals("NewClassExpression")) {
                        Node newClassExpressionNode = (Node) argumentsNode.getNode(1);
                        String newClassIdentifier = "new ";
                        newClassIdentifier += newClassExpressionNode.getNode(2).getString(0) + "(";
                        expressionStatement += newClassIdentifier;
                        expressionStatement += newClassExpressionNode.getNode(3).getNode(0).getString(0);
                        expressionStatement += ")";
                    }
                    else if (argumentsNode.getNode(1).getName().equals("PrimaryIdentifier")) {
                        expressionStatement += argumentsNode.getNode(1).getString(0);
                    } else { expressionStatement += argumentsNode.getString(0); }
                    expressionStatement += ");";
                }
            }

        } else if (n.getNode(0).getName().equals("Expression")) {
            Node expressionNode = n.getNode(0);
            for (Object o : expressionNode) {
                if (o instanceof Node) {
                    Node currNode = (Node) o;
                    if (currNode.getName().equals("SelectionExpression")) {
                        String varName = currNode.getNode(0).getString(0);
                        String field = currNode.getString(1);
                        expressionStatement += varName + "." + field + " = ";
                    } else if (currNode.getName().equals("PrimaryIdentifier")) {
                        expressionStatement += currNode.getString(0) + ";\n";
                    } else if (currNode.getName().equals("CastExpression")) {
                        String castType = currNode.getNode(0).getNode(0).getString(0);
                        String varName = currNode.getNode(1).getString(0);
                        expressionStatement += "(" + castType + ") " + varName + ";\n";
                    }
                }
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
        ArrayList<String> classNames = new ArrayList<String>();
        ArrayList<String> variables = new ArrayList<String>();
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

        for (Object o : n) {
            if (((Node) o).getName().equals("PackageDeclaration")) {
                Node currentNode = (Node) o;
                int size = currentNode.getNode(1).size();
                for (Object o1 : currentNode.getNode(1)) {
                    size--;
                    if (size > 0) {
                        s1.append(o1.toString() + "::");
                    } else {
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
                    summary.currentClassName = currentClass.getString(1);
                    visitClassDeclaration((GNode) currentClass);
                }
            }
        }

        s1.append(mainImplementation);


        // append the return statement
        s1.append("\treturn 0;\n");
        s1.append("}");

        s1.append("\n\n//------------------\n\n");

        out.println(s1.toString());

        summary.filePrinted = s1.toString();

        return summary;
    }


    public static void main(String[] args) {

        //LoadFileImplementations.prettyPrintAst(node);
        for (int i = 0; i < 21; i++) {
            if (i != 7) {
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
