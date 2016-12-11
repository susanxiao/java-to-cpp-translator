package edu.nyu.oop;

import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.util.Runtime;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

import static java.lang.System.out;

import xtc.tree.Visitor;

import java.lang.*;

import java.io.IOException;


/**
 * Created by Garrett on 10/21/16.
 */


public class PrintHeaderFile extends Visitor {
    private PrintHeaderFile.headerFileSummary summary = new headerFileSummary();
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Runtime runtime;
    private AstTraversal.AstTraversalSummary summaryTraversal;

    //check overloading
    public ArrayList<ArrayList<String>> methodNames;
    public ArrayList<ArrayList<String>> methodIsOverloaded;

    // visitXXX methods
    public void visitHeaderDeclaration(GNode n) {
        String className = n.getString(0);

        if (!className.contains("Test")) {
            summary.classCount++;
        }

        if (summary.scope == 0) {
            String[] namespaces = n.getString(1).split("_");

            for (String name : namespaces) {
                summary.addNamespace(name);
            }

            summary.code.append("%s\n%s\n"); // will be replaced by forwardDeclaration and typeDef
        } else {
            summary.code.append("\n");
        }

        if (className.startsWith("Test"))
            return;

        summary.currentClass = summaryTraversal.classes.get(className);

        //Object and VTable Forward Declaration
        summary.addForwardDeclaration("struct __" + className + ";\n");
        summary.addForwardDeclaration("struct __" + className + "_VT;\n\n");

        //Typedef
        summary.addTypeDef("typedef __" + className + "* " + className + ";\n");

        //Construction
        summary.addLine("struct __" + className);
        summary.incScope();
        //VPointer
        summary.addLine("__" + className + "_VT* __vptr;\n");

        //Global Declarations
        TreeMap<String, String> declarationsMap = new TreeMap<>(); //sorted based key
        ClassImplementation currentClass = summary.currentClass;
        int declarationsCounter = 0;
        while (currentClass != null) {
            for (FieldDeclaration currentDeclaration : currentClass.declarations) {
                declarationsCounter += 1;
                String type = (currentDeclaration.staticType.equals("int") ? "int32_t" : currentDeclaration.staticType);
                if (!currentDeclaration.isStatic)
                    declarationsMap.put(currentDeclaration.variableName, type + " " + currentDeclaration.variableName);
                else if (currentDeclaration.literalValue == null)
                    declarationsMap.put(currentDeclaration.variableName, "static " + type + " " + currentDeclaration.variableName);
                else
                    declarationsMap.put(currentDeclaration.variableName, "static const " + type + " " + currentDeclaration.variableName + " = " + currentDeclaration.literalValue);
            }
            currentClass = currentClass.superClass;
            if(currentClass != null) {
                declarationsMap.put("parentPointer", "__" + currentClass.name + " parent");
            }
            break;
        }

        summary.classDeclarationCounts.put(summary.currentClass.name, declarationsCounter);

        Collection<String> declarations = declarationsMap.values();

        for (String s : declarations) {
            summary.addLine(s + ";\n");
        }
        summary.code.append("\n");


        //Constructors
        if (summary.currentClass.constructors.size() > 0) {
            for (ConstructorImplementation currentConstructor : summary.currentClass.constructors) {
                StringBuilder constructor = new StringBuilder("__" + className + "(");

                for (int i = 0; i < currentConstructor.parameters.size(); i++) {
                    if (i > 0)
                        constructor.append(", ");
                    ParameterImplementation currentParameter = currentConstructor.parameters.get(i);
                    String type = (currentParameter.type.equals("int") ? "int32_t" : currentParameter.type);
                    constructor.append(type + " " + currentParameter.name);
                }

                constructor.append(");\n");
                summary.addLine(constructor.toString());
            }
            summary.code.append("\n");
        } else {
            summary.addLine("__" + className + "();\n\n");
        }

        //Class method that all Objects have
        summary.addLine("static Class __class();\n\n");

        //Vtable declaration
        summary.addLine("static __" + summary.currentClass.name + "_VT __vtable;\n\n");

        //Methods that will be implemented in output.cpp

        //check If Methods Are Overloaded
        ArrayList<String> a = new ArrayList<String>();
        //String[] a = new String[summary.currentClass.methods.size()];
        a.add(summary.currentClass.name);
        for (int i=0; i< summary.currentClass.methods.size(); i++){
            MethodImplementation currentMethod = summary.currentClass.methods.get(i);
            String name = currentMethod.name;
            //a[i]=name;
            a.add(name);
        }
        //System.out.println("a araylsit " + a.toString());
        //System.out.println(methodNames.toString());
        //this.methodNames.add(a);
        summaryTraversal.allMethods_checkMethodOverloading.add(a);
        //System.out.println(summaryTraversal.allMethods_checkMethodOverloading.toString());

        //boolean[] isOverloaded = new boolean[summary.currentClass.methods.size()];
        ArrayList<String> isOverloaded = new ArrayList<String>();
        isOverloaded.add(summary.currentClass.name);
        for (int i=0; i< summary.currentClass.methods.size(); i++){
            //boolean isAOverloadedMethod=false;
            String isAOverloadedMethod = "false";
            int howManyTimesItAppears=0;
            for (int j=0; j< summary.currentClass.methods.size(); j++){
                if(summary.currentClass.methods.get(i).name.equals(summary.currentClass.methods.get(j).name)){
                    howManyTimesItAppears++;
                }
            }
            if (howManyTimesItAppears>1){
                isAOverloadedMethod="true";
            }
            //isOverloaded[i]=isAOverloadedMethod;
            isOverloaded.add(isAOverloadedMethod);
        }
        //System.out.println("isOverloaded.  " + isOverloaded.toString());
        summaryTraversal.isOverLoaded.add(isOverloaded);

        int classMethodCount = 0;
        //System.out.println( "size "+summary.currentClass.methods.size());
        for (MethodImplementation currentMethod : summary.currentClass.methods) {
            //check if the method is overloaded
            //System.out.println( currentMethod.name.toString());
            //int index = Arrays.asList(a).indexOf(currentMethod.name.toString());
            int index = a.indexOf(currentMethod.name.toString());

            classMethodCount += 1;
            String type = (currentMethod.returnType.equals("int") ? "int32_t" : currentMethod.returnType);

            StringBuilder method = new StringBuilder("static " + type + " " + currentMethod.name);
            //System.out.println( "error?");
            //System.out.println( "intdes: " + index);
            if(isOverloaded.get(index).equals("true")){
                //System.out.println( "error?");
                method.append("_");
                //System.out.println("is overloaded -edit");
                ArrayList<String> arr = new ArrayList<String>();
                arr.add(summary.currentClass.name.toString());
                //System.out.println("currentClass.name.toString()" + summary.currentClass.name.toString());

                String newNameForOverloaded=currentMethod.name+"_";
                for(int i=0; i<currentMethod.parameters.size();i++){
                    //System.out.println("size?");
                    //System.out.println(currentMethod.parameters.get(i));
                    String paramType="";
                    for(char c: currentMethod.parameters.get(i).toString().toCharArray()){
                        if (c != ' '){
                            paramType += c;
                        }
                        else{
                            break;
                        }
                    }
                    method.append(paramType+"fix");
                    newNameForOverloaded+=paramType;
                    System.out.println("newNameForOverloaded" + newNameForOverloaded);
                    arr.add(newNameForOverloaded);
                }
                System.out.println("summaryTraversal.overloadedMethodNames.add(arr); " + summaryTraversal.overloadedMethodNames.toString());
                summaryTraversal.overloadedMethodNames.add(arr);
                System.out.println("summaryTraversal.overloadedMethodNames.add(arr); " + summaryTraversal.overloadedMethodNames.toString());

            }

            //System.out.println( "error?");
            if (!currentMethod.isStatic) {
                method.append("(" + summary.currentClass.name);

                for (ParameterImplementation currentParameter : currentMethod.parameters) {
                    method.append(", " + currentParameter.type);
                }
                method.append(");\n");
            } else {
                method.append("(");
                for (int i = 0; i < currentMethod.parameters.size(); i++) {
                    ParameterImplementation currentParameter = currentMethod.parameters.get(i);
                    method.append(currentParameter.type);
                    if (i < currentMethod.parameters.size() - 1)
                        method.append(", ");
                }
                method.append(");\n");
            }

            summary.addLine(method.toString());
        }

        // adding the number of methods that are going to be implemented by the
        // the class so that we can perform unit testing
        summary.classMethodCounts.put(summary.currentClass.name, classMethodCount);

        summary.decScope();
        summary.code.append("\n");
        //End Construction

        //VTable construction
        summary.addLine("struct __" + className + "_VT");
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

    public void visitVTable(GNode n) {

        //NOTE: this does not handle overloading
        //ensure that the order is the same

        LinkedHashMap<String, String> vMethods = new LinkedHashMap<>();
        LinkedHashMap<String, String> vConstructor = new LinkedHashMap<>();

        //populate the maps

        //Non superclass methods
        for (int i = summary.currentClass.methods.size() - 1; i >= 0; i--) {
            MethodImplementation currentMethod = summary.currentClass.methods.get(i);
            String currentMethodName = currentMethod.name;
            if (!currentMethod.isStatic) {
                if (!(currentMethodName.startsWith("method"))) {
                    switch (currentMethodName) {
                        case "toString":
                            break;
                        case "hashCode":
                            break;
                        case "equals":
                            break;
                        case "getClass":
                            break;
                        default:
                            currentMethodName = "method" + currentMethodName.substring(0, 1).toUpperCase() + currentMethodName.substring(1);

                    }
                }
                //Todo: summary.currentClass.methods only stores ONE methodM (before overloaded methods names are chaneged)
                //check overloaded
                String methodIsOverloaded="don't know yet";
                System.out.println("checek equals");
                System.out.println(summaryTraversal.allMethods_checkMethodOverloading.toString());
                ArrayList<String> currentClass_methods = new ArrayList<String>();
                for(int j=0; j<summaryTraversal.allMethods_checkMethodOverloading.size();j++){
                    ArrayList<String> a=  summaryTraversal.allMethods_checkMethodOverloading.get(j);
                    System.out.println(a.get(0));
                    System.out.println(summary.currentClass.name);
                    if (a.get(0).equals(summary.currentClass.name)){
                        currentClass_methods=a;
                        System.out.println("search this part");
                        System.out.println(a.toString());
                        int index = a.indexOf(currentMethodName);
                        methodIsOverloaded = summaryTraversal.isOverLoaded.get(j).get(index);
                    }
                }

                System.out.println("methodIsOverloaded.equals:" + methodIsOverloaded);
                //System.out.println("summaryTraversal.overloadedMethodNames." + summaryTraversal.overloadedMethodNames.toString());
                if(methodIsOverloaded.equals("true")){
                    String overLoadedName="";
                    //System.out.println(currentClass_methods.toString());
                    //System.out.println();
                    for(int j=0; j<summaryTraversal.overloadedMethodNames.size(); j++){//6
                        if (summaryTraversal.overloadedMethodNames.get(j).get(0).equals(summary.currentClass.name)){
                            //System.out.println("yes");
                            //System.out.println(summaryTraversal.overloadedMethodNames.get(j).toString());
                            int lastIndex=summaryTraversal.overloadedMethodNames.get(j).size()-1;
                            //System.out.println(summaryTraversal.overloadedMethodNames.get(j).get(lastIndex));
                            //System.out.println("comepare with: "+currentMethodName);
                            if(summaryTraversal.overloadedMethodNames.get(j).get(lastIndex).contains(currentMethodName)){
                                overLoadedName = summaryTraversal.overloadedMethodNames.get(j).get(lastIndex);
                                //System.out.println("**overLoadedName)" + overLoadedName);
                                //vConstructor.
                                vConstructor.put(overLoadedName, overLoadedName  + " (&__" + summary.currentClass.name + "::" + overLoadedName + ")");
                                //System.out.println("vConstructor: " + vConstructor.toString());
                            }
                        }
                        //System.out.println();
                    }
                    //System.out.println();

                    }else {
                    //vConstructor.put(currentMethodName, currentMethodName + summary.currentClass.name + " (&__" + summary.currentClass.name + "::" + currentMethodName + ")");
                    vConstructor.put(currentMethodName, currentMethodName + " (&__" + summary.currentClass.name + "::" + currentMethodName + ")");

                }
                StringBuilder method = new StringBuilder((currentMethod.returnType.equals("int") ? "int32_t" : currentMethod.returnType) + " (*" + currentMethodName + ")(%s");
                for (ParameterImplementation p : currentMethod.parameters)
                    method.append(", " + p.type);
                method.append(");\n");
                vMethods.put(currentMethodName, method.toString());
            }
        }

        //superclass methods
        ClassImplementation superClass = summary.currentClass.superClass;
        while (superClass != null) {
            for (int i = superClass.methods.size() - 1; i >= 0; i--) {
                MethodImplementation currentMethod = superClass.methods.get(i);
                String currentMethodName = currentMethod.name;
                if (!(currentMethodName.startsWith("method"))) {
                    switch (currentMethodName) {
                    case "toString":
                        break;
                    case "hashCode":
                        break;
                    case "equals":
                        break;
                    case "getClass":
                        break;
                    default:
                        currentMethodName = "method" + currentMethodName.substring(0, 1).toUpperCase() + currentMethodName.substring(1);
                    }
                }
                if (!vConstructor.containsKey(currentMethodName)) { //if it already exists, it is the overwriting method
                    StringBuilder method = new StringBuilder((currentMethod.returnType.equals("int") ? "int32_t" : currentMethod.returnType) + " (*" + currentMethodName + ")");

                    StringBuilder parameters = new StringBuilder("(%s");
                    for (ParameterImplementation p : currentMethod.parameters)
                        parameters.append(", " + p.type);
                    parameters.append(")");

                    method.append(parameters.toString() + ";\n");
                    vMethods.put(currentMethodName, method.toString());

                    String paramString = String.format(parameters.toString(), summary.currentClass.name);
                    vConstructor.put(currentMethodName,
                                     currentMethodName + "((" + (currentMethod.returnType.equals("int") ? "int32_t" : currentMethod.returnType) + "(*)" + paramString + ")&__" + superClass.name + "::" + currentMethodName + ")");

                } else { //if it already exists, we need to move its location to where the superclass holds it
                    String key = currentMethod.name;
                    String constructorValue = vConstructor.remove(key);
                    String methodValue = vMethods.remove(key);
                    vConstructor.put(key, constructorValue);
                    vMethods.put(key, methodValue);
                }
            }
            superClass = superClass.superClass;
        }

        //toString
        if (!vConstructor.containsKey("toString")) {
            vConstructor.put("toString", "toString((String(*)(" + summary.currentClass.name + "))&__Object::toString)");
            vMethods.put("toString", "String (*toString)(%s);\n");
        } else {
            String key = "toString";
            String constructorValue = vConstructor.remove(key);
            String methodValue = vMethods.remove(key);
            vConstructor.put(key, constructorValue);
            vMethods.put(key, methodValue);
        }

        //getClass
        if (!vConstructor.containsKey("getClass")) {
            vConstructor.put("getClass", "getClass((Class(*)(" + summary.currentClass.name + "))&__Object::getClass)");
            vMethods.put("getClass", "Class (*getClass)(%s);\n");
        } else {
            String key = "getClass";
            String constructorValue = vConstructor.remove(key);
            String methodValue = vMethods.remove(key);
            vConstructor.put(key, constructorValue);
            vMethods.put(key, methodValue);
        }

        //equals
        if (!vConstructor.containsKey("equals")) {
            vConstructor.put("equals", "equals((bool(*)(" + summary.currentClass.name + ", Object))&__Object::equals)");
            vMethods.put("equals", "bool (*equals)(%s, Object);\n");
        } else {
            String key = "equals";
            String constructorValue = vConstructor.remove(key);
            String methodValue = vMethods.remove(key);
            vConstructor.put(key, constructorValue);
            vMethods.put(key, methodValue);
        }

        //hashCode
        if (!vConstructor.containsKey("hashCode")) {
            vConstructor.put("hashCode", "hashCode((int32_t(*)(" + summary.currentClass.name + "))&__Object::hashCode)");
            vMethods.put("hashCode", "int32_t (*hashCode)(%s);\n");
        } else {
            String key = "hashCode";
            String constructorValue = vConstructor.remove(key);
            String methodValue = vMethods.remove(key);
            vConstructor.put(key, constructorValue);
            vMethods.put(key, methodValue);
        }

        //vtable methods
        summary.addLine("Class __isa;\n\n");
        ArrayList<String> vMethodValues = new ArrayList<>(vMethods.values());
        for (int i = vMethodValues.size() - 1; i >= 0; i--) {
            String s = vMethodValues.get(i);
            summary.addLine(String.format(s, summary.currentClass.name));
        }
        summary.code.append("\n");


        //vtable constructor
        summary.addLine("__" + summary.currentClass.name + "_VT()\n");
        summary.addLine(": __isa(__" + summary.currentClass.name + "::" + "__class())");

        ArrayList<String> vConstructorValues = new ArrayList<>(vConstructor.values());
        for (int i = vConstructorValues.size() - 1; i >= 0; i--) {
            String s = vConstructorValues.get(i);
            summary.code.append(",\n");
            summary.addLine(s);
        }

        summary.code.append("\n");

        summary.addLine("{}\n");
    }

    public void visitVTableMethodDeclaration(GNode n) {
        StringBuilder method = new StringBuilder();

        //Modifiers
        Node modifiers = n.getNode(0);
        for (int i = 0; i < modifiers.size(); i++) {
            method.append(modifiers.getString(i) + " ");
        }

        //return type
        method.append(n.getString(1) + " ");

        //name
        String name = n.getString(2);
        if (name.equals("__isa"))
            method.append(name + ";\n");
        else {
            method.append("(*" + name + ")");

            //parameters
            Node parameters = n.getNode(4);

            method.append("(" + summary.currentClass.name);
            for (int i = 0; i < parameters.size(); i++) {
                method.append(", " + parameters.getString(i).replace("__", ""));
            }
            method.append(");\n");
        }

        summary.addLine(method.toString());
    }

    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) {
                dispatch((Node) o);
            }
        }
    }


    public PrintHeaderFile(Runtime runtime, AstTraversal.AstTraversalSummary summaryTraversal) {
        this.runtime = runtime;
        this.summaryTraversal = summaryTraversal;
    }

    static class headerFileSummary {

        // testing information so that we can perform unit testing
        int classCount = 0;
        TreeMap<String, Integer> classMethodCounts = new TreeMap<>();
        TreeMap<String, Integer> classDeclarationCounts = new TreeMap<>();


        StringBuilder forwardDeclarations;
        StringBuilder typeDef;
        StringBuilder code;
        int scope;
        ClassImplementation currentClass;

        public headerFileSummary() {
            forwardDeclarations = new StringBuilder();
            typeDef = new StringBuilder();

            code = new StringBuilder(
                "#pragma once\n"
                + "#include <iostream>\n"
                + "#include \"java_lang.h\"\n\n"
                + "using namespace java::lang;\n\n"
            );
            scope = 0;
        }

        public void addForwardDeclaration(String line) {
            for (int i = 0; i < scope; i++)
                forwardDeclarations.append("\t");

            forwardDeclarations.append(line);
        }

        public void addTypeDef(String line) {
            for (int i = 0; i < scope; i++)
                typeDef.append("\t");

            typeDef.append(line);
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


    public headerFileSummary getSummary(GNode n) {
        visit(n);

        summary.code = new StringBuilder(String.format(summary.code.toString(), summary.forwardDeclarations.toString(), summary.typeDef.toString()));
        while (summary.scope > 0) {
            summary.closeNamespace();
        }

        return summary;
    }

    public static void main(String[] args) {

        //TO RUN: run-main edu.nyu.oop.PrintHeaderFile ***
        // *** a number 0-20, or nothing to run all test cases
        //NOTE: running a specific file will place it in the output folder.
        //      running all files will place it in testOutputs/translationOutputs

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

        for (int i = start; i <= end; i++) {
            String test = String.format("./src/test/java/inputs/test%03d/Test%03d.java", i, i);

            GNode node = (GNode) ImplementationUtil.loadTestFile(test);
            AstTraversal visitorTraversal = new AstTraversal(ImplementationUtil.newRuntime());
            AstTraversal.AstTraversalSummary summaryTraversal = visitorTraversal.getTraversal(node);
            //System.out.println("--------Before Mutation--------");
            //ImplementationUtil.prettyPrintAst(node);//Before mutated
            GNode parentNode = HeaderAst.getHeaderAst(summaryTraversal).parent;
            System.out.println("--------After Mutation----------");
            ImplementationUtil.prettyPrintAst(parentNode); //

            try {
                PrintWriter printerHeader;

                File header;

                // printing the header file
                PrintHeaderFile visitor = new PrintHeaderFile(ImplementationUtil.newRuntime(), summaryTraversal);
                PrintHeaderFile.headerFileSummary summary = visitor.getSummary(parentNode);

		String headerPath = String.format("./testOutputs/translationOutputs/test%03d/output.h", i);
                header = new File(headerPath);
                header.getParentFile().mkdirs();
                header.createNewFile();
                printerHeader = new PrintWriter(header);
                printerHeader.println(summary.code.toString());
                printerHeader.flush();
                printerHeader.close();
                out.println(summary.code.toString());
                out.println("header " + i + " printed\n");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

