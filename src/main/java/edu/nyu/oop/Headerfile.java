package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.lang.JavaEntities;
import xtc.tree.GNode;
import xtc.*;
import xtc.tree.Node;
import xtc.util.Runtime;
import xtc.util.Tool;

import java.io.File;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;


/**
 * Created by Garrett on 10/17/16.
 */
public class Headerfile {

    // helper functions
    static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static void main(String[] args) {
        // helper functions
        boolean debug = false;

        GNode node = null;
        LoadFile temp = new LoadFile();
        node = (GNode) LoadFile.loadTestFile("./src/test/java/inputs/test001/Test001.java");
        AstTraversal visitor = new AstTraversal(LoadFile.newRuntime());
        AstTraversal.AstBuilderSummary summary = visitor.getClassDeclarations(node);

        ArrayList<String> classDeclarations = summary.tempClassDec;
        ArrayList<String> classes = new ArrayList<String>();

        out.println("\n\n");

        for (Object o : classDeclarations) {

            if (o.toString() != "public" && o.toString() != "private" && !isNumeric(o.toString())) {
                classes.add(o.toString());
            }
        }

        /**
         *  Printing the forward declarations
         */

        out.println(" // forward declarations");
        for (Object o : classes) {
            out.println("struct __" + o.toString() + ";");
            out.println("struct __" + o.toString() + "_VT;");
        }
        // styling adding space between the typedefs and the structs
        out.print("\n");
        for (Object o : classes) {
            out.println("typedef __" + o.toString() + "* " + o.toString());
        }

        /**
         *  Printing the structures for the classes
         */
        ArrayList<String> methods = new ArrayList<String>();
        ArrayList<String> methods1 = new ArrayList<String>();
        ArrayList<String> methodTypes = new ArrayList<String>();

        boolean methodGated = false;
        boolean methodTypeGated = false;
        String className = "";
        out.print("\n\n");

        for (Object o : summary.tempClassDec) {
            if (isNumeric(o.toString())) {
                methods = new ArrayList<String>();
                methodTypes = new ArrayList<String>();
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
                            if(parseInt(classCheck) != parseInt(tempNumber)){
                                continue;
                            }
                            methods.add(o1.toString());
                        }
                    }
                }
                if(debug) {
                    out.println("Methods for " + tempNumber);
                    out.println(methods);
                }
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
                            if(parseInt(classCheck) != parseInt(tempNumber)){
                                continue;
                            }
                            methodTypes.add(o1.toString());
                        }
                    }
                }

                methods1 = new ArrayList<String>();
                int tempCounter = 0;
                for(Object o2 : methods){
                    if(tempCounter > 0){
                        tempCounter--;
                        continue;
                    }
                    if(o2.toString().equals("main")){
                        tempCounter = 3;
                    }
                    methods1.add(o2.toString());
                }

                if(debug) {
                    out.println("methodTypes for " + tempNumber);
                    out.println(methodTypes);
                }

                out.println("// printing the struct for " + className);
                out.println("struct __" + className + "\n{\n");
                out.println("   // pointer to the vtable");
                out.println("   __" + className + "_VT* __vtpr;");
                out.println("\n");

                out.println("   // the constructor");
                out.println("   __" + className + "();");
                out.println("\n");

                out.println("   // the methods implemented");
                for (int i = 0; i < methods1.size(); i++) {
                    out.println("   static " + methodTypes.get(i) + " " + methods1.get(i) + "(" + className + ");");
                }
                out.println("\n");

                out.println("   // function return class");
                out.println("   static Class _class();");
                out.println("\n");

                out.println("   // the vtable");
                out.println("   static __" + className + "_VT" + " vtable;");

                out.println("};");

                // printing the virtual table
                out.println("\n");
                out.println("struct __" + className + "_VT\n{");
                out.println("\n");
                out.println("   // the dynamic type");
                out.println("   Class __isa;");
                out.println("\n");

                out.println("   // the function pointers");
                for (int i = 0; i < methods1.size(); i++) {
                    out.println("   " + methodTypes.get(i) + " (*" + methods1.get(i) + ")(" + className + ");");
                }

                // vtable constructor
                out.println("\n");
                out.println("   __" + className + "_VT()");
                out.println("    :__isa(__" + className + "::__class()),");
                for (int i = 0; i < methods1.size(); i++) {
                    out.println("     " + methods1.get(i) + "(&__" + className + "::" + methods1.get(i) + "),");
                }
                out.println("    {}");
                out.println("\n");
                out.println("};");


            } else {

            }
        }

    }

}

class LoadFile {
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
