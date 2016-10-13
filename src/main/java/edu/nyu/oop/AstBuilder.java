package edu.nyu.oop;

import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

import static java.lang.System.out;

import java.lang.*;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Garrett on 10/12/16.
 */
public class AstBuilder extends Visitor {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private Runtime runtime;

    private AstBuilder.AstBuilderSummary summary = new AstBuilder.AstBuilderSummary();

    public void visitCompilationUnit(GNode n) {
        if (summary.first) {
            summary.first = false;
            summary.classCount = n.size() - 1;
            System.out.println("The number of classes " + summary.classCount);
        }
        visit(n);
        runtime.console().flush();
    }

    public void visitPackageDeclaration(GNode n) {
        if (summary.headerDeclaration == true) {
            for (Object o : n) {
                System.out.println(o + " <- visit package declaration");
                if (o instanceof Node) {
                    visitQualifiedIdentifier((GNode) o);
                }
            }
        }
    }

    public void visitQualifiedIdentifier(GNode n) {
        if (summary.headerDeclaration == true) {
            System.out.println(n + " <- visit qualified identifier");
            for (Object o : n) {
                System.out.println(o + " <- visit qualified identifier");
                summary.tempClass.add((String) o);
            }
            summary.headerDeclaration = false;
            summary.PackageDeclarations.add(summary.tempClass);
        }
        if (summary.classBody == true && !summary.parameterTypes == true) {
            summary.tempMethodTypes.add(n.getString(0));
            System.out.println(n.getString(0) + " <- QualifiedIdentifier method declaration");
        }
        if (summary.methodBody == true) {
            summary.tempMethodBody.add(n.getString(0));
            out.println(n.getString(0) + " <- QualifiedIdentifier method body");
        }
        if (summary.parameterTypes == true) {
            out.println(n.getString(0) + "      <- QualifiedIdentifier parameter ");
            summary.tempMethods.add(n.getString(0));
        }
    }

    public void visitClassDeclaration(GNode n) {
        if (summary.classDeclaration == true) {
            summary.classCounter++;
            summary.methodCounter.add(0);
            summary.tempClassDec.add(summary.classCounter + "");
            for (Object o : n) {
                if (o instanceof Node) {
                    if (((Node) o).getName() == "Modifiers") {
                        summary.classModifiers = true;
                        System.out.println(o + " <- visit class declaration");
                        visitModifiers((GNode) o);
                    } else if (((Node) o).getName() == "ClassBody") {
                        summary.classDeclaration = false;
                        summary.classBody = true;
                        visitClassBody((GNode) o);
                        return;
                    }
                } else {
                    System.out.println(o + " <- visit class declaration");
                    if (o != null) {
                        summary.tempClassDec.add((String) o);
                    }
                }
            }
        }
    }

    public void visitClassBody(GNode n) {
        if (summary.classBody == true) {
            summary.tempMethodTypes.add(summary.classCounter + "");
            for (Object o : n) {
                if (o instanceof Node) {
                    System.out.println(o + " <- visit classbody node");
                    summary.tempMethods.add(summary.classCounter + "");
                    if (o.toString().contains("MethodDeclaration")) {
                        out.println("-------");
                        out.println(((Node) o).getName());
                        int temp = summary.methodCounter.get(summary.classCounter - 1);
                        temp++;
                        summary.methodCounter.set(summary.classCounter - 1, temp);
                        out.println(summary.methodCounter.get(summary.classCounter - 1) + " <- current method count");
                        out.println("-------");
                        summary.methodDeclaration = true;
                        for (Object o1 : (Node) o) {
                            out.println(o1 + "<- CURRENT OBJECT PRINTING");
                            if (o1 instanceof Node) {
                                out.println(((Node) o1).getName() + " <- CURRENT NAME ");
                                if (((Node) o1).getName() == "Modifiers") {
                                    System.out.println(o1 + " <- Modifiers method declaration");
                                    visitModifiers((GNode) o1);
                                } else if (((Node) o1).getName() == "VoidType") {
                                    System.out.println(o1.toString() + " <- voidtype");
                                    summary.tempMethodTypes.add("Void");
                                } else if (((Node) o1).getName() == "Type") {
                                    if (o1 instanceof Node) {
                                        summary.methodType = true;
                                        out.println(" <-- inside of Type");
                                        visitType((GNode) o1);
                                    }
                                } else if (((Node) o1).getName() == "FormalParameters") {
                                    summary.methodParameters = true;
                                    System.out.println(o1 + " <- formal parameters");
                                    visitFormalParameters((GNode) o1);
                                } else if (((Node) o1).getName() == "Block") {
                                    summary.methodBody = true;
                                    summary.classDeclaration = true;
                                    summary.classBody = false;
                                    summary.tempMethodBody.add(summary.classCounter + "");
                                    summary.tempMethodBody.add(summary.methodCounter.get(summary.classCounter - 1) + "");
                                    if (o1 instanceof Node) {
                                        visit((GNode) o1);
                                    }
                                    summary.methodBody = false;
                                } else {
                                    out.println("NOT MARKED YET -> " + o1);
                                }
                            } else {
                                if (o1 != null) {
                                    System.out.println(o1 + " <- added to MethodDeclarationList");
                                    summary.tempMethods.add(o1.toString());
                                    System.out.println(summary.tempMethods);
                                } else {
                                    System.out.println(o1 + " <- Not instance of node/added as string");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println(o + " <- visit classbody");
                    if (o != null) {
                        summary.tempClassDec.add((String) o);
                    }
                }
            }
            summary.classBody = false;
        }
    }

    public void visitType(GNode n) {
        if (summary.methodType == true) {
            out.println("   -> Type method body");
            out.println("   -> " + n.getNode(0).getString(0));
            summary.tempMethodTypes.add(n.getNode(0).getString(0));
            summary.methodType = false;
        } else if (summary.parameterTypes == true) {
            out.println("   -> Type Parameters");
            for (Object o : n) {
                out.println(o);
                if (o instanceof Node) {
                    if (((Node) o).getName() == "QualifiedIdentifier") {
                        visitQualifiedIdentifier((GNode) o);
                    } else if (((Node) o).getName() == "Dimensions") {
                        summary.tempMethods.add("[]");
                    }
                } else {
                    if (o != null) {
                        summary.tempMethods.add(o.toString());
                    }
                }
            }
            summary.parameterTypes = false;
        }
    }

    public void visitArguments(GNode n) {
        if (summary.methodBody == true) {
            out.println("   -> Arguments method body");
            for (Object o : n) {
                if (o instanceof Node) {
                    if (((GNode) o).getName() == "CallExpression") {
                        visitCallExpression((GNode) o);
                    }
                } else {
                    if (o != null) {
                        summary.tempMethodBody.add(o.toString());
                    }
                }
            }
        }
    }

    public void visitExpressionStatement(GNode n) {
        if (summary.methodBody == true) {
            summary.tempMethodBody.add("ExpressionStatement");
            out.println("ExpressionStatement method body");
            for (Object o : n) {
                if (o instanceof Node) {
                    if (((Node) o).getName() == "CallExpression") {
                        visitCallExpression((GNode) o);
                    }
                }
            }
        }
    }

    public void visitCallExpression(GNode n) {
        if (summary.methodBody == true) {
            out.println("    -> Call expression method body");
            for (Object o : n) {
                out.println("       -> sub CallExpression " + o);
                if (o instanceof Node) {
                    if (((Node) o).getName() == "SelectionExpression") {
                        visitSelectionExpression((GNode) o);
                    } else if (((Node) o).getName() == "Arguments") {
                        visitArguments((GNode) o);
                    } else if (((Node) o).getName() == "PrimaryIdentifier") {
                        visitPrimaryIdentifier((GNode) o);
                    }
                } else {
                    if (o != null) {
                        out.println("   -> " + o.toString());
                        summary.tempMethodBody.add(o.toString());
                    }
                }
            }
        }
    }

    public void visitSelectionExpression(GNode n) {
        if (summary.methodBody == true) {
            out.println("   -> Selection expression method body");
            for (Object o : n) {
                if (o instanceof Node) {
                    //visit((GNode) o);
                    if (((GNode) o).getName() == "PrimaryIdentifier") {
                        visitPrimaryIdentifier((GNode) o);
                    }
                } else {
                    if (o != null) {
                        out.println("   -> " + o.toString());
                        summary.tempMethodBody.add(o.toString());
                    }
                }
            }
        }
    }

    public void visitPrimaryIdentifier(GNode n) {
        if (summary.methodBody == true) {
            out.println("   -> PrimaryIdentifier method body");
            for (Object o : n) {
                if (o instanceof Node) {
                    visit((GNode) o);
                } else {
                    if (o != null) {
                        out.println("   ->" + o.toString());
                        summary.tempMethodBody.add(o.toString());
                    }
                }
            }
        }
    }

    /**
     * public void visitDeclarators(GNode n) {
     * if (summary.methodBody == true) {
     * out.println("-==================\n\n");
     * out.println("   -> Declarators method body");
     * for (Object o : n) {
     * visit((GNode) o);
     * }
     * }
     * }
     **/

    public void visitDeclarator(GNode n) {
        if (summary.methodBody == true) {
            out.println("   -> Declarator method body");
            for (Object o : n) {
                if (o instanceof Node) {
                    out.println("       " + ((Node) o).getName());
                    //visit((GNode)o);
                    visitNewClassExpression((GNode) o);
                } else {
                    if (o != null) {
                        out.println("       " + o.toString());
                        summary.tempMethodBody.add(o.toString());
                    }
                }
            }
        }
    }

    public void visitNewClassExpression(GNode n) {
        if (summary.methodBody == true) {
            out.println("   -> new class expression method body");
            summary.tempMethodBody.add("new");
            for (Object o : n) {
                if (o instanceof Node) {
                    if (((Node) o).getName() == "QualifiedIdentifier") {
                        visitQualifiedIdentifier((GNode) o);
                    }
                }
            }
        }
    }


    public void visitFieldDeclaration(GNode n) {
        if (summary.methodBody == true) {
            out.println("FieldDeclaration method body");
            summary.tempMethodBody.add("FieldDeclaration");
            for (Object o : n) {
                if (o instanceof Node) {
                    out.println("   " + ((Node) o).getName());
                    visit((GNode) o);
                }
            }
        }
    }

    public void visitReturnStatement(GNode n) {
        out.println(n + " <- return statement ");
        if (summary.methodBody == true) {
            for (Object o : n) {
                out.println(o + " <- return statement sub methodBody");
                if (o instanceof Node) {
                    summary.tempMethodBody.add("RETURN");
                    if (((Node) o).getName() == "StringLiteral") {
                        summary.tempMethodBody.add(((Node) o).getString(0));
                    }
                }
            }
        }
    }

    public void visitModifier(GNode n) {
        if (summary.classModifiers == true) {
            out.println("   class modifier -> " + n.toString());
            summary.tempClassDec.add(n.getString(0));
        } else if (summary.classBody == true) {
            System.out.println(n.getString(0) + " <- modifier method declaration");
            summary.tempMethodTypes.add(n.getString(0));
        }
    }

    public void visitModifiers(GNode n) {
        if (summary.classModifiers == true) {
            int counter = 0;
            for (Object o : n) {
                System.out.println(o + " <- class declaration modifiers");
                if (o instanceof Node) {
                    if (((Node) o).getName() == "Modifier") {
                        visitModifier((GNode) o);
                    }
                }
            }
            summary.classModifiers = false;
        } else if (summary.classBody == true) {
            int counter = 0;
            for (Object o : n) {
                System.out.println(o + " <- block declaration modifiers");
                for (Object o2 : (Node) o) {
                    out.println(o2);
                }
            }
        } else if (summary.methodBody == true) {
            out.println(" <- Modifiers method body ");
        }
    }

    public void visitStringLiteral(GNode n) {
        out.println("hello");
        if (summary.classBody == true) {
            System.out.println(n.getString(0) + " <- String literal Return statement");
            summary.tempMethods.add(n.getString(0));
        }
        if (summary.methodBody == true) {
            out.println(n + " <- inside method body string literal");
        }
    }

    public void visitFormalParameters(GNode n) {
        if (summary.methodParameters == true) {
            out.println("   -> FormalParameters method body");
            for (Object o : n) {
                if (o instanceof Node) {
                    visitFormalParameter((GNode) o);
                } else {
                    if (o != null) {
                        summary.tempMethods.add(o.toString());
                    }
                }
            }
        }
    }

    public void visitFormalParameter(GNode n) {
        if (summary.methodParameters == true) {
            out.println("   -> FormalParameter method body");
            for (Object o : n) {
                if (o instanceof Node) {
                    summary.parameterTypes = true;
                    if (((Node) o).getName() == "Type") {
                        visitType((GNode) o);
                    }
                } else {
                    if (o != null) {
                        out.println("   ->" + o.toString());
                        summary.tempMethods.add(o.toString());
                    }
                }
            }
        }
    }


    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) {
                if (((Node) o).getName() == "ClassDeclaration") {
                    out.println("======");
                    out.println(((Node) o).getName());
                    out.println("======");
                }
                dispatch((Node) o);
            }
        }
    }


    private void scope(Node curr, Node next) {
        //runtime.console().p("Entering scope at ").loc(curr).pln();
        if (null != next) visit(next);
        //runtime.console().p("Exiting scope at ").loc(curr).pln();
    }

    public AstBuilder(Runtime runtime) {
        this.runtime = runtime;
    }

    public AstBuilder.AstBuilderSummary getSummary(Node n) {
        super.dispatch(n);
        return summary;
    }

    public AstBuilder.AstBuilderSummary getClassDeclarations(Node n) {

        summary.headerDeclaration = true;
        summary.tempClass = new ArrayList<String>();
        super.dispatch(n);

        summary.headerDeclaration = false;

        summary.classDeclaration = true;
        summary.tempMethodTypes = new ArrayList<String>();
        summary.tempClassDec = new ArrayList<String>();
        summary.tempMethods = new ArrayList<String>();
        summary.tempMethodBody = new ArrayList<String>();

        super.dispatch(n);


        out.println("\n");
        out.println("Package declarations: " + summary.PackageDeclarations);
        out.println("Class declarations: " + summary.tempClassDec);
        out.println("Method types: " + summary.tempMethodTypes);
        out.println("Methods: " + summary.tempMethods);
        out.println("MethodBody: " + summary.tempMethodBody);

        summary.tempClass = new ArrayList<String>();

        ArrayList<String> classTemp = new ArrayList<String>();
        for (int j = 1; j < summary.tempClassDec.size(); j++) {
            if (isNumeric(summary.tempClassDec.get(j))) {
                summary.ClassDeclarations.add(classTemp);
                classTemp = new ArrayList<String>();
            } else {
                classTemp.add(summary.tempClassDec.get(j));
            }
        }
        summary.ClassDeclarations.add(classTemp);
        out.println("Classes ->" + summary.ClassDeclarations);

        return summary;
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    static class AstBuilderSummary {

        Boolean first = true;
        int classCount = 0;
        int classCounter = 0;
        ArrayList<Integer> methodCounter = new ArrayList<Integer>();

        ArrayList<String> tempClass;
        ArrayList<String> tempClassDec;
        ArrayList<String> tempMethodTypes;
        ArrayList<String> tempMethods;
        ArrayList<String> tempMethodBody;


        Boolean headerDeclaration = false;
        List<ArrayList<String>> PackageDeclarations = new ArrayList<ArrayList<String>>();


        Boolean classDeclaration = false;
        Boolean classModifiers = false;
        Boolean classBody = false;
        Boolean classBodyModifiers = false;
        Boolean methodDeclaration = false;
        Boolean methodBody = false;
        Boolean methodType = false;
        Boolean methodParameters = false;
        Boolean parameterTypes = false;

        List<ArrayList<String>> ClassDeclarations = new ArrayList<ArrayList<String>>();


    }
}
