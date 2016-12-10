package edu.nyu.oop;

import xtc.tree.GNode;

import java.util.*;

import static edu.nyu.oop.AstTraversal.*;

/**
 * Created by Garrett on 10/24/16.
 */
public class HeaderAst {

    static HeaderAstSummary getHeaderAst(AstTraversalSummary summary) {

        HeaderAstSummary constructionSummary = new HeaderAstSummary();
        constructionSummary = ConstructHeaderAst(summary, constructionSummary);
        return constructionSummary;
    }

    static class HeaderAstSummary {
        // we have to have the headerNode in the summary so that we are able to return it
        GNode parent = null;

        // testing information so that we can perform unit testing
        // when obtaining testing information the test class will not be tracked
        // this is because we do not include the test class inside of the .h file
        int numberClasses = 0;
        TreeMap<String, Integer> classMethodCounts = new TreeMap<>();


    }

    static HeaderAstSummary ConstructHeaderAst(AstTraversalSummary summary,HeaderAstSummary summaryConstruction) {

        HashMap<String, ClassImplementation> classes = summary.classes;
        ArrayList<String> keys = summary.classNames;

        GNode parent = GNode.create("Header");

        // Nodes
        GNode currentClassNode;
        GNode headerDeclaration;
        GNode dataLayoutNode;
        GNode FieldDeclarationNode;
        GNode ModifiersNode;
        GNode ParametersNode;
        GNode DataLayoutMethodDeclarationNode;
        GNode DeclaratorsNode;
        GNode VTableNode;
        GNode VTableMethodDeclarationNode;

        // Cycles through the classes
        for (Object key : keys) {
            // This is the current class Node that is being implemented
            ClassImplementation currentClass = classes.get(key);
            currentClassNode = GNode.create(currentClass.name);


            if (!currentClass.name.contains("Test")) {
                summaryConstruction.numberClasses++;
            }

            headerDeclaration = GNode.create("HeaderDeclaration");
            currentClassNode.add(0, headerDeclaration);

            // HeaderDeclaration
            headerDeclaration.add(currentClass.name);
            String currentPackageList = "";
            int n = currentClass.packages.size();
            for (Object packages : currentClass.packages) {
                if (n > 0) {
                    n--;
                    currentPackageList += packages + "_";
                    continue;
                }
                currentPackageList += packages;
            }
            headerDeclaration.add(currentPackageList);

            // The DataLayout
            dataLayoutNode = GNode.create("DataLayout");
            headerDeclaration.add(dataLayoutNode);

            // HardCoded FieldDeclarationNodes

            // FIRST HARDCODED
            FieldDeclarationNode = GNode.create("FieldDeclaration");
            dataLayoutNode.add(FieldDeclarationNode);
            ModifiersNode = GNode.create("Modifiers");
            FieldDeclarationNode.add(ModifiersNode);
            FieldDeclarationNode.add("__" + currentClass.name + "_VT*");
            FieldDeclarationNode.add("__vptr");
            // DeclaratorsNode
            DeclaratorsNode = GNode.create("Declarators");
            FieldDeclarationNode.add(DeclaratorsNode);

            // SECOND HARDCODED FIELD DECLARATION
            FieldDeclarationNode = GNode.create("FieldDeclaration");
            dataLayoutNode.add(FieldDeclarationNode);

            // Modifiers
            ModifiersNode = GNode.create("Modifiers");

            FieldDeclarationNode.add(ModifiersNode);
            // TODO: Check is the modifiers node needs to have elements added to it?
            ModifiersNode.add("static");

            FieldDeclarationNode.add("__" + currentClass.name + "_VT");
            FieldDeclarationNode.add("__vtable");

            // TODO: Check if there are Declarators?
            DeclaratorsNode = GNode.create("Declarators");
            FieldDeclarationNode.add(DeclaratorsNode);

            // DATA LAYOUT

            // Cycle through the methods
            TreeMap<String, ArrayList<String>> methods = new TreeMap<>();
            Set<String> methodNames = new TreeSet<>();

            System.out.println("check overloading");
            ArrayList<String> methodNames_checkOverloading = new ArrayList<String>();
            for(MethodImplementation m : currentClass.methods) {
                System.out.println(m.name);
                methodNames_checkOverloading.add(m.name);
            }
            ArrayList<String> methodIsOverloaded = new ArrayList<String>();
            for(int i=0; i< methodNames_checkOverloading.size(); i++){
                String checkThisMethod = methodNames_checkOverloading.get(i);
                int howMayOfthisMethod = 0;
                for(int j=0; j< methodNames_checkOverloading.size(); j++){
                    if(checkThisMethod.equals(methodNames_checkOverloading.get(j))){
                        howMayOfthisMethod++;
                    }
                }
                if(howMayOfthisMethod>1){
                    methodIsOverloaded.add("true");
                }
                else{
                    methodIsOverloaded.add("false");
                }
            }

            ClassImplementation superClass = currentClass.superClass;
            while(superClass != null) {
                for (MethodImplementation m : superClass.methods) {
                    ArrayList<String> currentMethod1 = new ArrayList<>();
                    currentMethod1.add(m.name);
                    if(methodNames.contains(m.name))
                        continue;
                    currentMethod1.add(m.returnType);
                    currentMethod1.add(superClass.name);
                    currentMethod1.add("Parameters");
                    for(ParameterImplementation param : m.parameters) {
                        currentMethod1.add(param.toString());
                    }
                    methodNames.add(m.name);
                    methods.put(m.name, currentMethod1);
                }
                superClass = superClass.superClass;
            }


            int methodCount = 0;
            for(MethodImplementation m : currentClass.methods) {
                methodCount++;
                //check if this method is overloaded
                int index = methodNames_checkOverloading.indexOf(m.name);
                System.out.println("index: " + index);
                String isOverloaded= methodIsOverloaded.get(index);
                if(isOverloaded.equals("true")){
                    ArrayList<String> currentMethod1 = new ArrayList<>();
                    currentMethod1.add(m.returnType);

                    //getParams
                    ArrayList<String> paramForEachMethod = new ArrayList<>();
                    for (ParameterImplementation param : m.parameters) {
                        System.out.println("get params");
                        System.out.println(param.toString());
                        String paramName="";
                        String paramToString = param.toString();
                        for(char c: paramToString.toCharArray()){
                            if(c != ' '){
                                paramName+=c;
                            }
                            else{
                             break;
                            }
                        }
                        System.out.println("paramNAme: " + paramName);
                        paramForEachMethod.add(paramName);
                    }
                    //End: got all parameters

                    String overloadedName = m.name + "_";
                    for(String s: paramForEachMethod){
                        overloadedName += s;
                    }
                    System.out.println("overloadedName: " +overloadedName);

                    currentMethod1.add(overloadedName);
                    currentMethod1.add(currentClass.name);
                    currentMethod1.add("Parameters");
                    for (ParameterImplementation param : m.parameters) {
                        currentMethod1.add(param.toString());
                    }
                    methodNames.add(overloadedName);
                    methods.put(overloadedName, currentMethod1);

                }
                else {

                    ArrayList<String> currentMethod1 = new ArrayList<>();
                    currentMethod1.add(m.returnType);
                    currentMethod1.add(m.name);
                    currentMethod1.add(currentClass.name);
                    currentMethod1.add("Parameters");
                    for (ParameterImplementation param : m.parameters) {
                        currentMethod1.add(param.toString());
                    }
                    methodNames.add(m.name);
                    methods.put(m.name, currentMethod1);
                }
            }
            System.out.println("methodCount: " + methodCount);

            System.out.println("methodNames.size()" + methodNames.size());
            for(Object name : methodNames) {
                //
                System.out.println("name: " + name);
                String mName = (String) name;
                DataLayoutMethodDeclarationNode = GNode.create("DataLayoutMethodDeclaration");
                dataLayoutNode.add(DataLayoutMethodDeclarationNode);
                DataLayoutMethodDeclarationNode.add(ModifiersNode);
                ModifiersNode = GNode.create("Modifiers");
                DataLayoutMethodDeclarationNode.add(ModifiersNode);
                DataLayoutMethodDeclarationNode.add(methods.get(mName).get(0));
                DataLayoutMethodDeclarationNode.add(methods.get(mName).get(1));
                DataLayoutMethodDeclarationNode.add(methods.get(mName).get(2));
                ParametersNode = GNode.create("Parameters");
                DataLayoutMethodDeclarationNode.add(ParametersNode);
                for(int i = 4; i < methods.get(mName).size(); i++) {
                    String type = methods.get(mName).get(i);
                    ParametersNode.add(type.substring(0, type.indexOf(" ")));
                }
            }

            if (!currentClass.name.contains("Test")) {
                summaryConstruction.classMethodCounts.put(currentClass.name,methodCount);
            }

            // HardCoded Methods?
            DataLayoutMethodDeclarationNode = GNode.create("DataLayoutMethodDeclaration");
            dataLayoutNode.add(DataLayoutMethodDeclarationNode);
            ModifiersNode = GNode.create("Modifiers");
            DataLayoutMethodDeclarationNode.add(ModifiersNode);
            ModifiersNode.add("static");
            DataLayoutMethodDeclarationNode.add("int32_t");
            DataLayoutMethodDeclarationNode.add("hashCode");
            DataLayoutMethodDeclarationNode.add("Object");
            ParametersNode = GNode.create("Parameters");
            DataLayoutMethodDeclarationNode.add(ParametersNode);
            ParametersNode.add("__Object");

            DataLayoutMethodDeclarationNode = GNode.create("DataLayoutMethodDeclaration");
            dataLayoutNode.add(DataLayoutMethodDeclarationNode);
            ModifiersNode = GNode.create("Modifiers");
            DataLayoutMethodDeclarationNode.add(ModifiersNode);
            ModifiersNode.add("static");
            DataLayoutMethodDeclarationNode.add("Class");
            DataLayoutMethodDeclarationNode.add("getClass");
            DataLayoutMethodDeclarationNode.add("Object");
            ParametersNode = GNode.create("Parameters");
            DataLayoutMethodDeclarationNode.add(ParametersNode);
            ParametersNode.add("__Object");

            DataLayoutMethodDeclarationNode = GNode.create("DataLayoutMethodDeclaration");
            dataLayoutNode.add(DataLayoutMethodDeclarationNode);
            ModifiersNode = GNode.create("Modifiers");
            DataLayoutMethodDeclarationNode.add(ModifiersNode);
            ModifiersNode.add("static");
            DataLayoutMethodDeclarationNode.add("bool");
            DataLayoutMethodDeclarationNode.add("equals");
            DataLayoutMethodDeclarationNode.add("Object");
            ParametersNode = GNode.create("Parameters");
            DataLayoutMethodDeclarationNode.add(ParametersNode);
            ParametersNode.add("Object");

            DataLayoutMethodDeclarationNode = GNode.create("DataLayoutMethodDeclaration");
            dataLayoutNode.add(DataLayoutMethodDeclarationNode);
            ModifiersNode = GNode.create("Modifiers");
            DataLayoutMethodDeclarationNode.add(ModifiersNode);
            ModifiersNode.add("static");
            DataLayoutMethodDeclarationNode.add("Class");
            DataLayoutMethodDeclarationNode.add("__class");
            DataLayoutMethodDeclarationNode.add("Object");
            ParametersNode = GNode.create("Parameters");
            DataLayoutMethodDeclarationNode.add(ParametersNode);

            // VTable
            VTableNode = GNode.create("VTable");
            dataLayoutNode.add(VTableNode);


            VTableMethodDeclarationNode = GNode.create("VTableMethodDeclaration");
            VTableNode.add(VTableMethodDeclarationNode);

            ModifiersNode = GNode.create("Modifiers");
            VTableMethodDeclarationNode.add(ModifiersNode);
            VTableMethodDeclarationNode.add("Class");
            VTableMethodDeclarationNode.add("__isa");
            VTableMethodDeclarationNode.add("Object");
            ParametersNode = GNode.create("Parameters");
            VTableMethodDeclarationNode.add(ParametersNode);

            // Cycle methods

            for(Object name : methodNames) {
                String mName = (String) name;
                VTableMethodDeclarationNode = GNode.create("VTableMethodDeclaration");
                VTableNode.add(VTableMethodDeclarationNode);

                ModifiersNode = GNode.create("Modifiers");
                VTableMethodDeclarationNode.add(ModifiersNode);
                // TODO: Add modifiers to methods
                /*
                for(ModifierImplementation modifier : currentMethod.modifiers){
                    ModifiersNode.add(modifier.name);
                }
                */

                VTableMethodDeclarationNode.add(methods.get(mName).get(0));
                VTableMethodDeclarationNode.add(methods.get(mName).get(1));
                VTableMethodDeclarationNode.add(methods.get(mName).get(2));


                ParametersNode = GNode.create("Parameters");
                VTableMethodDeclarationNode.add(ParametersNode);
                for(int i = 4; i < methods.get(mName).size(); i++) {
                    String type = methods.get(mName).get(i);
                    ParametersNode.add(type.substring(0, type.indexOf(" ")));
                }

            }

            // HardCoded Methods

            VTableMethodDeclarationNode = GNode.create("VTableMethodDeclaration");
            VTableNode.add(VTableMethodDeclarationNode);

            ModifiersNode = GNode.create("Modifiers");
            VTableMethodDeclarationNode.add(ModifiersNode);
            VTableMethodDeclarationNode.add("int32_t");
            VTableMethodDeclarationNode.add("hashCode");
            VTableMethodDeclarationNode.add("Object");
            ParametersNode = GNode.create("Parameters");
            VTableMethodDeclarationNode.add(ParametersNode);

            VTableMethodDeclarationNode = GNode.create("VTableMethodDeclaration");
            VTableNode.add(VTableMethodDeclarationNode);

            ModifiersNode = GNode.create("Modifiers");
            VTableMethodDeclarationNode.add(ModifiersNode);
            VTableMethodDeclarationNode.add("Class");
            VTableMethodDeclarationNode.add("getClass");
            VTableMethodDeclarationNode.add("Object");
            ParametersNode = GNode.create("Parameters");
            VTableMethodDeclarationNode.add(ParametersNode);

            VTableMethodDeclarationNode = GNode.create("VTableMethodDeclaration");
            VTableNode.add(VTableMethodDeclarationNode);

            ModifiersNode = GNode.create("Modifiers");
            VTableMethodDeclarationNode.add(ModifiersNode);
            VTableMethodDeclarationNode.add("bool");
            VTableMethodDeclarationNode.add("equals");
            VTableMethodDeclarationNode.add("Object");
            ParametersNode = GNode.create("Parameters");
            VTableMethodDeclarationNode.add(ParametersNode);
            ParametersNode.add("__Object");

            // Add the currentClassNode to the parent Node
            parent.add(currentClassNode);
        }
        summaryConstruction.parent = parent;
        return summaryConstruction;
    }

}

