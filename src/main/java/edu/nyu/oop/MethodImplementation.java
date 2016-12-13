package edu.nyu.oop;

/**
 * Created by susan on 10/10/16.
 */

import java.util.ArrayList;

public class MethodImplementation {
    //TODO: modifiers
    boolean isStatic;
    String name;
    String returnType;
    ArrayList<ParameterImplementation> parameters;
    ArrayList<MethodStatement> implementation;

    String overLoadedName;
    boolean isOverloaded;

    ClassImplementation className;

    public MethodImplementation(String name) {
        isStatic = false;
        switch(name) {
        case "toString":
        case "hashCode":
        case "equals":
        case "getClass":
        case "main":
            this.name = name;
            break;
        default:
            this.name = "method" + name.substring(0,1).toUpperCase() + name.substring(1);
        }
        parameters = new ArrayList<>();
        implementation = new ArrayList<>();

        isOverloaded = false;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void addParameter(ParameterImplementation param) {
        parameters.add(param);
    }

    public void addMethodStatement(MethodStatement m) {
        implementation.add(m);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        /** Return type **/
        s.append(returnType);

        /** Name **/
        s.append(" " + name);

        /** Parameters **/
        s.append("(");
        for (int i = 0; i < parameters.size(); i++) {
            s.append(parameters.get(i).toString());
            if (i < parameters.size() - 1)
                s.append(",");
        }
        s.append(")\n");

        /*
        /** Implementation **
        for (int i = 0; i < implementation.size(); i++) {
            s.append("\t\t" + implementation.get(i).toString() + "\n");
        }
        */

        return s.toString();
    }

}
