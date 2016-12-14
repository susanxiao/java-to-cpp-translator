package edu.nyu.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by willk on 12/13/16.
 */

public class PrintMainFileSummary {
    String currentClassName;
    String filePrinted;
    int checkClassCounter;
    ArrayList<String> classNames = new ArrayList<>();
    ArrayList<String> variables = new ArrayList<>();
    TreeMap<String, String> classVariables = new TreeMap<>();

    HashMap<String, String> localVariables;
    HashMap<String, ArrayList<MethodImplementation>> overLoadedMethods;

    String init2D;
    String init2DSize;
    String init2DType;
    String init2DDec;

    boolean needsSizeCheck = false;
    Integer size;
    
}