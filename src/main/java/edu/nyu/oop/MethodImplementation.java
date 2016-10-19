package edu.nyu.oop;

/**
 * Created by susan on 10/10/16.
 */

import java.util.ArrayList;

public class MethodImplementation {
    //TODO: modifiers
    String name;
    String returnType;
    ArrayList<ArrayList<String>> parameters;
    String implementation;

    public MethodImplementation(String name) {
        this.name = name;
        parameters = new ArrayList<>();
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void addParameter(String parameterType, String name) {
        ArrayList<String> newParam = new ArrayList<>();
        newParam.add(parameterType);
        newParam.add(name);
        parameters.add(newParam);
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
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
            ArrayList<String> p = parameters.get(i);
            s.append(p.get(0)+" "+p.get(1));
            if (i < parameters.size() - 1)
                s.append(",");
        }
        s.append(")");

        return s.toString();
    }
}
