package edu.nyu.oop;

/**
 * Created by susan on 10/10/16.
 */

import java.util.ArrayList;
import java.util.Queue;

public class ClassImplementation {
    //TODO: modifiers, global instances
    String modifier;
    String name;
    String superClassName;

    ClassImplementation superClass;

    ArrayList<String> packages;
    ArrayList<ConstructorImplementation> constructors;
    ArrayList<MethodImplementation> methods;
    ArrayList<FieldDeclaration> declarations;

    public ClassImplementation(ClassImplementation superClass, String name, String modifier) {
        if(superClass != null){
            this.superClassName = superClass.name;
        }else{
            this.superClassName = null;
        }

        this.name = name;

        if(modifier != ""){
            this.modifier = modifier;
        }

        packages = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();
        declarations = new ArrayList<>();

    }

    public boolean isSuperClass(String superClassName) {
        if (superClass == null) return false;
        else if (superClass.name.equals(superClassName)) return true;

        return superClass.isSuperClass(superClassName);
    }

    public ClassImplementation(String name, String modifier) {
        this(null, name, modifier);
    }

    public void setSuperClass(ClassImplementation superClass) {
        this.superClass = superClass;
    }

    public void addMethod(MethodImplementation m) {
        methods.add(m);
    }

    public void addConstructor(ConstructorImplementation constructor) {
        constructors.add(constructor);
    }

    public void addDeclaration(FieldDeclaration declaration) {
        declarations.add(declaration);
    }

    public MethodImplementation findMethod(String name) {
        for (int i = 0; i < methods.size(); i++) {
            MethodImplementation m = methods.get(i);
            if (name.equals(m.name)) return m;
        }
        return null;
        //if (superClass == null) return null;
        //else return superClass.findMethod(name);
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
            packageCounter++;
            if (packageCounter == packages.size()) {
                s.append(p);
                break;
            }
            s.append(p + ".");
        }
        s.append("\n");

        /** Modifier **/

        if(modifier != null){
            s.append("Modifier\n\t");
            s.append(modifier + "\n");
        }

        /** Class **/
        s.append("Class:\n\t");
        ClassImplementation currentClass = this;
        while (currentClass != null) {
            s.append(currentClass.name);

            if (superClassName != null)
                s.append(" extends " + superClassName);
            currentClass = currentClass.superClass;
        }
        s.append("\n");

        /** Declarations **/
        s.append("Declarations:\n");
        for(FieldDeclaration declaration : declarations){
            s.append("\t" + declaration.toString() + "\n");
        }

        /** Constructors **/
        s.append("Constructor:\n");
        for (ConstructorImplementation constructor : constructors) {
            s.append("\t" + constructor.toString() + "\n");
        }

        /** Methods **/
        s.append("Methods:\n");
        for (MethodImplementation m : methods) {
            s.append("\t" + m.toString() + "\n");
        }

        return s.toString();
    }
}
