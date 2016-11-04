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
public class PrintCppFile extends Visitor {

    private cppFileSummary summary = new cppFileSummary();
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Runtime runtime;
    private AstTraversal.AstTraversalSummary summaryTraversal;

    StringBuilder cppImplementation = new StringBuilder();

    // visitXXX methods

    public void visitPackageDeclaration(GNode n) {
        Node qualifiedIdentifier = n.getNode(1);
        if (qualifiedIdentifier != null) {
            for (int i = 0; i < qualifiedIdentifier.size(); i++) {
                summary.addNamespace(qualifiedIdentifier.getString(i));
            }
        }
    }

    public void visitClassDeclaration(GNode n) {
        if (n.getString(1).contains("Test")) {
            return;
        }
        String className = n.getString(1);
        summary.currentClass = summaryTraversal.classes.get(className);

        //information for initializer list
        summary.initializerVariables = new ArrayList<>();
        summary.initializerValues = new ArrayList<>();

        summary.initializerVariables.add("__vptr");
        summary.initializerValues.add("&__vtable");

        visitClassBody((GNode) n.getNode(5));

        //__class() method
        summary.addLine("Class __" + summary.currentClass.name + "::__class()");
        summary.incScope();
        summary.addLine("static Class k =\n");
        summary.addLine("new __Class(__rt::literal(\"class inputs.javalang." + summary.currentClass.name + "\"), (Class) __rt::null());\n");
        summary.addLine("return k;\n");
        summary.decScope();
        summary.code.append("\n");

        //vtable
        summary.addLine("__"+summary.currentClass.name+"_VT __"+summary.currentClass.name+"::__vtable;\n");
    }

    public void visitClassBody(GNode n) {
        boolean constructorCreated = false;
        for (Object methods : n) {
            GNode currentMethod = (GNode) methods;
            if (currentMethod.getName().equals("FieldDeclaration")) {
                visitFieldDeclaration(currentMethod);
            }
            else if (currentMethod.getName().equals("ConstructorDeclaration")) {
                visitConstructorDeclaration(currentMethod);
                constructorCreated = true;
                summary.code.append("\n");
            } else if (currentMethod.getName().equals("MethodDeclaration")) {
                visitMethodDeclaration(currentMethod);
                summary.code.append("\n");
            }
        }
        if (!constructorCreated) {
            summary.addLine("__"+summary.currentClass.name+"::__"+summary.currentClass.name+"() : __vptr(&__vtable) {};\n");
        }
    }

    public void visitFieldDeclaration(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                Node fieldNode = (Node) o;
                if (fieldNode.getName().equals("Declarators")) {
                    Node declarator = fieldNode.getNode(0);
                    summary.initializerVariables.add(declarator.getString(0));
                    Object o1 = declarator.get(2);
                    if (o1 != null && o1 instanceof Node) {
                        Node assignment = (Node) o1;
                        if (assignment.getName().equals("NewClassExpression")) {
                            Node qualifiedIdentifier = assignment.getNode(2);
                            String className = qualifiedIdentifier.getString(0);

                            StringBuilder arguments = new StringBuilder();
                            for (int i = 0; i < assignment.size(); i++) {
                                Object o2 = assignment.get(i);
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
                            summary.initializerValues.add("new "+className+"("+arguments.toString()+")");
                        }
                        //TODO: other cases here too
                    }
                }
            }
        }
    }

    public void visitConstructorDeclaration(GNode n) {
        String constructorName = n.getString(2);
        Node formalParameters = n.getNode(3);
        Node block = n.getNode(5);

        StringBuilder constructor = new StringBuilder("__"+constructorName+"::__"+constructorName+"(");
        for (int i = 0; i < formalParameters.size(); i++) {
            Object o = formalParameters.get(i);
            if (o instanceof Node) {
                Node formalParameter = (Node) o;
                if (formalParameter.getName().equals("FormalParameter"))
                for (Object o1 : formalParameter) {
                    if (o1 instanceof Node) {
                        Node type = (Node) o1;
                        if (type.getName().equals("Type")) {
                            String typeString = type.getNode(0).getString(0);
                            if (i > 0)
                                constructor.append(", ");
                            constructor.append(typeString+" ");
                        }
                    }
                    else if (o1 instanceof String) {
                        String variableName = (String) o1;
                        constructor.append(variableName);
                        summary.initializerValues.add(variableName);
                    }
                }
            }
        }
        constructor.append(") :\n");
        for (int i = 0; i < summary.initializerVariables.size(); i++) {
            if (i > 0)
                constructor.append(",\n");
            for (int j = 0; j < summary.scope + 1; j++) {
                constructor.append("\t");
            }
            constructor.append(summary.initializerVariables.get(i)+"("+summary.initializerValues.get(i)+")");
        }

        summary.addLine(constructor.toString()+"\n");
        for (int i = 0 ; i < summary.scope; i++)
            summary.code.append("\t");

        summary.incScope();

        for (Object o : block) {
            /*if (o instanceof Node) {
                Node currentNode = (Node) o;
                if (currentNode.getName().equals("ExpressionStatement")) {
                    for (Object o1 : currentNode) {
                        if (o1 instanceof Node) {
                            Node currentNode1 = (Node) o1;
                            if (currentNode1.getName().equals("Expression")) {
                                blockStrings += "\n\t\t";
                                if (currentNode1.getNode(0).getName().equals("PrimaryIdentifier")
                                        && currentNode1.getNode(2).getName().equals("NewClassExpression")) {

                                    String primaryIdentifier = currentNode1.getNode(0).getString(0);
                                    Node newClassExpression = currentNode1.getNode(2);
                                    String newClassType = newClassExpression.getNode(2).getString(0);
                                    Node newClassExpressionArgs = newClassExpression.getNode(3);
                                    String qualifiedIdentifier = newClassExpression.getNode(2).getString(0);

                                    int argsSize = newClassExpressionArgs.size();
                                    if (newClassExpressionArgs.size() > 0) {
                                        if(summary.superClassName == null) {
                                            blockStrings += primaryIdentifier + " = ";
                                        }else{
                                            boolean gateParent = true;
                                            for(FieldDeclaration curr : summary.currentFieldDeclarationList){
                                                if(primaryIdentifier.equals(curr.variableName)){
                                                    blockStrings += primaryIdentifier + " = ";
                                                    gateParent = false;
                                                }
                                            }
                                            if(gateParent){
                                                blockStrings += "parent." + primaryIdentifier + " = ";
                                            }
                                        }
                                        for (Object arg : newClassExpressionArgs) {
                                            argsSize--;
                                            if (arg instanceof Node) {
                                                Node currentArg = (Node) arg;
                                                if (currentArg.getName().equals("StringLiteral")) {
                                                    blockStrings += "new " + qualifiedIdentifier + "("
                                                            + currentArg.getString(0) + ");";
                                                } else if (currentArg.getNode(0) instanceof Node) {

                                                    if (currentArg.getNode(0).getName().equals("StringLiteral")) {
                                                        if (argsSize > 0) {
                                                            blockStrings += currentArg.getNode(0).getString(0) + ",";
                                                        } else {
                                                            blockStrings += currentArg.getNode(0).getString(0);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else if (currentNode1.getNode(0).getName().equals("PrimaryIdentifier")) {
                                    blockStrings += currentNode1.getNode(0).getString(0);
                                    if(currentNode1.getNode(2).getName().equals("ThisExpression")){
                                        blockStrings += " = this;\n";
                                    }
                                } else if (currentNode1.getNode(0).getName().equals("SelectionExpression")) {
                                    if (currentNode1.getNode(0).getNode(0).getName().equals("ThisExpression")) {
                                        blockStrings += currentNode1.getNode(0).getString(1);
                                    }
                                    blockStrings += "(";
                                    blockStrings += currentNode1.getNode(2).getString(0);
                                    blockStrings += ")";
                                }

                            }
                        }
                    }
                }
            }*/
        }
        summary.decScope();
        /*//constructor += blockStrings;
        constructor += "  {\n";

        // initializer block
        blockStrings += "\n\t\t";
        for (Object o : block) {
            if (o instanceof Node) {
                Node currentNode = (Node) o;
                if (currentNode.getName().equals("ExpressionStatement")) {
                    for (Object o1 : currentNode) {
                        if (o1 instanceof Node) {
                            Node currentNode2 = (Node) o1;
                            if (currentNode2.getName().equals("CallExpression")) {
                                if (currentNode2.getNode(0).getNode(0).getString(0).equals("cout")) {
                                    blockStrings += "cout << ";
                                }
                                if (currentNode2.getString(2).equals("endl")) {
                                    Node args = currentNode2.getNode(3);
                                    for (Object o3 : args) {
                                        if (o3 instanceof Node) {
                                            Node currentNode3 = (Node) o3;
                                            if (currentNode3.getName().equals("PrimaryIdentifier")) {
                                                String primaryIdentifier = currentNode3.getString(0);
                                                if(summary.superClassName == null) {
                                                    blockStrings += primaryIdentifier + "->data ";
                                                }else{
                                                    boolean gateParent = true;
                                                    for(FieldDeclaration curr : summary.currentFieldDeclarationList){
                                                        if(primaryIdentifier.equals(curr.variableName)){
                                                            blockStrings += primaryIdentifier + "->data ";
                                                            gateParent = false;
                                                        }
                                                    }
                                                    if(gateParent){
                                                        blockStrings += "parent." + primaryIdentifier + "->data ";
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    blockStrings += "<< endl;\n";
                                }
                            }


                        }
                    }
                }
            }
        }
       */
    }

    public void visitMethodDeclaration(GNode n) {

        //String __A::getFld(A __this) {
        //return  __this->fld;
        //}

        StringBuilder methodSignature = new StringBuilder();

        Node methodType = n.getNode(2);
        String returnType = methodType.getNode(0).getString(0);
        String methodName = n.getString(3);

        methodSignature.append(returnType+" __"+summary.currentClass.name+"::"+methodName+"(");

        Node formalParameters = n.getNode(4);
        for (int i = 0 ; i < formalParameters.size(); i++) {
            Object o = formalParameters.get(i);
            if (o instanceof Node) {
                Node formalParameter = (Node) o;
                if (formalParameter.getName().equals("FormalParameter")) {
                    Node paramType = formalParameter.getNode(1);
                    String className = paramType.getNode(0).getString(0);
                    String paramName = formalParameter.getString(3);

                    if (i > 0)
                        methodSignature.append(", ");
                    methodSignature.append(className + " "+paramName);
                }
            }
        }
        methodSignature.append(")");
        summary.addLine(methodSignature.toString());
        summary.incScope();

        Node methodBlock = n.getNode(7);

        for (Object o : methodBlock) {
            GNode currentNode = (GNode) o;
            /*if (currentNode.getName().equals("FieldDeclaration")) {
                String fieldDeclaration = "\t\t";
                // type
                fieldDeclaration += currentNode.getNode(1).getNode(0).getString(0) + " ";
                // field
                if (currentNode.getNode(2).getName().equals("Declarators")) {
                    Node declaratorsNode = currentNode.getNode(2);
                    fieldDeclaration += declaratorsNode.getNode(0).getString(0);
                }
                cppImplementation.append(fieldDeclaration + ";\n");
            } else if (currentNode.getName().equals("ExpressionStatement")) {
                String expressionStatement = "\t\t";
                if (currentNode.getNode(0).getNode(0).getName().equals("PrimaryIdentifier")) {
                    String currentPrimaryIdentifier = currentNode.getNode(0).getNode(0).getString(0);
                    if (summary.thisGate && summary.currentMethodName.startsWith("set")) {
                        expressionStatement += "__this->";
                        expressionStatement += currentPrimaryIdentifier;
                    } else {
                        expressionStatement += currentPrimaryIdentifier;
                    }
                    if (currentNode.getNode(0).getNode(2).getName().equals("PrimaryIdentifier")) {
                        expressionStatement += " = " + currentNode.getNode(0).getNode(2).getString(0);
                    }
                }
                cppImplementation.append(expressionStatement + ";\n");
            } else if (currentNode.getName().equals("ReturnStatement")) {
                visitReturnStatement(currentNode);
                return;
            }*/
        }

        summary.decScope();
    }

    public void visitReturnStatement(GNode n) {


/*        String returnStatement = "\t\treturn";

        String returnStatementString = "";
        String returnPrimaryIdentifier = "";

        String identifier = "";

        for (Object o : n) {
            Node currentNode = (Node) o;
            if (currentNode.getName().equals("NewClassExpression")) {
                for (Object o1 : currentNode) {
                    if (o1 instanceof Node) {
                        Node currentNodeO1 = (Node) o1;
                        if (currentNodeO1.getName().equals("QualifiedIdentifier")) {
                            identifier = currentNodeO1.getString(0);
                        } else if (currentNodeO1.getName().equals("Arguments")) {
                            if (currentNodeO1.size() > 0) {
                                for (Object argument : currentNodeO1) {
                                    Node currentArgument = (Node) argument;
                                    if (currentArgument.getNode(0).getName().equals("StringLiteral")) {
                                        if (identifier.equals("__String")) {
                                            returnStatementString = "\t\tstd::ostringstream sout;\n";
                                            returnStatementString += "\t\tsout << ";
                                            returnStatementString += currentArgument.getNode(0).getString(0) + ";\n";
                                            returnStatementString += "\t\treturn new __String(sout.str());\n\t}\n";
                                            cppImplementation.append(returnStatementString + "\n\n");
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (currentNode.getName().equals("PrimaryIdentifier")) {
                if (summary.thisGate) {
                    returnPrimaryIdentifier += "\t\treturn  __this->" + currentNode.getString(0) + ";";
                    cppImplementation.append(returnPrimaryIdentifier + "\n\t}\n\n");
                    return;
                } else {
                    returnPrimaryIdentifier += "\t\treturn " + currentNode.getString(0) + ";";
                    cppImplementation.append(returnPrimaryIdentifier + "\n\t}\n\n");
                    return;
                }
            } else if (currentNode.getName().equals("StringLiteral")) {
                returnStatementString = "\t\tstd::ostringstream sout;\n";
                returnStatementString += "\t\tsout << ";
                returnStatementString += currentNode.getString(0) + ";\n";
                returnStatementString += "\t\treturn new __String(sout.str());\n\t}\n";
                cppImplementation.append(returnStatementString + "\n\n");
                return;
            }
        }*/


    }

    // visitMethod
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
        StringBuilder code;
        int scope;
        ClassImplementation currentClass;
        ArrayList<String> initializerVariables;
        ArrayList<String> initializerValues;

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

            code.append("namespace "+name);
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
        int end = 20;

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

                output = new File("testOutputs/printCppFile/v2", String.format("Test%03d", i));
                output.getParentFile().mkdirs();
                output.createNewFile();

                printerOutput = new PrintWriter(output);
                printerOutput.println(summaryCpp.code.toString());
                printerOutput.flush();
                printerOutput.close();
                out.println("output " + i + " printed\n");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
