package edu.nyu.oop;

/**
 * Created by susan on 10/10/16.
 */

import java.util.ArrayList;

public class MethodImplementation {

    String name;
    String returnType;
    ArrayList<String> parameters;
    String implementation;

    public MethodImplementation(String name, String returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    public void addParameter(String parameter) {
        parameters.add(parameter);
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }
}
