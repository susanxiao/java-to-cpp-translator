package edu.nyu.oop;

/**
 * Created by susan on 10/10/16.
 */

import java.util.ArrayList;
import java.util.Queue;

public class ClassImplementation {
    //TODO: modifiers
    String name;
    ClassImplementation superClass;
    ArrayList<MethodImplementation> methods;
    ArrayList<String> packages;

    public ClassImplementation(ClassImplementation superClass, String name) {
        this.superClass = superClass;
        this.name = name;

        methods = new ArrayList<>();
        packages = new ArrayList<>();
    }

    public ClassImplementation(String name) {
        this(null, name);
    }

    public void setSuperClass(ClassImplementation superClass) {
        this.superClass = superClass;
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

    public void addPackage(String name) {
        packages.add(name);
    }

    //TODO
    public String toHeader() {
        return "";
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        /** Package **/
        s.append("Package:\n\t");
        for (String p : packages) {
            s.append(p+".");
        }
        s.append("\n");

        /** Class **/
        s.append("Class:\n\t");
        ClassImplementation currentClass = this;
        while (currentClass != null) {
            s.append(currentClass.name);
            if (superClass != null)
                s.append(" extends ");
            currentClass = currentClass.superClass;
        }
        s.append("\n");

        /** Methods **/
        s.append("Methods:\n");
        for (MethodImplementation m : methods) {
            s.append("\t"+m.toString()+"\n");
        }

        return s.toString();
    }
}
