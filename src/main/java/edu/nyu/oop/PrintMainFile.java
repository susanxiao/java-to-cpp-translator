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
import java.util.List;
import java.util.TreeMap;

import edu.nyu.oop.util.*;

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
        mainImplementation.append(" {\n");

        if (summary.init2D != null) {
            String initPrimaryIdentifier = String.format(summary.init2D, summary.init2DDec);

            mainImplementation.append("\t\t"+initPrimaryIdentifier+" = "+"new __rt::Array<"+summary.init2DType+">("+summary.init2DSize+");\n");

            //reset them;
            summary.init2D = null;
            summary.init2DSize = null;
            summary.init2DType = null;
            summary.init2DDec = null;
        }

        for (Object o : n) {
            if (o instanceof Node) {
                GNode currentNode = (GNode) o;
                if (currentNode.getName().equals("FieldDeclaration")) {
                    mainImplementation.append("\t");
                    visitFieldDeclaration(currentNode);
                } else if (currentNode.getName().equals("ExpressionStatement")) {
                    mainImplementation.append("\t");
                    visitExpressionStatement(currentNode);
                } else if (currentNode.getName().equals("ForStatement")) {
                    mainImplementation.append("\t");
                    visitForStatement(currentNode);
                }
            }
        }
        mainImplementation.append("\t}\n");
    }

    public void visitMethodDeclaration(GNode n) {
        //System.out.println(n.getString(3));
        /*if(n.getString(3).equals("methodMain")){
            mainImplementation.append("int main (int argc, char ** args) \n{\n\n");
        }*/
        // block node is always last child of main method node
        int blockIndex = n.size() - 1;
        GNode block = (GNode) n.getNode(blockIndex);
        String methodName = n.getString(3);
        //System.out.println(methodName);

        summary.localVariables = new HashMap<>();

        if (methodName.equals("methodMain")) {
            if (summaryTraversal.usesArgs) {
                mainImplementation.append("int main (int argc, char ** argv) \n{\n\n");
                mainImplementation.append("\t//get command line arguments. convert between args(java) and argv(c++). for test22, 23...\n");
                mainImplementation.append("\t__rt::Array<String>* args = new __rt::Array<String>(argc-1);\n");
                mainImplementation.append("\tfor (int a = 1; a < argc; a++) {\n");//starts with a=1(not a=0) because the first argument in cpp is the command
                mainImplementation.append("\t\tString argument = new __String(argv[a]);\n");
                //mainImplementation.append("\t//cout << \"add arg: \"<< argument->data << endl;");
                mainImplementation.append("\t\targs->__data[a-1] = argument;\n");
                mainImplementation.append("\t}\n\n");

                summary.localVariables.put("args", "String[");
            } else
                mainImplementation.append("int main(void)\n{\n\n");
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
            mainImplementation.append("\treturn 0;\n");
            mainImplementation.append("}");
        }
    }

    public void visitFieldDeclaration(GNode n) {
        String fieldDeclaration = "\t";

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
                    fieldDeclaration += "__rt::Array<__rt::Array<" + type + ">*>* ";
                }
                else {
                    fieldDeclaration += "__rt::Array<" + type + ">* ";
                }
            }
        }
        else {
            fieldDeclaration += type+" ";
        }

        if (n.getNode(2).getName().equals("Declarators")) {
            Node declaratorsNode = n.getNode(2);
            for (Object o : declaratorsNode) {
                if (o instanceof Node) {
                    Node currentDeclarator = (Node) o;
                    String variable = currentDeclarator.getString(0);
                    /*if(type.equals("byte")){
                        //System.out.println("need to change byte");
                        type = "unsigned char";
                    }*/
                    summary.classVariables.put(variable, type);

                    if (summary.localVariables == null)
                        summary.localVariables = new HashMap<>();

                    summary.localVariables.put(variable, type);
                    fieldDeclaration += variable;
                    if (currentDeclarator.getNode(2) != null) {
                        if (currentDeclarator.getNode(2).getName().equals("NewClassExpression")) {
                            fieldDeclaration += " = new ";
                            if (isTypeArray) {
                                fieldDeclaration += "__rt::Array<" + n.getNode(1).getNode(0).getString(0) + ">";

                                String typeString = n.getNode(1).getNode(0).getString(0) + "[";
                                summary.localVariables.put(variable, typeString);
                            } else {
                                String qualifiedIdentifier = currentDeclarator.getNode(2).getNode(2).getString(0);
                                fieldDeclaration += qualifiedIdentifier;

                                summary.localVariables.put(variable, qualifiedIdentifier);
                            }
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
                                        } else if (currentArg.getName().equals("IntegerLiteral")) {
                                            fieldDeclaration += currentArg.getString(0);
                                        }
                                    }
                                    fieldDeclaration += ");";
                                }
                            } else {
                                fieldDeclaration += "();\n";
                            }

                        } else if (currentDeclarator.getNode(2).getName().equals("NewArrayExpression")) {
                            fieldDeclaration += " = ";

                            Node newArray = currentDeclarator.getNode(2);
                            String qualifiedIdentifier = newArray.getNode(0).getString(0);
                            //check if size is negative or positive
                            boolean sizeIsNegative = false;
                            String negSize="";
                            Node sizeMightBeNegative = NodeUtil.dfs(newArray,"UnaryExpression");
                            if(sizeMightBeNegative!=null){
                                if(sizeMightBeNegative.getString(0).equals("-")){
                                    sizeIsNegative = true;
                                    negSize = "-"+sizeMightBeNegative.getNode(1).getString(0);
                                }
                            }

                            String size = newArray.getNode(1).getNode(0).getString(0);//size is positive
                            if(sizeIsNegative){//size is negative
                                size = negSize;
                            }
                            if (is2D) {
                                String size2 = newArray.getNode(1).getNode(1).getString(0);

                                fieldDeclaration += "new __rt::Array<__rt::Array<" + n.getNode(1).getNode(0).getString(0) + ">*>("+size2+");\n";

                                summary.init2D = variable+"->__data[%s]";
                                summary.init2DSize = size2;
                                summary.init2DType = n.getNode(1).getNode(0).getString(0);

                                summary.localVariables.put(variable, type+"[[");
                            }
                            else {
                                if (!type.equals(qualifiedIdentifier))
                                    fieldDeclaration += "(__rt::Array<" + type + ">*) ";


                                fieldDeclaration += "new __rt::Array<" + qualifiedIdentifier + ">(" + size + ");\n";

                                //check NegativeArraySizeException()
                                //fieldDeclaration += "checkNegativeArraySize(" + size + ");";
                                if (Integer.parseInt(size) < 0) {
                                    //System.out.println("its a negative size");
                                    //need to throw NegativeArraySizeException() BEFORE assigning the array
                                    String fieldDeclaration_deepCopy = "";
                                    for (int i = 0; i < fieldDeclaration.length(); i++) {
                                        char c = fieldDeclaration.charAt(i);
                                        //System.out.println("c" + c);
                                        fieldDeclaration_deepCopy += c;
                                    }
                                    //System.out.println("fieldDeclaration_deepCopy" + fieldDeclaration_deepCopy);
                                    fieldDeclaration = "";
                                    fieldDeclaration += "\tthrow java::lang::NegativeArraySizeException();  //size of array is negative\n\t\t";
                                    fieldDeclaration += fieldDeclaration_deepCopy;
                                }
                                //else{
                                //System.out.println("its a positive size");
                                //}
                                summary.localVariables.put(variable, type + "[");
                            }
                        } else if (currentDeclarator.getNode(2).getName().equals("CastExpression")) {
                            String typeDeclarator = currentDeclarator.getNode(2).getNode(0).getNode(0).getString(0);
                            String primaryIdentifier = currentDeclarator.getNode(2).getNode(1).getString(0);

                            fieldDeclaration += " = (" + typeDeclarator + ") " + primaryIdentifier + ";\n";
                        } else if (currentDeclarator.getNode(2).getName().equals("ArrayCastExpression")) {
                            Node typeNode = currentDeclarator.getNode(2).getNode(0).getNode(0);
                            String typeString = typeNode.getString(0);
                            Node primaryIdNode = currentDeclarator.getNode(2).getNode(0).getNode(1);
                            String primaryId = primaryIdNode.getString(0);

                            fieldDeclaration += " = (__rt::Array<" + typeString + ">*) " + primaryId + ";\n";

                            summary.localVariables.put(variable, typeString + "[");

                            /*fieldDeclaration += "("+ currentDeclarator.getNode(2).getNode(0).getNode(0).getString(0)+");";
                            mainImplementation.append(fieldDeclaration+"\n");//append here, Orelse the order are incorrect becasue the visit statments directly appends to mainImplementation string
                            fieldDeclaration="";
                            visitForStatement((GNode) currentDeclarator.getNode(2).getNode(1).getNode(0)); */


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

                        } else if (currentDeclarator.getNode(2).getName().equals("IntegerLiteral")) {
                            fieldDeclaration += " = "+currentDeclarator.getNode(2).getString(0)+";\n";
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
        //fieldDeclaration += "end field dec";
        mainImplementation.append(fieldDeclaration + "\n");
    }

    public String checkIndexBoundsForSubscriptExpression(GNode n){
        String arrayVariableName = n.getNode(0).getString(0);
        String arrayIndex = n.getNode(1).getString(0);

        return "\tcheckIndex("+ arrayVariableName + ", " + arrayIndex+ ");\n\t\t";
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

        return subscriptExpression_str;
    }

    public void visitExpressionStatement(GNode n) {
        String expressionStatement = "\t\t";
        //See if there is an array in the expression Statement
        //If there is an array, make sure we do not get an ArrayIndexOutOfBoundsException
        Node checkIfThereIsAnArray = NodeUtil.dfs(n,"SubscriptExpression");
        if(checkIfThereIsAnArray!=null){
            Node checkIfItsA2DArray = NodeUtil.dfs(checkIfThereIsAnArray,"SubscriptExpression");
            if(checkIfItsA2DArray!=null){//2D array
                //Todo Do I Also need to checkIndexboudns error for 2d Arrays?
                //Node secondDimension = checkIfItsA2DArray;
                //String  checkIndexBoundError =checkIndexBoundsForSubscriptExpression((GNode) checkIfItsA2DArray );
                //expressionStatement+=checkIndexBoundError;

            }else{//1D array
                String  checkIndexBoundError =checkIndexBoundsForSubscriptExpression((GNode) checkIfThereIsAnArray);

                //System.out.println("there is an array in the expression");
                //System.out.println(checkIfThereIsAnArray.getName());

                /*
                String arrayVariableName = checkIfThereIsAnArray.getNode(0).getString(0);
                String arrayIndex = checkIfThereIsAnArray.getNode(1).getString(0);
                expressionStatement += "\tcheckIndex("+ arrayVariableName + ", " + arrayIndex+ ");\n\t\t";
                */
                expressionStatement+=checkIndexBoundError;
            }

        }
        else{
            //System.out.println("No array in the expression");
        }

        if (n.getNode(0).getName().equals("CallExpression")) {
            if (n.getNode(0).getNode(0).getName().equals("SelectionExpression")) {
                if (n.getNode(0).getNode(0).getNode(0).getString(0).equals("cout")) {
                    expressionStatement += n.getNode(0).getNode(0).getNode(0).getString(0) + " << ";
                    String primaryId = null;
                    Node arguments = n.getNode(0).getNode(3);
                    for (Object o : arguments) {
                        if (o instanceof Node) {
                            Node currentNode = (Node) o;
                            if (currentNode.getName().equals("CallExpression")) {
                                if (currentNode.getNode(0).getName().equals("PrimaryIdentifier")) {
                                    primaryId = currentNode.getNode(0).getString(0);
                                    if (summaryTraversal.classes.containsKey(primaryId)) {
                                        expressionStatement += "__" + primaryId + "::";
                                        for (int i = 1; i < currentNode.size(); i++) {
                                            Object o1 = currentNode.get(i);
                                            if (o1 instanceof String)
                                                expressionStatement += (String) o1;
                                            else if (o1 instanceof Node) {
                                                Node callChild = (Node) o1;
                                                expressionStatement += "(";
                                                if (callChild.getName().equals("Arguments")) {
                                                    for (int j = 0; j < callChild.size(); j++) {
                                                        if (!summaryTraversal.classes.containsKey(callChild.getString(j))) {
                                                            expressionStatement += callChild.getString(j);
                                                            if (j < callChild.size() - 1)
                                                                expressionStatement += ", ";
                                                        }
                                                    }
                                                }
                                                expressionStatement += ")";
                                            }
                                        }
                                    } else
                                        expressionStatement += primaryId;
                                } else if (currentNode.getNode(0).getName().equals("SelectionExpression")) {
                                    primaryId = currentNode.getNode(0).getNode(0).getString(0);
                                    expressionStatement += primaryId;
                                    expressionStatement += "->" + currentNode.getNode(0).getString(1);
                                } else if (currentNode.getNode(0).getName().equals("CallExpression")) {
                                    primaryId = currentNode.getNode(0).getNode(0).getString(0);
                                    expressionStatement += primaryId;
                                    String methodNameCall = currentNode.getNode(0).getString(currentNode.getNode(0).size() - 2);
                                    if (!methodNameCall.startsWith("method")) {
                                        switch (methodNameCall) {
                                        case "toString":
                                        case "hashCode":
                                        case "equals":
                                        case "getClass":
                                            break;
                                        default:
                                            methodNameCall = "method" + methodNameCall.substring(0, 1).toUpperCase() + methodNameCall.substring(1);
                                        }
                                    }
                                    expressionStatement += "->__vptr->" + methodNameCall + "(";
                                    Node arguments1 = currentNode.getNode(0).getNode(3);
                                    for (int i = 0; i < arguments1.size(); i++) {
                                        if (i > 0)
                                            expressionStatement += ", ";
                                        expressionStatement += arguments1.getString(i);
                                    }
                                    expressionStatement += ")";
                                } else if (currentNode.getNode(0).getName().equals("CastExpression")) {
                                    expressionStatement += "(";
                                    for (Object o1 : currentNode.getNode(0)) {
                                        if (o1 instanceof Node) {
                                            Node currentChild = (Node) o1;
                                            if (currentChild.getName().equals("Type")) {
                                                primaryId = (primaryId == null ? "" : primaryId) + "("+currentChild.getNode(0).getString(0)+") ";
                                                expressionStatement += primaryId;
                                            }
                                            else if (currentChild.getName().equals("SubscriptExpression")) {
                                                //String subscriptExpressionSTR= returnSubscriptExpression((GNode) currentChild);
                                                GNode primaryIdentifier0 = (GNode) currentChild.get(0);
                                                GNode primaryIdentifier1 = (GNode) currentChild.get(1);
                                                primaryId = (primaryId == null ? "" : primaryId) + primaryIdentifier0.get(0).toString() + "->__data[";
                                                primaryId += primaryIdentifier1.get(0).toString() + "]";

                                                expressionStatement += primaryIdentifier0.get(0).toString() + "->__data[";
                                                expressionStatement += primaryIdentifier1.get(0).toString() + "]";
                                            }
                                        }
                                    }
                                    expressionStatement += ")";
                                } else {
                                    expressionStatement += currentNode.getNode(0).getString(0);
                                }
                                // expressionStatement += currentNode.getNode(0).getString(0);
                                String method = currentNode.getString(2);
                                if (!(method.startsWith("method"))) {
                                    switch (method) {
                                    case "toString":
                                    case "hashCode":
                                    case "equals":
                                    case "getClass":
                                        break;
                                    default:
                                        method = "method" + method.substring(0, 1).toUpperCase() + method.substring(1);
                                    }
                                }
                                if (primaryId != null && !summaryTraversal.classes.containsKey(primaryId)) {
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
                                            } else if (currentNode.getNode(0).getName().equals("CallExpression")) {
                                                Node callExpression = currentNode.getNode(0);
                                                String primaryIdentifier = callExpression.getNode(0).getString(0);

                                                expressionStatement += primaryIdentifier + "->__vptr";
                                                for (int i = 1; i < callExpression.size(); i++) {
                                                    Object o2 = callExpression.get(i);
                                                    if (o2 instanceof String) {
                                                        String methodNameCallExpression = (String) o2;
                                                        if (!(methodNameCallExpression.startsWith("method"))) {
                                                            switch (methodNameCallExpression) {
                                                            case "toString":
                                                            case "hashCode":
                                                            case "equals":
                                                            case "getClass":
                                                                break;
                                                            default:
                                                                methodNameCallExpression = "method" + methodNameCallExpression.substring(0, 1).toUpperCase()
                                                                                           + methodNameCallExpression.substring(1);
                                                            }
                                                        }

                                                        expressionStatement += "->" + methodNameCallExpression;
                                                    } else if (o2 instanceof Node) {
                                                        Node arguments1 = (Node) o2;
                                                        expressionStatement += "(";
                                                        for (int j = 0; j < arguments1.size(); j++) {
                                                            if (j > 0)
                                                                expressionStatement += ", ";
                                                            expressionStatement += arguments1.getString(j);
                                                        }
                                                        expressionStatement += ")";
                                                    }
                                                }
                                            } else if (o1 instanceof String) {
                                                expressionStatement += o1.toString();
                                            }
                                        }
                                    }
                                    else {
                                        expressionStatement += primaryId;
                                    }
                                    expressionStatement += ")";
                                    /*if (method.equals("toString")) {
                                        expressionStatement += "->data";
                                    } else if (primaryIdentifer.equals("cout")) {
                                        expressionStatement += "->data";
                                    }*/
                                }
                            } else if (currentNode.getName().equals("StringLiteral")) {
                                expressionStatement += currentNode.getString(0) + " ";
                            } else if (currentNode.getName().equals("SelectionExpression")) {
                                boolean isClass = false;
                                String key = currentNode.getNode(0).getString(0); //primary identifier
                                if (summaryTraversal.classes.containsKey(key)) { //primary identifier is class, not var
                                    expressionStatement += "__" + key;
                                    isClass = true;
                                } else
                                    expressionStatement += key;
                                for (int i = 1; i < currentNode.size(); i++) {
                                    String field = currentNode.getString(i);
                                    if (isClass) { //primary identifier is class, not var
                                        isClass = summaryTraversal.classes.containsKey(field);
                                        if (isClass) //if field is also class
                                            expressionStatement += "::__" + field;
                                        else
                                            expressionStatement += "::" + field;
                                    } else {
                                        boolean gateParent = true;

                                        // check if parent should be used
                                        String className = summary.classVariables.get(key); //class of primary identifier

                                        for (FieldDeclaration dec : summaryTraversal.findClass(className).declarations) {
                                            if (dec.variableName.equals(field) || field.equals("data"))
                                                gateParent = false;
                                        }

                                        expressionStatement += gateParent ? "->parent." + field : "->" + field;
                                        //expressionStatement += gateParent ? "->parent." + field + "->data" : "->" + field + "->data";
                                    }

                                }
                            } else if (currentNode.getName().equals("PrimaryIdentifier")) {
                                expressionStatement += currentNode.getString(0);
                                // we have to determine if the parent should be used when accessing the dataString
                                if (currentNode.size() - 1 > 0) { //when the primary identifier has more children than just a variable name, parent usage becomes important
                                    String variable = currentNode.getNode(0).getString(0);
                                    String dataString = currentNode.getString(1);
                                    boolean gateParent = true;
                                    for (int i = 0; i < summary.classNames.size(); i++) {
                                        if (summary.variables.get(i).equals(variable)) {
                                            String classVar = summary.classNames.get(i);
                                            for (int j = 0; i < summary.classNames.size(); i++) {
                                                if (summary.classNames.get(i).equals(classVar) && summary.variables.get(i).equals(dataString)) {
                                                    gateParent = false;
                                                }
                                            }
                                        }
                                    }
                                    if (gateParent) {
                                        expressionStatement += variable;
                                        expressionStatement += "->parent." + dataString; //+ "->data";
                                    } else {
                                        expressionStatement += variable;
                                        expressionStatement += "->" + dataString; // + "->data";
                                    }
                                }
                            } else if (currentNode.getName().equals("SubscriptExpression")) {
                                boolean itsA1DArray = currentNode.getNode(0).getName().equals("PrimaryIdentifier");
                                if(itsA1DArray) {
                                    //expressionStatement += "reached here : subscript expression";
                                    GNode primaryIdentifier0 = (GNode) currentNode.get(0);
                                    GNode primaryIdentifier1 = (GNode) currentNode.get(1);
                                    if (!summary.localVariables.get(primaryIdentifier0.get(0).toString()).equals("String["))
                                        expressionStatement += "(String) ";

                                    expressionStatement += primaryIdentifier0.get(0).toString() + "->__data[";
                                    expressionStatement += primaryIdentifier1.get(0).toString() + "]";
                                }
                                else{ //2D array. assume we don't have 3D,4D,5D...
                                    primaryId = (primaryId == null ? "" : primaryId) + currentNode.getNode(0).getNode(0).getString(0) + "->__data[";
                                    primaryId += currentNode.getNode(0).getNode(1).getString(0) + "]";
                                    primaryId += "->data["+currentNode.getNode(1).getString(0) + "]";

                                    expressionStatement += currentNode.getNode(0).getNode(0).getString(0) + "->__data[";
                                    expressionStatement += currentNode.getNode(0).getNode(1).getString(0) + "]->__data[";
                                    expressionStatement += currentNode.getNode(1).getString(0) + "]";

                                }

                            }
                        }
                    }
                }
                expressionStatement += " << endl;";
            } else {
                Node callExpressionNode = n.getNode(0);
                String methodName = callExpressionNode.getString(2);
                if (!(methodName.startsWith("method"))) {
                    switch (methodName) {
                        case "toString":
                        case "hashCode":
                        case "equals":
                        case "getClass":
                            break;
                        default:
                            methodName = "method" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

                    }
                }

                    /*
                    //check method overloading
                    //System.out.println("check index");
                    //System.out.println(methodName);
                    //get the class the method belongs to
                    String varName = callExpressionNode.getNode(0).getString(0);
                    String classOfVar = summaryTraversal.fieldsInMainInfo.get(varName);
                    //System.out.println("classOfVar: " + classOfVar);

                    int index=-1;
                    int theClassWithOverloading=-1;
                    for(int i=0; i<summaryTraversal.allMethods_checkMethodOverloading.size();i++){
                        if(summaryTraversal.allMethods_checkMethodOverloading.get(i).get(0).equals(classOfVar)){
                            index= summaryTraversal.allMethods_checkMethodOverloading.get(i).indexOf(methodName);//returns the first on it finds.
                            theClassWithOverloading=i;
                        }
                    }

                    //System.out.println(index);
                    if(index>-1){
                        if(summaryTraversal.isOverLoaded.get(theClassWithOverloading).get(index).equals("overloaded")) {
                            //System.out.println("--fix overloading--");
                            methodName += "_";
                            //get type of parameters
                            Node arguments = callExpressionNode.getNode(3);
                            ArrayList<String> addParamsToMethodName = new ArrayList<String>();
                            for (int i = 1; i < arguments.size(); i++) {
                                Node currNode = arguments.getNode(i);
                                if (currNode.getName().equals("PrimaryIdentifier")) {
                                    String varType = summaryTraversal.fieldsInMainInfo.get(currNode.getString(0));
                                    //System.out.println(varType);
                                    if (summaryTraversal.fieldsInMainInfo.get(currNode.getString(0)).equals("unsigned char")) {
                                        //methodName += "int";
                                        addParamsToMethodName.add("int");
                                    } else {
                                        //methodName += varType;
                                        addParamsToMethodName.add(varType);
                                    }
                                } else if (currNode.getName().equals("FloatingPointLiteral")) {
                                    //methodName += "double";
                                    addParamsToMethodName.add("double");
                                } else if (currNode.getName().equals("CastExpression")) {

                                    //methodName += currNode.getNode(0).getNode(0).getString(0);
                                    addParamsToMethodName.add(currNode.getNode(0).getNode(0).getString(0));
                                } else if (currNode.getName().equals("NewClassExpression")) {
                                    //methodName += currNode.getNode(2).getString(0);
                                    addParamsToMethodName.add(currNode.getNode(2).getString(0));
                                }
                            }
                            String tempNewMethodName = methodName;
                            for (int i = 0; i < addParamsToMethodName.size(); i++) {
                                tempNewMethodName += addParamsToMethodName.get(i);
                                //System.out.println("temp: " + tempNewMethodName);
                            }
                            //if tempNewMethod is not in the list of methods, change(downcast/upcast)
                            int theIndexofParamToChange = addParamsToMethodName.size()-1;
                            boolean methodDoesNotExist = true;
                            while(methodDoesNotExist) {
                                for (int i = 0; i < summaryTraversal.overloadedMethodNames.size(); i++) {
                                    if (summaryTraversal.overloadedMethodNames.get(i).get(1).equals(tempNewMethodName)) {
                                        methodDoesNotExist = false;
                                        methodName = tempNewMethodName;
                                        break;
                                    }
                                }

                                if(methodDoesNotExist) {
                                    //try editing tempNewMethodName
                                    addParamsToMethodName.set(theIndexofParamToChange, "Object");
                                    //Todo :fix Bug- works with test32, but not sure for future tests
                                    theIndexofParamToChange--;
                                    tempNewMethodName = methodName;
                                    for (int i = 0; i < addParamsToMethodName.size(); i++) {
                                        tempNewMethodName += addParamsToMethodName.get(i);
                                    }

                                    //System.out.println("new temp name : " + tempNewMethodName);

                                    if (!methodDoesNotExist) {
                                        System.out.println("need to change name");
                                    } else {
                                        System.out.println("use this name");
                                    }
                                }
                            }
                        }

                    }

                } */
                    
                String expressionStatement1 = "";
                String variableCalling = callExpressionNode.getNode(0).getString(0);
                expressionStatement += callExpressionNode.getNode(0).getString(0) + "->";
                String primaryIdentifer = callExpressionNode.getNode(0).getString(0);
                expressionStatement += callExpressionNode.getNode(1).getString(0) + "->";

                String primaryClass = summary.localVariables.get(variableCalling).replace("__", "");
                summary.overLoadedMethods = summaryTraversal.overLoadedMethods.get(primaryClass);

                if (summary.overLoadedMethods != null && summary.overLoadedMethods.containsKey(methodName)) {

                    if (callExpressionNode.getNode(3).getName().equals("Arguments")) {
                        String arguments = "(";
                        Node argumentsNode = callExpressionNode.getNode(3);

                        ArrayList<MethodImplementation> methods = new ArrayList<>(summary.overLoadedMethods.get(methodName));
                        ArrayList<Integer> distances = new ArrayList<>();

                        for (int i = 0; i < methods.size(); i++) {
                            if (methods.get(i).parameters.size() != argumentsNode.size() - 1) //number of arguments in Node includes __this
                                //number of arguments is wrong
                                methods.remove(i--);
                            else
                                distances.add(0);
                        }

                        if (methods.size() == 1) {
                            arguments += argumentsNode.getString(0) + ", ";

                            if (argumentsNode.getNode(1).getName().equals("NewClassExpression")) {
                                Node newClassExpressionNode = argumentsNode.getNode(1);
                                arguments += "new "+ newClassExpressionNode.getNode(2).getString(0) + "(";
                                if (newClassExpressionNode.getNode(3).size() > 0)
                                    arguments += newClassExpressionNode.getNode(3).getNode(0).getString(0);
                                arguments += ")";
                            } else if (argumentsNode.getNode(1).getName().equals("PrimaryIdentifier")) {
                                String primaryIdentifier1 = argumentsNode.getNode(1).getString(0);
                                if (summary.classVariables.get(primaryIdentifer).equals(summary.classVariables.get(primaryIdentifier1))) {
                                    arguments += argumentsNode.getNode(1).getString(0);
                                }
                            } else {
                                arguments += argumentsNode.getString(0);
                            }
                            arguments += ");";

                            expressionStatement += methods.get(0).name + arguments;
                        }
                        else {
                            int argIndex = 0;
                            for (Object o : argumentsNode) {
                                if (o instanceof Node) {
                                    for (int i = 0; i < methods.size(); i++) {
                                        MethodImplementation m = methods.get(i);
                                        if (m.parameters.size() <= argIndex) {
                                            methods.remove(i);
                                            distances.remove(i--);
                                        } else {

                                            //p is the one the method requires
                                            //argumentType is the one when calling
                                            ParameterImplementation p = m.parameters.get(argIndex);
                                            String argumentType = "";

                                            if (((Node) o).getName().equals("PrimaryIdentifier")) {
                                                argumentType = summary.localVariables.get(((Node) o).getString(0)).replace("__", "");
                                            }
                                            else if (((Node) o).getName().equals("FloatingPointLiteral")) {
                                                argumentType = "float";
                                            }
                                            else if (((Node) o).getName().equals("CastExpression")) {
                                                argumentType = ((Node) o).getNode(0).getNode(0).getString(0);
                                            }
                                            else if (((Node) o).getName().equals("NewClassExpression")) {
                                                argumentType = ((Node) o).getNode(2).getString(0);
                                            }
                                            else {
                                                //TODO: other things
                                                System.out.println("Missed the argumentType");
                                            }

                                            //chain of primitives: byte->short->char?->int->long->float->double
                                            if (!p.type.equals(argumentType)) {
                                                int increaseBy = 1;
                                                switch (argumentType) {
                                                    case "uint8_t":
                                                    case "byte":
                                                        if (!p.type.equals("short"))
                                                            increaseBy++;
                                                        else
                                                            break;
                                                    case "short":
                                                        if (!p.type.equals("char"))
                                                            increaseBy++;
                                                        else
                                                            break;
                                                    case "char" :
                                                        if (!p.type.equals("int"))
                                                            increaseBy++;
                                                        else
                                                            break;
                                                    case "int32_t":
                                                    case "int":
                                                        if (!p.type.equals("long"))
                                                            increaseBy++;
                                                        else
                                                            break;
                                                    case "long":
                                                        if (!p.type.equals("float"))
                                                            increaseBy++;
                                                        else
                                                            break;
                                                    case "float":
                                                        if (!p.type.equals("double"))
                                                            increaseBy++;
                                                        else
                                                            break;
                                                    case "double": //then p.type is an object
                                                        methods.remove(i);
                                                        distances.remove(i--);
                                                        continue;
                                                    default: //both are objects...does this check for String?
                                                        ClassImplementation currentClass = summaryTraversal.findClass(argumentType);
                                                        while (currentClass != null && !currentClass.name.equals(p.type)) {
                                                            currentClass = currentClass.superClass;
                                                            increaseBy++;
                                                        }

                                                        if (currentClass == null) {
                                                            if (p.type.equals("Object"))
                                                                increaseBy++;
                                                            else {
                                                                methods.remove(i);
                                                                distances.remove(i--);
                                                                continue;
                                                            }
                                                        }
                                                        break;
                                                }
                                                distances.set(i, distances.get(i)+increaseBy);
                                            }
                                        }
                                    }
                                    argIndex++;
                                }
                            }

                            if (methods.size() > 0) {
                                //find the smallest distance one
                                int min = 0;
                                for (int i = 1; i < methods.size(); i++) {
                                    if (distances.get(i) < distances.get(min))
                                        min = i;
                                }

                                arguments += argumentsNode.getString(0);

                                MethodImplementation chosenMethod = methods.get(min);
                                for (int i = 1; i < argumentsNode.size(); i++) {
                                    arguments += ", ";
                                    Object o = argumentsNode.getNode(i);
                                    if (o instanceof Node) {
                                        if (argumentsNode.getNode(i).getName().equals("NewClassExpression")) {
                                            Node newClassExpressionNode = argumentsNode.getNode(i);
                                            String paramType = newClassExpressionNode.getNode(2).getString(0);
                                            if (!chosenMethod.parameters.get(i-1).type.equals(paramType)) //if it does not match argument type add cast
                                                arguments += "("+chosenMethod.parameters.get(i-1).type+") ";
                                            arguments += "new __"+ paramType + "(";
                                            if (argumentsNode.getNode(i).getNode(3).size() > 0)
                                                arguments += argumentsNode.getNode(i).getNode(3).getNode(0).getString(0);
                                            arguments += ")";
                                        }
                                        else if (argumentsNode.getNode(i).getName().equals("PrimaryIdentifier")){
                                            String primaryIdentifier1 = argumentsNode.getNode(i).getString(0);
                                            if (!chosenMethod.parameters.get(i-1).type.equals(summary.classVariables.get(primaryIdentifier1))) {
                                                String type = chosenMethod.parameters.get(i-1).type;
                                                arguments += "("+(type.equals("int")? "int32_t" : type)+ ") ";
                                            }
                                            arguments += argumentsNode.getNode(i).getString(0);
                                        }
                                        else if (argumentsNode.getNode(i).getName().equals("FloatingPointLiteral")) {
                                            arguments += argumentsNode.getNode(i).getString(0);
                                        }
                                        else if (argumentsNode.getNode(i).getName().equals("CastExpression")) {
                                            String cast = argumentsNode.getNode(i).getNode(0).getNode(0).getString(0);
                                            if (cast.equals("byte")) cast = "uint8_t";
                                            else if (cast.equals("int")) cast = "int32_t";
                                            arguments += "("+argumentsNode.getNode(i).getNode(0).getNode(0).getString(0)+") ";
                                            if (argumentsNode.getNode(i).getNode(1).getName().equals("PrimaryIdentifier"));
                                                arguments += argumentsNode.getNode(i).getNode(1).getString(0);
                                        }
                                        else {
                                            //TODO: here
                                            System.out.println("Missed the calling argumentType");
                                        }
                                    }
                                }
                                arguments += ");";

                                expressionStatement += chosenMethod.overLoadedName + arguments;
                            }
                            else {
                                System.out.println("SOMETHING WENT WRONG!!");
                            }
                        }
                    }
                }
                else {
                    expressionStatement += methodName;
                    if (callExpressionNode.getNode(3).getName().equals("Arguments")) {
                        expressionStatement += "(";
                        Node argumentsNode = callExpressionNode.getNode(3);
                        expressionStatement += argumentsNode.getString(0) + ", ";

                        if (argumentsNode.getNode(1).getName().equals("NewClassExpression")) {
                            Node newClassExpressionNode = argumentsNode.getNode(1);
                            String newClassIdentifier = "new ";
                            newClassIdentifier += newClassExpressionNode.getNode(2).getString(0) + "(";
                            expressionStatement += newClassIdentifier;
                            if (newClassExpressionNode.getNode(3).size() > 0)
                                expressionStatement += newClassExpressionNode.getNode(3).getNode(0).getString(0);
                            expressionStatement += ")";
                        } else if (argumentsNode.getNode(1).getName().equals("PrimaryIdentifier")) {
                            String primaryIdentifier1 = argumentsNode.getNode(1).getString(0);
                            if (summary.classVariables.get(primaryIdentifer).equals(summary.classVariables.get(primaryIdentifier1))) {
                                expressionStatement += argumentsNode.getNode(1).getString(0);
                            } else {
                                expressionStatement += "(" + summary.classVariables.get(primaryIdentifer) + ") " + primaryIdentifier1;
                                expressionStatement1 += "\tClass k" + summary.checkClassCounter + " = " + variableCalling + "->__vptr->getClass(" + variableCalling + ");\n";
                                expressionStatement1 += "\tcheckClass(k" + summary.checkClassCounter + ", " + primaryIdentifier1 + ");\n\n";
                                summary.checkClassCounter++;
                            }
                        } else {
                            expressionStatement += argumentsNode.getString(0);
                        }
                        expressionStatement += ");";
                        String temp = expressionStatement;
                        expressionStatement = "";
                        expressionStatement += expressionStatement1;
                        expressionStatement += temp;
                    }
                }
            }

        } else if (n.getNode(0).getName().equals("Expression")) {
            Node expressionNode = n.getNode(0);
            String primaryIdentifierExpression = "";
            boolean isArray = false;
            for (int nodeIndex=0; nodeIndex< expressionNode.size(); nodeIndex++) {
                Object o = expressionNode.get(nodeIndex);
                if (o instanceof Node) {
                    Node currNode = (Node) o;
                    boolean needToGetSecondArgumentForCheckStoreFunction = false;
                    if (currNode.getName().equals("SelectionExpression")) {
                        String varName = currNode.getNode(0).getString(0);
                        primaryIdentifierExpression = varName;
                        expressionStatement += varName;

                        for (int i = 1; i < currNode.size(); i++) {
                            boolean gateParent = true;
                            String field = currNode.getString(i);
                            // check if parent should be used
                            String className = summary.classVariables.get(varName); //class of primary identifier
                            for (FieldDeclaration dec : summaryTraversal.findClass(className).declarations) {
                                if (dec.variableName.equals(field))
                                    gateParent = false;
                            }
                            expressionStatement += gateParent ? "->parent." + field : "->" + field;
                        }
                    } else if (currNode.getName().equals("SubscriptExpression")) {
                        boolean itsA2DArray = currNode.getNode(0).getName().equals("SubscriptExpression");
                        if(!itsA2DArray) { //1D Array
                            //expressionStatement += "//check array Types: use the checkStore() function in java_lang.h\n\t\t";
                            GNode primaryIdentifier0 = (GNode) currNode.get(0);//test26: as
                            GNode primaryIdentifier1 = (GNode) currNode.get(1);//test26: i

                            expressionStatement += "checkStore(" + primaryIdentifier0.getString(0) + ", ";
                            needToGetSecondArgumentForCheckStoreFunction = true;
                            //Get RightHandSide type (should be the next next node. the next node should be "=")
                            Node rightSideOfExpression = expressionNode.getNode(nodeIndex + 2);
                            if (rightSideOfExpression.getName().equals("NewClassExpression")) {
                                String qualifiedIdentifier = rightSideOfExpression.getNode(2).getString(0);
                                expressionStatement += "new __" + qualifiedIdentifier;

                                for (int i = 3; i < rightSideOfExpression.size(); i++) { // write "(i)"
                                    Object o1 = rightSideOfExpression.get(i);
                                    if (o1 instanceof Node) {
                                        Node currentChild = (Node) o1;
                                        if (currentChild.getName().equals("Arguments")) {
                                            expressionStatement += "(";
                                            for (int j = 0; j < currentChild.size(); j++) {
                                                if (j > 0)
                                                    expressionStatement += ", ";
                                                Object o3 = currentChild.get(j);
                                                if (o3 instanceof Node) {
                                                    if (((Node) o3).getName().equals("PrimaryIdentifier")) {
                                                        expressionStatement += ((Node) o3).getString(0);
                                                    }
                                                }
                                            }
                                            expressionStatement += ")";
                                        }
                                    }
                                }
                                expressionStatement += ");\n\t\t";
                            }


                            /*
                            //check type of primaryIdentifier0(variable as in test26)
                            java.util.List<Node> fieldDecNodes = NodeUtil.dfsAll(methodDecNode, "FieldDeclaration");
                            String theLeftSideArrayType="";
                            for (Node f : fieldDecNodes ) {
                                //System.out.println(f.getNode(2).getNode(0).getString(0)+", "+primaryIdentifier0.getString(0)+".");
                                if(f.getNode(2).getNode(0).getString(0).equals(primaryIdentifier0.getString(0))){
                                    Node declaratorNodeInFieldDec = NodeUtil.dfs(f,"Declarator");
                                    Node qualifiedIdentifierInFieldDec  = NodeUtil.dfs(declaratorNodeInFieldDec,"QualifiedIdentifier");
                                     theLeftSideArrayType = qualifiedIdentifierInFieldDec.getString(0);
                                    //System.out.println("theLeftSideArrayType: " + theLeftSideArrayType);
                                }else{
                                    //System.out.println("not found");
                                }
                            }

                            //Check if the right side type matches
                            Node rightSideArrayType = expressionNode.getNode(2);
                            Node qualifiedIdentifier_arrayType = NodeUtil.dfs(rightSideArrayType, "QualifiedIdentifier");
                            String theRightSideArrayType  =   qualifiedIdentifier_arrayType.getString(0);
                            //System.out.println("theRightSideArrayType: " + theRightSideArrayType);

                            if(!theLeftSideArrayType.equals(theRightSideArrayType) )  {
                                //System.out.println("check summary traversal");
                                //System.out.println(summaryTraversal.classes.toString());
                                //System.out.println(summaryTraversal.classes.containsKey(theRightSideArrayType));
                                ClassImplementation classOf_theRightSideArryaType = summaryTraversal.classes.get(theRightSideArrayType);
                                //System.out.println(classOf_theRightSideArryaType.superClassName);
                                if(classOf_theRightSideArryaType.superClassName !=null){
                                    if(classOf_theRightSideArryaType.superClassName.equals(theLeftSideArrayType)){//rightSideArrayType is a subclass of LeftClassArryType
                                        //System.out.println("(checking for ArrayStoreException)rightSideArrayType is a subclass of LeftClassArryType - so its okay");
                                    }
                                }
                                else{//doesn't have a superClass - rightSideArrayType is NOT a subclass of LeftClassArryType
                                    System.out.println("throw java.lan.ArrayStoreException" );
                                    expressionStatement += "throw java::lang::ArrayStoreException();\n";
                                }

                            }else{
                                 //System.out.println("(checking for ArrayStoreException) Same type");
                            }*/


                            primaryIdentifierExpression = primaryIdentifier0.get(0).toString();

                            expressionStatement += primaryIdentifier0.get(0).toString() + "->__data[";
                            expressionStatement += primaryIdentifier1.get(0).toString() + "]";
                            isArray = true;
                        }
                        else {
                            GNode primaryIdentifier = currNode.getGeneric(0);//test31: as[i]
                            String secondDimension = currNode.getNode(1).getString(0);//test31: j

                            String mainIdentifier = primaryIdentifier.getNode(0).getString(0);
                            String firstIdentifier = primaryIdentifier.getNode(1).getString(0);

                            expressionStatement += mainIdentifier+"->__data["+firstIdentifier+"]->__data["+secondDimension+"]";
                        }

                    } else if (currNode.getName().equals("NewClassExpression")) {
                        String qualifiedIdentifier = currNode.getNode(2).getString(0);

                        if (isArray && !summary.localVariables.get(primaryIdentifierExpression).equals(qualifiedIdentifier+"["))
                            expressionStatement += "("+summary.localVariables.get(primaryIdentifierExpression).replace("[", "")+") ";

                        expressionStatement += "new __"+qualifiedIdentifier;

                        for (int i = 3; i < currNode.size(); i++) {
                            Object o1 = currNode.get(i);
                            if (o1 instanceof Node) {
                                Node currentChild = (Node) o1;
                                if (currentChild.getName().equals("Arguments")) {
                                    expressionStatement+="(";
                                    for (int j = 0; j < currentChild.size(); j++) {
                                        if (j > 0)
                                            expressionStatement += ", ";
                                        Object o3 = currentChild.get(j);
                                        if (o3 instanceof Node) {
                                            if (((Node) o3).getName().equals("PrimaryIdentifier")) {
                                                expressionStatement += ((Node) o3).getString(0);
                                            }
                                        }
                                    }
                                    expressionStatement += ")";
                                }
                            }
                        }

                    } else if (currNode.getName().equals("PrimaryIdentifier")) {
                        expressionStatement += currNode.getString(0);
                    } else if (currNode.getName().equals("AdditiveExpression") || currNode.getName().equals("MultiplicativeExpression")) {
                        for (Object o1 : currNode) {
                            if (o1 instanceof Node) {
                                if (((Node) o1).getName().equals("PrimaryIdentifier")) {
                                    expressionStatement += ((Node) o1).getString(0);
                                }
                                else if (((Node) o1).getName().equals("IntegerLiteral")) {
                                    expressionStatement += ((Node) o1).getString(0);
                                }
                            }
                            else if (o1 instanceof String) { //the operator
                                expressionStatement += " "+o1+" ";
                            }
                        }
                    } else if (currNode.getName().equals("CastExpression")) {
                        String castType = currNode.getNode(0).getNode(0).getString(0);
                        String varName = currNode.getNode(1).getString(0);
                        expressionStatement += "(" + castType + ") " + varName;
                        String expressionStatement1 = "";
                        expressionStatement1 += "\tClass k" + summary.checkClassCounter + " = " + primaryIdentifierExpression
                                + "->__vptr->getClass(" + primaryIdentifierExpression + ");\n";
                        expressionStatement1 += "\tcheckClass(k" + summary.checkClassCounter + ", " + varName + ");\n\n";
                        summary.checkClassCounter++;
                        String temp = expressionStatement;
                        expressionStatement = "";
                        expressionStatement += expressionStatement1 + temp;
                    } else if (currNode.getName().equals("IntegerLiteral")) {
                        expressionStatement += currNode.getString(0);
                    }
                } else {
                    expressionStatement += " " + o.toString() + " ";
                }
            }
            expressionStatement += ";";
        }
        mainImplementation.append(expressionStatement + "\n");
    }

    public void visitWhileStatement(GNode n) {
        String whileStatement = "\twhile ";

        for (Object o : n) {
            if (o instanceof Node) {
                GNode currentNode = (GNode) o;
                if (currentNode.getName().equals("RelationalExpression")) {
                    GNode primaryIdentifierNode = (GNode) currentNode.get(0);
                    whileStatement += "("+primaryIdentifierNode.get(0).toString() +  " " + currentNode.get(1).toString() + " ";//<
                    for (int i = 2; i < currentNode.size(); i++) {
                        Object o1 = currentNode.get(i);
                        if (o1 instanceof Node) {
                            if (((Node) o1).getName().equals("IntegerLiteral")) {
                                whileStatement += ((Node) o1).getString(0);
                            }
                        }
                    }
                    whileStatement += ")";
                    mainImplementation.append(whileStatement);
                }
                else if (currentNode.getName().equals("Block")) {
                    visitNestedBlock(currentNode);
                }
            }
        }

    }

    public void visitForStatement(GNode n) {
        String forStatement = "\tfor ";
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

                            } else if (b_Node.getName().equals("RelationalExpression")) {

                                GNode primaryIdentifierNode = (GNode) b_Node.get(0);
                                forStatement += primaryIdentifierNode.get(0).toString(); //i
                                forStatement += " " + b_Node.get(1).toString() + " ";//<
                                GNode SelectionExpressNode = (GNode) b_Node.get(2);


                                //System.out.println("test Sel Node: "+SelectionExpressNode.toString());
                                if(SelectionExpressNode.getNode(0).getName().equals("SubscriptExpression")){
                                    //Select expression in 2D array
                                    Node twoDimension_SubscriptExpression = SelectionExpressNode.getNode(0);
                                    String subscriptExpression = returnSubscriptExpression((GNode) twoDimension_SubscriptExpression);
                                    //System.out.print("2nd for loop: "+subscriptExpression+"\n");
                                    forStatement += subscriptExpression;

                                }else {
                                    //Select expression in 1D array
                                    GNode PrimaryId_inSelectionExNode = (GNode) SelectionExpressNode.get(0);
                                    forStatement += PrimaryId_inSelectionExNode.get(0).toString(); // as
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
                    visitNestedBlock(currentNode);
                }
            }
        }

        //mainImplementation.append(forStatement + "\n\n");
    }

    // visitMethod


    public PrintMainFile(Runtime runtime, AstTraversal.AstTraversalSummary summaryTraversal) {
        this.runtime = runtime;
        this.summaryTraversal = summaryTraversal;
    }

    static class printMainFileSummary {
        String currentClassName;
        String filePrinted;
        int checkClassCounter;
        ArrayList<String> classNames = new ArrayList<>();
        ArrayList<String> variables = new ArrayList<>();
        TreeMap<String, String> classVariables = new TreeMap<>();

        HashMap<String, String> localVariables;
        HashMap<String, ArrayList<MethodImplementation>> overLoadedMethods;

        String init2D;
        String init2DSize;
        String init2DType;
        String init2DDec;
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

        //  visit the main method
        for (Object o : n) {
            Node currentClass = (Node) o;
            if (currentClass.getName().equals("ClassDeclaration")) {
                if (currentClass.getString(1).contains("Test")) { //Main
                    //save information for fields in main(use for method overloading)

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
                    //END: save information for fields in main(use for method overloading)

                    summary.currentClassName = currentClass.getString(1);
                    visitClassDeclaration((GNode) currentClass);

                }
                else{//it's not the main class





                    //check for method overloading
                    String className = currentClass.getString(1);
                    ArrayList<String> a = new ArrayList<String>();
                    a.add(className);



                    Node ClassBody = currentClass.getNode(5);
                    for(Object obj: ClassBody){
                        ArrayList<String> finalMethodNames = new ArrayList<String>();
                        finalMethodNames.add(className);
                        Node currClass = (Node) obj;
                        if (currClass.getName().equals("MethodDeclaration")) {
                            String methodName = currClass.getString(3);
                            //System.out.println(methodName);
                            a.add(methodName);

                            Node formalParamSNode = currClass.getNode(4);
                            if(formalParamSNode.size()>1){
                                methodName += "_";
                                for(int i=1; i<formalParamSNode.size();i++){
                                    methodName += formalParamSNode.getNode(i).getNode(1).getNode(0).getString(0);
                                    //System.out.println("new methodNAMe: " + methodName);
                                    //System.out.println(formalParamSNode.getNode(i).getNode(1).getNode(0).getString(0));

                                }
                            }
                            finalMethodNames.add(methodName);
                        }
                        //System.out.println("final names" + finalMethodNames);
                        summaryTraversal.overloadedMethodNames.add(finalMethodNames);
                    }
                    //System.out.println("final names" + summaryTraversal.overloadedMethodNames.toString());
                    summaryTraversal.allMethods_checkMethodOverloading.add(a);
                    //System.out.println( summaryTraversal.allMethods_checkMethodOverloading.toString() );

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
                                //System.out.println(checkIfThisMethodIsOverloaded + "is overloaded");
                                arrayListElement.add("overloaded");
                            }else{
                                arrayListElement.add("not overloaded");
                            }
                        }
                        summaryTraversal.isOverLoaded.add(arrayListElement);
                    }
                    //System.out.println( summaryTraversal.isOverLoaded.toString() );







                }
            }

        }

        s1.append(mainImplementation);

        s1.append("\n\n//------------------\n\n");

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
                //System.out.println("before mutation - origianl AST");
                //ImplementationUtil.prettyPrintAst(node);

                // get the mutated tree
                AstMutator visitor1 = new AstMutator(ImplementationUtil.newRuntime());
                visitor1.mutate(node);
                //System.out.println("after mutation ");
                //ImplementationUtil.prettyPrintAst(node);

                // get the summary of the cpp implementations
                PrintMainFile visitor = new PrintMainFile(ImplementationUtil.newRuntime(), summaryTraversal);
                PrintMainFile.printMainFileSummary summaryMain = visitor.getSummary(node);
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
