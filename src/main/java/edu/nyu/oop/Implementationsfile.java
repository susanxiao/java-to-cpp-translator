package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.lang.JavaEntities;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.util.Runtime;

import java.io.File;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

/**
 * Created by Garrett on 10/17/16.
 */
public class Implementationsfile {

    static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    /**
     * Obtain the classes
     */
    static ArrayList<String> getClasses(AstTraversal.AstBuilderSummary summary) {
        ArrayList<String> classDeclarations = summary.tempClassDec;
        ArrayList<String> classes = new ArrayList<String>();
        for (Object o : classDeclarations) {
            if (o.toString() != "public" && o.toString() != "private" && !isNumeric(o.toString())) {
                classes.add(o.toString());
            }
        }
        return classes;
    }

    /**
     * Obtain method groupings by the class
     */
    static ArrayList<ArrayList<String>> methodClassGrouping(AstTraversal.AstBuilderSummary summary) {

        int currentClass = 0;

        // Gates
        boolean gate1 = false;
        boolean gate2 = false;
        boolean classIndicatorGate = false;
        boolean methodIndicatorGate = false;

        // counters
        int tempCounter1 = 0;
        int classMethodCurrent = 0;

        ArrayList<String> methods = new ArrayList<String>();
        ArrayList<String> methods1 = new ArrayList<String>();
        ArrayList<String> methodTypes = new ArrayList<String>();

        ArrayList<String> methodBody = new ArrayList<String>();

        ArrayList<ArrayList<String>> methodClasses = new ArrayList<ArrayList<String>>();
        ArrayList<String> methodTemp = new ArrayList<String>();

        for (int i = 0; i < summary.classCount; i++) {
            currentClass = i + 1;
            methodTemp = new ArrayList<String>();
            for (Object n : summary.tempMethodBody) {
                // obtain the class number
                if (!classIndicatorGate) {
                    if (parseInt(summary.tempMethodBody.get(tempCounter1)) == currentClass) {
                        classIndicatorGate = true;
                        methodIndicatorGate = false;
                        tempCounter1 += 1;
                        continue;
                    }
                }
                // obtain the methods that are within the current class
                if (!methodIndicatorGate) {
                    if (isNumeric(summary.tempMethodBody.get(tempCounter1))) {
                        classMethodCurrent = parseInt(summary.tempMethodBody.get(tempCounter1));
                        tempCounter1 += 1;
                        methodTemp.add(classMethodCurrent + "");
                        continue;
                    }
                }
                if (!isNumeric(summary.tempMethodBody.get(tempCounter1)) && currentClass == i + 1) {
                    /**
                     if(debug){
                     out.println("   The current class is " + currentClass);
                     out.println("   Printing current from tempMethodBody");
                     out.println("   " + summary.tempMethodBody.get(tempCounter1));
                     }
                     **/
                    methodTemp.add(summary.tempMethodBody.get(tempCounter1));
                    methodIndicatorGate = true;
                    tempCounter1 += 1;
                }
                if (tempCounter1 == summary.tempMethodBody.size()) {
                    break;
                }
                if (isNumeric(summary.tempMethodBody.get(tempCounter1))) {
                    currentClass = -1;
                    classIndicatorGate = false;
                }
            }
            methodClasses.add(methodTemp);
        }
        return methodClasses;
    }

    /**
     * Obtain the method body strings that are going to be used for implementation
     */
    // obtain the method body strings
    static ArrayList<ArrayList<String>> bodyStrings(AstTraversal.AstBuilderSummary summary, ArrayList<ArrayList<String>> methodClasses) {
        ArrayList<ArrayList<String>> stringsClasses = new ArrayList<ArrayList<String>>();
        ArrayList<String> strings = new ArrayList<String>();

        // loop to obtain the strings
        strings = new ArrayList<String>();

        boolean FieldDeclarationGate = false;
        boolean ExpressionStatementGate = false;
        boolean addSwitch = false;
        boolean returnGate = false;
        String tempString = "";
        int currentClass;

        // creating the strings for the method implementations
        for (int i = 0; i < summary.classCount; i++) {

            currentClass = i + 1;
            strings = new ArrayList<String>();

            // reset the gates
            FieldDeclarationGate = false;
            ExpressionStatementGate = false;
            addSwitch = false;
            returnGate = false;

            for (Object q1 : methodClasses.get(currentClass - 1)) {
                if (q1.toString().equals("FieldDeclaration")) {
                    if (addSwitch) {
                        strings.add(tempString);
                    }

                    addSwitch = true;

                    tempString = "";

                    ExpressionStatementGate = false;
                    FieldDeclarationGate = true;
                    returnGate = false;

                    continue;
                }
                if (q1.toString().equals("ExpressionStatement")) {
                    if (addSwitch) {
                        strings.add(tempString);
                    }

                    addSwitch = true;

                    tempString = "";

                    FieldDeclarationGate = false;
                    ExpressionStatementGate = true;
                    returnGate = false;

                    continue;
                }
                if (q1.toString().equals("RETURN")) {
                    if (addSwitch) {
                        strings.add(tempString);
                    }

                    addSwitch = true;

                    tempString = "return ";

                    FieldDeclarationGate = false;
                    ExpressionStatementGate = false;
                    returnGate = true;

                    continue;
                }
                if (FieldDeclarationGate) {
                    if (q1.toString().equals("new")) {
                        tempString += "= new ";
                        continue;
                    }
                    tempString += q1.toString() + " ";
                }
                if (ExpressionStatementGate) {
                    if (q1.toString().equals("println")) {
                        tempString += "println(";
                        continue;
                    }
                    if (q1.toString().equals("toString")) {
                        tempString += "toString()";
                        continue;
                    }
                    tempString += q1.toString() + ".";
                }
                if (returnGate) {
                    tempString += q1.toString() + " ";
                }
            }
            if (addSwitch) {
                strings.add(tempString);
            }
            stringsClasses.add(strings);
        }

        return stringsClasses;
    }

    /**
     * Obtain the methods
     */
    static ArrayList<ArrayList<String>> methodGroupings(AstTraversal.AstBuilderSummary summary, ArrayList<String> classes) {

        ArrayList<String> methods = new ArrayList<String>();
        ArrayList<String> methods1 = new ArrayList<String>();
        ArrayList<ArrayList<String>> methodGroupings = new ArrayList<ArrayList<String>>();

        boolean methodGated = false;
        boolean methodTypeGated = false;
        String className = "";
        String bodyCheck = "";

        for (Object o : summary.tempClassDec) {
            if (isNumeric(o.toString())) {
                methods = new ArrayList<String>();
                className = classes.get(parseInt(o.toString()) - 1);
                String tempNumber = o.toString();
                String classCheck = "";
                for (Object o1 : summary.tempMethods) {
                    if (isNumeric(o1.toString()) && parseInt(o1.toString()) != parseInt(tempNumber)
                            && parseInt(o1.toString()) > parseInt(tempNumber)) {
                        break;
                    } else {
                        if (isNumeric(o1.toString()) && !methodGated) {
                            methodGated = true;
                            classCheck = o1.toString();
                        } else {
                            methodGated = false;
                            if (parseInt(classCheck) != parseInt(tempNumber)) {
                                continue;
                            }
                            methods.add(o1.toString());
                        }
                    }
                }
                methods1 = new ArrayList<String>();
                int tempCounter = 0;
                for (Object o2 : methods) {
                    if (tempCounter > 0) {
                        tempCounter--;
                        continue;
                    }
                    if (o2.toString().equals("main")) {
                        tempCounter = 3;
                    }
                    methods1.add(o2.toString());
                }
                methodGroupings.add(methods1);
            }
        }

        return methodGroupings;
    }

    /**
     * Obtain the method type groupings
     */
    static ArrayList<ArrayList<String>> methodTypeGroupings(AstTraversal.AstBuilderSummary summary, ArrayList<String> classes) {
        ArrayList<ArrayList<String>> typeGroupings = new ArrayList<ArrayList<String>>();
        ArrayList<String> methodTypes = new ArrayList<String>();

        boolean methodGated = false;
        boolean methodTypeGated = false;
        String className = "";
        String bodyCheck = "";

        for (Object o : summary.tempClassDec) {
            if (isNumeric(o.toString())) {
                methodTypes = new ArrayList<String>();
                className = classes.get(parseInt(o.toString()) - 1);
                String tempNumber = o.toString();
                String classCheck = "";
                for (Object o1 : summary.tempMethodTypes) {
                    if (isNumeric(o1.toString()) && parseInt(o1.toString()) != parseInt(tempNumber)
                            && parseInt(o1.toString()) > parseInt(tempNumber)) {
                        break;
                    } else {
                        if (isNumeric(o1.toString()) && !methodTypeGated) {
                            methodTypeGated = true;
                            classCheck = o1.toString();
                        } else {
                            methodTypeGated = false;
                            if (parseInt(classCheck) != parseInt(tempNumber)) {
                                continue;
                            }
                            methodTypes.add(o1.toString());
                        }
                    }
                }
                typeGroupings.add(methodTypes);
            }
        }
        return typeGroupings;
    }

    /**
     * Print the implementation
     */

    static void printImplementations(ArrayList<String> classes, ArrayList<ArrayList<String>> methodClasses,
                                     ArrayList<ArrayList<String>> methodBodyStrings, ArrayList<ArrayList<String>> methodGrouping,
                                     ArrayList<ArrayList<String>> methodTypeGrouping) {

        String className;
        int classCounter = -1;
        for (Object o : classes) {
            classCounter += 1;
            className = o.toString();
            out.println("\n");
            out.println("// " + className + " implementations");
            out.println("__" + className + "::__" + className + "()" + " : " + "__vptr(&__vtable) {}");
            out.println("\n");

            int counter = -1;
            for (Object o2 : methodGrouping.get(classCounter)) {
                counter += 1;
                out.print(methodTypeGrouping.get(classCounter).get(counter) + " __" + className + "::" + o2.toString());
                out.println("(" + className + " __this) {");
                if (o2.equals("toString")) {
                    String tempString = "";
                    for(Object o3 : methodBodyStrings.get(classCounter)){
                        if(o3.toString().equals("return")){
                            continue;
                        }else{
                            tempString += o3.toString() + " ";
                        }
                    }
                    tempString = tempString.replaceFirst("return", "").trim();
                    out.println("   std::ostringstream sout");
                    out.println("   sout << " + tempString + ";");
                    out.println("   return new __String(sout.str());");
                }
                if (o2.equals("main")) {
                    for (Object o3 : methodBodyStrings.get(classCounter)) {
                        out.println("   " + o3);
                    }
                }
                out.println("}");
            }
            out.println("\n");

            out.println("Class __" + className + "::" + "__class(){");
            out.println("   static Class k =");
            out.println("     new __Class(__rt::literal(\"class inputs.javalang." + className + "\"), (Class) __rt::null());");
            out.println("   return k\n}");
            out.print("\n");

            out.println("__" + className + "_VT" + " __" + className + "::__vtable;");
            out.print("\n");
        }


        return;

    }


    /**
     * Main method runs the program
     */

    public static void main(String[] args) {
        boolean debug = true;

        GNode node = (GNode) LoadFile.loadTestFile("./src/test/java/inputs/test001/Test001.java");
        AstTraversal visitor = new AstTraversal(LoadFile.newRuntime());
        AstTraversal.AstBuilderSummary summary = visitor.getClassDeclarations(node);
        out.println("\n\n-----------------\n\n");

        if(debug){
            out.println("IMPLEMENTATION FIle");
            out.println("DEBUGGING IS ON");
        }

        // obtain classes
        ArrayList<String> classes = getClasses(summary);
        if (debug) {
            out.println("   Classes");
            out.println("       " + classes);
        }

        // obtain method class grouping
        ArrayList<ArrayList<String>> methodClasses = methodClassGrouping(summary);
        if (debug) {
            out.println("   Method class groupings");
            out.println("       " + methodClasses);
        }

        // obtain method body strings
        ArrayList<ArrayList<String>> methodBodyStrings = bodyStrings(summary, methodClasses);
        if (debug) {
            out.println("   method body strings");
            out.println("       " + methodBodyStrings);
        }

        // obtain method groupings by class
        ArrayList<ArrayList<String>> methodGrouping = methodGroupings(summary, classes);
        if (debug) {
            out.println("   Method grouping");
            out.println("       " + methodGrouping);
        }

        // obtain method types by class
        ArrayList<ArrayList<String>> methodTypeGrouping = methodTypeGroupings(summary, classes);
        if (debug) {
            out.println("   method type groupings");
            out.println("       " + methodTypeGrouping);
        }

        // print the implementations
        printImplementations(classes, methodClasses, methodBodyStrings, methodGrouping, methodTypeGrouping);

    }
}


class LoadFileImplementations {
    public static Runtime newRuntime() {
        Runtime runtime = new Runtime();
        runtime.initDefaultValues();
        runtime.dir("in", Runtime.INPUT_DIRECTORY, true, "");
        runtime.setValue(Runtime.INPUT_DIRECTORY, JavaEntities.TEMP_DIR);
        return runtime;
    }

    public static Node loadTestFile(String filename) {
        File file = new File(filename);
        return NodeUtil.parseJavaFile(file);
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

}
