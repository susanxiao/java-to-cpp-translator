package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.tree.Node;

import java.util.HashMap;

/**
 * Created by willk on 12/13/16.
 */

//This class handles some responsibility in visitFieldDeclaration in PrintMainFile.
public class PrintMainFieldDeclarationUtil {

    public static String handleDeclarators(Node n, Node declaratorsNode, String type, boolean isTypeArray, boolean is2D, PrintMainFileSummary summary, AstTraversal.AstTraversalSummary summaryTraversal){
        String fieldDeclaration="";
        for (Object o : declaratorsNode) {
            if (o instanceof Node) {
                Node currentDeclarator = (Node) o;
                String variable = currentDeclarator.getString(0);
                summary.classVariables.put(variable, type);

                if (summary.localVariables == null)
                    summary.localVariables = new HashMap<>();

                summary.localVariables.put(variable, type);

                if (currentDeclarator.getNode(2) != null) {
                    fieldDeclaration += variable;
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
                       fieldDeclaration += handleNewArrayExpression(n, currentDeclarator, type, variable, is2D, summary);
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
                    fieldDeclaration += variable+";\n";
                }
            }
        }
        return fieldDeclaration;
    }

    /*Handles NewArrayExpressions within Declarator nodes.
    All parameters are passed in through handleDeclarator(), with variables of the same name.
     */
    public static String handleNewArrayExpression(Node n, Node currentDeclarator, String type, String variable, boolean is2D, PrintMainFileSummary summary){
        summary.needsSizeCheck = true;
        String fieldDeclaration="";
        fieldDeclaration += " = ";

        Node newArray = currentDeclarator.getNode(2);
        String qualifiedIdentifier = newArray.getNode(0).getString(0);
        /*//check if size is negative or positive
        boolean sizeIsNegative = false;
        String negSize="";
        Node sizeMightBeNegative = NodeUtil.dfs(newArray,"UnaryExpression");
        if(sizeMightBeNegative!=null){
            if(sizeMightBeNegative.getString(0).equals("-")){
                sizeIsNegative = true;
                negSize = "-"+sizeMightBeNegative.getNode(1).getString(0);
            }
        }*/

        String size = "";
        if (newArray.getNode(1).getNode(0).getName().equals("UnaryExpression"))
            size = newArray.getNode(1).getNode(0).getString(0)+newArray.getNode(1).getNode(0).getNode(1).getString(0);
        else
            size = newArray.getNode(1).getNode(0).getString(0);
        summary.size = new Integer(size);
        /*if(sizeIsNegative){//size is negative
            size = negSize;
        }*/
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

            /*
            //check NegativeArraySizeException()
            if (Integer.parseInt(size) < 0) {
                //need to throw NegativeArraySizeException() BEFORE assigning the array
                String fieldDeclaration_deepCopy = "";
                for (int i = 0; i < fieldDeclaration.length(); i++) {
                    char c = fieldDeclaration.charAt(i);
                    fieldDeclaration_deepCopy += c;
                }
                fieldDeclaration = "";
                fieldDeclaration += "\tthrow java::lang::NegativeArraySizeException();  //size of array is negative\n\t\t";
                fieldDeclaration += fieldDeclaration_deepCopy;
            }*/
            summary.localVariables.put(variable, type + "[");
        }
        return fieldDeclaration;
    }
}
