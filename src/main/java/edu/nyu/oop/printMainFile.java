package edu.nyu.oop;

import xtc.tree.GNode;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.out;

/**
 * Created by Garrett on 10/21/16.
 */
public class printMainFile {
    public static void main(String[] args) {

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
            if (currentClass.modifier == "public") {
                classesImplementing.add(className);
            }
        }

        StringBuilder s = new StringBuilder();

        ClassImplementation mainClass = classes.get(classesImplementing.get(0));

        s.append("int main(void)\n{\n");
        for(MethodImplementation currentMethod : mainClass.methods){

            for(MethodStatement currentStatement : currentMethod.implementation){
                s.append("  " + currentStatement.toCpp() + "\n");
            }

        }

        s.append("  return 0;\n");
        s.append("}\n");

        out.println(s.toString());
    }
}
