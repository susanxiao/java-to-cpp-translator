package edu.nyu.oop;

import xtc.tree.GNode;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.out;

/**
 * Created by Garrett on 10/21/16.
 */
public class printCppFile {

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
         String className = o.toString();
         ClassImplementation currentClass = summary.classes.get(className);
         s.append("__" + className + "::__" + className + "()" + " : " + "__vptr(&__vtable) {}\n");
         s.append("\n");
         for (MethodImplementation currentMethod : currentClass.methods) {
         String methodName = currentMethod.name;
         s.append(currentMethod.returnType + " __" + className + "::" + methodName + "(");
         s.append("(" + className + " __this) {\n");
         if (methodName.equals("toString")) {
         String implementation = currentMethod.implementation.get(0).toString();
         s.append("   std::ostringstream sout;");
         s.append("   sout << " + implementation.replaceFirst("return", "").trim() + ";\n");
         s.append("   return new_String(sout.str());");
         s.append("\n}\n");
         }
         }
         s.append("Class __" + className + "::" + "__class() {\n");
         s.append("   static Class k =");
         s.append("     new __Class(__rt::literal(\"class inputs.javalang." + className + "\"), (Class) __rt::null());\n");
         s.append("   return k;\n}\n");
         s.append("\n");

         s.append("__" + className + "_VT" + " __" + className + "::__vtable;\n");
         s.append("\n");
         }
         out.println(s.toString());
         **/
    }
}
