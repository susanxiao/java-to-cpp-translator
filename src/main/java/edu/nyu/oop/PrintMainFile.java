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
import java.util.TreeMap;

import static java.lang.System.out;

/**
 * Created by Garrett on 10/21/16.
 */
public class PrintMainFile extends Visitor {


    private PrintMainFile.printMainFileSummary summary = new PrintMainFile.printMainFileSummary();
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
        for (Object o : n) {
            if (o instanceof Node) {
                GNode currentNode = (GNode) o;
                if (currentNode.getName().equals("FieldDeclaration")) {
                    visitFieldDeclaration(currentNode);
                } else if (currentNode.getName().equals("MethodDeclaration")) {
                    visitMethodDeclaration(currentNode);
                }
            }
        }
        //GNode methodMain = (GNode) n.getNode(0);
        //visitMethodDeclaration(methodMain);
    }

    public void visitNestedBlock(GNode n) {
        mainImplementation.append("\t{\n");
        for (Object o : n) {
            if (o instanceof Node) {
                GNode currentNode = (GNode) o;
                if (currentNode.getName().equals("FieldDeclaration")) {
                    mainImplementation.append("\t");
                    visitFieldDeclaration(currentNode);
                } else if (currentNode.getName().equals("ExpressionStatement")) {
                    mainImplementation.append("\t");
                    visitExpressionStatement(currentNode);
                }
            }
        }
        mainImplementation.append("\t}\n\n");
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
                } else if (currentNode.getName().equals("Block")) {
                    visitNestedBlock(currentNode);
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

                    // parent
                    summary.classVariables.remove(type);
                    summary.classVariables.put(variable, type);


                    fieldDeclaration += variable;
                    if (currentDeclarator.getNode(2) != null) {
                        if (currentDeclarator.getNode(2).getName().equals("NewClassExpression")) {
                            fieldDeclaration += " = new ";
                            String qualifiedIdentifier = currentDeclarator.getNode(2).getNode(2).getString(0);
                            fieldDeclaration += qualifiedIdentifier;

                            // have to add the class and variable to a list so that we can use inheritance
                            summary.classNames.add(variable);
                            summary.variables.add(type);

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
                                    fieldDeclaration += ");";
                                }
                            } else {
                                fieldDeclaration += "();\n";
                            }

                        } else if (currentDeclarator.getNode(2).getName().equals("CastExpression")) {
                            String typeDeclarator = currentDeclarator.getNode(2).getNode(0).getNode(0).getString(0);
                            String primaryIdentifier = currentDeclarator.getNode(2).getNode(1).getString(0);
                            fieldDeclaration += " = (" + typeDeclarator + ") " + primaryIdentifier + ";\n";
                        } else if (currentDeclarator.getNode(2).getName().equals("PrimaryIdentifier")) {
                            String primaryIdentifier = currentDeclarator.getNode(2).getString(0);
                            fieldDeclaration += " = " + primaryIdentifier + ";\n";
                        } else if (currentDeclarator.getNode(2).getName().equals("SelectionExpression")) {
                            String primaryIdentifier = currentDeclarator.getNode(2).getNode(0).getString(0);
                            fieldDeclaration += " = " + primaryIdentifier;

                            for (int i = 1; i < currentDeclarator.getNode(2).size(); i++) {
                                String field = currentDeclarator.getNode(2).getString(i);
                                fieldDeclaration += "->" + field;
                            }
                            fieldDeclaration += ";\n";

                        } else {
                            fieldDeclaration += ";\n";
                        }
                    } else {
                        //only instantiated variable, not defined
                        fieldDeclaration += ";\n";
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
                                    expressionStatement += "->" + currentNode.getNode(0).getString(1);
                                } else if (currentNode.getNode(0).getName().equals("CallExpression")) {
                                    expressionStatement += currentNode.getNode(0).getNode(0).getString(0);
                                    expressionStatement += "->" + currentNode.getNode(0).getString(currentNode.getNode(0).size() - 2) + "()";
                                } else {
                                    expressionStatement += currentNode.getNode(0).getString(0);
                                }
                                // expressionStatement += currentNode.getNode(0).getString(0);
                                String method = currentNode.getString(2);
                                expressionStatement += "->__vptr->" + method + "(";
                                Node args = currentNode.getNode(3);
                                int argSize = args.size();
                                if (args.size() > 0) {
                                    for (Object o1 : args) {
                                        argSize--;
                                        if (currentNode.getNode(0).getName().equals("PrimaryIdentifier")) {
                                            if (argSize > 0) {
                                                expressionStatement += o1.toString() + ",";
                                            } else {
                                                expressionStatement += o1.toString();
                                            }
                                        } else if (o1 instanceof String) {
                                            expressionStatement += o1.toString();
                                        }
                                    }
                                }
                                expressionStatement += ")";
                                if (method.equals("toString")) {
                                    expressionStatement += "->data";
                                } else if (primaryIdentifer.equals("cout")) {
                                    expressionStatement += "->data";
                                }
                            } else if (currentNode.getName().equals("StringLiteral")) {
                                expressionStatement += currentNode.getString(0) + " ";
                            } else if (currentNode.getName().equals("SelectionExpression")) {
                                expressionStatement += currentNode.getNode(0).getString(0);
                                String key = currentNode.getNode(0).getString(0);
                                boolean gateParent;
                                for (int i = 1; i < currentNode.size(); i++) {
                                    gateParent = true;
                                    String field = currentNode.getString(i);
                                    // check if parent should be used
                                    String className = summary.classVariables.get(key);
                                    for (FieldDeclaration dec : summaryTraversal.classes.get(className).declarations) {
                                        if(dec.variableName.equals(field) || field.equals("data"))
                                            gateParent = false;
                                    }
                                    expressionStatement += gateParent ? "->parent." + field : "->" + field;

                                }
                            } else if (currentNode.getName().equals("PrimaryIdentifier")) {
                                expressionStatement += currentNode.getString(0);
                                // we have to determine if the parent should be used when accessing the dataString
                                if (currentNode.size() - 1 > 0) { //when the primary identifier has more children than just a variable name, parent usage becomes important
                                    String variable = currentNode.getNode(0).getString(0);
                                    String dataString = currentNode.getString(1);
                                    String classVar = "";
                                    boolean gateParent = true;
                                    for (int i = 0; i < summary.classNames.size(); i++) {
                                        out.println(summary.variables.get(i));
                                        out.println(variable);
                                        if (summary.variables.get(i).equals(variable)) {
                                            classVar = summary.classNames.get(i);
                                            out.println(classVar);
                                            for (int j = 0; i < summary.classNames.size(); i++) {
                                                if (summary.classNames.get(i).equals(classVar) && summary.variables.get(i).equals(dataString)) {
                                                    gateParent = false;
                                                }
                                            }
                                        }
                                    }
                                    if (gateParent) {
                                        expressionStatement += variable;
                                        expressionStatement += "->parent." + dataString + "->data";
                                    } else {
                                        expressionStatement += variable;
                                        expressionStatement += "->" + dataString + "->data";
                                    }
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
                primaryIdentifer = callExpressionNode.getNode(0).getString(0);
                expressionStatement += callExpressionNode.getNode(1).getString(0) + "->";
                expressionStatement += methodName;
                if (callExpressionNode.getNode(3).getName().equals("Arguments")) {
                    expressionStatement += "(";
                    Node argumentsNode = (Node) callExpressionNode.getNode(3);
                    expressionStatement += argumentsNode.getString(0) + ", ";
                    String argument1 = argumentsNode.getString(0);
                    if (argumentsNode.getNode(1).getName().equals("NewClassExpression")) {
                        Node newClassExpressionNode = (Node) argumentsNode.getNode(1);
                        String newClassIdentifier = "new ";
                        newClassIdentifier += newClassExpressionNode.getNode(2).getString(0) + "(";
                        expressionStatement += newClassIdentifier;
                        expressionStatement += newClassExpressionNode.getNode(3).getNode(0).getString(0);
                        expressionStatement += ")";
                    } else if (argumentsNode.getNode(1).getName().equals("PrimaryIdentifier")) {
                        String primaryIdentifier1 = argumentsNode.getNode(1).getString(0);
                        if(summary.classVariables.get(primaryIdentifer) == summary.classVariables.get(primaryIdentifier1)){
                            expressionStatement += argumentsNode.getNode(1).getString(0);
                        }else{
                            expressionStatement += "(" + summary.classVariables.get(primaryIdentifer) + ") " + primaryIdentifier1;
                        }
                    } else {
                        expressionStatement += argumentsNode.getString(0);
                    }
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
                        expressionStatement += varName;

                        for (int i = 1; i < currNode.size(); i++) {
                            String field = currNode.getString(i);
                            expressionStatement += "->" + field;
                        }
                    } else if (currNode.getName().equals("PrimaryIdentifier")) {
                        expressionStatement += currNode.getString(0);
                    } else if (currNode.getName().equals("CastExpression")) {
                        String castType = currNode.getNode(0).getNode(0).getString(0);
                        String varName = currNode.getNode(1).getString(0);
                        expressionStatement += "(" + castType + ") " + varName;
                    } else if (currNode.getName().equals("IntegerLiteral")) {
                        expressionStatement += currNode.getString(0);
                    }
                } else {
                    expressionStatement += " " + o.toString() + " ";
                }
            }
            expressionStatement += ";";
        }
        mainImplementation.append(expressionStatement + "\n\n");
    }

    // visitMethod


    public PrintMainFile(Runtime runtime, AstTraversal.AstTraversalSummary summaryTraversal) {
        this.runtime = runtime;
        this.summaryTraversal = summaryTraversal;
    }

    static class printMainFileSummary {
        String currentClassName;
        String filePrinted;
        ArrayList<String> classNames = new ArrayList<String>();
        ArrayList<String> variables = new ArrayList<String>();
        TreeMap<String, String> classVariables = new TreeMap<>();

    }

    public PrintMainFile.printMainFileSummary getSummary(GNode n) {
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
        //TO RUN: run-main edu.nyu.oop.PrintMainFile ***
        // *** a number 0-20, or nothing to run all test cases
        int start = 7;
        int end = 7;
        start = end = 10;

        if (args.length > 0) {
            int value = ImplementationUtil.getInteger(args[0]);
            if (value >= 0) {
                start = value;
                end = value;
            }
        }

        //LoadFileImplementations.prettyPrintAst(node);
        for (int i = start; i <= end; i++) {
            String test = String.format("./src/test/java/inputs/test%03d/Test%03d.java", i, i);

            try {
                PrintWriter printerMain;

                File main;

                GNode node = (GNode) ImplementationUtil.loadTestFile(test);

                // get the summary traversal (class implementations)
                AstTraversal visitorTraversal = new AstTraversal(ImplementationUtil.newRuntime());
                AstTraversal.AstTraversalSummary summaryTraversal = visitorTraversal.getTraversal(node);

                // get the mutated tree
                AstMutator visitor1 = new AstMutator(ImplementationUtil.newRuntime());
                visitor1.mutate(node);

                // get the summary of the cpp implementations
                PrintMainFile visitor = new PrintMainFile(ImplementationUtil.newRuntime(), summaryTraversal);
                PrintMainFile.printMainFileSummary summaryMain = visitor.getSummary(node);
                ImplementationUtil.prettyPrintAst(node);

                String mainFile = "";
                mainFile += summaryMain.filePrinted;

                main = new File("testOutputs/mainFileOutputs", String.format("Test%03d", i));
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
