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
    public void visitPackageDeclaration(GNode n) {
        //Node qualifiedIdentifier = (Node) n.getProperty("QualifiedIdentifier");
        Node qualifiedIdentifier = n.getNode(1);
        for (int i = 0; i < qualifiedIdentifier.size(); i++) {
            summary.currentPackages.add(qualifiedIdentifier.getString(i));
        }

    }

    public void visitClassDeclaration(GNode n) {
        String name = n.getString(1);
        Node extension = n.getNode(3);
        if (extension != null) {
            //TODO: we may need to add all classes first before attempting to extend
            Node type = n.getNode(0);
            Node qualifiedIdentifier = type.getNode(0);
            String superClassName = qualifiedIdentifier.getString(0);
            ClassImplementation superClass = summary.findClass(superClassName);
            summary.addClass(superClass, name);
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
                        //TODO: If class body contains fields or constructor
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
        visitMethodFormalParameters(n.getGeneric(4));

        /** Implementation **/
        visitMethodBlock(n.getGeneric(7));
    }

    public void visitMethodFormalParameters(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                Node current = (Node) o;
                if (current.getName().equals("FormalParameter")) {
                    Node type = current.getNode(1);
                    Node qualifiedIdentifier = type.getNode(0);

                    String paramType = qualifiedIdentifier.getString(0);
                    String paramName =  current.getString(3);
                    ParameterImplementation currentParam = new ParameterImplementation(paramType, paramName);

                    Node dimensions = type.getNode(1);
                    if (dimensions != null) {
                        currentParam.dimension = dimensions.getString(0);
                    }
                    summary.currentMethod.addParameter(currentParam);
                }
            }
        }
    }

    public void visitMethodBlock(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                Node current = (Node) o;
                if (current.getName().equals("FieldDeclaration")) {
                    FieldDeclaration currentStatement = new FieldDeclaration();

                    Node type = current.getNode(1);
                    Node qualifiedIdentifier = type.getNode(0);
                    String staticType = qualifiedIdentifier.getString(0);
                    currentStatement.staticType = staticType;

                    Node declarators = current.getNode(2);
                    Node declarator = declarators.getNode(0);
                    String variableName = declarator.getString(0);
                    currentStatement.variableName = variableName;

                    //TODO: handle primitive variables that are initialized in the same line

                    Node newClassExpression = declarator.getNode(2);
                    if (newClassExpression != null) { /** non primitive type **/
                        Node newClassQualifiedIdentifier = newClassExpression.getNode(2);
                        String dynamicType = newClassQualifiedIdentifier.getString(0);
                        currentStatement.dynamicType = dynamicType;

                        Node arguments = newClassExpression.getNode(3);
                        //TODO: handle arguments--is this implementation correct
                        //TODO: clean up duplicate code
                        if (arguments.size() > 0) {
                            currentStatement.arguments = new ArrayList<>();
                            ExpressionStatement currentArgument = new ExpressionStatement();
                            for (int i = 0; i < arguments.size(); i++) {
                                Node argumentsCallExpression = arguments.getNode(i);
                                String argumentsPrimaryIdentifier = argumentsCallExpression.getNode(0).getString(0);
                                currentArgument.primaryIdentifier = argumentsPrimaryIdentifier;

                                //TODO: handle fields of primary identifier within arguments

                                String argumentsMethod = argumentsCallExpression.getString(2);
                                currentArgument.method = argumentsMethod;

                                currentStatement.arguments.add(currentArgument);
                            }
                        }
                    }

                    summary.currentMethod.addMethodStatement(currentStatement);
                }
                else if (current.getName().equals("ExpressionStatement")) {
                    ExpressionStatement currentStatement = new ExpressionStatement();
                    Node callExpression = current.getNode(0);
                    Node selectionExpression = callExpression.getNode(0);
                    String primaryIdentifier = selectionExpression.getNode(0).getString(0);
                    currentStatement.primaryIdentifier = primaryIdentifier;

                    for (int i = 1; i < selectionExpression.size(); i++) {
                        if (currentStatement.fields == null)
                            currentStatement.fields = new ArrayList<>();
                        if (selectionExpression.get(i) != null)
                            currentStatement.fields.add(selectionExpression.getString(i));
                    }

                    String method = callExpression.getString(2);
                    currentStatement.method = method;

                    Node arguments = callExpression.getNode(3);
                    if (arguments.size() > 0) {
                        currentStatement.arguments = new ArrayList<>();
                        ExpressionStatement currentArgument = new ExpressionStatement();
                        for (int i = 0; i < arguments.size(); i++) {
                            Node argumentsCallExpression = arguments.getNode(i);
                            String argumentsPrimaryIdentifier = argumentsCallExpression.getNode(0).getString(0);
                            currentArgument.primaryIdentifier = argumentsPrimaryIdentifier;

                            //TODO: handle fields of primary identifier within arguments

                            String argumentsMethod = argumentsCallExpression.getString(2);
                            currentArgument.method = argumentsMethod;

                            currentStatement.arguments.add(currentArgument);
                        }
                    }

                    summary.currentMethod.addMethodStatement(currentStatement);
                }
                else if (current.getName().equals(("ReturnStatement"))) {
                    ReturnStatement currentStatement = new ReturnStatement();
                    Node returnType = current.getNode(0);
                    if (returnType.getName().equals("StringLiteral")) {
                        currentStatement.value = returnType.getString(0);
                    }
                    else {
                        //TODO: nonliteral return value
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
