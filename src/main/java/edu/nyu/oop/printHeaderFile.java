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
        GNode node = (GNode) LoadFileImplementations.loadTestFile("./src/test/java/inputs/test001/Test001.java");
        AstTraversal visitor = new AstTraversal(LoadFileImplementations.newRuntime());
        AstTraversal.AstTraversalSummary summary = visitor.getTraversal(node);

        HashMap<String, ClassImplementation> classes = summary.classes;
        ArrayList<String> keys = summary.classNames;
        ArrayList<String> classNames = keys;

        StringBuilder s = new StringBuilder();


        for (Object o : classNames) {
            s.append("struct __" + o.toString() + ";\n");
            s.append("struct __" + o.toString() + "_VT;\n");
        }
        s.append("\n");
        for (Object o : classNames) {
            s.append("typedef __" + o.toString() + "* " + o.toString() + ";\n");
        }

        for (Object o : classNames) {
            String className = o.toString();
            s.append("struct __" + className + "\n{\n");
            s.append("   __" + className + "_VT* __vtpr;\n");
            s.append("\n");

            s.append("   __" + className + "();\n");
            s.append("\n");
        }

        out.println(s.toString());

    }

    /*

        for (int j = 0; j < methodGrouping.get(classCounter).size(); j++) {
            out.print("   static " + methodTypeGrouping.get(classCounter).get(j) + " ");
            out.println(methodGrouping.get(classCounter).get(j) + "(" + className + ");");
        }
        out.println("\n");

        out.println("   static Class __class();");
        out.println("\n");

        out.println("   static __" + className + "_VT " + " __vtable;");

        out.println("};");

        // printing the virtual table
        out.println("\n");
        out.println("struct __" + className + "_VT\n{");
        out.println("\n");
        out.println("   Class __isa;");
        out.println("\n");

        for (int j = 0; j < methodGrouping.get(classCounter).size(); j++) {
            out.print("   " + methodTypeGrouping.get(classCounter).get(j) + " (*");
            out.println(methodGrouping.get(classCounter).get(j) + ")(" + className + ");");
        }

        // vtable constructor
        out.println("\n");
        out.println("   __" + className + "_VT()");
        out.println("    :__isa(__" + className + "::__class()),");
        for (int j = 0; j < methodGrouping.get(classCounter).size(); j++) {
            out.print("     " + methodGrouping.get(classCounter).get(j) + "(&__" + className);
            out.print("::" + methodGrouping.get(classCounter).get(j) + ")");
            if (j != methodGrouping.get(classCounter).size() - 1) {
                out.println(',');
            } else {
                out.println();
            }
        }
        out.println("    {}");
        out.println("\n");
        out.println("};");
        out.print("\n");
    }

    */


}

