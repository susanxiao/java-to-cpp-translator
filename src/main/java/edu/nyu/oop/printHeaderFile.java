package edu.nyu.oop;

import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.util.Runtime;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import static java.lang.System.out;

import xtc.tree.Visitor;

import java.lang.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;


/**
 * Created by Garrett on 10/21/16.
 */

/**
 * Nodes
 * GNode currentClassNode;
 * GNode headerDeclaration;
 * GNode dataLayoutNode;
 * GNode FieldDeclarationNode;
 * GNode ModifiersNode;
 * GNode ParametersNode;
 * GNode DataLayoutMethodDeclarationNode;
 * GNode DeclaratorsNode;
 * GNode VTableNode;
 * GNode VTableMethodDeclarationNode;
 */

public class printHeaderFile extends Visitor {

    private printHeaderFile.headerFileSummary summary = new headerFileSummary();
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Runtime runtime;
    private AstTraversal.AstTraversalSummary summaryTraversal;

    StringBuilder s1 = new StringBuilder();

    // visitXXX methods
    public void visitHeaderDeclaration(GNode n) {
        String className = n.getString(0);

        summary.currentClassName = className;
        summary.currentClass = summaryTraversal.classes.get(className);
        summary.currentMethodList = summaryTraversal.classes.get(className).methods;
        summary.currentFieldDeclarationList = summaryTraversal.classes.get(className).declarations;
        summary.currentConstructorList = summaryTraversal.classes.get(className).constructors;

        s1.append("\tstruct __" + className + "\n\t{\n\n");

        if (summary.currentFieldDeclarationList.size() != 0) {
            for (FieldDeclaration currentDeclaration : summary.currentFieldDeclarationList) {
                String type;
                if (currentDeclaration.staticType.equals("int")) {
                    type = "int32_t";
                } else {
                    type = currentDeclaration.staticType;
                }
                s1.append("\t" + type + " " + currentDeclaration.variableName + ";\n\n");
            }
        }

        // Is this correct?
        if (summary.currentConstructorList.size() > 0) {
            s1.append("\t__" + className + "(");
            for (ConstructorImplementation currentConstructor : summary.currentConstructorList) {
                int size = currentConstructor.parameters.size();
                for (ParameterImplementation param : currentConstructor.parameters) {
                    size--;
                    String type;
                    if (param.type.equals("int")) {
                        type = "int32_t";
                    } else {
                        type = param.type;
                    }
                    s1.append(type + " " + param.name);
                    if (size > 0) {
                        s1.append(",");
                    }
                }
            }
            s1.append(");\n\n");
        } else {
            s1.append("\t__" + className + "(" + ");\n\n");
        }


        if (summary.currentClass.superClassName != null) {
            s1.append("\tClass parent;\n\n");
        }


        for (Object o : n) {
            if (o instanceof Node) {
                GNode currentNode = (GNode) o;
                if (currentNode.getName().equals("DataLayout")) {
                    visitDataLayout(currentNode);
                }
            }
        }

    }

    public void visitDataLayout(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                dispatch((GNode) o);
            }
        }
    }

    public void visitFieldDeclaration(GNode n) {
        String currentFieldDeclaration = "\t";
        if (n instanceof Node) {
            if (n.getNode(0).size() > 0) {
                currentFieldDeclaration += n.getNode(0).getString(0) + " ";
            }
        }
        currentFieldDeclaration += n.getString(1) + " ";
        currentFieldDeclaration += n.getString(2);
        if (n.getNode(3).size() > 0) {
            currentFieldDeclaration += " ";
            int k = n.getNode(3).size();
            for (Object o : n.getNode(3)) {
                k--;
                if (k > 0) {
                    currentFieldDeclaration += o.toString() + " ";
                } else {
                    currentFieldDeclaration += o.toString();
                }
            }
        }
        currentFieldDeclaration += ";";
        s1.append(currentFieldDeclaration + "\n\n");
    }

    public void visitDataLayoutMethodDeclaration(GNode n) {
        for (MethodImplementation currMethod : summaryTraversal.classes.get(summary.currentClassName).methods) {
            // if the class implements the method
            String methodComparing = n.getString(2);
            if (currMethod.name == methodComparing) {
                String currentMethodDeclaration = "\t";
                // TODO: modifiers?
                currentMethodDeclaration += "static ";
                /*
                if (n.getNode(0).size() > 0) {
                    currentMethodDeclaration += n.getNode(0).getString(0) + " ";
                }*/

                currentMethodDeclaration += n.getString(1) + " ";
                currentMethodDeclaration += n.getString(2) + "(";
                if (n.getString(2) != "__class") {
                    if (n.getString(2).equals("equals")) {
                        currentMethodDeclaration += summary.currentClassName + ",";
                    }
                    if (currMethod.parameters.size() != 0) {
                        for (ParameterImplementation param : currMethod.parameters) {
                            currentMethodDeclaration += param.type;
                        }
                    } else {
                        currentMethodDeclaration += n.getString(3);
                    }
                }

                // Parameters
                /*
                if (n.getNode(4).size() > 0) {
                    int size = n.getNode(4).size();
                    for (Object o : n.getNode(4)) {
                        if (size > 1) {
                            size--;
                            currentMethodDeclaration += o.toString() + ",";
                        } else {
                            currentMethodDeclaration += o.toString();
                        }
                    }
                }*/

                currentMethodDeclaration += ");";
                s1.append(currentMethodDeclaration + "\n");
            }
        }

    }

    public void visitVTable(GNode n) {
        s1.append("\n\t};\n\n\tstruct __" + summary.currentClassName + "_VT\n\t{\n\n");
        for (Object o : n) {
            if (o instanceof Node) {
                visitVTableMethodDeclaration((GNode) o);
            }
        }
        // vtable constructor
        s1.append(vTableConstructor(n));
        s1.append("\t};\n\n");
    }

    public String vTableConstructor(GNode n) {
        StringBuilder x = new StringBuilder();

        // TODO: Inheritance
        x.append("\n\t__" + summary.currentClassName + "_VT()\n");
        x.append("\t: __isa(__" + summary.currentClassName + "::" + "__class()),\n");
        int size = n.size();

        for (Object o : n) {
            String vTableMethod = "";
            GNode currentMethod = (GNode) o;
            if (currentMethod.getString(2).equals("__isa")) {
                continue;
            } else {
                String methodComparing = currentMethod.getString(2);
                // checking if the class implements the method
                for (MethodImplementation currMethod : summaryTraversal.classes.get(summary.currentClassName).methods) {
                    // if the class implements the method
                    if (currMethod.name == methodComparing) {
                        vTableMethod += "\t\t" + currentMethod.getString(2);
                        vTableMethod += "(&__";
                        vTableMethod += summary.currentClassName;
                        break;
                    }
                }

                if (methodComparing.equals("hashCode")) {
                    vTableMethod += "\t\thashCode((int_32t(*)(" + summary.currentClassName + ")) &__Object";
                } else if (methodComparing.equals("getClass")) {
                    vTableMethod += "\t\tgetClass((Class(*)(" + summary.currentClassName + ")) &__Object";
                } else if (methodComparing.equals("equals")) {
                    vTableMethod += "\t\tequals((bool(*)(" + summary.currentClassName + ",Object)) &__Object";
                }

                vTableMethod += "::" + currentMethod.getString(2) + ")";
            }
            if (size > 2) {
                size--;
                vTableMethod += ",\n";
            } else {
                vTableMethod += "\n";
            }
            x.append(vTableMethod);
        }
        x.append("\t\t{}\n\n");
        return x.toString();
    }

    public void visitVTableMethodDeclaration(GNode n) {
        String currentMethodDeclaration = "\t";
        if (n.getString(2).equals("__isa")) {
            currentMethodDeclaration += n.getString(1) + " ";
            currentMethodDeclaration += n.getString(2) + ";";
            s1.append(currentMethodDeclaration + "\n");
        } else {
            if (n.getNode(0).size() > 0) {
                currentMethodDeclaration += n.getNode(0).getString(0) + " ";
            }
            currentMethodDeclaration += n.getString(1) + " ";
            currentMethodDeclaration += "(*" + n.getString(2) + ")";

            // Case equals
            if (n.getString(2).equals("equals")) {
                currentMethodDeclaration += "(" + summary.currentClassName + "," + n.getString(3) + ");";
            } else {
                currentMethodDeclaration += "(";
                boolean gateMatch = false;
                for (MethodImplementation currMethod : summary.currentMethodList) {
                    if (currMethod.name.equals(n.getString(2))) {
                        if (currMethod.parameters.size() != 0) {
                            gateMatch = true;
                            for (ParameterImplementation param : currMethod.parameters) {
                                currentMethodDeclaration += param.type;
                            }
                        }
                    }
                }
                if (!gateMatch) {
                    currentMethodDeclaration += n.getString(3);

                }
                currentMethodDeclaration += ");";
            }

            s1.append(currentMethodDeclaration + "\n");
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


    public printHeaderFile(Runtime runtime, AstTraversal.AstTraversalSummary summaryTraversal) {
        this.runtime = runtime;
        this.summaryTraversal = summaryTraversal;
    }

    static class headerFileSummary {
        String fowardDeclarations = "";
        String typeDefs = "";
        String structs = "";
        String currentClassName = "";
        String headerGuard = "";

        String namespace = "";
        String closeNameSpace = "";

        String usingNamespace = "";

        ClassImplementation currentClass;
        ArrayList<MethodImplementation> currentMethodList;
        ArrayList<FieldDeclaration> currentFieldDeclarationList;
        ArrayList<ConstructorImplementation> currentConstructorList;

    }


    public headerFileSummary getSummary(GNode n) {

        StringBuilder s = new StringBuilder();

        s.append("#pragma once\n");
        s.append("#include <iostream>\n");
        s.append("#include \"java_lang.h\"\n\n");
        summary.headerGuard += s;

        summary.usingNamespace = "using namespace java::lang;\n\n";

        s = new StringBuilder();
        // all classes in the same namespace?
        String namespace;
        namespace = n.getNode(0).getNode(0).getString(1);
        int namespaceSize = 0;
        for (Object o : namespace.split("_")) {
            for (int i = 0; i < namespaceSize; i++) {
                s.append("\t");
            }
            s.append("namespace " + o + " {\n");
            namespaceSize++;
        }
        s.append("\n\n");
        summary.namespace = s.toString();

        s = new StringBuilder();
        // ForwardDeclarations
        for (Object classNode : n) {
            GNode currentNode = (GNode) classNode;
            if (!currentNode.getName().contains("Test")) {
                String className = currentNode.getName();
                s.append("\tstruct __" + className + ";\n");
                s.append("\tstruct __" + className + "_VT;\n");
            }
        }
        s.append("\n");
        summary.fowardDeclarations = s.toString();

        s = new StringBuilder();
        // TypeDefs
        for (Object classNode : n) {
            GNode currentNode = (GNode) classNode;
            if (!currentNode.getName().contains("Test")) {
                String className = currentNode.getName();
                s.append("\ttypedef __" + className + "* " + className + ";\n");
            }
        }
        s.append("\n");
        summary.typeDefs = s.toString();

        for (Object classNode : n) {
            // TODO:
            GNode currentNode = (GNode) classNode;
            if (!currentNode.getName().contains("Test")) {
                s1 = new StringBuilder();
                super.dispatch(currentNode.getNode(0));
                summary.structs += s1.toString();
            }
        }


        // appending the namespace size
        s = new StringBuilder();
        s.append("\n\n");
        while (namespaceSize > 0) {
            for (int i = 0; i < namespaceSize - 1; i++) {
                s.append("\t");
            }
            s.append("}\n");
            namespaceSize--;
        }

        summary.closeNameSpace += s;

        return summary;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 21; i++) {
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

            out.println(test);
            GNode node = (GNode) LoadFileImplementations.loadTestFile(test);
            AstTraversal visitorTraversal = new AstTraversal(LoadFileImplementations.newRuntime());
            AstTraversal.AstTraversalSummary summaryTraversal = visitorTraversal.getTraversal(node);
            GNode parentNode = AstC.cAst(summaryTraversal);

            try {
                PrintWriter printerHeader;

                File header;
                File output;
                File main;

                // printing the header file
                printHeaderFile visitor = new printHeaderFile(LoadFileImplementations.newRuntime(), summaryTraversal);
                printHeaderFile.headerFileSummary summary = visitor.getSummary(parentNode);
                String headerFile = "";
                headerFile += summary.headerGuard + summary.usingNamespace + summary.namespace;
                headerFile += summary.fowardDeclarations + summary.typeDefs + summary.structs + summary.closeNameSpace;

                header = new File("testOutputs/printHeaderOutputs", test2);
                header.createNewFile();
                printerHeader = new PrintWriter(header);
                printerHeader.println(headerFile);
                printerHeader.flush();
                printerHeader.close();
                out.println("header " + i + "printed\n");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

