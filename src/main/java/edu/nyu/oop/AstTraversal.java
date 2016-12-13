package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

import java.io.File;
import java.lang.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by Garrett on 10/12/16.
 */
public class AstTraversal extends Visitor {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private Runtime runtime;
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
        String superClassName = null;
        String modifier = "";
        summary.constructorCount = 0;

        for (Object o : n) {
            if (o instanceof Node) {
                Node classNode = (Node) o;
                if(classNode.getName().equals("Modifiers")) {
                    for(Object o1 : classNode) {
                        if(o1 instanceof Node) {
                            modifier = classNode.getNode(0).getString(0);
                        }
                    }
                } else if (classNode.getName().equals("Extension")) {
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
        if (superClassName != null) {
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
                    summary.constructorCount += 1;
                    visitConstructorDeclaration((GNode) o);
                } else if (currentNode.getName().equals("FieldDeclaration")) {
                    visitFieldDeclaration((GNode) o);
                } else {
                    //TODO
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
                                    if (modifierString.equals("static"))
                                        currentStatement.isStatic = true;
                                }
                            }
                        }
                    }
                } else if (currentNode.getName().equals("Type")) {
                    for (Object o1 : currentNode) {
                        if (o1 instanceof Node) {
                            Node typeNode = (Node) o1;
                            if (typeNode.getName().equals("QualifiedIdentifier") || typeNode.getName().equals("PrimitiveType")) {
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
                                        if (declaratorNode.getName().equals("StringLiteral") || declaratorNode.getName().equals("IntegerLiteral")) {
                                            currentStatement.literalValue = declaratorNode.getString(0);
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

        if (!(name.startsWith("method"))) {
            switch (name) {
                case "toString":
                case "hashCode":
                case "equals":
                case "getClass":
                    break;
                default:
                    name = "method" + name.substring(0, 1).toUpperCase() + name.substring(1);
            }
        }

        summary.currentMethod = m;

        m.className = summary.currentClass; //reflexivity for overloading

        /** Static **/
        Node modifiers = n.getNode(0);
        for (Object o : modifiers) {
            if (o instanceof Node) {
                Node modifier = (Node) o;
                if (modifier.getString(0).equals("static"))
                    m.isStatic = true;
            }
        }

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
        String overLoadedName = name;
        for (ParameterImplementation p : m.parameters) {
            overLoadedName += p.type.substring(0, 1).toUpperCase() + p.type.substring(1);
        }

        m.overLoadedName = overLoadedName;

        /** Check overloading **/

        ArrayList<MethodImplementation> potentials = summary.currentClass.deepFindMethod(name);

        if (!potentials.isEmpty()) {
            if (potentials.size() == 1 && potentials.get(0).overLoadedName.equals(m.overLoadedName)) {
                //THIS IS OVERRIDING!!!!
            }
            else {
                for (MethodImplementation potential : potentials) {
                    potential.isOverloaded = true;

                    //if the overloadedMethods table does not contain the classes, put them in
                    if (!summary.overLoadedMethods.containsKey(potential.className.name))
                        summary.overLoadedMethods.put(potential.className.name, new HashMap<String, ArrayList<MethodImplementation>>());

                    HashMap<String, ArrayList<MethodImplementation>> potentialOverLoadMap = summary.overLoadedMethods.get(potential.className.name);

                    //if the class-specific overloadedMethods table does not contain the current method, put it in
                    if (!potentialOverLoadMap.containsKey(name)) {
                        //if potential's overLoadMap doesn't have the current method name, it does not have potential in there
                        ArrayList<MethodImplementation> temp = new ArrayList<>();
                        temp.add(potential);
                        potentialOverLoadMap.put(name, temp);

                        //if it does, we do not have to add potential again
                    }
                }

                m.isOverloaded = true;

                if (!summary.overLoadedMethods.containsKey(summary.currentClass.name))
                    summary.overLoadedMethods.put(summary.currentClass.name, new HashMap<String, ArrayList<MethodImplementation>>());

                HashMap<String, ArrayList<MethodImplementation>> mOverLoadMap = summary.overLoadedMethods.get(summary.currentClass.name);


                if (!mOverLoadMap.containsKey(name))
                    mOverLoadMap.put(name, new ArrayList<MethodImplementation>());
                mOverLoadMap.get(name).add(m);
            }
        }

        summary.addMethod(m);
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
                            if (arguments.size() > 0) {
                                currentStatement.arguments = new ArrayList<>();
                                ExpressionStatement currentArgument = new ExpressionStatement();
                                for (int i = 0; i < arguments.size(); i++) {
                                    Node argumentsCallExpression = arguments.getNode(i);
                                    String argumentType = arguments.getNode(i).getName().toString();
                                    if (argumentType.toString().equals("PrimaryIdentifier")) {
                                        String argumentsPrimaryIdentifier = argumentsCallExpression.getNode(0).getString(0);
                                        if (summary.currentMethod.name.equals("main") && argumentsPrimaryIdentifier.equals("args"))
                                            summary.usesArgs = true;
                                        currentArgument.primaryIdentifier = argumentsPrimaryIdentifier;

                                        //TODO: handle fields of primary identifier within arguments
                                        String argumentsMethod = argumentsCallExpression.getString(2);
                                        currentArgument.method = argumentsMethod;

                                        currentStatement.arguments.add(currentArgument);
                                    } else if (argumentType.toString().equals("StringLiteral")) {
                                        String argumentsStringLiteral = argumentsCallExpression.getString(0);


                                        currentArgument.literalAssignment = argumentsStringLiteral;

                                        currentStatement.arguments.add(currentArgument);
                                    }
                                }
                            }
                        } else if (declaratorNodeName.equals("PrimaryIdentifier")) {
                            Node primaryIdentifier = declarator.getNode(2);
                            String primaryIdentifierLiteral = primaryIdentifier.getString(0);
                            if (summary.currentMethod.name.equals("main") && primaryIdentifierLiteral.equals("args"))
                                summary.usesArgs = true;
                            currentStatement.primaryIdentifier = primaryIdentifierLiteral;
                        } else if (declaratorNodeName.equals("SelectionExpression")) {
                            ExpressionStatement assignment = new ExpressionStatement();
                            for (Object o1 : declarator.getNode(2)) {
                                if (o1 instanceof Node) {
                                    String primaryIdentifier = ((Node) o1).getString(0);
                                    if (summary.currentMethod.name.equals("main") && primaryIdentifier.equals("args"))
                                        summary.usesArgs = true;
                                    assignment.primaryIdentifier = primaryIdentifier;
                                } else if (o1 instanceof String) {
                                    if (assignment.fields == null)
                                        assignment.fields = new ArrayList<>();
                                    assignment.fields.add((String) o1);
                                }
                            }
                            currentStatement.assignment = assignment;
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
                                            if (summary.currentMethod.name.equals("main") && primaryIdentifierString.equals("args"))
                                                summary.usesArgs = true;
                                            currentStatement.primaryIdentifier = primaryIdentifierString;
                                        }
                                    } else if (summary.operators.contains(o2.toString())) {
                                        String assignmentString = "";
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
                                                    if (summary.currentMethod.name.equals("main") && primaryIdentifierString.equals("args"))
                                                        summary.usesArgs = true;
                                                    assignmentString += primaryIdentifierString;
                                                    currentStatement.literalAssignment = assignmentString;
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
                                            if (summary.currentMethod.name.equals("main") && primaryIdentifierString.equals("args"))
                                                summary.usesArgs = true;
                                            currentStatement.primaryIdentifier = primaryIdentifierString;
                                        } else if (callExpressionNode.getName().equals("Arguments")) {
                                            currentStatement.arguments = new ArrayList<>();
                                            ExpressionStatement currentArgument = new ExpressionStatement();
                                            for (Object o3 : callExpressionNode) {
                                                if (o3 instanceof Node) {
                                                    Node argumentsNode = (Node) o3;
                                                    if (argumentsNode.getName().equals("StringLiteral")) {
                                                        String literalString = argumentsNode.getString(0);
                                                        currentArgument.literalAssignment = literalString;
                                                    } else if (argumentsNode.getName().equals("CallExpression")) {
                                                        for (Object o4 : argumentsNode) {
                                                            if (o4 instanceof Node) {
                                                                Node callExpressionNodeArgs = (Node) o4;
                                                                if (callExpressionNodeArgs.getName().equals("PrimaryIdentifier")) {
                                                                    String argPrimaryIdentifier = callExpressionNodeArgs.getString(0);
                                                                    if (summary.currentMethod.name.equals("main") && argPrimaryIdentifier.equals("args"))
                                                                        summary.usesArgs = true;
                                                                    currentArgument.primaryIdentifier = argPrimaryIdentifier;
                                                                } else if (callExpressionNodeArgs.getName().equals("SelectionExpression")) {
                                                                    for (Object o5 : callExpressionNodeArgs) {
                                                                        if (o5 instanceof Node) {
                                                                            Node selectionExpressionArgs = (Node) o5;
                                                                            if (selectionExpressionArgs.getName().equals("PrimaryIdentifier")) {
                                                                                String argPrimaryIdentifier = selectionExpressionArgs.getString(0);
                                                                                if (summary.currentMethod.name.equals("main") && argPrimaryIdentifier.equals("args"))
                                                                                    summary.usesArgs = true;
                                                                                currentArgument.primaryIdentifier = argPrimaryIdentifier;
                                                                            }
                                                                        } else if (o5 instanceof String) {
                                                                            if (currentArgument.fields == null)
                                                                                currentArgument.fields = new ArrayList<>();
                                                                            currentArgument.fields.add((String) o5);
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if (o4 != null) {
                                                                    currentArgument.method = o4.toString();
                                                                }
                                                            }
                                                        }
                                                    } else if (argumentsNode.getName().equals("PrimaryIdentifier")) {
                                                        String primaryIdentifier = argumentsNode.getString(0);
                                                        if (summary.currentMethod.name.equals("main") && primaryIdentifier.equals("args"))
                                                            summary.usesArgs = true;
                                                        currentArgument.primaryIdentifier = primaryIdentifier;
                                                    } else if (argumentsNode.getName().equals("SelectionExpression")) {
                                                        for (Object o4 : argumentsNode) {
                                                            if (o4 instanceof Node) {
                                                                Node selectionExpressionArgs = (Node) o4;
                                                                if (selectionExpressionArgs.getName().equals("PrimaryIdentifier")) {
                                                                    String argPrimaryIdentifier = selectionExpressionArgs.getString(0);
                                                                    if (summary.currentMethod.name.equals("main") && argPrimaryIdentifier.equals("args"))
                                                                        summary.usesArgs = true;
                                                                    currentArgument.primaryIdentifier = argPrimaryIdentifier;
                                                                }
                                                            } else if (o4 instanceof String) {
                                                                if (currentArgument.fields == null)
                                                                    currentArgument.fields = new ArrayList<>();
                                                                currentArgument.fields.add((String) o4);
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
                                                        String primaryIdentifier = selectionExpressionNode.getString(0);
                                                        if (summary.currentMethod.name.equals("main") && primaryIdentifier.equals("args"))
                                                            summary.usesArgs = true;
                                                        currentStatement.primaryIdentifier = primaryIdentifier;
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
                } else if (current.getName().equals(("ReturnStatement"))) {
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
                } else {
                    dispatch(current);
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
                    ExpressionStatement currentStatement = new ExpressionStatement();
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
                                            //TODO: this needs to be split up and not all saved as primaryIdentifier i think
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
                                        } else if (expressionNode.getName().equals("ThisExpression")) {
                                            currentStatement.literalAssignment = " = this";
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
                                                } else if (currentNode.getName().equals("StringLiteral")) {
                                                    String stringLiteral = currentNode.getString(0);
                                                    assignmentString += stringLiteral;
                                                }
                                            }
                                        }
                                        // Not sure about this
                                        currentStatement.literalAssignment = assignmentString;
                                    }
                                }
                            } else if (expressionStatementNode.getName().equals("CallExpression")) {
                                if(expressionStatementNode.getNode(0)!=null){
                                    Node selectionExpression = expressionStatementNode.getNode(0);
                                    for (Object o2 : selectionExpression) {
                                        if (o2 instanceof Node) {
                                            String primaryIdentifier = ((Node) o2).getString(0);
                                            currentStatement.primaryIdentifier = primaryIdentifier;
                                        } else if (o2 instanceof String) {
                                            if (currentStatement.fields == null)
                                                currentStatement.fields = new ArrayList<>();
                                            String field = (String) o2;
                                            currentStatement.fields.add(field);
                                        }
                                    }
                                }


                                String method = expressionStatementNode.getString(2);
                                currentStatement.method = method;

                                //TODO: not sure what the structure of AST is for multiple arguments
                                Node arguments = expressionStatementNode.getNode(3);
                                if (arguments.size() > 0) {
                                    currentStatement.arguments = new ArrayList<>();
                                    for (Object o2 : arguments) {
                                        ExpressionStatement currentArgument = new ExpressionStatement();
                                        if (o2 instanceof Node) {
                                            String primaryIdentifier = ((Node) o2).getString(0);
                                            currentArgument.primaryIdentifier = primaryIdentifier;
                                        } else if (o2 instanceof String) {
                                            //is this a field in primary identifier, or second argument, or...
                                        }
                                        currentStatement.arguments.add(currentArgument);
                                    }
                                }

                            }
                            summary.currentConstructor.addConstructorStatement(currentStatement);
                            //return;
                        }
                    }
                }
            }
        }
    }

    public void visitPrimaryIdentifier(GNode n) {
        if (summary.currentMethod.name.equals("main") && n.getString(0).equals("args"))
            summary.usesArgs = true;
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

    public static void main(String[] args) {
        int start = 0;
        int end = 20;

        if (args.length > 1) {
            start = ImplementationUtil.getInteger(args[0]);
            end = ImplementationUtil.getInteger(args[1]);
        } else if (args.length > 0) {
            int value = ImplementationUtil.getInteger(args[0]);
            if (value >= 0) {
                start = value;
                end = value;
            }
        }

        for (int i = start; i <= end; i++) {
            GNode node = (GNode) NodeUtil.parseJavaFile(new File(String.format("./src/test/java/inputs/test%03d/Test%03d.java", i, i)));
            ImplementationUtil.prettyPrintToFile(new File(String.format("./testOutputs/astOutputs/Test%03d.txt", i)), node);
        }
    }

    public AstTraversal(Runtime runtime) {
        this.runtime = runtime;
    }

    public AstTraversalSummary getTraversal(Node n) {
        super.dispatch(n);

        // updating the testing information for unit testing

        // obtain the number of class names so we can check that the correct amount of classes
        // were traversed
        summary.classCount = summary.classNames.size();

        // obtain the number of methods for each class so we can check that the correct number of
        // methods were traversed for each class
        for(String className : summary.classNames) {
            summary.currentClass = summary.classes.get(className);
            summary.classMethodCounts.put(className, summary.currentClass.methods.size());
        }


        return summary;
    }

    static class AstTraversalSummary {

        // summary testing information so that we can perform unit testing
        int classCount;
        int constructorCount;
        TreeMap<String, Integer> classMethodCounts = new TreeMap<>();


        HashMap<String, ClassImplementation> classes = new HashMap<>();
        ArrayList<String> classNames = new ArrayList<>();

        ArrayList<String> currentPackages = new ArrayList<>();

        ClassImplementation currentClass;
        MethodImplementation currentMethod;
        ConstructorImplementation currentConstructor;

        HashMap<String, HashMap<String, ArrayList<MethodImplementation>>> overLoadedMethods = new HashMap<>();

        boolean usesArgs = false;

        ArrayList<ArrayList<String>> allMethods_checkMethodOverloading = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> isOverLoaded = new ArrayList<ArrayList<String>>();
        //ArrayList<ArrayList<String>> fieldsInMainInfo = new ArrayList<ArrayList<String>>();
        HashMap<String, String> fieldsInMainInfo =  new HashMap<>();
        ArrayList<ArrayList<String>> overloadedMethodNames = new ArrayList<ArrayList<String>>();


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
