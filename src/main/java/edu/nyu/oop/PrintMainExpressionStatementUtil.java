package edu.nyu.oop;

import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.ArrayList;

/**
 * Created by willk on 12/13/16.
 */

//This class contains utility methods used by PrintMainFile.

public class PrintMainExpressionStatementUtil {

    public static String handleCout(GNode expressionStatementRoot, PrintMainFileSummary summary, AstTraversal.AstTraversalSummary summaryTraversal){
        String expressionStatement = "";
        expressionStatement += expressionStatementRoot.getNode(0).getNode(0).getNode(0).getString(0) + " << ";
        String primaryId = null;
        Node arguments = expressionStatementRoot.getNode(0).getNode(3);
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
        return expressionStatement;
    } //End handleCout();

    public static String handleCallExpressionPrimaryIdentifier(Node callExpressionNode, String methodName, PrintMainFileSummary summary, AstTraversal.AstTraversalSummary summaryTraversal){
        summary.chosenMethod = null; //reset the chosen overloaded method;
        String expressionStatement="";
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

        String expressionStatement1 = "";
        String primaryIdentifier = "";
        String returned = "";
        boolean isStatic = false;
        if (callExpressionNode.getNode(0).getName().equals("CallExpression")) {
            expressionStatement += handleCallExpressionPrimaryIdentifier(callExpressionNode.getNode(0), methodName, summary, summaryTraversal)+"->__vptr->";
            returned = summary.chosenMethod.returnType; //get the return type of the first call
            summary.overLoadedMethods = summaryTraversal.overLoadedMethods.get(returned);
        }
        else if (callExpressionNode.getNode(0).getName().equals("CastExpression")) {
            String castedType = callExpressionNode.getNode(0).getNode(0).getNode(0).getString(0);
            expressionStatement += "(("+castedType+") ";
            Node castedThing = callExpressionNode.getNode(0).getNode(1);
            if (castedThing.getName().equals("CallExpression")) {
                expressionStatement += handleCallExpressionPrimaryIdentifier(castedThing, methodName, summary, summaryTraversal);
                returned = castedType; //get the return type of the first call
                summary.overLoadedMethods = summaryTraversal.overLoadedMethods.get(returned);
            }
            expressionStatement += ")->__vptr->";
        }
        else {
            String variableCalling = callExpressionNode.getNode(0).getString(0);
            primaryIdentifier = variableCalling;

            if (summaryTraversal.classes.containsKey(variableCalling)) { // then this is static
                expressionStatement += "__" + variableCalling + "::";
                summary.overLoadedMethods = summaryTraversal.overLoadedMethods.get(variableCalling);
                isStatic = true;
            } else {
                expressionStatement += variableCalling + "->";

                if (callExpressionNode.getNode(1) == null)
                    expressionStatement += "__vptr->"; //for some reason this was not mutated
                else
                    expressionStatement += callExpressionNode.getNode(1).getString(0) + "->";

                String primaryClass = summary.localVariables.get(variableCalling).replace("__", "");
                summary.overLoadedMethods = summaryTraversal.overLoadedMethods.get(primaryClass);
            }
        }
        if (summary.overLoadedMethods != null && summary.overLoadedMethods.containsKey(methodName)) {
            if (callExpressionNode.getNode(3).getName().equals("Arguments")) {
                String arguments = "(";
                Node argumentsNode = callExpressionNode.getNode(3);

                ArrayList<MethodImplementation> methods = new ArrayList<>(summary.overLoadedMethods.get(methodName));
                ArrayList<Integer> distances = new ArrayList<>();

                for (int i = 0; i < methods.size(); i++) {
                    if (methods.get(i).parameters.size() != ((isStatic || argumentsNode.size() == 0 || !(argumentsNode.get(0) instanceof String)) ? argumentsNode.size() : argumentsNode.size() - 1)) //number of arguments in Node includes __this for nonstatic
                        //number of arguments is wrong
                        methods.remove(i--);
                    else
                        distances.add(0);
                }

                if (methods.size() == 1) {

                    int offset = 0;

                    if (argumentsNode.size() == 0 && !isStatic) //it is missing the primary identifier;
                        arguments += "new __"+returned+"()";
                    else if (!(argumentsNode.get(0) instanceof String))
                        arguments += "new __"+returned+"(), ";
                    else
                        offset = 1;

                    for (int i = 0; i < argumentsNode.size(); i++) {
                        if (i > 0)
                            arguments += ", ";

                        Object o = argumentsNode.get(i);
                        if (o instanceof Node) {
                            Node argument = (Node) o;
                            if (argument.getName().equals("NewClassExpression")) {
                                arguments += "new "+ argument.getNode(2).getString(0) + "(";
                                if (argument.getNode(3).size() > 0)
                                    arguments += argument.getNode(3).getNode(0).getString(0);
                                arguments += ")";
                            } else if (argument.getName().equals("PrimaryIdentifier")) {
                                String primaryIdentifier1 = argument.getString(0);
                                if (!primaryIdentifier.isEmpty() && summary.classVariables.get(primaryIdentifier).equals(summary.classVariables.get(primaryIdentifier1))) {
                                    arguments += primaryIdentifier1;
                                }
                                else {
                                    arguments += "("+methods.get(0).parameters.get(i - offset).type+") "+primaryIdentifier1;
                                }
                            } else if (argument.getName().equals("CallExpression")) {
                                arguments += handleCallExpressionPrimaryIdentifier(argument, methodName, summary, summaryTraversal);
                            } else if (argument.getName().equals("CastExpression")) {
                               arguments += "("+argument.getNode(0).getNode(0).getString(0)+") " + argument.getNode(1).getString(0);
                            } else {
                                System.out.println("handle this");
                            }
                        }
                        else if (o instanceof String)
                            arguments += o;
                    }
                    arguments += ")";

                    if (methods.get(0).isOverloaded) {
                        expressionStatement += methods.get(0).overLoadedName + arguments;
                        summary.chosenMethod = methods.get(0);
                    }
                    else
                        expressionStatement += methods.get(0).name + arguments;
                }
                else { //methods.size() is greater than 1
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
                                    else if (((Node) o).getName().equals("AdditiveExpression")) {
                                        argumentType = "int";
                                    }
                                    else if (((Node) o).getName().equals("CallExpression")) {
                                        handleCallExpressionPrimaryIdentifier((Node) o, methodName, summary, summaryTraversal);
                                        argumentType = summary.chosenMethod.returnType;
                                    }
                                    else {
                                        //TODO: other things
                                        System.out.println("Missed the argumentType");
                                    }

                                    if (argumentType.equals("uint8_t")) argumentType = "byte";

                                    //chain of primitives: byte->short->char?->int->long->float->double
                                    if (!p.type.equals(argumentType)) {
                                        int increaseBy = 1;
                                        switch (argumentType) {
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

                        if (!isStatic) {
                            if (argumentsNode.size() > 0 && argumentsNode.get(0) instanceof String)
                                arguments += argumentsNode.getString(0);
                            else if (!(argumentsNode.get(0) instanceof String)) {
                                if (primaryIdentifier.isEmpty())
                                    arguments += "new __" + returned + "(), "; //this is kind of bad but how else can you do it
                                else
                                    arguments += primaryIdentifier +", ";
                            }
                            else
                                arguments += "new __"+returned+"()";
                        }

                        MethodImplementation chosenMethod = methods.get(min);

                        int offset  = (isStatic || !(argumentsNode.get(0) instanceof String)) ? 0 : 1;

                        for (int i = 0 + offset; i < argumentsNode.size(); i++) {
                            if (i > 0)
                                arguments += ", ";

                            Object o = argumentsNode.getNode(i);
                            if (o instanceof Node) {
                                if (argumentsNode.getNode(i).getName().equals("NewClassExpression")) {
                                    Node newClassExpressionNode = argumentsNode.getNode(i);
                                    String paramType = newClassExpressionNode.getNode(2).getString(0);
                                    if (paramType.equals("byte")) paramType.equals("uint8_t");
                                    if (!chosenMethod.parameters.get(i - offset).type.equals(paramType)) //if it does not match argument type add cast
                                        arguments += "("+chosenMethod.parameters.get(i - offset).type+") ";
                                    arguments += "new __"+ paramType + "(";
                                    if (argumentsNode.getNode(i).getNode(3).size() > 0)
                                        arguments += argumentsNode.getNode(i).getNode(3).getNode(0).getString(0);
                                    arguments += ")";
                                }
                                else if (argumentsNode.getNode(i).getName().equals("PrimaryIdentifier")){
                                    String primaryIdentifier1 = argumentsNode.getNode(i).getString(0);
                                    String callWithType = summary.classVariables.get(primaryIdentifier1);
                                    if (callWithType.equals("uint8_t")) callWithType = "byte";
                                    if (!chosenMethod.parameters.get(i - offset).type.equals(callWithType)) {
                                        String type = chosenMethod.parameters.get(i - offset).type;
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
                                    arguments += "("+cast+") ";
                                    if (argumentsNode.getNode(i).getNode(1).getName().equals("PrimaryIdentifier"))
                                        arguments += argumentsNode.getNode(i).getNode(1).getString(0);
                                    else if (argumentsNode.getNode(i).getNode(1).getName().equals("NewClassExpression")) {
                                        arguments += "new " + argumentsNode.getNode(i).getNode(1).getNode(2).getString(0)+"(";
                                        Node innerArgs = argumentsNode.getNode(i).getNode(1).getNode(3);
                                        for (int j = 0; j < innerArgs.size(); j++) {
                                            System.out.println("need to handle this");
                                        }
                                        arguments += ")";
                                    }
                                }
                                else if (argumentsNode.getNode(i).getName().equals("AdditiveExpression")) {
                                    for (Object o1 : argumentsNode.getNode(i)) {
                                        if (o1 instanceof Node) {
                                            if (((Node) o1).getName().equals("PrimaryIdentifier")) {
                                                arguments += ((Node) o1).getString(0);
                                            }
                                        }
                                        else if (o1 instanceof String) {
                                            arguments += " "+o1+" ";
                                        }
                                    }
                                }
                                else if (argumentsNode.getNode(i).getName().equals("CallExpression")) {
                                    String nestedCall = handleCallExpressionPrimaryIdentifier(argumentsNode.getNode(i), methodName, summary, summaryTraversal);
                                    if (!chosenMethod.parameters.get(i - offset).type.equals(summary.chosenMethod.returnType)) {
                                        String type = chosenMethod.parameters.get(i - offset).type;
                                        arguments += "("+(type.equals("int")? "int32_t" : type)+ ") ";
                                    }
                                    arguments += nestedCall;
                                }
                                else {
                                    //TODO: here
                                    System.out.println("Missed the calling argumentType");
                                }
                            }
                        }
                        arguments += ")";

                        expressionStatement += chosenMethod.overLoadedName + arguments;
                        summary.chosenMethod = chosenMethod;
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
                    if (summary.classVariables.get(primaryIdentifier).equals(summary.classVariables.get(primaryIdentifier1))) {
                        expressionStatement += argumentsNode.getNode(1).getString(0);
                    } else {
                        expressionStatement += "(" + summary.classVariables.get(primaryIdentifier) + ") " + primaryIdentifier1;
                        expressionStatement1 += "\tClass k" + summary.checkClassCounter + " = " + primaryIdentifier + "->__vptr->getClass(" + primaryIdentifier + ");\n";
                        expressionStatement1 += "\tcheckClass(k" + summary.checkClassCounter + ", " + primaryIdentifier1 + ");\n\n";
                        summary.checkClassCounter++;
                    }
                } else {
                    expressionStatement += argumentsNode.getString(0);
                }
                expressionStatement += ")";
                String temp = expressionStatement;
                expressionStatement = "";
                expressionStatement += expressionStatement1;
                expressionStatement += temp;
            }
        }
        return expressionStatement;
    } //End handleCallExpressionPrimaryIdentifier()

    public static String handleExpressionNode(Node expressionNode, PrintMainFileSummary summary, AstTraversal.AstTraversalSummary summaryTraversal){
        String expressionStatement="";
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
        return expressionStatement;
    }

}
