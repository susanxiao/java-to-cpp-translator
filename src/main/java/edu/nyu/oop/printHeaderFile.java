package edu.nyu.oop;

import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.util.Runtime;
import java.util.ArrayList;
import static java.lang.System.out;
import xtc.tree.Visitor;
import java.lang.*;


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
    StringBuilder s1 = new StringBuilder();

    // visitXXX methods
    public void visitHeaderDeclaration(GNode n) {
        String className = n.getString(0);
        summary.currentClassName = className;
        s1.append("struct __" + className + "\n{\n");
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
        String currentFieldDeclaration = "";
        if (n instanceof Node) {
            if (n.getNode(0).size() > 0) {
                currentFieldDeclaration += n.getNode(0).getString(0) + " ";
            }
        }
        currentFieldDeclaration += n.getString(1) + " ";
        currentFieldDeclaration += n.getString(2) + " ";
        if (n.getNode(3).size() > 0) {
            for (Object o : n.getNode(3)) {
                currentFieldDeclaration += o.toString() + " ";
            }
        }
        s1.append(currentFieldDeclaration + "\n\n");
    }

    public void visitDataLayoutMethodDeclaration(GNode n) {
        String currentMethodDeclaration = "";
        if (n.getNode(0).size() > 0) {
            currentMethodDeclaration += n.getNode(0).getString(0) + " ";
        }
        currentMethodDeclaration += n.getString(1) + " ";
        currentMethodDeclaration += n.getString(2) + "(";
        currentMethodDeclaration += n.getString(3) + " ";
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
        }
        currentMethodDeclaration += ");";
        s1.append(currentMethodDeclaration + "\n");
    }

    public void visitVTable(GNode n) {
        s1.append("};\n\nstruct __" + summary.currentClassName + "_VT\n{\n\n");
        for (Object o : n) {
            if (o instanceof Node) {
                dispatch((GNode) o);
            }
        }
        // vtable constructor
        // TODO: Inheritance
        s1.append("\n__" + summary.currentClassName + "_VT()\n");
        s1.append(": __isa(__" + summary.currentClassName + "::" + "__class(),\n");
        int size = n.size();
        for (Object o : n) {
            String vTableMethod = "";
            GNode currentMethod = (GNode) o;
            if (currentMethod.getString(2).equals("__isa")) {
                continue;
            } else if (currentMethod.getString(2).equals("getClass")) {
                vTableMethod += "   getClass((Class(*)(" + summary.currentClassName + ")) &__Object::getClass)";
            } else {
                vTableMethod += "   " + currentMethod.getString(2);
                vTableMethod += "(&__::" + currentMethod.getString(2) + ")";
            }
            if (size > 1) {
                size--;
                vTableMethod += ",\n";
            }
            s1.append(vTableMethod);
        }
        s1.append("{}\n\n");
        s1.append("};\n");
    }

    public void visitVTableMethodDeclaration(GNode n) {
        String currentMethodDeclaration = "";
        if (n.getString(2).equals("__isa")) {
            currentMethodDeclaration += n.getString(1) + " ";
            currentMethodDeclaration += n.getString(2) + ";";
        } else {
            if (n.getNode(0).size() > 0) {
                currentMethodDeclaration += n.getNode(0).getString(0) + " ";
            }
            currentMethodDeclaration += n.getString(1) + " ";
            currentMethodDeclaration += "(*" + n.getString(2) + ")";
            currentMethodDeclaration += "(" + n.getString(3) + ");";
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


    public printHeaderFile(Runtime runtime) {
        this.runtime = runtime;
    }

    static class headerFileSummary {
        String fowardDeclarations = "";
        String typeDefs = "";
        String structs = "";
        String currentClassName = "";
        String headerGuard = "";
        String namespace = "";
        ArrayList<String> packages = new ArrayList<String>();

    }

    public headerFileSummary getSummary(GNode n) {
        StringBuilder s = new StringBuilder();

        s.append("#pragma once\n");
        s.append("#include <iostream>\n");
        s.append("#include \"java_lang.h\"\n\n\n");

        summary.headerGuard += s;

        s = new StringBuilder();
        // all classes in the same namespace?
        String namespace = "";
        namespace = n.getNode(0).getNode(0).getString(1);
        int namespaceSize = 0;
        for(Object o : namespace.split("_")){
            namespaceSize++;
            s.append("namespace " + o + " {\n");
        }
        s.append("\n\n");
        summary.namespace = s.toString();

        s = new StringBuilder();
        // ForwardDeclarations
        for (Object classNode : n) {
            GNode currentNode = (GNode) classNode;
            if (!currentNode.getName().contains("Test")) {
                String className = currentNode.getName();
                s.append("struct __" + className + ";\n");
                s.append("struct __" + className + "_VT;\n");
            }
        }
        summary.fowardDeclarations = s.toString();

        s = new StringBuilder();
        // TypeDefs
        for (Object classNode : n) {
            GNode currentNode = (GNode) classNode;
            if (!currentNode.getName().contains("Test")) {
                String className = currentNode.getName();
                s.append("typedef __*" + className + " " + className + ";\n");
            }
        }
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
        for(int i = 0; i < namespaceSize; i++){
            s.append("} \n");
        }
        summary.structs += s;

        return summary;
    }

    public static void main(String[] args) {
        GNode parentNode = AstC.C_AST("./src/test/java/inputs/test001/Test001.java");
        printHeaderFile visitor = new printHeaderFile(LoadFileImplementations.newRuntime());
        printHeaderFile.headerFileSummary summary = visitor.getSummary(parentNode);

        String headerFile = "";
        headerFile += summary.headerGuard + summary.namespace;
        headerFile += summary.fowardDeclarations + summary.typeDefs + summary.structs;

        out.println(headerFile);


    }

}

