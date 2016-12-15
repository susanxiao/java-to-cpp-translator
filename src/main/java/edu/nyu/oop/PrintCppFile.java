package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static java.lang.System.out;

/**
 * Created by Garrett on 10/21/16.
 */
public class PrintCppFile extends Visitor {

    private cppFileSummary summary = new cppFileSummary();
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Runtime runtime;
    private AstTraversal.AstTraversalSummary summaryTraversal;

    // visitXXX methods

    public void visitPackageDeclaration(GNode n) {
        Node qualifiedIdentifier = n.getNode(1);
        if (qualifiedIdentifier != null) {
            for (int i = 0; i < qualifiedIdentifier.size(); i++) {
                summary.addNamespace(qualifiedIdentifier.getString(i));
                if (i > 0)
                    summary.classLocation += ".";
                summary.classLocation += qualifiedIdentifier.getString(i);
            }
            summary.classLocation += ".";
        }
    }

    public void visitClassDeclaration(GNode n) {
        if (n.getString(1).contains("Test"))
            summary.isMainClass = true;
        else
            summary.isMainClass = false;

        if (!summary.isMainClass) {
            summary.numberClasses += 1;
            summary.currentClassMethodCount = 0;

            String className = n.getString(1);
            summary.currentClass = summaryTraversal.classes.get(className);
            summary.currentClassLocation = summary.classLocation + summary.currentClass.name;


            //information for initializer list
            summary.initializerList = new HashMap<>();

            visitClassBody((GNode) n.getNode(5));

            //__class() method
            summary.addLine("Class __" + summary.currentClass.name + "::__class()");
            summary.incScope();
            summary.addLine("static Class k =\n");
            String superClasses = "";
            ClassImplementation superClass = summary.currentClass.superClass;
            while (superClass != null) {
                superClasses += ",";
                superClasses += "__" + superClass.name + "::__class()";

                superClass = superClass.superClass;
            }
            if (superClasses != "") {
                superClasses += ");\n";
                summary.addLine("new __Class(__rt::literal(\"" + summary.currentClassLocation + "\")" + superClasses);
            } else {
                summary.addLine("new __Class(__rt::literal(\"" + summary.currentClassLocation + "\"), (Class) __rt::null());\n");
            }
            summary.addLine("return k;\n");
            summary.decMethodScope();
            summary.code.append("\n");

            //vtable
            summary.addLine("__" + summary.currentClass.name + "_VT __" + summary.currentClass.name + "::__vtable;\n\n");

            summary.classMethodCounts.put(summary.currentClass.name, summary.currentClassMethodCount);
        }
        else {
            visitClassBody((GNode)n.getNode(5));
        }

    }

    public void visitClassBody(GNode n) {
        if (!summary.isMainClass) {
            //check If methods are overloaded
            ArrayList<String> checkMethodOverloading = new ArrayList<String>();
            for (Object methods : n) {
                GNode currentMethod = (GNode) methods;
                if (currentMethod.getName().equals("MethodDeclaration")) {
                    String methodName = currentMethod.getString(3);
                    checkMethodOverloading.add(methodName);
                    //summaryTraversal.isTheMethodOverloaded.put(methodName, "");
                }
            }
            int numOfMethods = checkMethodOverloading.size();
            boolean[] isAOverloadedMethod = new boolean[numOfMethods];//default is false
            if(numOfMethods>1){//method overloading possible
                for(int s=0; s< numOfMethods; s++){
                    String methodName= checkMethodOverloading.get(s);
                    int numOfSameMethodNames=0;
                    for(String findStringsWithSameName : checkMethodOverloading){
                        if(methodName.equals(findStringsWithSameName)){
                            numOfSameMethodNames++;
                        }
                    }
                    if(numOfSameMethodNames>1){
                        isAOverloadedMethod[s]=true;
                        //summaryTraversal.isTheMethodOverloaded.
                    }
                }
            }
            for(boolean overload : isAOverloadedMethod) {
            }

            boolean constructorCreated = false;
            int nthMethod=0;//use for checking overloading
            for (Object methods : n) {
                GNode currentMethod = (GNode) methods;
                if (currentMethod.getName().equals("FieldDeclaration")) {
                    visitFieldDeclaration(currentMethod);
                } else if (currentMethod.getName().equals("ConstructorDeclaration")) {
                    visitConstructorDeclaration(currentMethod);
                    constructorCreated = true;
                    summary.code.append("\n");
                } else if (currentMethod.getName().equals("MethodDeclaration")) {
                    visitMethodDeclaration(currentMethod, n);
                    nthMethod++;
                    summary.code.append("\n");

                }
            }

            if (!constructorCreated) {
                summary.addLine("__" + summary.currentClass.name + "::__" + summary.currentClass.name + "() : __vptr(&__vtable)");
                for (FieldDeclaration var : summaryTraversal.classes.get(summary.currentClass.name).declarations) {
                    if (!(var.assignment != null || var.literalValue != null
                            || var.dynamicType != null || var.primaryIdentifier != null)) {
                        if (summaryTraversal.classes.containsKey(var.staticType)) {
                            summary.code.append(",\n");
                            summary.addLine("\t" + var.variableName + "((" + summary.currentClass.name + ")__rt::null())");
                        } else {
                            summary.code.append(",\n");
                            summary.addLine("\t" + var.variableName + "((" + var.staticType + ")__rt::null())");
                        }
                    }
                }
                summary.code.append("\n");
                summary.addLine("{};\n\n");
            }
        }
        else {
            for (Object o : n) {
                if (o instanceof Node && ((Node) o).getName().equals("MethodDeclaration"))
                    visitMethodDeclaration(GNode.cast(o), n);
            }
        }
    }

    public void visitFieldDeclaration(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                Node fieldNode = (Node) o;
                if (fieldNode.getName().equals("Declarators")) {
                    Node declarator = fieldNode.getNode(0);
                    String declaredValue = null;
                    Object o1 = declarator.get(2);
                    if (o1 != null && o1 instanceof Node) {
                        Node assignment = (Node) o1;
                        if (assignment.getName().equals("NewClassExpression")) {
                            Node qualifiedIdentifier = assignment.getNode(2);
                            String className = qualifiedIdentifier.getString(0);
                            Node argumentsNode = assignment.getNode(3);

                            StringBuilder arguments = new StringBuilder();
                            for (int i = 0; i < argumentsNode.size(); i++) {
                                Object o2 = argumentsNode.get(i);
                                if (o2 instanceof Node) {
                                    if (i > 0)
                                        arguments.append(", ");

                                    Node argument = (Node) o2;
                                    if (argument.getName().equals("StringLiteral")) {
                                        arguments.append(argument.getString(0));
                                    }
                                    //TODO: other cases here
                                }
                            }
                            declaredValue = "new " + className + "(" + arguments.toString() + ")";
                        }
                        else if (assignment.getName().equals("NewArrayExpression")) {
                            String qualifiedIdentifier = assignment.getNode(0).getString(0);
                            if (!qualifiedIdentifier.equals("String") && !qualifiedIdentifier.equals("Object")) {
                                /*orginal*/
                                //summary.addRunTimeLine("\tnew java::lang::__Class(literal(\"[java.lang.*EDITHERE*"+qualifiedIdentifier+";\"),\n");

                                /*edit*/
                                String packageName="";//ex)inputs.test028
                                ArrayList<String> currentPackages = summaryTraversal.currentPackages;
                                int currentPackages_size = currentPackages.size();
                                for(int i=0; i<currentPackages_size ; i++){
                                //for(String p: currentPackages){
                                    String p = currentPackages.get(i);
                                    packageName+=p;
                                    if(i+1<currentPackages_size ){
                                        packageName+=".";
                                    }
                                }

                                //Find out Dimension of Array
                                int arrayDimension=0;
                                if(n.getNode(1).getNode(1).getName().equals("Dimensions")){
                                    arrayDimension=n.getNode(1).getNode(1).size();
                                }
                                String addDimensions="";
                                for(int i=0; i<arrayDimension;i++){
                                    addDimensions+="[";
                                }
                                //if it is a 1D array of int, do not create another template
                                if (!(arrayDimension == 1 && qualifiedIdentifier.equals("int"))) {
                                    if (!summary.hasRunTime)
                                        summary.startRunTime();
                                    summary.addRunTimeLine("template<>\n");

                                    if (arrayDimension > 1) { //only handles 2dimensions
                                        if (qualifiedIdentifier.equals("int"))
                                            summary.addRunTimeLine("java::lang::Class Array< __rt::Array<" + qualifiedIdentifier + ">* >::__class()");
                                        else {
                                            //add the first dimensional array
                                            summary.addRunTimeLine("java::lang::Class Array<" + summary.classLocation.replace(".", "::") + "__"+qualifiedIdentifier + ">::__class()");
                                            summary.incRunTimeScope();
                                            summary.addRunTimeLine("static java::lang::Class k =\n");
                                            summary.addRunTimeLine("\tnew java::lang::__Class(literal(\"" + addDimensions.substring(1) + "L" + packageName + "." + qualifiedIdentifier + ";\"),\n");
                                            summary.addRunTimeLine("\t\t\tjava::lang::__Object::__class(),\n");
                                            summary.addRunTimeLine("\t\t\t" + summary.classLocation.replace(".", "::") + "__" + qualifiedIdentifier + "::__class());\n");
                                            summary.addRunTimeLine("return k;\n");
                                            summary.decRunTimeScope();

                                            summary.addRunTimeLine("template<>\n");
                                            summary.addRunTimeLine("java::lang::Class Array< __rt::Array<" + summary.classLocation.replace(".", "::") + "__"+ qualifiedIdentifier + ">* >::__class()");
                                        }
                                    }
                                    else
                                        summary.addRunTimeLine("java::lang::Class Array<" + summary.classLocation.replace(".", "::") + qualifiedIdentifier + ">::__class()");
                                    summary.incRunTimeScope();
                                    summary.addRunTimeLine("static java::lang::Class k =\n");

                                    summary.addRunTimeLine("\tnew java::lang::__Class(literal(\"" + addDimensions + "L" + packageName + "." + qualifiedIdentifier + ";\"),\n");
                                /*End editing*/

                                    summary.addRunTimeLine("\t\t\tjava::lang::__Object::__class(),\n");

                                    if (arrayDimension > 1) { //only handles 2 dimensions
                                        if (qualifiedIdentifier.equals("int"))
                                            summary.addRunTimeLine("\t\t\t__rt::Array<" + qualifiedIdentifier + ">::__class());\n");
                                        else
                                            summary.addRunTimeLine("\t\t\t__rt::Array<"+summary.classLocation.replace(".", "::") +"__" + qualifiedIdentifier + ">::__class());\n");
                                    }
                                    else
                                        summary.addRunTimeLine("\t\t\t" + summary.classLocation.replace(".", "::") + "__" + qualifiedIdentifier + "::__class());\n");

                                    summary.addRunTimeLine("return k;\n");
                                    summary.decRunTimeScope();
                                }

                            }
                        }
                        //TODO: other cases here too
                    }

                    if (!summary.isMainClass)
                        summary.initializerList.put(declarator.getString(0), declaredValue);
                }
            }
        }
    }

    public void visitConstructorDeclaration(GNode n) {
        String constructorName = n.getString(2);
        Node formalParameters = n.getNode(3);
        Node constructorBlock = n.getNode(5);

        StringBuilder constructor = new StringBuilder("__" + constructorName + "::__" + constructorName + "(");
        for (int i = 0; i < formalParameters.size(); i++) {
            Object o = formalParameters.get(i);
            if (o instanceof Node) {
                Node formalParameter = (Node) o;
                if (formalParameter.getName().equals("FormalParameter")) {
                    for (Object o1 : formalParameter) {
                        if (o1 instanceof Node) {
                            Node type = (Node) o1;
                            if (type.getName().equals("Type")) {
                                String typeString = type.getNode(0).getString(0);
                                if (i > 0)
                                    constructor.append(", ");
                                constructor.append(typeString.equals("int") ? "int32_t " : typeString + " ");
                            }
                        } else if (o1 instanceof String) {
                            String variableName = (String) o1;
                            constructor.append(variableName);
                        }
                    }
                }
            }
        }
        constructor.append(") : %s"); //this is the placeholder for the initializer list

        //test025: class B is a subclass and directly calls super()
        //Need to initialize parent before Block
        //When you create a object in c++, by default it runs the default constructor on all of it's objects
        //explicitly initialize the member 'parent' which does not have a default constructor
        if(constructorBlock.size()>0) {
            if (constructorBlock.getNode(0) != null) { // ExpressionStatementNode
                if (constructorBlock.getNode(0).getName().equals("ExpressionStatement") && constructorBlock.getNode(0).getNode(0) != null) {
                    if (constructorBlock.getNode(0).getNode(0).getName().equals("CallExpression")) {
                        if (constructorBlock.getNode(0).getNode(0).getNode(0) == null) {
                            if (constructorBlock.getNode(0).getNode(0).getNode(1) == null) {
                                if (constructorBlock.getNode(0).getNode(0).getString(2).equals("super")) {
                                    if (constructorBlock.getNode(0).getNode(0).getNode(3).getName().equals("Arguments")) {
                                        //there may be more than one argument
                                        String constructorArguments = "";
                                        int num_constructorArguments = constructorBlock.getNode(0).getNode(0).getNode(3).getNode(0).size();
                                        for (int i = 0; i < num_constructorArguments; i++) {
                                            String arg = constructorBlock.getNode(0).getNode(0).getNode(3).getNode(0).getString(i);
                                            constructorArguments += arg;
                                            if (i + 1 < num_constructorArguments) {
                                                constructorArguments += ", ";
                                            }
                                        }
                                        constructor.append(", parent(" + constructorArguments + ")");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        summary.addLine(constructor.toString());

        summary.incScope();
        for (Object o : constructorBlock) {
            if (o instanceof Node) {
                Node currentNode = (Node) o;
                if (currentNode.getName().equals("ExpressionStatement")) {
                    Node expressionStatementChild = currentNode.getNode(0);
                    if (expressionStatementChild.getName().equals("Expression")) {
                        Node expressionChild = expressionStatementChild.getNode(0);
                        if (expressionChild.getName().equals("SelectionExpression")) {
                            Node selectionChild = expressionChild.getNode(0);
                            if (selectionChild.getName().equals("ThisExpression")) {
                                String variableName = expressionChild.getString(1);
                                String operator = expressionStatementChild.getString(1);
                                Node expressionPrimaryIdentifier = expressionStatementChild.getNode(2);
                                String variableValue = expressionPrimaryIdentifier.getString(0);

                                if (summary.initializerList.containsKey(variableName) && summary.initializerList.get(variableName) == null)
                                    summary.initializerList.put(variableName, variableValue);
                                else
                                    summary.addLine("this->" + variableName + " " + operator + " " + variableValue + ";\n");
                            }
                        } else if (expressionChild.getName().equals("PrimaryIdentifier")) {
                            String variableName = expressionChild.getString(0);
                            String operator = expressionStatementChild.getString(1);
                            Node primarySibling = expressionStatementChild.getNode(2);
                            if (primarySibling.getName().equals("PrimaryIdentifier")) {
                                String variableValue = primarySibling.getString(0);
                                
                                if (summary.initializerList.containsKey(variableName) && summary.initializerList.get(variableName) == null)
                                    summary.initializerList.put(variableName, variableValue);
                                else
                                    summary.addLine(variableName + " " + operator + " " + variableValue + ";\n");
                            } else if (primarySibling.getName().equals("ThisExpression")) {
                                if (summary.initializerList.containsKey(variableName) && summary.initializerList.get(variableName) == null) {
                                    /* if (formalParameters.size() > 0) {
                                        summary.initializerList.put(variableName, "this");
                                    } else { */
                                    summary.addLine(variableName + " " + operator + " this;\n");
                                    // }
                                } else
                                    summary.addLine(variableName + " " + operator + " this;\n");
                            } else if (primarySibling.getName().equals("NewClassExpression")) {
                                String className = primarySibling.getNode(2).getString(0);
                                Node argumentsNode = primarySibling.getNode(3);


                                StringBuilder arguments = new StringBuilder("(");
                                for (int i = 0; i < argumentsNode.size(); i++) {
                                    Object o1 = argumentsNode.get(i);

                                    if (o1 instanceof Node) {
                                        if (i > 0)
                                            arguments.append(", ");

                                        Node argument = (Node) o1;
                                        if (argument.getName().equals("StringLiteral")) {
                                            String value = argument.getString(0);
                                            arguments.append(value);
                                        }
                                    }
                                }
                                arguments.append(")");

                                boolean gateParent = true;
                                String className1 = summary.currentClass.name;
                                String blockPrimaryIdentifier = variableName;
                                for (FieldDeclaration dec : summaryTraversal.classes.get(className1).declarations) {
                                    if (dec.variableName.equals(blockPrimaryIdentifier))
                                        gateParent = false;
                                }
                                String statement = gateParent ? "parent." : "";

                                if (summary.initializerList.containsKey(variableName) && summary.initializerList.get(variableName) == null) {
                                    if (formalParameters.size() > 0) {
                                        //summary.initializerList.put(variableName, "new " + className + arguments.toString());
                                    } else {
                                        summary.addLine(statement + variableName + " " + operator + " new " + className + arguments.toString() + ";\n");
                                    }
                                } else
                                    summary.addLine(statement + variableName + " " + operator + " new " + className + arguments.toString() + ";\n");
                            }
                        }
                    } else if (expressionStatementChild.getName().equals("CallExpression")) {
                        if(expressionStatementChild.getNode(0)!=null){
                            String primaryIdentifier = expressionStatementChild.getNode(0).getNode(0).getString(0);
                            if (primaryIdentifier.equals("cout")) {
                                StringBuilder line = new StringBuilder("cout << ");
                                Node arguments = expressionStatementChild.getNode(3);
                                Node argumentsPrimaryIdentifier = arguments.getNode(0);
                                boolean gateParent = true;
                                String className = summary.currentClass.name;
                                String blockPrimaryIdentifier = argumentsPrimaryIdentifier.getString(0);
                                for (FieldDeclaration dec : summaryTraversal.classes.get(className).declarations) {
                                    if (dec.variableName.equals(blockPrimaryIdentifier))
                                        gateParent = false;
                                }
                                String statement = gateParent ? "parent." + blockPrimaryIdentifier : blockPrimaryIdentifier;
                                //String statement = gateParent ? "parent." + blockPrimaryIdentifier + "->data" : blockPrimaryIdentifier + "->data";

                                line.append(statement);
                                for (int i = 1; i < arguments.size(); i++) {
                                    String field = arguments.getString(i);
                                    line.append("->" + field);
                                }
                                if (expressionStatementChild.getString(2) != null) {
                                    line.append(" << " + expressionStatementChild.getString(2));
                                }
                                summary.addLine(line.toString() + ";\n");
                            }
                        }


                    }
                }
            }
        }
        summary.decMethodScope();

        Set<Map.Entry<String, String>> initializerSet = summary.initializerList.entrySet();
        StringBuilder initializers = new StringBuilder("__vptr(&__vtable) ");
        Set<String> variables = new TreeSet<>();

        for (Map.Entry<String, String> entry : initializerSet) {
            if (entry.getValue() != null) {
                initializers.append(",\n");
                for (int i = 0; i < summary.scope + 2; i++)
                    initializers.append("\t");
                initializers.append(entry.getKey() + "(" + entry.getValue() + ")");
                variables.add(entry.getKey());
            }
        }





        String className = summary.currentClass.name;
        for (FieldDeclaration declaration : summaryTraversal.classes.get(summary.currentClass.name).declarations) {
            if (!variables.contains(declaration.variableName)) {
                if (!(declaration.assignment != null || declaration.literalValue != null
                        || declaration.dynamicType != null || declaration.primaryIdentifier != null)) {
                    if (summaryTraversal.classes.containsKey(declaration.staticType)) {
                        initializers.append(",\n");
                        for (int i = 0; i < summary.scope + 1; i++)
                            initializers.append("\t");
                        initializers.append(declaration.variableName + "((" + className + ")__rt::null())");
                    } else {
                        initializers.append(",\n");
                        for (int i = 0; i < summary.scope + 1; i++)
                            initializers.append("\t");
                        initializers.append(declaration.variableName + "((" + declaration.staticType + ")__rt::null())");
                    }
                }
            }
        }
        summary.code = new StringBuilder(String.format(summary.code.toString(), initializers.toString()));
    }

    public void visitMethodDeclaration(GNode n, GNode classBodyNode) {
        if (!summary.isMainClass) {
            summary.currentClassMethodCount++;

            StringBuilder methodSignature = new StringBuilder();

            boolean isStatic = false;
            Node modifiers = n.getNode(0);
            for (Object o : modifiers) {
                if (o instanceof Node) {
                    Node modifier = (Node) o;
                    if (modifier.getString(0).equals("static"))
                        isStatic = true;
                }
            }

            Node methodType = n.getNode(2);
            String returnType = (methodType.getName().equals("VoidType") ? "void" : methodType.getNode(0).getString(0));
            if (returnType.equals("int"))
                returnType = "int32_t";

            String methodName = n.getString(3);

            boolean isOverloaded = false;
            HashMap<String, ArrayList<MethodImplementation>> map = summaryTraversal.overLoadedMethods.get(summary.currentClass.name);
            if (map != null && map.containsKey(methodName)) {
                isOverloaded = true;
                methodSignature.append(returnType + " __" + summary.currentClass.name + "::%s("); //methodName goes here;
            }
            else {
                methodSignature.append(returnType + " __" + summary.currentClass.name + "::" + methodName + "(");
            }

            HashMap<String, String> paramNames = new HashMap<>();

            Node formalParameters = n.getNode(4);
            for (int i = 0; i < formalParameters.size(); i++) {
                Object o = formalParameters.get(i);
                if (o instanceof Node) {
                    Node formalParameter = (Node) o;
                    if (formalParameter.getName().equals("FormalParameter")) {
                        Node paramType = formalParameter.getNode(1);
                        String className = paramType.getNode(0).getString(0);
                        String paramName = formalParameter.getString(3);
                        if (isStatic) {
                            if (!paramName.equals("__this"))
                                methodSignature.append((className.equals("int") ? "int32_t" : (className.equals("byte") ? "uint8_t" : className)) + " " + paramName);
                            paramNames.put(paramName, (className.equals("int") ? "int32_t" : (className.equals("byte") ? "uint8_t" : className)));
                        } else {
                            methodSignature.append((className.equals("int") ? "int32_t" : (className.equals("byte") ? "uint8_t" : className)) + " " + paramName);
                            paramNames.put(paramName, (className.equals("int") ? "int32_t" : (className.equals("byte") ? "uint8_t" : className)));
                        }
                        if (!(isStatic && paramName.equals("__this")) && i < formalParameters.size() - 1)
                            methodSignature.append(", ");

                        if (isOverloaded && !paramName.equals("__this"))
                            methodName += className.substring(0, 1).toUpperCase() + className.substring(1);
                    }
                }
            }
            methodSignature.append(")");
            summary.addLine(isOverloaded ? String.format(methodSignature.toString(), methodName) : methodSignature.toString());

            summary.incScope();
            Node methodBlock = n.getNode(7);

            for (String name : paramNames.keySet()) {
                String paramType = paramNames.get(name);
                if (!name.equals("__this") &&
                        !(paramType.equals("int32_t")
                        || paramType.equals("double")
                        || paramType.equals("uint8_t"))) //TODO: other primitives
                    summary.addLine("__rt::checkNotNull(" + name + ");\n");

            }

            HashSet<String> localVariables = new HashSet<>();
            for (Object o : methodBlock) {
                GNode currentNode = (GNode) o;
                if (currentNode.getName().equals("FieldDeclaration")) {
                    Node type = currentNode.getNode(1);
                    String staticType = type.getNode(0).getString(0);
                    Node declarator = currentNode.getNode(2).getNode(0);
                    String variableName = declarator.getString(0);
                    Node declaratorChild = declarator.getNode(2);

                    localVariables.add(variableName);

                    if (declaratorChild == null) {
                        summary.addLine(staticType + " " + variableName + ";\n");
                    } else {
                        visitFieldDeclaration(currentNode);
                    }
                } else if (currentNode.getName().equals("ExpressionStatement")) {
                    Node expressionStatementChild = currentNode.getNode(0);
                    if (expressionStatementChild.getNode(0).getName().equals("PrimaryIdentifier")) {
                        String variableName = expressionStatementChild.getNode(0).getString(0);
                        String operation = expressionStatementChild.getString(1);
                        if (expressionStatementChild.getNode(2).getName().equals("PrimaryIdentifier")) {
                            String assignment = expressionStatementChild.getNode(2).getString(0);
                            if (!localVariables.contains(variableName) && summary.initializerList.containsKey(variableName))
                                summary.addLine("__this->" + variableName + " " + operation + " " + assignment + ";\n");
                            else
                                summary.addLine(variableName + " " + operation + " " + assignment + ";\n");
                        }
                        //TODO: else
                    } else if (expressionStatementChild.getName().equals("CallExpression")) {
                        String primaryIdentifier = expressionStatementChild.getNode(0).getNode(0).getString(0);
                        if (primaryIdentifier.equals("cout")) {
                            StringBuilder line = new StringBuilder("cout << ");
                            Node arguments = expressionStatementChild.getNode(3);
                            if (arguments.getNode(0).getName().equals("StringLiteral")) {
                                line.append(arguments.getNode(0).getString(0));
                            } else if (arguments.getNode(0).getName().equals("PrimaryIdentifier")) {
                                Node argumentsPrimaryIdentifier = arguments.getNode(0);
                                line.append(argumentsPrimaryIdentifier.getString(0) + "->__vptr");
                                for (int i = 1; i < arguments.size(); i++) {
                                    String field = arguments.getString(i);
                                    line.append("->" + field);
                                }
                            } else if (arguments.getNode(0).getName().equals("SelectionExpression")) {
                                Node selectionExpression = arguments.getNode(0);
                                Node argumentsPrimaryIdentifier = selectionExpression.getNode(0);
                                line.append(argumentsPrimaryIdentifier.getString(0));
                                for (int i = 1; i < selectionExpression.size(); i++) {
                                    String field = selectionExpression.getString(i);
                                    line.append("->" + field);
                                }
                                //line.append("->data");
                            } else if (arguments.getNode(0).getName().equals("CallExpression")) {
                                Node callExpression = arguments.getNode(0);
                                Node argumentsPrimaryIdentifier = callExpression.getNode(0);
                                line.append(argumentsPrimaryIdentifier.getString(0) + "->__vptr");

                                for (int i = 1; i < callExpression.size(); i++) {
                                    if (callExpression.get(i) instanceof String) {
                                        String field = callExpression.getString(i);
                                        line.append("->" + field);
                                    } else if (callExpression.get(i) instanceof Node) {
                                        if (callExpression.getNode(i).getName().equals("Arguments")) {
                                            line.append("(");
                                            for (int j = 0; j < callExpression.getNode(i).size(); j++) {
                                                if (j > 0)
                                                    line.append(",");
                                                String field = callExpression.getNode(i).getString(j);
                                                line.append(field);
                                            }
                                            line.append(")");
                                        }
                                    }
                                }
                                //line.append("->data");
                            }
                            if (expressionStatementChild.getString(2) != null) {
                                line.append(" << " + expressionStatementChild.getString(2));
                            }
                            summary.addLine(line.toString() + ";\n");
                        }
                    }
                } else if (currentNode.getName().equals("ReturnStatement")) {
                    visitReturnStatement(currentNode, classBodyNode);
                }
            }
            summary.decMethodScope();
        } else {
            for (Object o : n.getNode(7)) {
                if (o instanceof Node && ((Node) o).getName().equals("FieldDeclaration"))
                    visitFieldDeclaration(GNode.cast(o));
            }
        }
    }

    public void visitReturnStatement(GNode n,GNode classBodyNode) {
        if (!summary.isMainClass) {
            for (Object o : n) {
                Node currentNode = (Node) o;
                if (currentNode.getName().equals("NewClassExpression")) {
                    //TODO:
                    String newClass = "new "+currentNode.getNode(2).getString(0)+"(";
                    Node arg = currentNode.getNode(3);
                    for (int i = 0; i < arg.size(); i++) {
                        //add arguments!
                    }
                    newClass += ");\n";
                    summary.addLine("return "+newClass);
                } else if (currentNode.getName().equals("PrimaryIdentifier")) {
                    String variable = currentNode.getString(0);
                    if (summary.initializerList.containsKey(variable))
                        summary.addLine("return __this->" + variable + ";\n");
                    else
                        summary.addLine("return " + variable + ";\n");
                } else if (currentNode.getName().equals("StringLiteral")) {
                    String value = currentNode.getString(0);
                    summary.addLine("return new __String(" + value + ");\n");
                } else if (currentNode.getName().equals("IntegerLiteral")) {
                    String value = currentNode.getString(0);
                    summary.addLine("return " + value + ";\n");
                } else if (currentNode.getName().equals("CallExpression")) {
                    String primaryIdentifier = currentNode.getNode(0).getString(0);
                    boolean parentGate = true; //if true, we need to call parent Object to get its field
                    for (FieldDeclaration f : summary.currentClass.declarations) {
                        if (f.variableName.equals(primaryIdentifier)) {
                            parentGate = false;
                            break;
                        }
                    }
                    StringBuilder line = new StringBuilder("return " + (parentGate ? "__this->parent." + primaryIdentifier : "__this->" + primaryIdentifier));
                    for (int i = 1; i < currentNode.size(); i++) {
                        Object o1 = currentNode.get(i);
                        if (o1 instanceof Node) {
                            Node currentChild = (Node) o1;
                            if (currentChild.getName().equals("Fields")) {
                                for (int j = 0; j < currentChild.size(); j++) {
                                    line.append("->" + currentChild.getString(j));
                                }
                            } else if (currentChild.getName().equals("Arguments")) {
                                line.append("(");
                                for (int j = 0; j < currentChild.size(); j++) {
                                    if (j > 0)
                                        line.append(",");

                                    parentGate = true; //if true, we need to call parent Object to get its field
                                    for (FieldDeclaration f : summary.currentClass.declarations) {
                                        if (f.variableName.equals(currentChild.getString(j))) {
                                            parentGate = false;
                                            break;
                                        }
                                    }
                                    line.append(parentGate ? "__this->parent." + currentChild.getString(j) : "__this->" + currentChild.getString(j));
                                }
                                line.append(")");
                            }
                        } else if (o1 instanceof String) {
                            line.append("->" + (String) o1);
                        }
                    }
                    summary.addLine(line + ";\n");
                } else if (currentNode.getName().equals("AdditiveExpression")) { //test025
                    String additiveExpression = "";
                    for (int i = 0; i < currentNode.size(); i++) {
                        Object o1 = currentNode.get(i);
                        if (o1 instanceof Node) {
                            Node currentChild = (Node) o1;
                            if (currentChild.getName().equals("IntegerLiteral")) {
                                additiveExpression += currentChild.getString(0);
                            } else if (currentChild.getName().equals("PrimaryIdentifier")) {
                                String primaryID_str = currentChild.getString(0);
                                boolean thisVariableIsDeclaredInCurrentClass=false;

                                //The following lines may have bugs(wasn't able to test them in test 25)
                                for (int classBody_child_index = 0; classBody_child_index < classBodyNode.size(); classBody_child_index++) {
                                    Object classBody_child = classBodyNode.get(classBody_child_index);
                                    if(classBody_child instanceof Node){
                                        Node classBody_child_NODE = (Node) classBody_child;
                                        if(classBody_child_NODE.getName().equals("FieldDeclaration")){
                                            if(classBody_child_NODE.getNode(2).getNode(0).getName().equals("Declarator")){
                                                Node declaratorNode = (Node) classBody_child_NODE.getNode(2).getNode(0);
                                                for (int decNode_index = 0; decNode_index < declaratorNode.size(); decNode_index++) {
                                                    Object declaredVariables = currentNode.get(decNode_index);
                                                    if(declaredVariables instanceof String ){
                                                        if(declaredVariables.equals(primaryID_str)){
                                                            thisVariableIsDeclaredInCurrentClass=true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if(thisVariableIsDeclaredInCurrentClass){//If the variable is declared in the struct, just use it
                                    additiveExpression += "__this->"+primaryID_str;
                                }else{//Otherwise, the variable should be from a parent class
                                    additiveExpression += "__this->parent."+primaryID_str;
                                }

                            }
                        } else if (o1 instanceof String) {
                            additiveExpression += o1;
                        }
                    }
                    summary.addLine("return " + additiveExpression + ";\n");

                }

            }
        }
    }

    // visit method
    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) {
                dispatch((Node) o);
            }
        }
    }


    public PrintCppFile(Runtime runtime, AstTraversal.AstTraversalSummary summaryTraversal) {
        this.runtime = runtime;
        this.summaryTraversal = summaryTraversal;
    }

    static class cppFileSummary {

        // testing information so that we can perform unit testing
        int numberClasses = 0;
        int currentClassMethodCount = 0;
        TreeMap<String, Integer> classMethodCounts = new TreeMap<>();

        StringBuilder code;
        int scope;
        ClassImplementation currentClass;
        HashMap<String, String> initializerList;
        String classLocation = "";
        String currentClassLocation = "";

        boolean hasRunTime;
        StringBuilder runTime;
        int runTimeScope;

        boolean isMainClass = false;

        public cppFileSummary() {
            code = new StringBuilder(
                    "#include \"output.h\"\n" +
                            "#include <sstream>\n\n" +
                            "using namespace java::lang;\n" +
                            "using namespace std;\n");
            scope = 0;

            hasRunTime = false;
            runTime = new StringBuilder();
            runTimeScope = 0;
        }

        public void addNamespace(String name) {
            for (int i = 0; i < scope; i++)
                code.append("\t");

            code.append("namespace " + name);
            incScope();
        }

        public void decScope() {
            scope--;

            for (int i = 0; i < scope; i++)
                code.append("\t");
            code.append("}\n");
        }

        public void incScope() {
            code.append(" {\n");
            scope++;
        }

        public void decMethodScope() {
            scope--;

            for (int i = 0; i < scope; i++)
                code.append("\t");
            code.append("};\n");
        }

        public void addLine(String line) {
            for (int i = 0; i < scope; i++)
                code.append("\t");

            code.append(line);
        }

        public void startRunTime() {
            hasRunTime = true;
            runTime.append("namespace __rt {\n");
            runTimeScope++;
        }

        public void closeRunTime() {
            runTimeScope = 0;
            runTime.append("}");
        }

        public void addRunTimeLine(String line) {
            for (int i = 0; i < runTimeScope; i++)
                runTime.append("\t");

            runTime.append(line);
        }

        public void decRunTimeScope() {
            runTimeScope--;

            for (int i = 0; i < runTimeScope; i++)
                runTime.append("\t");
            runTime.append("}\n");
        }

        public void incRunTimeScope() {
            runTime.append(" {\n");
            runTimeScope++;
        }


    }

    public cppFileSummary getSummary(GNode n) {
        visit(n);

        while (summary.scope > 0) {
            summary.decScope();
        }

        if (summary.hasRunTime) {
            while (summary.runTimeScope > 1) {
                summary.decRunTimeScope();
            }
            summary.closeRunTime();
        }

        summary.code.append("\n"+summary.runTime.toString());

        return summary;
    }


    public static void main(String[] args) {

        //TO RUN: run-main edu.nyu.oop.PrintCppFile ***
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
                PrintWriter printerOutput;

                File output;

                GNode node = (GNode) ImplementationUtil.loadTestFile(test);
                // get the summary traversal (class implementations)
                AstTraversal visitorTraversal = new AstTraversal(ImplementationUtil.newRuntime());
                AstTraversal.AstTraversalSummary summaryTraversal = visitorTraversal.getTraversal(node);

                // get the mutated tree
                AstMutator visitor1 = new AstMutator(ImplementationUtil.newRuntime());
                visitor1.mutate(node);

                ImplementationUtil.prettyPrintAst(node);

                // get the summary of the cpp implementations
                PrintCppFile visitor = new PrintCppFile(ImplementationUtil.newRuntime(), summaryTraversal);
                cppFileSummary summaryCpp = visitor.getSummary(node);

                String outputPath = String.format("./testOutputs/translationOutputs/test%03d/output.cpp", i);
                output = new File(outputPath);
                output.getParentFile().mkdirs();
                output.createNewFile();

                printerOutput = new PrintWriter(output);
                printerOutput.println(summaryCpp.code.toString());
                out.println(summaryCpp.code.toString());
                printerOutput.flush();
                printerOutput.close();
                out.println("output " + i + " printed\n");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
