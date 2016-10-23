package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.lang.JavaEntities;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.util.Runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

/**
 * Created by Garrett on 10/21/16.
 */


public class printHeaderFile {
    public static void main(String[] args) {
        /**
         GNode node = (GNode) LoadFileImplementations.loadTestFile("./src/test/java/inputs/test001/Test001.java");
         AstTraversal visitor = new AstTraversal(LoadFileImplementations.newRuntime());
         AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);

         HashMap<String, ClassImplementation> classes = summary.classes;

         ArrayList<String> keys = summary.classNames;
         ArrayList<String> classNames = keys;

         ArrayList<String> classesImplementing = new ArrayList<>();

         for (Object o : classNames) {
         String className = o.toString();
         ClassImplementation currentClass = classes.get(className);
         if (currentClass.modifier == null) {
         classesImplementing.add(className);
         }
         }


         StringBuilder s = new StringBuilder();

         for (Object o : classesImplementing) {
         s.append("struct __" + o.toString() + ";\n");
         s.append("struct __" + o.toString() + "_VT;\n");
         }
         s.append("\n");
         for (Object o : classesImplementing) {
         s.append("typedef __" + o.toString() + "* " + o.toString() + ";\n");
         }
         s.append("\n");
         for (Object o : classesImplementing) {

         String className = o.toString();
         ClassImplementation currentClass = classes.get(className);

         s.append("struct __" + className + "\n{\n");
         s.append("   __" + className + "_VT* __vtpr;\n");
         s.append("\n");

         s.append("   __" + className + "();\n");
         s.append("\n");

         for (MethodImplementation o1 : currentClass.methods) {
         MethodImplementation currentMethod = o1;
         s.append("   static " + currentMethod.typeNameToString());
         s.append(currentMethod.parametersToString());
         }

         s.append("   static Class __class();\n");
         s.append("\n");
         s.append("   static __" + className + "_VT " + " __vtable;\n");
         s.append("};\n");

         s.append("\n");
         s.append("struct __" + className + "_VT\n{");
         s.append("\n");
         s.append("   Class __isa;");
         s.append("\n");

         for (MethodImplementation o1 : currentClass.methods) {
         MethodImplementation currentMethod = o1;
         s.append("   " + currentMethod.pointerToString() + "(" + className + ");\n");
         }

         s.append("\n");
         s.append("   __" + className + "_VT()\n");
         s.append("    :__isa(__" + className + "::__class()),\n");

         // TODO: if the class inherits the method
         int methodCounter = currentClass.methods.size();
         for (MethodImplementation o1 : currentClass.methods) {
         MethodImplementation currentMethod = o1;
         s.append("     " + currentMethod.name + "(&__" + className);
         s.append("::" + currentMethod.name + ")");
         if(methodCounter > 0){
         methodCounter--;
         s.append(",\n");
         }
         }
         s.append("    {}");
         s.append("\n");
         s.append("};");
         s.append("\n");
         }



         out.println(s.toString());

         **/
    }

}

