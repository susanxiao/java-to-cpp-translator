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
public class AstTraversal extends Visitor {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private Runtime runtime;
    boolean debug = true;
    private AstTraversalSummary summary = new AstTraversalSummary();

    /**
     * visitXXX methods
     */
    public void visitCompilationUnit(GNode n) {
        if (summary.first) {
            summary.first = false;
            summary.classCount = n.size() - 1;
            if (debug) {
                out.println("The number of classes " + summary.classCount);
            }
        }
        visit(n);
        runtime.console().flush();
    }

    public void visitPackageDeclaration(GNode n) {
        if (summary.headerDeclaration) {
            for (Object o : n) {
                if (o instanceof Node) {
                    visitQualifiedIdentifier((GNode) o);
                }
            }
        }
    }

    public void visitQualifiedIdentifier(GNode n) {
        if (summary.headerDeclaration) {
            for (Object o : n) {
                summary.tempClass.add((String) o);
            }
            summary.headerDeclaration = false;
            summary.PackageDeclarations.add(summary.tempClass);
        }
        if (summary.classBody && !summary.parameterTypes) {
            summary.tempMethodTypes.add(n.getString(0));
        }
        if (summary.methodBody) {
            summary.tempMethodBody.add(n.getString(0));
        }
        if (summary.parameterTypes) {
            summary.tempMethods.add(n.getString(0));
        }
    }

    public void visitClassDeclaration(GNode n) {
        if (summary.classDeclaration) {
            summary.classCounter++;
            if(debug){
                out.println("current class count " + summary.classCounter);
            }
            summary.methodCounter.add(0);
            summary.tempClassDec.add(summary.classCounter + "");
            for (Object o : n) {
                if (o instanceof Node) {
                    if (((Node) o).getName() == "Modifiers") {
                        summary.classModifiers = true;
                        visitModifiers((GNode) o);
                    } else if (((Node) o).getName() == "ClassBody") {
                        summary.classDeclaration = false;
                        summary.classBody = true;
                        visitClassBody((GNode) o);
                        return;
                    }
                } else {
                    if (o != null) {
                        summary.tempClassDec.add((String) o);
                    }
                }
            }
        }
    }

    public void visitClassBody(GNode n) {
        if (summary.classBody) {
            summary.tempMethodTypes.add(summary.classCounter + "");
            for (Object o : n) {
                if (o instanceof Node) {
                    summary.tempMethods.add(summary.classCounter + "");
                    if (o.toString().contains("MethodDeclaration")) {
                        int temp = summary.methodCounter.get(summary.classCounter - 1);
                        temp++;
                        summary.methodCounter.set(summary.classCounter - 1, temp);
                        if (debug) {
                            out.println(summary.methodCounter.get(summary.classCounter - 1) + " <- current method count");
                        }
                        summary.methodDeclaration = true;
                        for (Object o1 : (Node) o) {
                            if (o1 instanceof Node) {
                                if (((Node) o1).getName() == "Modifiers") {
                                    visitModifiers((GNode) o1);
                                } else if (((Node) o1).getName() == "VoidType") {
                                    summary.tempMethodTypes.add("Void");
                                } else if (((Node) o1).getName() == "Type") {
                                    if (o1 instanceof Node) {
                                        summary.methodType = true;
                                        visitType((GNode) o1);
                                    }
                                } else if (((Node) o1).getName() == "FormalParameters") {
                                    summary.methodParameters = true;
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
                                    summary.tempMethods.add(o1.toString());
                                } else {
                                }
                            }
                        }
                    }
                } else {
                    if (o != null) {
                        summary.tempClassDec.add((String) o);
                    }
                }
            }
            summary.classBody = false;
        }
    }

    public void visitType(GNode n) {
        if (summary.methodType) {
            summary.tempMethodTypes.add(n.getNode(0).getString(0));
            summary.methodType = false;
        } else if (summary.parameterTypes) {
            for (Object o : n) {
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
        if (summary.methodBody) {
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
        if (summary.methodBody) {
            summary.tempMethodBody.add("ExpressionStatement");
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
        if (summary.methodBody) {
            for (Object o : n) {
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
                        summary.tempMethodBody.add(o.toString());
                    }
                }
            }
        }
    }

    public void visitSelectionExpression(GNode n) {
        if (summary.methodBody) {
            for (Object o : n) {
                if (o instanceof Node) {
                    //visit((GNode) o);
                    if (((GNode) o).getName() == "PrimaryIdentifier") {
                        visitPrimaryIdentifier((GNode) o);
                    }
                } else {
                    if (o != null) {
                        summary.tempMethodBody.add(o.toString());
                    }
                }
            }
        }
    }

    public void visitPrimaryIdentifier(GNode n) {
        if (summary.methodBody) {
            for (Object o : n) {
                if (o instanceof Node) {
                    visit((GNode) o);
                } else {
                    if (o != null) {
                        summary.tempMethodBody.add(o.toString());
                    }
                }
            }
        }
    }

    public void visitDeclarator(GNode n) {
        if (summary.methodBody) {
            for (Object o : n) {
                if (o instanceof Node) {
                    visitNewClassExpression((GNode) o);
                } else {
                    if (o != null) {
                        summary.tempMethodBody.add(o.toString());
                    }
                }
            }
        }
    }

    public void visitNewClassExpression(GNode n) {
        if (summary.methodBody) {
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
        if (summary.methodBody) {
            summary.tempMethodBody.add("FieldDeclaration");
            for (Object o : n) {
                if (o instanceof Node) {
                    visit((GNode) o);
                }
            }
        }
    }

    public void visitReturnStatement(GNode n) {
        if (summary.methodBody) {
            for (Object o : n) {
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
        if (summary.classModifiers) {
            summary.tempClassDec.add(n.getString(0));
        } else if (summary.classBody) {
            summary.tempMethodTypes.add(n.getString(0));
        }
    }

    public void visitModifiers(GNode n) {
        if (summary.classModifiers) {
            int counter = 0;
            for (Object o : n) {
                if (o instanceof Node) {
                    if (((Node) o).getName() == "Modifier") {
                        visitModifier((GNode) o);
                    }
                }
            }
            summary.classModifiers = false;
        } else if (summary.classBody) {
            int counter = 0;
            for (Object o : n) {
                for (Object o2 : (Node) o) {
                    //out.println(o2);
                }
            }
        } else if (summary.methodBody) {
        }
    }

    public void visitStringLiteral(GNode n) {
        if (summary.classBody) {
            summary.tempMethods.add(n.getString(0));
        }
        if (summary.methodBody) {
        }
    }

    public void visitFormalParameters(GNode n) {
        if (summary.methodParameters) {
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
        if (summary.methodParameters) {
            for (Object o : n) {
                if (o instanceof Node) {
                    summary.parameterTypes = true;
                    if (((Node) o).getName() == "Type") {
                        visitType((GNode) o);
                    }
                } else {
                    if (o != null) {
                        summary.tempMethods.add(o.toString());
                    }
                }
            }
        }
    }

    /**
     * visit method
     */

    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) {
                dispatch((Node) o);
            }
        }
    }


    public AstTraversal(Runtime runtime) {
        this.runtime = runtime;
    }


    public AstTraversalSummary getTraversal(Node n) {

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


        if (debug) {
            out.println("\n");
            out.println("AST TRAVERSAL");
            out.println("DEBUGGING IS ON");
            out.println("   Package declarations: " + summary.PackageDeclarations);
            out.println("   Class declarations: " + summary.tempClassDec);
            out.println("   Method types: " + summary.tempMethodTypes);
            out.println("   Methods: " + summary.tempMethods);
            out.println("   MethodBody: " + summary.tempMethodBody);
        }

        return summary;
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    static class AstTraversalSummary {

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


    }
}
