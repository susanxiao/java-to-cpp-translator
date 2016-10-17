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

    public static void main(String[] args) {
        boolean debug = true;

        GNode node = null;
        LoadFile temp = new LoadFile();
        node = (GNode) LoadFile.loadTestFile("./src/test/java/inputs/test001/Test001.java");
        AstTraversal visitor = new AstTraversal(LoadFile.newRuntime());
        AstTraversal.AstBuilderSummary summary = visitor.getClassDeclarations(node);


        ArrayList<String> classDeclarations = summary.tempClassDec;
        ArrayList<String> classes = new ArrayList<String>();
        for (Object o : classDeclarations) {
            if (o.toString() != "public" && o.toString() != "private" && !isNumeric(o.toString())) {
                classes.add(o.toString());
            }
        }

        ArrayList<String> methods = new ArrayList<String>();
        ArrayList<String> methods1 = new ArrayList<String>();
        ArrayList<String> methodTypes = new ArrayList<String>();
        ArrayList<String> methodBody = new ArrayList<String>();

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
                            if (parseInt(classCheck) != parseInt(tempNumber)) {
                                continue;
                            }
                            methods.add(o1.toString());
                        }
                    }
                }
                if (debug) {
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
                            if (parseInt(classCheck) != parseInt(tempNumber)) {
                                continue;
                            }
                            methodTypes.add(o1.toString());
                        }
                    }
                }

                for (Object o1 : summary.tempMethodBody) {
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
                            methodBody.add(o1.toString());
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

                if (debug) {
                    out.println("methodTypes for " + tempNumber);
                    out.println(methodTypes);
                    out.println("current classname " + className);
                    out.println("method body for " + tempNumber);
                    out.println(methodBody);
                    out.println("methods " + methods1);
                }

                out.println("\n");
                out.println("// " + className + " implementations");
                out.println("__" + className + "::__" + className + "()" + " : " + "__vptr(&__vtable) {}");

                int counter = 0;
                for (Object o2 : methods1) {
                    out.println("\n");
                    out.print(methodTypes.get(counter) + " __" + className + "::" + methods1.get(counter));
                    out.println("(" + className + " __this) {");
                    if (methods1.get(counter).equals("toString")) {
                        out.println("   std::ostringstream sout");
                        out.println("   sout << " + methodBody.get(2) + ";");
                        out.println("   return new __String(sout.str());");
                    } else if (methods1.get(counter).equals("main")) {
                        out.println("   MAIN METHOD IMPLEMENTATION");
                    }
                    out.println("}");
                }
                out.print("\n");

                out.println("Class __" + className + "::" + "__class(){");
                out.println("   static Class k =");
                out.println("     new __Class(__rt::literal(\"class inputs.javalang." + className + "\"), (Class) __rt::null());");
                out.println("   return k\n}");
                out.print("\n");

                out.println("__" + className + "_VT" + " __" + className + "::__vtable;");
                out.print("\n");

            }

        }
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
