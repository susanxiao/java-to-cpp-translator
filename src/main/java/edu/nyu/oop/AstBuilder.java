package edu.nyu.oop;

import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

import static java.lang.System.out;

import java.lang.*;


import java.lang.reflect.Array;
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
        if (summary.classBody == true) {
            summary.tempClassBody.add(n.getString(0));
            System.out.println(n.getString(0) + " <- QualifiedIdentifier method declaration");
        }
    }

    public void visitClassDeclaration(GNode n) {
        //System.out.println(n.getString(1));
        if (summary.classDeclaration == true) {
            summary.classCounter++;
            summary.tempClassDec.add(summary.classCounter + "");
            for (Object o : n) {
                if (o instanceof Node) {
                    summary.classModifiers = true;
                    if (o.toString().contains("Modifiers") && !o.toString().contains("ClassBody")) {
                        System.out.println(o + " <- visit class declaration");
                        visitModifiers((GNode) o);
                    } else if (o.toString().contains("ClassBody")) {
                        System.out.println("\ngetClassBody running");
                        summary.classDeclaration = false;
                        summary.classBody = true;
                        visit(n);
                        return;
                    }
                } else {
                    System.out.println(o + " <- visit class declaration");
                    summary.tempClassDec.add((String) o);
                }
            }
        }
    }

    public void visitClassBody(GNode n) {
        if (summary.classBody == true) {
            summary.tempClassBody.add(summary.classCounter + "");
            for (Object o : n) {
                if (o instanceof Node) {
                    System.out.println(o + " <- visit classbody node");
                    summary.tempMethodDeclaration.add(summary.classCounter + "");
                    if (o.toString().contains("MethodDeclaration")) {
                        for (Object o1 : (Node) o) {
                            if (o1 instanceof Node) {
                                if (o1.toString().contains("Modifiers")) {
                                    System.out.println(o1 + " <- Modifiers method declaration");
                                    visit((GNode) o1);
                                } else if (o1.toString().equals("Type")) {
                                    if (o1 instanceof Node) {
                                        if (o1.toString().contains("QualifiedIdentifier")) {
                                            visit((GNode) o1);
                                        }
                                    }
                                } else if (o1.toString().contains("VoidType")) {
                                    System.out.println(o1.toString() + " <- voidtype");
                                    summary.tempClassBody.add("Void");
                                } else if (o1.toString().contains("FormalParameters")) {
                                    System.out.println(o1 + " <- formal parameters not implemented");
                                } else if (o1.toString().contains("Block")) {
                                    if (o1 instanceof Node) {
                                        for (Object o2 : (Node) o1) {
                                            out.println(o2 + " <- block testing");
                                            if (o2.toString().contains("ReturnStatement")) {
                                                summary.classDeclaration = true;
                                                for (Object o3 : (Node) o2) {
                                                    if (o3.toString().contains("StringLiteral")) {
                                                        visit((GNode) o2);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    out.println("NOT MARKED YET -> " + o1);
                                }
                            } else {
                                if (o1 != null) {
                                    System.out.println(o1 + " <- added to MethodDeclarationList");
                                    summary.tempMethodDeclaration.add(o1.toString());
                                    System.out.println(summary.tempMethodDeclaration);
                                } else {
                                    System.out.println(o1 + " <- Not instance of node/added as string");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println(o + " <- visit classbody");
                    summary.tempClassDec.add((String) o);
                }
            }
            summary.classBody = false;
        }
    }

    public void visitModifier(GNode n) {
        if (summary.classBody == true) {
            System.out.println(n.getString(0) + " <- modifier method declaration");
            summary.tempClassBody.add(n.getString(0));
        }
    }

    public void visitStringLiteral(GNode n) {
        if (summary.classBody == true) {
            System.out.println(n.getString(0) + " <- String literal Return statement");
            summary.tempMethodDeclaration.add(n.getString(0));
        }
    }

    public void visitModifiers(GNode n) {
        if (summary.classModifiers == true) {
            int counter = 0;
            for (Object o : n) {
                System.out.println(o + " <- class declaration modifiers");
                for (Object o2 : (Node) o) {
                    out.println(o2);
                }
            }
            summary.classModifiers = false;
        }
    }

    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) {
                if (o.toString().contains("ClassDeclaration")) {
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

    public AstBuilder.AstBuilderSummary getPackageDeclaration(Node n) {

        System.out.println("\ngetPackageDeclaration Running");
        summary.headerDeclaration = true;
        summary.tempClass = new ArrayList<String>();
        super.dispatch(n);
        System.out.println("getPackageDeclaration Complete");
        System.out.println("Package Declaration ArrayList " + summary.PackageDeclarations + "\n");

        return summary;
    }

    public AstBuilder.AstBuilderSummary getClassDeclarations(Node n) {
        System.out.println("getClassDeclarations running");
        summary.classDeclaration = true;
        summary.tempClassBody = new ArrayList<String>();
        summary.tempClassDec = new ArrayList<String>();
        summary.tempMethodDeclaration = new ArrayList<String>();

        super.dispatch(n);
        summary.ClassDeclarations.add(summary.tempClassDec);
        summary.ClassDeclarations.add(summary.tempClassBody);
        summary.ClassDeclarations.add(summary.tempMethodDeclaration);
        System.out.println("getClassBody complete");
        System.out.println("ClassDeclarations list " + summary.ClassDeclarations);
        return summary;
    }


    static class AstBuilderSummary {

        Boolean first = true;
        int classCount = 0;
        int classCounter = 0;

        ArrayList<String> tempClass;
        ArrayList<String> tempClassDec;
        ArrayList<String> tempClassBody;
        ArrayList<String> tempMethodDeclaration;


        Boolean headerDeclaration = false;
        List<ArrayList<String>> PackageDeclarations = new ArrayList<ArrayList<String>>();


        Boolean classDeclaration = false;
        Boolean classModifiers = false;
        Boolean classBody = false;
        Boolean classBodyModifiers = false;
        List<ArrayList<String>> ClassDeclarations = new ArrayList<ArrayList<String>>();
    }
}
