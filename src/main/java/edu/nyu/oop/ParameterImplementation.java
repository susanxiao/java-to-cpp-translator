package edu.nyu.oop;

/**
 * Created by susan on 10/19/16.
 */
public class ParameterImplementation {
    String type;
    String dimension;
    String name;
    public ParameterImplementation(String type, String name) {
        this.type = type;
        this.name = name;
        this.dimension = "";
    }

    @Override
    public String toString() {
        return type+dimension+" "+name;
    }
}
