package edu.nyu.oop;

/**
 * Created by susan on 10/10/16.
 */

import java.util.ArrayList;

public class MethodImplementation {
    //TODO: modifiers
    String name;
    String returnType;
    ArrayList<ParameterImplementation> parameters;
    ArrayList<MethodStatement> implementation;

    public MethodImplementation(String name) {
        this.name = name;
        parameters = new ArrayList<>();
        implementation = new ArrayList<>();
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
        s.append(" "+name);

        /** Parameters **/
        s.append("(");
        for (int i = 0; i < parameters.size(); i++) {
            s.append(parameters.get(i).toString());
            if (i < parameters.size() - 1)
                s.append(",");
        }
        s.append(")\n");

        /** Implementation **/
        for (int i = 0; i < implementation.size(); i++) {
            s.append("\t\t"+implementation.get(i).toString()+"\n");
        }

        return s.toString();
    }
}
