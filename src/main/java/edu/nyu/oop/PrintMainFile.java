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
import java.util.TreeMap;

import edu.nyu.oop.util.*;

import static java.lang.System.out;

/**
 * Created by Garrett on 10/21/16.
 */
public class PrintMainFile extends Visitor {


    public PrintMainFileSummary summary = new PrintMainFileSummary();
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Runtime runtime;
    private AstTraversal.AstTraversalSummary summaryTraversal;

    StringBuilder mainImplementation = new StringBuilder();
    static int scope = 0;

    public static void incScope() {
        scope++;
    }

    public static void decScope() {
        scope--;
    }

    public static String getTabs() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < scope; i++) s.append("\t");
        return s.toString();
    }

    //constructor
    public PrintMainFile(Runtime runtime, AstTraversal.AstTraversalSummary summaryTraversal) {
        this.runtime = runtime;
        this.summaryTraversal = summaryTraversal;
    }

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
        mainImplementation.append(" {\n");
        //incScope();
        mainImplementation.append(getTabs()+"if ("+summary.index+" < 0 || "+summary.arrayName+"->length <= "+summary.index+") throw java::lang::ArrayIndexOutOfBoundsException();\n");

        if (summary.init2D != null) {
            String initPrimaryIdentifier = String.format(summary.init2D, summary.init2DDec);

            mainImplementation.append(getTabs()+initPrimaryIdentifier+" = "+"new __rt::Array<"+summary.init2DType+">("+summary.init2DSize+");\n");

            //reset them;
            summary.init2D = null;
            summary.init2DSize = null;
            summary.init2DType = null;
            summary.init2DDec = null;
        }

        //mainImplementation.append(getTabs());

        for (Object o : n) {
            if (o instanceof Node) {
                GNode currentNode = (GNode) o;
                if (currentNode.getName().equals("FieldDeclaration")) {
                    mainImplementation.append(getTabs());
                    //mainImplementation.append("\t");
                    visitFieldDeclaration(currentNode);
                } else if (currentNode.getName().equals("ExpressionStatement")) {
                    //mainImplementation.append(getTabs());
                    //mainImplementation.append("\t");
                    visitExpressionStatement(currentNode);
                } else if (currentNode.getName().equals("ForStatement")) {
                    //mainImplementation.append(getTabs());
                    //incScope();
                    visitForStatement(currentNode);
                }
            }
        }
        decScope();
        mainImplementation.append(getTabs()+"}\n");
    }

    public void visitMethodDeclaration(GNode n) {

        // block node is always last child of main method node
        int blockIndex = n.size() - 1;
        GNode block = (GNode) n.getNode(blockIndex);
        String methodName = n.getString(3);

        summary.localVariables = new HashMap<>();

        if (methodName.equals("methodMain")) {
            if (summaryTraversal.usesArgs) {
                mainImplementation.append(getTabs()+"int main (int argc, char ** argv) {\n");
                incScope();
                //mainImplementation.append("\t//get command line arguments. convert between args(java) and argv(c++). for test22, 23...\n");
                mainImplementation.append(getTabs()+"__rt::Array<String>* args = new __rt::Array<String>(argc-1);\n");
                mainImplementation.append(getTabs()+"for (int a = 1; a < argc; a++) {\n");//starts with a=1(not a=0) because the first argument in cpp is the command
                incScope();
                mainImplementation.append(getTabs()+"String argument = new __String(argv[a]);\n");
                mainImplementation.append(getTabs()+"args->__data[a-1] = argument;\n");
                decScope();
                mainImplementation.append(getTabs()+"}\n\n");

                summary.localVariables.put("args", "String[");
            } else {
                mainImplementation.append(getTabs() + "int main(void) {\n");
                incScope();
            }

        }


        for (Object o : block) {
            if (o instanceof Node) {
                GNode currentNode = (GNode) o;
                if (currentNode.getName().equals("FieldDeclaration")) {
                    visitFieldDeclaration(currentNode);
                } else if (currentNode.getName().equals("ExpressionStatement")) {
                    visitExpressionStatement(currentNode );
                } else if (currentNode.getName().equals("ForStatement")) {
                    visitForStatement(currentNode);
                } else if (currentNode.getName().equals("WhileStatement")) {
                    visitWhileStatement(currentNode);
                }
            }
        }
        summary.localVariables = null;
        if (methodName.equals("methodMain")) {
            // append the return statement
            mainImplementation.append(getTabs()+"return 0;\n");
            decScope();
            mainImplementation.append(getTabs()+"}");

        }
    }

    public void visitFieldDeclaration(GNode n) {
        String fieldDeclaration = "%s";

        String type = n.getNode(1).getNode(0).getString(0);
        if (type.equals("byte"))
            type = "uint8_t";

        boolean isTypeArray = false;
        boolean is2D = false;
        if (n.getNode(1).getNode(1) != null) { // the type is an array
            if (n.getNode(1).getNode(1).getString(0).equals("[")) {
                isTypeArray = true;
                if (n.getNode(1).getNode(1).size() > 1 && n.getNode(1).getNode(1).getString(1).equals("[")) {
                    //2d array
                    is2D = true;
                    if (type.equals("int"))
                        fieldDeclaration += "__rt::Array<__rt::Array<" + type + ">*>* ";
                    else
                        fieldDeclaration += "__rt::Array<__rt::Array<__" + type + ">*>* ";
                }
                else {
                    fieldDeclaration += "__rt::Array<" + type + ">* ";
                }
            }
        }
        else { //if type is not an array.
            fieldDeclaration += type+" ";
        }
        if (n.getNode(2).getName().equals("Declarators")) {
            Node declaratorsNode = n.getNode(2);
            fieldDeclaration += PrintMainFieldDeclarationUtil.handleDeclarators(n, declaratorsNode, type, isTypeArray, is2D, summary, summaryTraversal);
        }
        mainImplementation.append(getTabs()+String.format(fieldDeclaration, summary.needsSizeCheck ? "if ("+summary.size+" < 0) throw java::lang::NegativeArraySizeException();\n"+getTabs() : "") + "\n");
    }

    public String checkIndexBoundsForSubscriptExpression(GNode n){
        String arrayVariableName = n.getNode(0).getString(0);
        String arrayIndex = n.getNode(1).getString(0);

        return "checkIndex("+ arrayVariableName + ", " + arrayIndex+ ");\n";
    }

    public String returnSubscriptExpression(GNode n){
        /*
        return a string subscriptExpression string
        rather than having a visitSubscriptExpression() and directing adding to main Implementation
        because subscript expressions are normally in the middle of an expression
        */
        GNode primaryIdentifier0 = (GNode) n.get(0);
        GNode primaryIdentifier1 = (GNode) n.get(1);

        String subscriptExpression_str = primaryIdentifier0.get(0).toString() + "->__data[";
        subscriptExpression_str += primaryIdentifier1.get(0).toString() + "]";

        summary.arrayName = subscriptExpression_str;
        return subscriptExpression_str;
    }

    public void visitExpressionStatement(GNode n) {
        String expressionStatement = "";
        //See if there is an array in the expression Statement
        //If there is an array, make sure we do not get an ArrayIndexOutOfBoundsException
        Node subscriptExpression = NodeUtil.dfs(n,"SubscriptExpression");
        if(subscriptExpression!=null){ //There is an array.
            Node checkIfItsA2DArray = NodeUtil.dfs(subscriptExpression,"SubscriptExpression");
            if(checkIfItsA2DArray!=null){//2D array
                //Todo Do I Also need to checkIndexboudns error for 2d Arrays?
                //Node secondDimension = checkIfItsA2DArray;
                //String  checkIndexBoundError =checkIndexBoundsForSubscriptExpression((GNode) checkIfItsA2DArray );
                //expressionStatement+=checkIndexBoundError;

            }else{//1D array
                String  checkIndexBoundError = checkIndexBoundsForSubscriptExpression((GNode) subscriptExpression);
                expressionStatement+=checkIndexBoundError;
            }
        }
        if (n.getNode(0).getName().equals("CallExpression")) {
            if (n.getNode(0).getNode(0).getName().equals("SelectionExpression")) {
                if (n.getNode(0).getNode(0).getNode(0).getString(0).equals("cout")) {
                    expressionStatement += PrintMainExpressionStatementUtil.handleCout(n, summary, summaryTraversal);
                }
                expressionStatement += " << endl;";
            } else { //Primary Identifier.
                Node callExpressionNode = n.getNode(0);
                String methodName = callExpressionNode.getString(2);
                expressionStatement += PrintMainExpressionStatementUtil.handleCallExpressionPrimaryIdentifier(callExpressionNode, methodName, summary, summaryTraversal)+";";
            }
        } else if (n.getNode(0).getName().equals("Expression")) {
            Node expressionNode = n.getNode(0);
            expressionStatement += PrintMainExpressionStatementUtil.handleExpressionNode(expressionNode, summary, summaryTraversal);
            expressionStatement += ";";
        }
        mainImplementation.append(getTabs()+expressionStatement + "\n");
    }

    public void visitWhileStatement(GNode n) {
        String whileStatement = "while ";

        for (Object o : n) {
            if (o instanceof Node) {
                GNode currentNode = (GNode) o;
                if (currentNode.getName().equals("RelationalExpression")) {
                    GNode primaryIdentifierNode = (GNode) currentNode.get(0);
                    whileStatement += "("+primaryIdentifierNode.get(0).toString() +  " " + currentNode.get(1).toString() + " ";//<
                    summary.index = primaryIdentifierNode.getString(0);
                    for (int i = 2; i < currentNode.size(); i++) {
                        Object o1 = currentNode.get(i);
                        if (o1 instanceof Node) {
                            if (((Node) o1).getName().equals("IntegerLiteral")) {
                                whileStatement += ((Node) o1).getString(0);
                            }
                        }
                    }
                    whileStatement += ")";
                    mainImplementation.append(getTabs()+whileStatement);
                }
                else if (currentNode.getName().equals("Block")) {
                    incScope();
                    visitNestedBlock(currentNode);
                    //decScope();
                }
            }
        }

    }

    public void visitForStatement(GNode n) {
        String forStatement = getTabs()+"for ";
        for (Object o : n) {
            if (o instanceof Node) {
                GNode currentNode = (GNode) o;

                if (currentNode.getName().equals("BasicForControl")) {
                    forStatement += "(";
                    GNode basicForControlNode = (GNode) o;
                    for (Object b : basicForControlNode) {
                        if (b instanceof Node) {
                            GNode b_Node = (GNode) b;

                            if (b_Node.getName().equals("Type")) {
                                GNode primitiveType = (GNode) b_Node.get(0);
                                if (primitiveType.get(0).toString().equals("int")) {
                                    forStatement += "int32_t "; //int
                                } else {
                                    forStatement += primitiveType.get(0).toString() + " ";
                                }
                            } else if (b_Node.getName().equals("Declarators")) {
                                GNode declaratorNode = (GNode) b_Node.get(0);
                                forStatement += declaratorNode.get(0).toString() + " = "; //i =
                                GNode IntegerLiteralNode = (GNode) declaratorNode.get(2);
                                forStatement += IntegerLiteralNode.get(0).toString() + "; "; //0;

                                summary.init2DDec = declaratorNode.getString(0);
                                summary.index = declaratorNode.getString(0);

                            } else if (b_Node.getName().equals("RelationalExpression")) {

                                GNode primaryIdentifierNode = (GNode) b_Node.get(0);
                                forStatement += primaryIdentifierNode.get(0).toString(); //i
                                forStatement += " " + b_Node.get(1).toString() + " ";//<
                                GNode SelectionExpressNode = (GNode) b_Node.get(2);


                                if(SelectionExpressNode.getNode(0).getName().equals("SubscriptExpression")){
                                    //Select expression in 2D array
                                    Node twoDimension_SubscriptExpression = SelectionExpressNode.getNode(0);
                                    String subscriptExpression = returnSubscriptExpression((GNode) twoDimension_SubscriptExpression);
                                    forStatement += subscriptExpression;

                                }else {
                                    //Select expression in 1D array
                                    GNode PrimaryId_inSelectionExNode = (GNode) SelectionExpressNode.get(0);
                                    summary.arrayName = PrimaryId_inSelectionExNode.get(0).toString(); // as
                                    forStatement += summary.arrayName; // as
                                }
                                forStatement +=  "->" + SelectionExpressNode.get(1).toString() + "; ";
                            } else if (b_Node.getName().equals("ExpressionList")) {
                                GNode postfixExpressionNode = (GNode) b_Node.get(0);
                                GNode primaryID_inPostfixExpression = (GNode) postfixExpressionNode.get(0);
                                forStatement += primaryID_inPostfixExpression.get(0).toString();
                                forStatement += postfixExpressionNode.get(1).toString();

                            }

                        }
                    }
                    forStatement += ")";
                    mainImplementation.append(forStatement);
                    } else if (currentNode.getName().equals("Block")) {
                    incScope();
                    visitNestedBlock(currentNode);
                    //decScope();
                }
            }
        }

        //mainImplementation.append(forStatement + "\n\n");
    }

    // visitMethod

    public PrintMainFileSummary getSummary(GNode n) {
        StringBuilder s1 = new StringBuilder();

        //s1.append("\n//------------------\n\n");

        s1.append("#include <iostream>\n");
        s1.append("#include <sstream>\n\n");
        s1.append("#include \"java_lang.h\"\n");
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

        s1.append(";\n");

        //  visit the main method
        for (Object o : n) {
            Node currentClass = (Node) o;
            if (currentClass.getName().equals("ClassDeclaration")) {
                if (currentClass.getString(1).contains("Test")) { //Main
                    /*save information for fields in main(use for method overloading)

                    //System.out.println(currentClass.getNode(5).getNode(0).getNode(7).getName());//Block
                    //GNode block = (GNode) currentClass.getNode(5).getNode(0).getNode(7);
                    GNode block = (GNode) NodeUtil.dfs(currentClass,"Block");
                    for(Object obj : block) {
                        Node currClass = (Node) obj;
                        if (currClass.getName().equals("FieldDeclaration")) {
                            String varName = currClass.getNode(2).getNode(0).getString(0);
                            String varType = currClass.getNode(1).getNode(0).getString(0);
                            if(varType.equals("byte")){
                                varType = "uint8_t";

                            }
                            summaryTraversal.fieldsInMainInfo.put(varName, varType);
                        }
                    }
                    //END: save information for fields in main(use for method overloading) */

                    summary.currentClassName = currentClass.getString(1);
                    visitClassDeclaration((GNode) currentClass);

                }
                /*else{//it's not the main class

                    //check for method overloading
                    String className = currentClass.getString(1);
                    ArrayList<String> a = new ArrayList<>();
                    a.add(className);

                    Node ClassBody = currentClass.getNode(5);
                    for(Object obj: ClassBody){
                        ArrayList<String> finalMethodNames = new ArrayList<String>();
                        finalMethodNames.add(className);
                        Node currClass = (Node) obj;
                        if (currClass.getName().equals("MethodDeclaration")) {
                            String methodName = currClass.getString(3);
                            a.add(methodName);

                            Node formalParamSNode = currClass.getNode(4);
                            if(formalParamSNode.size()>1){
                                methodName += "_";
                                for(int i=1; i<formalParamSNode.size();i++){
                                    methodName += formalParamSNode.getNode(i).getNode(1).getNode(0).getString(0);

                                }
                            }
                            finalMethodNames.add(methodName);
                        }
                        summaryTraversal.overloadedMethodNames.add(finalMethodNames);
                    }
                    summaryTraversal.allMethods_checkMethodOverloading.add(a);

                    for(int i=0; i<summaryTraversal.allMethods_checkMethodOverloading.size(); i++){
                        ArrayList<String> arrayListElement = new ArrayList<String>();
                        arrayListElement.add(summaryTraversal.allMethods_checkMethodOverloading.get(i).get(0));
                        for(int j=1;j<summaryTraversal.allMethods_checkMethodOverloading.get(i).size(); j++){
                            String checkIfThisMethodIsOverloaded= summaryTraversal.allMethods_checkMethodOverloading.get(i).get(j);
                            int howManyTimeTheMethodIsDefined = 0;
                            for(int k=1;k<summaryTraversal.allMethods_checkMethodOverloading.get(i).size(); k++){
                                if (checkIfThisMethodIsOverloaded.equals(summaryTraversal.allMethods_checkMethodOverloading.get(i).get(k))){
                                    howManyTimeTheMethodIsDefined++;
                                }
                            }
                            if (howManyTimeTheMethodIsDefined>1){
                                arrayListElement.add("overloaded");
                            }else{
                                arrayListElement.add("not overloaded");
                            }
                        }
                        summaryTraversal.isOverLoaded.add(arrayListElement);
                    }

                }*/
            }

        }

        s1.append(mainImplementation);

        //s1.append("\n\n//------------------\n\n");

        summary.filePrinted = s1.toString();

        return summary;
    }


    public static void main(String[] args) {
        //TO RUN: run-main edu.nyu.oop.PrintMainFile ***
        // *** a number 0-20, or nothing to run all test cases
        int start = 0;
        int end = 20;

        if (args.length > 1) {
            start = ImplementationUtil.getInteger(args[0]);
            end = ImplementationUtil.getInteger(args[1]);
        } else if (args.length > 0) {
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

                //before mutation - origianl AST
                //ImplementationUtil.prettyPrintAst(node);

                // get the mutated tree
                AstMutator visitor1 = new AstMutator(ImplementationUtil.newRuntime());
                visitor1.mutate(node);
                //ImplementationUtil.prettyPrintAst(node);

                // get the summary of the cpp implementations
                PrintMainFile visitor = new PrintMainFile(ImplementationUtil.newRuntime(), summaryTraversal);
                PrintMainFileSummary summaryMain = visitor.getSummary(node);
                //ImplementationUtil.prettyPrintAst(node);

                String mainFile = "";
                mainFile += summaryMain.filePrinted;
                out.println(mainFile);
                String mainPath = String.format("./testOutputs/translationOutputs//test%03d/main.cpp", i);
                main = new File(mainPath);
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
