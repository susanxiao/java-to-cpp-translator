package edu.nyu.oop;

/**
 * Created by susan on 10/10/16.
 */

import java.util.ArrayList;

public class ClassImplementation {
    String name;
    Class superClass;
    ArrayList<MethodImplementation> methods;

    public ClassImplementation(Class superClass) {
        this.superClass = superClass;
    }

    public ClassImplementation() {
        this(null);
    }

    public void addMethod(MethodImplementation m) {
        methods.add(m);
    }

    public MethodImplementation findMethod(String name) {
        for (int i =0; i < methods.size(); i++) {
            MethodImplementation m = methods.get(i);
            if (name.equals(m.name)) return m;
        }
        return null;
    }
}
