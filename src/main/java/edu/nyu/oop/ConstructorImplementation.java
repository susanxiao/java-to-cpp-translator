package edu.nyu.oop;

import java.util.ArrayList;

/**
 * Created by Garrett on 10/20/16.
 */
public class ConstructorImplementation {

    String name;
    ArrayList<ParameterImplementation> parameters;
    ArrayList<MethodStatement> implementations;

    public ConstructorImplementation(String name) {
        this.name = name;
        parameters = new ArrayList<>();
        implementations = new ArrayList<>();
    }

    public void addParameter(ParameterImplementation param) {
        parameters.add(param);
    }

    public void addConstructorStatement(MethodStatement statement) {
        implementations.add(statement);
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(name + "(");
        if(parameters != null) {
            int paramCounter = 0;
            for(Object o : parameters) {
                paramCounter++;
                if(paramCounter == parameters.size()) {
                    s.append(o);
                    break;
                }
                s.append(o + ",");
            }
        }
        s.append(")\n");


        if(implementations != null) {
            for(Object o : implementations) {
                s.append("\t\t" + o + "\n");
            }
        }
        return s.toString();
    }

    public String parametersToString() {
        StringBuilder s = new StringBuilder();
        s.append("(");
        for (int i = 0; i < parameters.size(); i++) {
            s.append(parameters.get(i).toString());
            if (i < parameters.size() - 1)
                s.append(",");
        }
        s.append(")");
        return s.toString();
    }


}
