package edu.nyu.oop;

/**
 * Created by susan on 10/10/16.
 */

import java.util.ArrayList;

public class ClassImplementation {
    String name;
    ClassImplementation superClass;
    ArrayList<MethodImplementation> methods;

    public ClassImplementation(ClassImplementation superClass) {
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
        if (superClass == null) return null;
        else return superClass.findMethod(name);
    }

    public String toHeader() {
        return "";
    }
}
