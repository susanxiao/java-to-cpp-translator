package edu.nyu.oop;

/**
 * Created by susan on 10/10/16.
 */

import java.util.ArrayList;
import java.util.Queue;

public class ClassImplementation {
    //TODO: modifiers, global instances
    String name;
    ClassImplementation superClass;
    ArrayList<MethodImplementation> methods;
    ArrayList<String> packages;
    ArrayList<ConstructorImplementation> constructors;

    public ClassImplementation(ClassImplementation superClass, String name) {
        this.superClass = superClass;
        this.name = name;

        packages = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();

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

    public void addConstructor(ConstructorImplementation constructor){
        constructors.add(constructor);
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
        int packageCounter = 0;
        for (String p : packages) {
            packageCounter ++;
            if(packageCounter == packages.size()){
                s.append(p);
                break;
            }
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

        /** Constructors **/
        s.append("Constructor:\n");
        for(ConstructorImplementation constructor : constructors){
            s.append("\t" + constructor.toString() + "\n");
        }

        /** Methods **/
        s.append("Methods:\n");
        for (MethodImplementation m : methods) {
            s.append("\t"+m.toString()+"\n");
        }

        return s.toString();
    }
}
