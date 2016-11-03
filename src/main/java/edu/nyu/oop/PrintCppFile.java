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

import static java.lang.System.out;

/**
 * Created by Garrett on 10/21/16.
 */
public class PrintCppFile extends Visitor {

    private PrintCppFile.printCppFileSummary summary = new PrintCppFile.printCppFileSummary();
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Runtime runtime;
    private AstTraversal.AstTraversalSummary summaryTraversal;

    StringBuilder cppImplementation = new StringBuilder();

    // visitXXX methods
    public void visitClassDeclaration(GNode n) {
        if (n.getString(1).contains("Test")) {
            return;
        }
        String className = n.getString(1);
        summary.currentClassName = className;
        summary.methodList = summaryTraversal.classes.get(className).methods;
        summary.currentFieldDeclarationList = summaryTraversal.classes.get(className).declarations;
        summary.superClassName = summaryTraversal.classes.get(className).superClassName;

        visitClassBody((GNode) n.getNode(5));

//        StringBuilder hashCodeMethod = new StringBuilder();
//        hashCodeMethod.append("\tint32_t __" + summary.currentClassName + "::hashCode(");
//        hashCodeMethod.append(summary.currentClassName + " __this){\n");
//        hashCodeMethod.append("\t\treturn 5;\n");
//        hashCodeMethod.append("\t}\n\n");
//        cppImplementation.append(hashCodeMethod);

        boolean toStringGate = true;
        for(MethodImplementation m : summary.methodList){
            if(m.name.equals("toString")){
                toStringGate = false;
                break;
            }
        }
        StringBuilder toString = new StringBuilder();
        if(toStringGate){
            toString.append("\tString __" + className + "::toString(" + className + " __this) {\n" +
                    "\t\tstd::ostringstream sout;\n" +
                    "\t\tsout << __this;\n" +
                    "\t\treturn new __String(sout.str());\n" +
                    "\t}\n\n");
        }
        cppImplementation.append(toString);
        /*
        // adding getMethods because we don't have a way to access variables defined in the class
        // when translating to C++
        StringBuilder getMethods = new StringBuilder();
        if (summary.currentFieldDeclarationList.size() != 0) {
            for (FieldDeclaration currentDeclaration : summary.currentFieldDeclarationList) {
                String type;
                if (currentDeclaration.staticType.equals("int")) {
                    type = "int32_t";
                } else {
                    type = currentDeclaration.staticType;
                }
                getMethods.append("\t" + type + " __" + className + "::get" + currentDeclaration.variableName + "("
                        + summary.currentClassName + " __this)" + "{\n");
                getMethods.append("\t\t" + "return __this->" + currentDeclaration.variableName + ";");
                getMethods.append("\n\t}\n\n");
            }
        }

        cppImplementation.append(getMethods);
        */

        cppImplementation.append("\tClass __" + summary.currentClassName + "::__class() {\n" +
                "\t\tstatic Class k =\n" +
                "\t\t\tnew __Class(__rt::literal(\"class inputs.javalang." + summary.currentClassName + "\"), (Class) __rt::null());\n" +
                " \t\treturn k;\n" +
                "\t}\n\n");

    }

    public void visitClassBody(GNode n) {
        boolean constructorGate = false;
        for (Object methods : n) {
            GNode currentMethod = (GNode) methods;
            if (currentMethod.getName().equals("ConstructorDeclaration")) {
                constructorGate = true;
                visitConstructorDeclaration(currentMethod);
            } else if (currentMethod.getName().equals("MethodDeclaration")) {
                if (!constructorGate) {
                    String constructor = "";
                    constructor += "\n\t__" + summary.currentClassName + "::";
                    constructor += "__" + summary.currentClassName + "(" + ")";
                    constructor += "  :  __vptr(&__vtable) {}";
                    cppImplementation.append(constructor + "\n\n");
                    cppImplementation.append("\t__" + summary.currentClassName);
                    cppImplementation.append("_VT __" + summary.currentClassName + "::__vtable;\n\n");
                }
                visitMethodDeclaration(currentMethod);
            }
        }
    }

    public void visitConstructorDeclaration(GNode n) {

        String constructorDeclaration = n.getString(2);
        Node formalParameters = n.getNode(3);
        Node block = n.getNode(5);

        String constructor = "";
        constructor += "\n\t__" + constructorDeclaration + "::";
        constructor += "__" + constructorDeclaration + "(";

        String constructorArguments = "";

        if (formalParameters.size() > 0) {
            for (Object o : formalParameters) {
                if (o instanceof Node) {
                    Node currentNode = (Node) o;
                    for (Object o1 : currentNode) {
                        if (o1 instanceof Node) {
                            Node currentNode1 = (Node) o1;
                            if (currentNode1.getName().equals("Type")) {
                                String type = currentNode1.getNode(0).getString(0);
                                /*
                                if(type.equals("String")){
                                    type = "std::string";
                                }
                                */
                                constructorArguments += type + " ";
                            }
                        } else {
                            if (o1 != null) {
                                constructorArguments += o1.toString();
                            }
                        }
                    }
                } else {

                }
            }
        }

        constructor += constructorArguments;
        constructor += ")";

        constructor += " : __vptr(&__vtable)";

        String blockStrings = "";
        for (Object o : block) {
            if (o instanceof Node) {
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
            }

        }

        //constructor += blockStrings;
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
       // constructor += initializerBlock;
        if (summary.currentFieldDeclarationList.size() > 0) {
            for (FieldDeclaration o1 : summary.currentFieldDeclarationList) {
                if (o1.staticType.equals("String")) {
                    if (!(o1.stringLiteral == null)) {
                        blockStrings += o1.variableName + " = new __String(" + o1.stringLiteral + ");\n";
                        continue;
                    }
                }
            }
        }

        constructor += blockStrings;

        constructor += "\n\t}";

        cppImplementation.append(constructor + "\n\n");

        cppImplementation.append("\t__" + summary.currentClassName + "_VT __" + summary.currentClassName + "::__vtable;\n\n");
    }

    public void visitMethodDeclaration(GNode n) {

        String type = "";
        String methodName = "";
        if (n.getNode(2).getName().equals("Type")) {
            type = n.getNode(2).getNode(0).getString(0);
            summary.qualifiedIdentifier = type;
            methodName = n.getString(3);
        } else if (n.getNode(2).getName().equals("VoidType")) {
            type = "void";
            methodName = n.getString(3);
        }

        summary.currentMethodName = methodName;


        cppImplementation.append("\t" + type + " ");
        cppImplementation.append("__" + summary.currentClassName + "::" + methodName + "(");

        if (n.getNode(4).getName().equals("Arguments")) {
            int numberArgs = n.getNode(4).size();
            if (n.getNode(4).size() > 0) {
                for (Object argument : n.getNode(4)) {
                    numberArgs--;
                    if (numberArgs > 0) {
                        cppImplementation.append(argument.toString() + ",");
                    } else {
                        cppImplementation.append(argument.toString());
                    }
                }
            } else {
                cppImplementation.append(summary.currentClassName + " __this");
            }
        } else if (n.getNode(4).getName().equals("FormalParameters")) {
            int numberParameters = n.getNode(4).size();
            Node formalParameters = n.getNode(4);
            for (Object o : formalParameters) {
                if (o instanceof Node) {
                    Node currentNode = (Node) o;
                    String parameter = "";
                    if (currentNode.getName().equals("FormalParameter")) {
                        summary.thisGate = true;
                        numberParameters--;
                        String paramIdentifier = currentNode.getNode(1).getNode(0).getString(0) + " ";
                        String paramName = currentNode.getString(3);
                        if (numberParameters > 0) {
                            if (!summary.currentMethodName.startsWith("set") && paramName.equals("__this")) {

                            } else {
                                parameter += paramIdentifier;
                                parameter += paramName + ",";
                                cppImplementation.append(parameter);
                            }
                        } else {
                            parameter += paramIdentifier;
                            parameter += paramName;
                            cppImplementation.append(parameter);
                        }
                    }
                }
            }
        }

        cppImplementation.append(") {\n");

        Node methodBlock = n.getNode(7);
        for (Object o : methodBlock) {
            GNode currentNode = (GNode) o;
            if (currentNode.getName().equals("FieldDeclaration")) {
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
            }
        }
        summary.thisGate = false;
        cppImplementation.append("\t}\n\n");
    }

    public void visitReturnStatement(GNode n) {


        String returnStatement = "\t\treturn";

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
        }


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

    static class printCppFileSummary {
        String currentClassName;
        String filePrinted;
        String qualifiedIdentifier;
        boolean thisGate = false;
        int numberParameters;
        String currentMethodName;
        ArrayList<FieldDeclaration> currentFieldDeclarationList;
        String superClassName;
        ArrayList<MethodImplementation> methodList;

    }

    public printCppFileSummary getSummary(GNode n) {
        StringBuilder s1 = new StringBuilder();
        s1.append("\n//------------------\n\n");
        s1.append("#include \"output.h\"\n");
        s1.append("#include <sstream>\n\n");
        s1.append("using namespace java::lang;\nusing namespace std;\n");
        // get the namespace
        Node packageNode = n.getNode(0).getNode(1);
        String namespace = "";
        int namespaceCounter = 0;
        for (Object packageName : packageNode) {
            for (int i = 0; i < namespaceCounter; i++) {
                namespace += "\t";
            }
            namespace += "namespace " + packageName.toString() + "{\n";
            namespaceCounter += 1;
        }
        s1.append(namespace);


        // visit the class declarations
        for (Object o : n) {
            GNode currentClass = (GNode) o;
            if (currentClass.getName().equals("ClassDeclaration")) {
                visitClassDeclaration(currentClass);
            }
        }

        s1.append(cppImplementation);


        // close the namespace
        s1.append("\n");
        String closeNamespace = "";
        while (namespaceCounter > 0) {
            for (int i = 0; i < namespaceCounter - 1; i++) {
                closeNamespace += ("\t");
            }
            closeNamespace += "}\n";
            namespaceCounter--;
        }
        s1.append(closeNamespace);
        s1.append("\n//------------------\n\n");
        out.println(s1.toString());

        summary.filePrinted = s1.toString();

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
                printCppFileSummary summaryCpp = visitor.getSummary(node);

                String outputCppFile = "";
                outputCppFile += summaryCpp.filePrinted;

                output = new File("testOutputs/PrintCppFile", String.format("Test%03d", i));
                output.createNewFile();

                printerOutput = new PrintWriter(output);
                printerOutput.println(outputCppFile);
                printerOutput.flush();
                printerOutput.close();
                out.println("output " + i + " printed\n");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
