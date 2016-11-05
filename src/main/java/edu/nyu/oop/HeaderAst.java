package edu.nyu.oop;

import xtc.tree.GNode;
import java.util.ArrayList;
import java.util.HashMap;


import static edu.nyu.oop.AstTraversal.*;

/**
 * Created by Garrett on 10/24/16.
 */
public class HeaderAst {

    static GNode ConstructHeaderAst(AstTraversalSummary summary) {

        // TODO: TESTING INFORMATION
        int numberClasses = 0;


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
            numberClasses++;
            // This is the current class Node that is being implemented
            ClassImplementation currentClass = classes.get(key);
            currentClassNode = GNode.create(currentClass.name);
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
            // TODO: Main method does not list the methods?
            for (MethodImplementation currentMethod : currentClass.methods) {
                DataLayoutMethodDeclarationNode = GNode.create("DataLayoutMethodDeclaration");
                dataLayoutNode.add(DataLayoutMethodDeclarationNode);
                // TODO: Method Implementation needs modifiers
                ModifiersNode = GNode.create("Modifiers");
                //ModifiersNode.add(currentMethod.modifer);
                DataLayoutMethodDeclarationNode.add(ModifiersNode);
                DataLayoutMethodDeclarationNode.add(currentMethod.returnType);
                DataLayoutMethodDeclarationNode.add(currentMethod.name);
                DataLayoutMethodDeclarationNode.add(currentClass.name);
                ParametersNode = GNode.create("Parameters");
                DataLayoutMethodDeclarationNode.add(ParametersNode);
                for (ParameterImplementation param : currentMethod.parameters) {
                    ParametersNode.add(param.toString());
                }
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
            for (MethodImplementation currentMethod : currentClass.methods) {

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

                VTableMethodDeclarationNode.add(currentMethod.returnType);
                VTableMethodDeclarationNode.add(currentMethod.name);
                VTableMethodDeclarationNode.add(currentClass.name);


                ParametersNode = GNode.create("Parameters");
                VTableMethodDeclarationNode.add(ParametersNode);
                for (ParameterImplementation param : currentMethod.parameters) {
                    ParametersNode.add(param.type);
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
        return parent;
    }

}

