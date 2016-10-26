package edu.nyu.oop;

import org.slf4j.Logger;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.out;

/**
 * Created by susan on 10/26/16.
 */
public class AstMutator extends Visitor {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private Runtime runtime;
    boolean debug = false;
    private AstMutator.AstMutatorSummary summary = new AstMutator.AstMutatorSummary();

    public AstMutator(Runtime runtime) {
        this.runtime = runtime;
    }

    public AstMutator.AstMutatorSummary getMutator(Node n) {

        super.dispatch(n);

        if (debug) {
            out.println("\n");
            out.println("DEBUGGING IS ON");
            for (ClassImplementation c : summary.classes.values()) {
                out.println(c.toString());
            }
        }

        return summary;
    }

    static class AstMutatorSummary {

        HashMap<String, ClassImplementation> classes = new HashMap<>();
        ArrayList<String> classNames = new ArrayList<>();

        ArrayList<String> currentPackages = new ArrayList<>();

        ClassImplementation currentClass;
        MethodImplementation currentMethod;
        ConstructorImplementation currentConstructor;

        // operators ?
        String operators = "=+-*/";

        public void addClass(ClassImplementation superClass, String name, String modifier) {
            ClassImplementation c = new ClassImplementation(superClass, name, modifier);
            c.packages.addAll(currentPackages);

            classes.put(name, c);
            classNames.add(name);
            currentClass = c;
        }

        public void addMethod(MethodImplementation m) {
            currentClass.addMethod(m);
            currentMethod = m;
        }

        public void addConstructor(ConstructorImplementation constructor) {
            currentClass.addConstructor(constructor);
            currentConstructor = constructor;
        }

        public ClassImplementation findClass(String name) {
            return classes.get(name);
        }
    }
}
