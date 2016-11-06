package edu.nyu.oop;

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
        if (n.getString(1).contains("Test")) {
            return;
        }
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
        summary.addLine("new __Class(__rt::literal(\"" + summary.currentClassLocation + "\"), (Class) __rt::null());\n");
        summary.addLine("return k;\n");
        summary.decScope();
        summary.code.append("\n");

        //vtable
        summary.addLine("__" + summary.currentClass.name + "_VT __" + summary.currentClass.name + "::__vtable;\n\n");

        summary.classMethodCounts.put(summary.currentClass.name, summary.currentClassMethodCount);

    }

    public void visitClassBody(GNode n) {
        boolean constructorCreated = false;
        for (Object methods : n) {
            GNode currentMethod = (GNode) methods;
            if (currentMethod.getName().equals("FieldDeclaration")) {
                visitFieldDeclaration(currentMethod);
            } else if (currentMethod.getName().equals("ConstructorDeclaration")) {
                visitConstructorDeclaration(currentMethod);
                constructorCreated = true;
                summary.code.append("\n");
            } else if (currentMethod.getName().equals("MethodDeclaration")) {
                visitMethodDeclaration(currentMethod);
                summary.code.append("\n");
            }
        }
        if (!constructorCreated) {
            summary.addLine("__" + summary.currentClass.name + "::__" + summary.currentClass.name + "() : __vptr(&__vtable) {};\n\n");
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
                        //TODO: other cases here too
                    }

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
                                constructor.append(typeString + " ");
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
                                    if (formalParameters.size() > 0) {
                                        //summary.initializerList.put(variableName, "this");
                                    } else {
                                        summary.addLine(variableName + " " + operator + " this;\n");
                                    }
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
                            String statement = gateParent ? "parent." + blockPrimaryIdentifier + "->data" : blockPrimaryIdentifier + "->data";

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
        summary.decScope();

        Set<Map.Entry<String, String>> initializerSet = summary.initializerList.entrySet();
        StringBuilder initializers = new StringBuilder("__vptr(&__vtable)");

        for (Map.Entry<String, String> entry : initializerSet) {
            if (entry.getValue() != null) {
                initializers.append(",\n");
                for (int i = 0; i < summary.scope + 2; i++)
                    initializers.append("\t");
                initializers.append(entry.getKey() + "(" + entry.getValue() + ")");
            }
        }
        summary.code = new StringBuilder(String.format(summary.code.toString(), initializers.toString()));
    }

    public void visitMethodDeclaration(GNode n) {

        summary.currentClassMethodCount++;

        StringBuilder methodSignature = new StringBuilder();

        Node methodType = n.getNode(2);
        String returnType = (methodType.getName().equals("VoidType") ? "void" : methodType.getNode(0).getString(0));
        if (returnType.equals("int"))
            returnType = "int32_t";

        String methodName = n.getString(3);

        methodSignature.append(returnType + " __" + summary.currentClass.name + "::" + methodName + "(");

        Node formalParameters = n.getNode(4);
        for (int i = 0; i < formalParameters.size(); i++) {
            Object o = formalParameters.get(i);
            if (o instanceof Node) {
                Node formalParameter = (Node) o;
                if (formalParameter.getName().equals("FormalParameter")) {
                    Node paramType = formalParameter.getNode(1);
                    String className = paramType.getNode(0).getString(0);
                    String paramName = formalParameter.getString(3);

                    if (i > 0)
                        methodSignature.append(", ");
                    methodSignature.append(className + " " + paramName);
                }
            }
        }
        methodSignature.append(")");
        summary.addLine(methodSignature.toString());

        summary.incScope();
        Node methodBlock = n.getNode(7);

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
                    //TODO
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
                        if (arguments.getNode(0).getName().equals("PrimaryIdentifier")) {
                            Node argumentsPrimaryIdentifier = arguments.getNode(0);
                            line.append(argumentsPrimaryIdentifier.getString(0) + "->__vptr");
                            for (int i = 1; i < arguments.size(); i++) {
                                String field = arguments.getString(i);
                                line.append("->" + field);
                            }
                            if (expressionStatementChild.getString(2) != null) {
                                line.append(" << " + expressionStatementChild.getString(2));
                            }
                            summary.addLine(line.toString() + ";\n");
                        } else if (arguments.getNode(0).getName().equals("SelectionExpression")) {
                            Node selectionExpression = arguments.getNode(0);
                            Node argumentsPrimaryIdentifier = selectionExpression.getNode(0);
                            line.append(argumentsPrimaryIdentifier.getString(0));
                            for (int i = 1; i < selectionExpression.size(); i++) {
                                String field = selectionExpression.getString(i);
                                line.append("->" + field);
                            }
                            line.append("->data");
                            if (expressionStatementChild.getString(2) != null) {
                                line.append(" << " + expressionStatementChild.getString(2));
                            }
                            summary.addLine(line.toString() + ";\n");

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
                            line.append("->data");
                            if (expressionStatementChild.getString(2) != null) {
                                line.append(" << " + expressionStatementChild.getString(2));
                            }
                            summary.addLine(line.toString() + ";\n");
                        }
                    }
                }
            } else if (currentNode.getName().equals("ReturnStatement")) {
                visitReturnStatement(currentNode);
            }
        }
        summary.decScope();
    }

    public void visitReturnStatement(GNode n) {
        for (Object o : n) {
            Node currentNode = (Node) o;
            if (currentNode.getName().equals("NewClassExpression")) {
                //TODO:
            } else if (currentNode.getName().equals("PrimaryIdentifier")) {
                String variable = currentNode.getString(0);
                if (summary.initializerList.containsKey(variable))
                    summary.addLine("return __this->" + variable + ";\n");
                else
                    summary.addLine("return " + variable + ";\n");
            } else if (currentNode.getName().equals("StringLiteral")) {
                String value = currentNode.getString(0);
                summary.addLine("return new __String(" + value + ");\n");
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

        public cppFileSummary() {
            code = new StringBuilder(
                    "#include \"output.h\"\n" +
                            "#include <sstream>\n\n" +
                            "using namespace java::lang;\n" +
                            "using namespace std;\n");
            scope = 0;
        }

        public void addNamespace(String name) {
            for (int i = 0; i < scope; i++)
                code.append("\t");

            code.append("namespace " + name);
            incScope();
        }

        public void closeNamespace() {
            scope--;

            for (int i = 0; i < scope; i++)
                code.append("\t");
            code.append("}\n");
        }

        public void incScope() {
            code.append(" {\n");
            scope++;
        }

        public void decScope() {
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

    }

    public cppFileSummary getSummary(GNode n) {
        visit(n);

        while (summary.scope > 0) {
            summary.closeNamespace();
        }

        return summary;
    }


    public static void main(String[] args) {

        //TO RUN: run-main edu.nyu.oop.PrintCppFile ***
        // *** a number 0-20, or nothing to run all test cases
        int start = 0;
        int end = 12;

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

                output = new File("testOutputs/printCppOutputs", String.format("Test%03d", i));
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
