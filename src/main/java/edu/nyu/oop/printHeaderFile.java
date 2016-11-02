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

import java.io.IOException;


/**
 * Created by Garrett on 10/21/16.
 */


public class printHeaderFile extends Visitor {

    //TODO: write in constructors
    //TODO: fix ordering of VTable struct

    //TODO: if time, collect top fields together

    private printHeaderFile.headerFileSummary summary = new headerFileSummary();
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Runtime runtime;
    private AstTraversal.AstTraversalSummary summaryTraversal;

    // visitXXX methods
    public void visitHeaderDeclaration(GNode n) {
        String className = n.getString(0);

        if (className.startsWith("Test"))
            return;

        if (summary.scope == 0) {
            String[] namespaces = n.getString(1).split("_");

            for (String name : namespaces) {
                summary.addNamespace(name);
            }
        }

        summary.currentClass = summaryTraversal.classes.get(className);

        //Object and VTable
        summary.addLine("struct __"+className+";\n");
        summary.addLine("struct __"+className+"_VT;\n\n");

        //Typedef
        summary.addLine("typedef __"+className+"* "+className+";\n\n");

        //Construction
        summary.addLine("struct __"+className);
        summary.incScope();

        //VPointer
        summary.addLine("__"+className+"_VT* __vptr;\n");

        //Global Declarations
        if (summary.currentClass.declarations.size() > 0) {
            for (FieldDeclaration currentDeclaration : summary.currentClass.declarations) {
                String type = (currentDeclaration.staticType.equals("int") ? "int32_t" : currentDeclaration.staticType);
                summary.addLine(type+" "+currentDeclaration.variableName+";\n");
            }
            summary.code.append("\n");
        }

        //Constructors
        if (summary.currentClass.constructors.size() > 0) {
            for (ConstructorImplementation currentConstructor : summary.currentClass.constructors) {
                StringBuilder constructor = new StringBuilder("__"+className+"(");

                for (int i = 0; i < currentConstructor.parameters.size(); i++) {
                    if (i > 0)
                        constructor.append(", ");
                    ParameterImplementation currentParameter = currentConstructor.parameters.get(i);
                    String type = (currentParameter.type.equals("int") ? "int32_t" : currentParameter.type);
                    constructor.append(type+" "+currentParameter.name);
                }

                constructor.append(");\n");
                summary.addLine(constructor.toString());
            }
            summary.code.append("\n");
        } else {
            summary.addLine("__" + className + "();\n\n");
        }

        //Superclass declaration
        if (summary.currentClass.superClassName != null) {
            summary.addLine("__" + summary.currentClass.superClassName + " parent;\n\n");
        }

        //Class method that all Objects have
        summary.addLine("static Class __class();\n\n");

        //Methods that will be implemented in output.cpp
        for (MethodImplementation currentMethod : summary.currentClass.methods) {
            String type = (currentMethod.returnType.equals("int") ? "int32_t" : currentMethod.returnType);

            StringBuilder method = new StringBuilder("static "+type+" "+currentMethod.name+"("+summary.currentClass.name);
            for (ParameterImplementation currentParameter : currentMethod.parameters) {
                method.append(", "+currentParameter.type);
            }
            method.append(");\n");

            summary.addLine(method.toString());
        }

        summary.decScope();
        summary.code.append("\n");
        //End Construction

        //VTable construction
        summary.addLine("struct __"+className+"_VT");
        summary.incScope();

        for (Object o : n) {
            if (o instanceof Node) {
                GNode currentNode = GNode.cast(o);
                if (currentNode.getName().equals("DataLayout")) {
                    visitDataLayout(currentNode);
                }
            }
        }

        summary.decScope();
        //End VTable construction
    }

    public void visitDataLayout(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                //dispatch(GNode.cast(o));
                GNode currentNode = GNode.cast(o);
                if (currentNode.getName().equals("VTable"))
                    visitVTable(currentNode);
            }
        }
    }
/*
    public void visitFieldDeclaration(GNode n) {
        String currentFieldDeclaration = "\t";
        if (n instanceof Node) {
            if (n.getNode(0).size() > 0) {
                currentFieldDeclaration += n.getNode(0).getString(0) + " ";
            }
        }

        if (n.getString(1).equals("String")) {
            currentFieldDeclaration += "std::string ";
        } else {
            currentFieldDeclaration += n.getString(1) + " ";
        }

        currentFieldDeclaration += n.getString(2);
        if (n.getString(2).equals("__vptr")) {
            return;
        }
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
    }*/

    /*public void visitDataLayoutMethodDeclaration(GNode n) {
        for (MethodImplementation currMethod : summaryTraversal.classes.get(summary.currentClassName).methods) {
            String methodComparing = n.getString(2);
            if (currMethod.name.equals(methodComparing)) {
                String currentMethodDeclaration = "\t";
                // TODO: modifiers?
                currentMethodDeclaration += "static ";
                *//*
                if (n.getNode(0).size() > 0) {
                    currentMethodDeclaration += n.getNode(0).getString(0) + " ";
                }*//*

                currentMethodDeclaration += n.getString(1) + " ";
                currentMethodDeclaration += n.getString(2) + "(";
                if (!n.getString(2).equals("__class")) {
                    if (n.getString(2).equals("equals")) {
                        currentMethodDeclaration += summary.currentClassName + ",";
                    }
                    if (currMethod.parameters.size() > 0) {
                        if (n.getString(2).startsWith("set")) {
                            currentMethodDeclaration += summary.currentClassName + ",";
                        }
                        for (ParameterImplementation param : currMethod.parameters) {
                            currentMethodDeclaration += param.type;
                        }
                    } else {
                        currentMethodDeclaration += n.getString(3);
                    }
                }

                currentMethodDeclaration += ");";
                s1.append(currentMethodDeclaration + "\n");
            }
        }

    }*/

    public void visitVTable(GNode n) {

        for (Object o : n) {
            if (o instanceof Node) {
                visitVTableMethodDeclaration(GNode.cast(o));
            }
        }
        summary.code.append("\n");

        vTableConstructor(n);

        /*
        // need to add getMethod function pointers
        if (summary.currentFieldDeclarationList.size() != 0) {
            for (FieldDeclaration currentDeclaration : summary.currentFieldDeclarationList) {
                String type;
                if (currentDeclaration.staticType.equals("int")) {
                    type = "int32_t";
                } else {
                    type = currentDeclaration.staticType;
                }
                s1.append("\t" + type + " (*get" + currentDeclaration.variableName + ")("
                        + summary.currentClassName + ")" + ";\n");
            }
        }
        */
        /*
        // adding methods that are from superClass to make inheritance work
        String superClassName = summaryTraversal.classes.get(summary.currentClassName).superClassName;
        ClassImplementation superClass = summaryTraversal.classes.get(superClassName);
        if(superClass != null){
            ArrayList<MethodImplementation> methodsSuper = superClass.methods;
            for(MethodImplementation m : methodsSuper){
                if(summary.currentClass.methods != null){
                    if(!(summary.currentClass.methods.contains(m))){
                        s1.append("\t" + m.returnType + " (*" + m.name + ")("
                                + summary.currentClassName + ")" + ";\n");
                    }
                }
            }
        }
        */

//        if(!(summary.methodNames.contains("toString"))){
//            s1.append("\tString (*toString)(" + summary.currentClassName + ");\n");
//        }
    }

    public void vTableConstructor(GNode n) {
        summary.addLine("__" + summary.currentClass.name + "_VT()\n");
        summary.addLine(": __isa(__" + summary.currentClass.name + "::" + "__class()),\n");
        int size = n.size();

        //superClass methods
        //TODO: expand this for all superclassing
        //order matters!!!

        ArrayList<MethodImplementation> clonedMethods = new ArrayList<>(summary.currentClass.methods);

        MethodImplementation currentMethod = summary.currentClass.findMethod("hashcode");
        if (currentMethod == null)
            summary.addLine("hashcode((int32_t(*)("+summary.currentClass.name+"))&__Object::hashcode),\n");
        else {
            summary.addLine("hashcode(&__"+summary.currentClass.name+"::hashcode,\n");
            removeMethod(clonedMethods, currentMethod.name);
        }

        currentMethod = summary.currentClass.findMethod("equals");
        if (currentMethod == null)
            summary.addLine("equals((bool(*)("+summary.currentClass.name+", Object))&__Object::equals),\n");
        else {
            summary.addLine("equals(&__"+summary.currentClass.name+"::equals,\n");
            removeMethod(clonedMethods, currentMethod.name);
        }

        currentMethod = summary.currentClass.findMethod("getClass");
        if (currentMethod == null)
            summary.addLine("getClass((Class(*)("+summary.currentClass.name+"))&__Object::getClass),\n");
        else {
            summary.addLine("getClass(&__"+summary.currentClass.name+"::getClass,\n");
            removeMethod(clonedMethods, currentMethod.name);
        }

        currentMethod = summary.currentClass.findMethod("toString");
        if (currentMethod == null)
            summary.addLine("toString((String(*)("+summary.currentClass.name+"))&__Object::toString)");
        else {
            summary.addLine("toString(&__"+summary.currentClass.name+"::toString)");
            removeMethod(clonedMethods, currentMethod.name);
        }


        ClassImplementation currentClass = summary.currentClass;
        ClassImplementation superClass = currentClass.superClass;
        while (superClass != null) {
            //TODO: fix this
            //for each superclass method, check if currentclass overrides
            //if no (currentMethod is null)
                //add superclass cast
                //remove method from clonedSuperclassMethods
            //if yes
                //add subclass version
                //remove from clonedMethods

            //then add all remaining in clonedMethods
            //set superclass = superclass.superclass
            //set currentclass = superclass
            //set clonedMethods = superClassMethods
            //set clonedsuperClassMethods = new (superClass.getmethods);

            //repeat until superclass = null;
            //add remaining clonedmethods

            currentMethod = summary.currentClass.findMethod("toString");
            if (currentMethod == null)
                summary.addLine("toString((String(*)("+summary.currentClass.name+"))&__Object::toString)");
            else {
                summary.addLine("toString(&__"+summary.currentClass.name+"::toString)");
                removeMethod(clonedMethods, currentMethod.name);
            }

            superClass = superClass.superClass;
        }

        //Non superclass methods

        for (int i = 0; i < clonedMethods.size(); i++) {
            summary.code.append(",\n");
            currentMethod = clonedMethods.get(i);
            summary.addLine(currentMethod.name+"(&__"+summary.currentClass.name+"::"+currentMethod.name+")");
        }
        summary.code.append("\n");

        summary.addLine("{}\n");

        /*
        // need to add the getMethods for the class members
        if (summary.currentFieldDeclarationList.size() != 0) {
            x.append(",\n");
            int declarationSize = summary.currentFieldDeclarationList.size();
            for (FieldDeclaration currentDeclaration : summary.currentFieldDeclarationList) {
                String type = currentDeclaration.staticType.equals("int") ? "int32_t" : currentDeclaration.staticType;
                declarationSize--;
                String methodString = "\t\t" + "get" + currentDeclaration.variableName + "(&__" + summary.currentClassName + "::get" + currentDeclaration.variableName + ")";
                if(declarationSize > 0) {
                    x.append(methodString + "\n");
                }else{
                    x.append(methodString);
                }
            }
        }else{
            x.append("\n");
        }
        */
        /*
        // adding methods that are from superClass to make inheritance work
        String superClassName = summaryTraversal.classes.get(summary.currentClassName).superClassName;
        ClassImplementation superClass = summaryTraversal.classes.get(superClassName);
        if(superClass != null){
            x.append(",\n");
            ArrayList<MethodImplementation> methodsSuper = superClass.methods;
            for(MethodImplementation m : methodsSuper){
                if(summary.currentClass.methods != null){
                    if(!(summary.currentClass.methods.contains(m))){
                        String methodString = "\t\t" + m.name + "((" + m.returnType + "(*)(" + summary.currentClassName + "))";
                        methodString += " &__" + superClassName + "::" + m.name+ ")";
                        x.append(methodString);
                    }
                }
            }
        }
        */
    }

    public void visitVTableMethodDeclaration(GNode n) {
        StringBuilder method = new StringBuilder();

        //Modifiers
        Node modifiers = n.getNode(0);
        for (int i = 0; i < modifiers.size(); i++) {
            method.append(modifiers.getString(i)+" ");
        }

        //return type
        method.append(n.getString(1)+" ");

        //name
        String name = n.getString(2);
        if (name.equals("__isa"))
            method.append(name+";\n");
        else {
            method.append("(*"+name+")");

            //parameters
            Node parameters = n.getNode(4);

            method.append("("+summary.currentClass.name);
            for (int i = 0; i < parameters.size(); i++) {
                method.append(", "+parameters.getString(i).replace("__", ""));
            }
            method.append(");\n");
        }

        summary.addLine(method.toString());
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
        StringBuilder code;
        int scope;
        ClassImplementation currentClass;

        public headerFileSummary() {
            code = new StringBuilder(
                      "#pragma once\n"
                    + "#include <iostream>\n"
                    + "#include \"java_lang.h\"\n\n"
                    + "using namespace java::lang;\n\n"
            );
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


    public headerFileSummary getSummary(GNode n) {
        visit(n);

        while (summary.scope > 0) {
            summary.closeNamespace();
        }
        return summary;
    }

    public static void removeMethod(ArrayList<MethodImplementation> methods, String name) {
        for (int i = 0; i < methods.size(); i++) {
            MethodImplementation currentMethod = methods.get(i);
            if (currentMethod.name.equals(name)) {
                methods.remove(i);
                return;
            }
        }
    }

    public static void main(String[] args) {

        //TO RUN: run-main printHeaderFile ***
        // *** a number 0-20, or nothing to run all test cases
        int start = 0;
        int end = 20;

        if (args.length > 0) {
            int value = LoadFileImplementations.getInteger(args[0]);
            if (value > 0) {
                start = value;
                end = value;
            }
        }

        for (int i = start; i <= end; i++) {
            String test = String.format("./src/test/java/inputs/test%03d/Test%03d.java", i, i);

            out.println(test);
            GNode node = (GNode) LoadFileImplementations.loadTestFile(test);
            AstTraversal visitorTraversal = new AstTraversal(LoadFileImplementations.newRuntime());
            AstTraversal.AstTraversalSummary summaryTraversal = visitorTraversal.getTraversal(node);
            GNode parentNode = HeaderAst.ConstructHeaderAst(summaryTraversal);
            LoadFileImplementations.prettyPrintAst(parentNode);

            try {
                PrintWriter printerHeader;

                File header;
                File output;
                File main;

                // printing the header file
                printHeaderFile visitor = new printHeaderFile(LoadFileImplementations.newRuntime(), summaryTraversal);
                printHeaderFile.headerFileSummary summary = visitor.getSummary(parentNode);

                out.println(summary.code.toString());

                header = new File("testOutputs/printHeaderOutputs/v2", String.format("Test%03d", i));
                header.getParentFile().mkdirs();
                header.createNewFile();
                printerHeader = new PrintWriter(header);
                printerHeader.println(summary.code.toString());
                printerHeader.flush();
                printerHeader.close();
                out.println("header " + i + " printed\n");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

