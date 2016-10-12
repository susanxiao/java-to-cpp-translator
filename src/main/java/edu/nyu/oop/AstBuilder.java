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
        if (summary.classBody == true) {
            summary.tempMethodTypes.add(n.getString(0));
            System.out.println(n.getString(0) + " <- QualifiedIdentifier method declaration");
        }
    }

    public void visitClassDeclaration(GNode n) {
        //System.out.println(n.getString(1));
        if (summary.classDeclaration == true) {
            summary.classCounter++;
            summary.methodCounter.add(0);
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
            summary.tempMethodTypes.add(summary.classCounter + "");
            for (Object o : n) {
                if (o instanceof Node) {
                    System.out.println(o + " <- visit classbody node");
                    summary.tempMethods.add(summary.classCounter + "");
                    if (o.toString().contains("MethodDeclaration")) {
                        out.println("-------");
                        out.println(((Node) o).getName());
                        int temp = summary.methodCounter.get(summary.classCounter-1);
                        temp++;
                        summary.methodCounter.set(summary.classCounter-1,temp);
                        out.println(summary.methodCounter.get(summary.classCounter-1) + " <- current method count");
                        out.println("-------");
                        summary.methodDeclaration = true;
                        for (Object o1 : (Node) o) {
                            out.println(o1 + "<- CURRENT OBJECT PRINTING");
                            if (o1 instanceof Node) {
                                if (o1.toString().contains("Modifiers") && !o1.toString().contains("Block")) {
                                    System.out.println(o1 + " <- Modifiers method declaration");
                                    visit((GNode) o1);
                                } else if (o1.toString().contains("VoidType")) {
                                    System.out.println(o1.toString() + " <- voidtype");
                                    summary.tempMethodTypes.add("Void");
                                } else if (o1.toString().contains("Type")) {
                                    if (o1 instanceof Node) {
                                        out.println(" <-- inside of Type");
                                        visit((GNode) o1);
                                    }
                                } else if (o1.toString().contains("FormalParameters")) {
                                    System.out.println(o1 + " <- formal parameters not implemented");
                                } else if (o1.toString().contains("Block")) {
                                    summary.methodBody = true;
                                    summary.classDeclaration = true;
                                    summary.classBody = false;
                                    summary.tempMethodBody.add(summary.classCounter + "");
                                    summary.tempMethodBody.add(summary.methodCounter.get(summary.classCounter-1) + "");
                                    if (o1 instanceof Node) {
                                        for (Object o2 : (Node) o1) {
                                            out.println("block testing -> " + o1);
                                            visit((GNode) o1);
                                            for (Object o3 : (Node) o2) {
                                                out.println("       " + o3 + " <- sub block testing");
                                            }
                                        }
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
                    summary.tempClassDec.add((String) o);
                }
            }
            summary.classBody = false;
        }
    }

    public void visitFieldDeclaration(GNode n) {
        if (summary.classBody == true) {
            out.println(n.getName() + " <- inside block");
            for (Object o : n) {
                out.println("sub inside block -> " + o);
                if (o instanceof Node) {
                    visit((GNode) o);
                } else {

                }
            }
        }
    }

    public void visitReturnStatement(GNode n) {
        out.println(n + " <- return statement ");
        if(summary.methodBody == true){
            for (Object o : n){
                out.println(o + " <- return statement sub methodBody");
                if(o instanceof Node){
                    summary.tempMethodBody.add("return");
                    if(((Node) o).getName() == "StringLiteral"){
                       summary.tempMethodBody.add(((Node) o).getString(0));
                    }
                }
            }
        }
    }

    public void visitModifier(GNode n) {
        if (summary.classBody == true) {
            System.out.println(n.getString(0) + " <- modifier method declaration");
            summary.tempMethodTypes.add(n.getString(0));
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
        if (summary.classBody == true) {
            int counter = 0;
            for (Object o : n) {
                System.out.println(o + " <- block declaration modifiers");
                for (Object o2 : (Node) o) {
                    out.println(o2);
                }
            }
        }
    }

    public void visitStringLiteral(GNode n) {
        out.println("hello");
        if (summary.classBody == true) {
            System.out.println(n.getString(0) + " <- String literal Return statement");
            summary.tempMethods.add(n.getString(0));
        }
        if (summary.methodBody == true){
            out.println(n + " <- inside method body string literal");
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
        summary.tempMethodTypes = new ArrayList<String>();
        summary.tempClassDec = new ArrayList<String>();
        summary.tempMethods = new ArrayList<String>();
        summary.tempMethodBody = new ArrayList<String>();

        super.dispatch(n);
        summary.ClassDeclarations.add(summary.tempClassDec);
        summary.ClassDeclarations.add(summary.tempMethodTypes);
        summary.ClassDeclarations.add(summary.tempMethods);
        System.out.println("getClassBody complete\n");
        out.println("Class declarations: " + summary.tempClassDec);
        out.println("Method types: " + summary.tempMethodTypes);
        out.println("Methods: " + summary.tempMethods);
        out.println("MethodBody: " + summary.tempMethodBody);
        return summary;
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
        List<ArrayList<String>> ClassDeclarations = new ArrayList<ArrayList<String>>();
    }
}
