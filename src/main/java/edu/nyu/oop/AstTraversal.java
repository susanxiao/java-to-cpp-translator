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
        String superClassName = "";
        String modifier = "";

        for (Object o : n) {
            if (o instanceof Node) {
                Node classNode = (Node) o;
                if(classNode.getName().equals("Modifiers")){
                    for(Object o1 : classNode){
                        if(o1 instanceof Node){
                            modifier = classNode.getNode(0).getString(0);
                        }
                    }
                }
                else if (classNode.getName().equals("Extension")) {
                    for (Object o1 : classNode) {
                        if (o1 instanceof Node) {
                            Node extensionNode = (Node) o1;
                            if (extensionNode.getName().equals("Type")) {
                                for (Object o2 : extensionNode) {
                                    if (o2 instanceof Node) {
                                        Node typeNode = (Node) o2;
                                        if (typeNode.getName().equals("QualifiedIdentifier")) {
                                            superClassName = typeNode.getString(0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (superClassName != "") {
            //TODO: we may need to add all classes first before attempting to extend
            /*
            Node type = n.getNode(0);
            Node qualifiedIdentifier = type.getNode(0);
            String superClassName = qualifiedIdentifier.getString(0);
            ClassImplementation superClass = summary.findClass(superClassName);
            */
            ClassImplementation superClass = summary.findClass(superClassName);
            summary.addClass(superClass, name, modifier);
        } else {
            summary.addClass(null, name, modifier);
        }
        //this can be cleaned up
        visitClassBody(n.getGeneric(5));
    }

    public void visitClassBody(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                Node currentNode = (Node) o;
                if (currentNode.getName().equals("MethodDeclaration"))
                    visitMethodDeclaration((GNode) o);
                else if (currentNode.getName().equals("ConstructorDeclaration")) {
                    visitConstructorDeclaration((GNode) o);
                } else if (currentNode.getName().equals("FieldDeclaration")) {
                    visitFieldDeclaration((GNode) o);
                } else {
                    if (o != null) {
                        //TODO: If class body contains fields or constructor
                    }
                }
            }
        }
    }

    public void visitFieldDeclaration(GNode n) {
        FieldDeclaration currentStatement = new FieldDeclaration();
        for (Object o : n) {
            if (o instanceof Node) {
                Node currentNode = (Node) o;
                if (currentNode.getName().equals("Modifiers")) {
                    for (Object o1 : currentNode) {
                        if (o1 instanceof Node) {
                            Node modifiersNode = (Node) o1;
                            if (modifiersNode.getName().equals("Modifier")) {
                                String modifierString = modifiersNode.getString(0);
                                if (modifierString != null) {
                                    currentStatement.modifiers = modifierString;
                                }
                            }
                        }
                    }
                } else if (currentNode.getName().equals("Type")) {
                    for (Object o1 : currentNode) {
                        if (o1 instanceof Node) {
                            Node typeNode = (Node) o1;
                            if (typeNode.getName().equals("QualifiedIdentifier")) {
                                String typeString = typeNode.getString(0);
                                if (typeString != null) {
                                    currentStatement.staticType = typeString;
                                }
                            }
                        }
                    }
                } else if (currentNode.getName().equals("Declarators")) {
                    for (Object o1 : currentNode) {
                        if (o1 instanceof Node) {
                            Node declaratorsNode = (Node) o1;
                            if (declaratorsNode.getName().equals("Declarator")) {
                                for (Object o2 : declaratorsNode) {
                                    if (o2 instanceof Node) {
                                        Node declaratorNode = (Node) o2;
                                        if (declaratorNode.getName().equals("StringLiteral")) {
                                            currentStatement.stringLiteral = declaratorNode.getString(0);
                                        }
                                    } else {
                                        if (o2 != null) {
                                            currentStatement.variableName = o2.toString();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        summary.currentClass.addDeclaration(currentStatement);
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
        } else if (type.getName().equals("VoidType")) {
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
                    String paramName = current.getString(3);
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

                    if (declarator.getNode(2) != null) { /** non primitive type **/
                        String declaratorNodeName = declarator.getNode(2).getName().toString();
                        if (declaratorNodeName.equals("NewClassExpression")) {
                            Node newClassExpression = declarator.getNode(2);
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
                                    String argumentType = arguments.getNode(i).getName().toString();
                                    if (argumentType.toString().equals("PrimaryIdentifier")) {
                                        String argumentsPrimaryIdentifier = argumentsCallExpression.getNode(0).getString(0);
                                        currentArgument.primaryIdentifier = argumentsPrimaryIdentifier;

                                        //TODO: handle fields of primary identifier within arguments
                                        String argumentsMethod = argumentsCallExpression.getString(2);
                                        currentArgument.method = argumentsMethod;

                                        currentStatement.arguments.add(currentArgument);
                                    } else if (argumentType.toString().equals("StringLiteral")) {
                                        String argumentsStringLiteral = argumentsCallExpression.getString(0);


                                        currentArgument.stringLiteral = argumentsStringLiteral;

                                        currentStatement.arguments.add(currentArgument);
                                    }
                                }
                            }
                        } else if (declaratorNodeName.equals("PrimaryIdentifier")) {
                            Node primaryIdentifier = declarator.getNode(2);
                            String primaryIdentifierLiteral = primaryIdentifier.getString(0);
                            currentStatement.primaryIdentifier = primaryIdentifierLiteral;
                        }
                    }
                    summary.currentMethod.addMethodStatement(currentStatement);
                } else if (current.getName().equals("ExpressionStatement")) {
                    ExpressionStatement currentStatement = new ExpressionStatement();
                    for (Object o1 : current) {
                        if (o1 instanceof Node) {
                            Node currentNode = (Node) o1;
                            if (currentNode.getName().equals("Expression")) {
                                int expressionStatementNodeCounter = 0;
                                for (Object o2 : currentNode) {
                                    expressionStatementNodeCounter++;
                                    if (o2 instanceof Node) {
                                        Node expressionNode = (Node) o2;
                                        if (expressionNode.getName().equals("PrimaryIdentifier")) {
                                            String primaryIdentifierString = expressionNode.getString(0);
                                            currentStatement.primaryIdentifier = primaryIdentifierString;
                                        }
                                    } else if (summary.operators.contains(o2.toString())) {
                                        String assignmentString = " ";
                                        assignmentString += o2.toString() + " ";
                                        for (Object o4 : currentNode) {
                                            if (expressionStatementNodeCounter > 0) {
                                                expressionStatementNodeCounter--;
                                                continue;
                                            }
                                            if (o4 instanceof Node) {
                                                Node currentExpressionNode = (Node) o4;
                                                if (currentExpressionNode.getName().equals("PrimaryIdentifier")) {
                                                    String primaryIdentifierString = currentExpressionNode.getString(0);
                                                    assignmentString += primaryIdentifierString;
                                                    currentStatement.assignment = assignmentString;
                                                    summary.currentMethod.addMethodStatement(currentStatement);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (currentNode.getName().equals("CallExpression")) {
                                for (Object o2 : currentNode) {
                                    if (o2 instanceof Node) {
                                        Node callExpressionNode = (Node) o2;
                                        if (callExpressionNode.getName().equals("PrimaryIdentifier")) {
                                            String primaryIdentifierString = callExpressionNode.getString(0);
                                            currentStatement.primaryIdentifier = primaryIdentifierString;
                                        } else if (callExpressionNode.getName().equals("Arguments")) {
                                            currentStatement.arguments = new ArrayList<>();
                                            ExpressionStatement currentArgument = new ExpressionStatement();
                                            for (Object o3 : callExpressionNode) {
                                                if (o3 instanceof Node) {
                                                    Node argumentsNode = (Node) o3;
                                                    if (argumentsNode.getName().equals("StringLiteral")) {
                                                        String literalString = argumentsNode.getString(0);
                                                        currentArgument.stringLiteral = literalString;
                                                    } else if (argumentsNode.getName().equals("CallExpression")) {
                                                        for (Object o4 : argumentsNode) {
                                                            if (o4 instanceof Node) {
                                                                Node callExpressionNodeArgs = (Node) o4;
                                                                if (callExpressionNodeArgs.getName().equals("PrimaryIdentifier")) {
                                                                    String argPrimaryIdentifier = callExpressionNodeArgs.getString(0);
                                                                    currentArgument.primaryIdentifier = argPrimaryIdentifier;
                                                                }
                                                            } else {
                                                                if (o4 != null) {
                                                                    currentArgument.method = o4.toString();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            currentStatement.arguments.add(currentArgument);
                                        } else if (callExpressionNode.getName().equals("SelectionExpression")) {
                                            currentStatement.fields = new ArrayList<>();
                                            for (Object o3 : callExpressionNode) {
                                                if (o3 instanceof Node) {
                                                    Node selectionExpressionNode = (Node) o3;
                                                    if (selectionExpressionNode.getName().equals("PrimaryIdentifier")) {
                                                        currentStatement.primaryIdentifier = selectionExpressionNode.getString(0);
                                                    }
                                                } else {
                                                    if (o3 != null) {
                                                        currentStatement.fields.add(o3.toString());
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        if (o2 != null) {
                                            //
                                            currentStatement.method = o2.toString();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    summary.currentMethod.addMethodStatement(currentStatement);
                }
                // field declaration expression statement
                else if (current.getName().equals(("ReturnStatement"))) {
                    ReturnStatement currentStatement = new ReturnStatement();
                    Node returnType = current.getNode(0);
                    if (returnType.getName().equals("StringLiteral")) {
                        currentStatement.literalValue = returnType.getString(0);
                    }
                    //TODO: nonliteral return value
                    else if (returnType.getName().equals("PrimaryIdentifier")) {
                        ExpressionStatement currentReturnValue = new ExpressionStatement();
                        currentReturnValue.primaryIdentifier = returnType.getString(0);
                        currentStatement.nonLiteralValue = currentReturnValue;
                    }

                    summary.currentMethod.addMethodStatement(currentStatement);
                }else{

                }
            }
        }
        return;
    }

    public void visitConstructorDeclaration(GNode n) {

        /** Name **/
        String name = n.getString(2);
        ConstructorImplementation constructor = new ConstructorImplementation(name);
        summary.addConstructor(constructor);

        /** Parameters **/
        visitConstructorFormalParameters(n.getGeneric(3));

        /** Implementation **/
        visitConstructorImplementation(n.getGeneric(5));
    }

    void visitConstructorFormalParameters(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                Node current = (Node) o;
                if (current.getName().equals("FormalParameter")) {
                    Node type = current.getNode(1);
                    Node qualifiedIdentifier = type.getNode(0);

                    String paramType = qualifiedIdentifier.getString(0);
                    String paramName = current.getString(3);
                    ParameterImplementation currentParam = new ParameterImplementation(paramType, paramName);

                    Node dimensions = type.getNode(1);
                    if (dimensions != null) {
                        currentParam.dimension = dimensions.getString(0);
                    }

                    summary.currentConstructor.addParameter(currentParam);

                }
            }
        }
    }

    public void visitConstructorImplementation(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                Node current = (Node) o;
                if (current.getName().equals("ExpressionStatement")) {
                    ExpressionStatementConstructor currentStatement = new ExpressionStatementConstructor();
                    for (Object o1 : current) {
                        if (o1 instanceof Node) {
                            Node expressionStatementNode = (Node) o1;
                            if (expressionStatementNode.getName().equals("Expression")) {
                                int expressionCounter = 0;
                                for (Object o2 : expressionStatementNode) {
                                    if (o2 instanceof Node) {
                                        Node expressionNode = (Node) o2;
                                        expressionCounter++;
                                        if (expressionNode.getName().equals("PrimaryIdentifier")) {
                                            String primaryIdentifier = expressionNode.getString(0);
                                            currentStatement.primaryIdentifier = primaryIdentifier;
                                        } else if (expressionNode.getName().equals("SelectionExpression")) {
                                            String selectionExpressionString = "";
                                            for (Object o3 : expressionNode) {
                                                if (o3 instanceof Node) {
                                                    Node selectionNode = (Node) o3;
                                                    if (selectionNode.getName().equals("ThisExpression")) {
                                                        selectionExpressionString += "this.";
                                                    }
                                                } else {
                                                    String selectionString = o3.toString();
                                                    selectionExpressionString += selectionString;
                                                    currentStatement.primaryIdentifier = selectionExpressionString;
                                                }
                                            }
                                        }
                                    } else if (summary.operators.contains(o2.toString())) {

                                        String assignmentString = "";
                                        String operator = o2.toString();
                                        assignmentString += operator + " ";

                                        for (Object o3 : (Node) o1) {
                                            if (expressionCounter > 0) {
                                                expressionCounter--;
                                                continue;
                                            }
                                            if (o3 instanceof Node) {
                                                Node currentNode = (Node) o3;
                                                if (currentNode.getName().equals("PrimaryIdentifier")) {
                                                    String primaryIdentifier = currentNode.getString(0);
                                                    assignmentString += primaryIdentifier;
                                                }
                                            }
                                        }
                                        // Not sure about this
                                        currentStatement.assignment = assignmentString;
                                        summary.currentConstructor.addConstructorStatement(currentStatement);
                                        return;
                                    }
                                }
                            }
                        }
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
        ArrayList<String> classNames = new ArrayList<>();

        ArrayList<String> currentPackages = new ArrayList<>();

        ClassImplementation currentClass;
        MethodImplementation currentMethod;
        ConstructorImplementation currentConstructor;

        // operators ?
        String operators = "=+-*/";

        public void addClass(ClassImplementation superClass, String name, String modifier) {
            ClassImplementation c = new ClassImplementation(superClass, name, modifier);
            c.packages.addAll(currentPackages);

            classes.put(name, c);
            classNames.add(name);
            currentClass = c;
        }

        public void addMethod(MethodImplementation m) {
            currentClass.addMethod(m);
            currentMethod = m;
        }

        public void addConstructor(ConstructorImplementation constructor) {
            currentClass.addConstructor(constructor);
            currentConstructor = constructor;
        }

        public ClassImplementation findClass(String name) {
            return classes.get(name);
        }
    }
}
