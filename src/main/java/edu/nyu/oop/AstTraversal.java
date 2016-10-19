package edu.nyu.oop;

import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

import static java.lang.System.out;

import java.lang.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

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
/*    public void visitCompilationUnit(GNode n) {
        if (summary.first) {
            summary.first = false;
            summary.classCount = n.size() - 1;
            if (debug) {
                out.println("The number of classes " + summary.classCount);
            }
        }
        visit(n);
        runtime.console().flush();
    }*/

    public void visitPackageDeclaration(GNode n) {
        //Node qualifiedIdentifier = (Node) n.getProperty("QualifiedIdentifier");
        Node qualifiedIdentifier = n.getNode(1);
        for (int i = 0; i < qualifiedIdentifier.size(); i++) {
            summary.currentPackages.add(qualifiedIdentifier.getString(i));
        }

    }

    public void visitClassDeclaration(GNode n) {
        String name = n.getString(1);
        if (n.hasProperty("Extension")) {
            Node extension = (Node) n.getProperty("Extension");
            //TODO: add traversal of "Extension" node to find superClass
            //Note: we may need to add all classes first before attempting to extend
        }
        else {
            summary.addClass(null, name);
        }
        visitClassBody(n.getGeneric(5));
    }

    public void visitClassBody(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                if (((Node) o).getName().equals("MethodDeclaration"))
                    visitMethodDeclaration((GNode) o);
                else {
                    if (o != null) {
                        //TODO: If class body contains fields or constructor...?
                    }
                }
            }
        }
    }

    public void visitMethodDeclaration(GNode n) {
        /** Name **/
        String name = n.getString(3);
        Node type = n.getNode(2);
        MethodImplementation m = new MethodImplementation(name);
        summary.addMethod(m);

        /** Return Type **/
        if (type.getName().equals("Type")) {
            Node qualifiedIdentifier = type.getNode(0);
            String returnType = qualifiedIdentifier.getString(0);
            m.setReturnType(returnType);
        }
        else if (type.getName().equals("VoidType")) {
            m.setReturnType("void");
        }

        /** Parameters **/
        //TODO: get parameters
        Node parameters = (Node) n.getProperty("FormalParameters");

        /** Implementation **/
        //TODO: store implementation
    }

    /*public void visitArguments(GNode n) {
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
    }*/

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

        super.dispatch(n);

        if (debug) {
            out.println("\n");
            out.println("DEBUGGING IS ON");
            for (ClassImplementation c : summary.classes.values()) {
                out.println(c.toString());
            }
        }

        return summary;
    }

    static class AstTraversalSummary {

        HashMap<String, ClassImplementation> classes = new HashMap<>();
        ArrayList<String> currentPackages = new ArrayList<>();

        ClassImplementation currentClass;
        MethodImplementation currentMethod;

        public void addClass(ClassImplementation superClass, String name) {
            ClassImplementation c = new ClassImplementation(superClass, name);
            c.packages.addAll(currentPackages);

            classes.put(name, c);
            currentClass = c;
        }

        public void addMethod(MethodImplementation m) {
            currentClass.addMethod(m);
            currentMethod = m;
        }

        public ClassImplementation findClass(String name) {
            return classes.get(name);
        }
    }
}
