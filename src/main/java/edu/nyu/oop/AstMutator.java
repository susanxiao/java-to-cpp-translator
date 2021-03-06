package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import org.slf4j.Logger;
import xtc.parser.StringLiteral;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static java.lang.System.out;

/**
 * Created by susan on 10/26/16.
 */
public class AstMutator extends Visitor {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private Runtime runtime;
    boolean debug = false;
    private AstMutator.AstMutatorSummary summary = new AstMutator.AstMutatorSummary();

    public void visitClassDeclaration(GNode n) {
        summary.currentClass = n.getString(1);
        for (Object o : n)
            if (o instanceof Node)
                visit((Node) o);
    }

    public void visitMethodDeclaration(GNode n) {
        // have to make variables and method names disjoint

        String methodNameString = n.getString(3);
        if (!(methodNameString.startsWith("method"))) {
            switch (methodNameString) {
            case "toString":
            case "hashCode":
            case "equals":
            case "getClass":
                break;
            default:
                methodNameString = "method" + methodNameString.substring(0, 1).toUpperCase() + methodNameString.substring(1);

            }
            GNode methodName = GNode.create(methodNameString);
            n.set(3, methodName.toString().substring(0, methodNameString.length()));
        }

        for (int i = 0; i < n.size(); i++) {
            Object o = n.get(i);
            if (o instanceof Node) {
                if (((Node) o).getName().equals("FormalParameters")) {
                    GNode formalParameters = n.getGeneric(i);
                    if (!formalParameters.hasVariable()) {
                        formalParameters = GNode.ensureVariable(formalParameters);
                        n.set(i, formalParameters);
                    }

                    GNode formalParameter = GNode.create("FormalParameter");
                    formalParameter.add(GNode.create("Modifiers"));
                    GNode type = GNode.create("Type");
                    GNode qualifiedIdentifier = GNode.create("QualifiedIdentifier");
                    qualifiedIdentifier.add(summary.currentClass);
                    type.add(qualifiedIdentifier);
                    type.add(null);
                    formalParameter.add(type);
                    formalParameter.add(null);
                    formalParameter.add("__this");
                    formalParameter.add(null);

                    if (formalParameters.size() > 0) {
                        formalParameters.add(formalParameters.get(formalParameters.size() - 1));
                        for (int j = formalParameters.size() - 2; j > 0; j--) {
                            formalParameters.set(j, formalParameters.get(j - 1));
                        }
                        formalParameters.set(0, formalParameter);
                    } else
                        formalParameters.add(formalParameter);
                } else
                    visit((Node) o);
            }
        }


    }

    public void visitFieldDeclaration(GNode n) {
        Node type = n.getNode(1);

        if (type != null) {
            Node qualifiedIdentifier = type.getNode(0);
            if (qualifiedIdentifier != null) {
                String staticType = qualifiedIdentifier.getString(0);
                if (n.get(2) instanceof Node && n.getNode(2).getName().equals("Declarators")) {
                    Node declarator = n.getNode(2).getNode(0);
                    String varName = declarator.getString(0);
                    summary.objects.put(varName, staticType);
                    if (declarator.get(2) instanceof Node) {
                        Node primaryIdentifier = declarator.getGeneric(2);
                        if (primaryIdentifier.getName().equals("PrimaryIdentifier")) {
                            String primaryIdentifierString = primaryIdentifier.getString(0);
                            String dynamicType = summary.objects.get(primaryIdentifierString);

                            if (!staticType.equals(dynamicType)) {
                                GNode type1 = GNode.create("Type");
                                Node castArray = type.getNode(1);
                                if(castArray != null && castArray.getName().equals("Dimensions") && castArray.getString(0).equals("[")) {

                                    //Cast Array
                                    GNode ArrayCastExpression = GNode.create("ArrayCastExpression");

                                    /* This can be consolidated to
                                        __rt::Array<Object>* as = (__rt::Array<Object>*) args;
                                     */

                                    GNode CastArrayConstructorExpression = GNode.create("CastArrayConstructorExpression");
                                    GNode typeNode = GNode.create("Type");
                                    typeNode.add(staticType);
                                    CastArrayConstructorExpression.add(typeNode);
                                    GNode primaryId = GNode.create("PrimaryIdentifier");
                                    primaryId.add(primaryIdentifierString);
                                    CastArrayConstructorExpression.add(primaryId);

                                    ArrayCastExpression.add(CastArrayConstructorExpression);

                                    declarator.set(2, ArrayCastExpression);

                                } else if(castArray == null) { //regular cast
                                    GNode castExpression = GNode.create("CastExpression");
                                    GNode qualifiedIdentifier1 = GNode.create("QualifiedIdentifier");
                                    qualifiedIdentifier1.add(staticType);
                                    type1.add(qualifiedIdentifier1);
                                    type1.add(null);
                                    castExpression.add(type1);
                                    GNode primaryIdentifier1 = GNode.create("PrimaryIdentifier");
                                    primaryIdentifier1.add(primaryIdentifierString);
                                    castExpression.add(primaryIdentifier1);
                                    declarator.set(2, castExpression);
                                }
                            }
                        } else visitDeclarators(n.getGeneric(2));
                    } else visitDeclarators(n.getGeneric(2));
                }
            }
        }


    }

    public void visitDeclarators(GNode n) {
        for (int i = 0; i < n.size(); i++) {
            Object o = n.get(i);
            if (o instanceof Node) {
                if (((Node) o).getName().equals("Declarator")) {
                    visitDeclarator(n, i);
                }
            }
        }
    }

    public void visitDeclarator(GNode parent, int declaratorIndex) {
        GNode declarator = parent.getGeneric(declaratorIndex);
        String varName = declarator.getString(0);

        for (Object o : declarator) {
            if (o instanceof Node) {
                Node node = (Node) o;
                if (node.getName().equals("NewClassExpression")) {
                    String type = node.getNode(2).getString(0);
                    visitNewClassExpression(GNode.cast(node));
                    summary.objects.put(varName, type);
                } else if (node.getName().equals("StringLiteral")) {
                    if (!declarator.hasVariable()) {
                        declarator = GNode.ensureVariable(declarator);
                        parent.set(declaratorIndex, declarator);
                    }

                    String value = ((Node) o).getString(0);

                    GNode newClassExpression = GNode.create("NewClassExpression");
                    newClassExpression.add(0, null);
                    newClassExpression.add(1, null);
                    GNode qualifiedIdentifier = GNode.create("QualifiedIdentifier");
                    qualifiedIdentifier.add(0, "__String");
                    newClassExpression.add(2, qualifiedIdentifier);
                    GNode arguments = GNode.create("Arguments");
                    GNode stringLiteral = GNode.create("StringLiteral");
                    stringLiteral.add(0, value);
                    arguments.add(0, stringLiteral);
                    newClassExpression.add(3, arguments);
                    newClassExpression.add(4, null);

                    declarator.set(2, newClassExpression);
                } else {
                    visit((Node) o);
                }
            }
        }
    }

    public void visitCallExpression(GNode n) {

        Object o = n.get(0);
        if (o instanceof Node) {
            if (((Node) o).getName().equals("SelectionExpression")) {
                Node selectionExpression = (Node) o;

                Object o1 = selectionExpression.get(0);
                Node primaryIdentifier = null;
                String primaryIdentifierString = null;

                if (o1 instanceof Node && ((Node) o1).getName().equals("PrimaryIdentifier")) {
                    primaryIdentifier = (Node) o1;
                    primaryIdentifierString = ((Node) o1).getString(0);
                }
                String selectionExpressionString = selectionExpression.getString(1);

                String callExpressionString = n.getString(2);

                if ((primaryIdentifierString != null && primaryIdentifierString.equals("System"))
                        && (selectionExpressionString != null && selectionExpressionString.equals("out"))
                        && (callExpressionString != null && callExpressionString.equals("println"))) {
                    primaryIdentifier.set(0, "cout");
                    selectionExpression.set(1, null);
                    n.set(2, "endl");
                }

                for (int i = 3; i < n.size(); i++) {
                    Object o2 = n.get(i);
                    if (o2 instanceof Node) {
                        if (((Node) o2).getName().equals("Arguments")) {
                            if (((Node) o2).size() > 0) {
                                Node argument = ((Node) o2).getNode(0);
                                if (argument.getName().equals("CallExpression")) {

                                    Node selectionExpression1 = argument.getNode(0);
                                    Node callExpressionArgument = null;
                                    if (selectionExpression1 != null) {
                                        if (selectionExpression1.getName().equals("SelectionExpression")) {
                                            callExpressionArgument = selectionExpression1.getNode(0);

                                            GNode arguments = argument.getGeneric(3);
                                            if (!arguments.hasVariable()) {
                                                arguments = GNode.ensureVariable(arguments);
                                            }

                                            if (arguments.size() > 0) {
                                                arguments.add(arguments.get(arguments.size() - 1));
                                                for (int j = arguments.size() - 2; j > 0; j--) {
                                                    arguments.set(j, arguments.get(j - 1));
                                                }
                                                arguments.set(0, callExpressionArgument.getString(0));
                                            } else
                                                arguments.add(callExpressionArgument.getString(0));

                                            argument.set(3, arguments);
                                            visitArguments(arguments);
                                        } else if (selectionExpression1.getName().equals("PrimaryIdentifier")) {
                                            /// sdjfk;asdfjk

                                            String primaryIdentifier2 = selectionExpression1.getString(0);
                                            GNode arguments = argument.getGeneric(3);
                                            if (!arguments.hasVariable()) {
                                                arguments = GNode.ensureVariable(arguments);
                                            }

                                            if (arguments.size() > 0) {
                                                arguments.add(arguments.get(arguments.size() - 1));
                                                for (int j = arguments.size() - 2; j > 0; j--) {
                                                    arguments.set(j, arguments.get(j - 1));
                                                }
                                                arguments.set(0, primaryIdentifier2);
                                            } else
                                                arguments.add(primaryIdentifier2);

                                            argument.set(3, arguments);
                                            visitArguments(arguments);
                                        } else if (selectionExpression1.getName().equals("CallExpression")) {
                                            callExpressionArgument = selectionExpression1;
                                            Node primaryIdentifier1 = selectionExpression1.getNode(0);
                                            for (int j = 1; j < selectionExpression1.size(); j++) {
                                                Object o3 = selectionExpression1.get(j);
                                                if (o3 instanceof Node) {
                                                    GNode callChild = GNode.cast(o3);
                                                    if (callChild.getName().equals("Arguments")) {
                                                        if (!callChild.hasVariable()) {
                                                            callChild = GNode.ensureVariable(callChild);
                                                            selectionExpression1.set(j, callChild);
                                                        }
                                                        if (primaryIdentifier1 != null)
                                                            callChild.add(primaryIdentifier1.getString(0));
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    GNode arguments = argument.getGeneric(3);
                                    if (arguments != null && arguments.getName().equals("Arguments")) {
                                        if (!arguments.hasVariable()) {
                                            arguments = GNode.ensureVariable(arguments);
                                            argument.set(3, arguments);
                                        }
                                        if (callExpressionArgument != null)
                                            arguments.add(0, callExpressionArgument);
                                    }
                                }
                            }
                        } else
                            visit((Node) o2);
                    }

                }

            } else if (((Node) o).getName().equals("PrimaryIdentifier")) {
                Node primaryIdentifier = (Node) o;
                Object fieldsObj = n.get(1);

                if (fieldsObj == null) {
                    GNode fields = GNode.create("Fields");
                    fields.add(0, "__vptr");
                    n.set(1, fields);
                } else {
                    GNode fields = n.getGeneric(1);
                    if (!fields.hasVariable()) {
                        fields = GNode.ensureVariable(fields);
                        fields.add(0, "__vptr");
                        n.set(1, fields);
                    }
                }
                if (primaryIdentifier != null && summary.objects.get(primaryIdentifier.getString(0)) != null) {
                    GNode arguments = n.getGeneric(3);

                    if (!arguments.hasVariable()) {
                        arguments = GNode.ensureVariable(arguments);
                    }

                    if (arguments.size() > 0) {
                        arguments.add(arguments.get(arguments.size() - 1));
                        for (int i = arguments.size() - 2; i > 0; i--) {
                            arguments.set(i, arguments.get(i - 1));
                        }
                        arguments.set(0, primaryIdentifier.getString(0));
                    } else
                        arguments.add(primaryIdentifier.getString(0));

                    n.set(3, arguments);
                    visitArguments(arguments);
                }
            } else if (((Node) o).getName().equals("Arguments")) {
                visitArguments(n.getGeneric(3));
            } else if (((Node) o).getName().equals("CallExpression")) {
               visitCallExpression(GNode.cast(o));
            } else visit((Node) o);
        }
    }

    public void visitArguments(GNode parent) {
        for (int i = 0; i < parent.size(); i++) {
            Object o = parent.get(i);
            if (o instanceof Node) {
                GNode argument = parent.getGeneric(i);
                if (argument.getName().equals("StringLiteral")) {
                    if (!argument.hasVariable()) {
                        argument = GNode.ensureVariable(argument);
                        parent.set(i, argument);
                    }

                    String value = argument.getString(0);

                    GNode newClassExpression = GNode.create("NewClassExpression");
                    newClassExpression.add(0, null);
                    newClassExpression.add(1, null);
                    GNode qualifiedIdentifier = GNode.create("QualifiedIdentifier");
                    qualifiedIdentifier.add(0, "__String");
                    newClassExpression.add(2, qualifiedIdentifier);
                    GNode arguments = GNode.create("Arguments");
                    GNode stringLiteral = GNode.create("StringLiteral");
                    stringLiteral.add(0, value);
                    arguments.add(0, stringLiteral);
                    newClassExpression.add(3, arguments);
                    newClassExpression.add(4, null);

                    parent.set(i, newClassExpression);
                } else if (argument.getName().equals("CallExpression"))
                    visitCallExpression(argument);
                else {
                    visit((Node) o);
                }
            }
        }
    }

    public void visitExpressionStatement(GNode n) {

        // had some problems here will fix at a later date.
        if (n.getNode(0).getName().equals("CallExpression")) {
            Node callExp = n.getNode(0);
            if (callExp.getNode(3).getName().equals("Arguments")) {
                Node args = callExp.getNode(3);
                if (args.size() > 0 && args.getNode(0).getName().equals("CallExpression")) {

                    String methodNameString = n.getNode(0).getNode(3).getNode(0).getString(2);
                    if (!(methodNameString.startsWith("method"))) {
                        switch (methodNameString) {
                        case "toString":
                        case "hashCode":
                        case "equals":
                        case "getClass":
                            break;
                        default:
                            methodNameString = "method" + methodNameString.substring(0, 1).toUpperCase() + methodNameString.substring(1);

                        }
                        GNode nPrime = (GNode) n.getNode(0).getNode(3).getNode(0);
                        GNode methodName = GNode.create(methodNameString);
                        nPrime.set(2, methodName.toString().substring(0, methodNameString.length()));

                    }

                }
            }
        }


        GNode expression = n.getGeneric(0);
        if (expression.getName().equals("Expression")) {
            for (int i = 0; i < expression.size(); i++) {
                Object o = expression.get(i);
                if (o instanceof Node) {
                    if (((Node) o).getName().equals("StringLiteral")) {
                        if (!expression.hasVariable()) {
                            expression = GNode.ensureVariable(expression);
                            n.set(0, expression);
                        }

                        String value = ((Node) o).getString(0);

                        GNode newClassExpression = GNode.create("NewClassExpression");
                        newClassExpression.add(0, null);
                        newClassExpression.add(1, null);
                        GNode qualifiedIdentifier = GNode.create("QualifiedIdentifier");
                        qualifiedIdentifier.add(0, "__String");
                        newClassExpression.add(2, qualifiedIdentifier);
                        GNode arguments = GNode.create("Arguments");
                        GNode stringLiteral = GNode.create("StringLiteral");
                        stringLiteral.add(0, value);
                        arguments.add(0, stringLiteral);
                        newClassExpression.add(3, arguments);
                        newClassExpression.add(4, null);

                        expression.set(i, newClassExpression);
                    } else visit((Node) o);
                }
            }
        } else if (expression.getName().equals("CallExpression")) visitCallExpression(expression);
        else visit(expression);
    }

    public void visitBlock(GNode n) {
        for (int i = 0; i < n.size(); i++) {
            Object o = n.get(i);
            if (o instanceof Node) {
                if (((Node) o).getName().equals("FieldDeclaration"))
                    visitFieldDeclaration(GNode.cast(o));
                else if (((Node) o).getName().equals("ReturnStatement"))
                    visitReturnStatement(n, i);
                else if (((Node) o).getName().equals("ExpressionStatement"))
                    visitExpressionStatement(GNode.cast(o));
                else
                    visit((Node) o);
            }
        }
    }

    public void visitReturnStatement(GNode parent, int returnStatementIndex) {
        GNode n = parent.getGeneric(returnStatementIndex);
        Object o = n.get(0);
        if (o instanceof Node && ((Node) o).getName().equals("StringLiteral")) {
            if (!n.hasVariable()) {
                n = GNode.ensureVariable(n);
                parent.set(returnStatementIndex, n);
            }

            String value = ((Node) o).getString(0);

            GNode newClassExpression = GNode.create("NewClassExpression");
            newClassExpression.add(0, null);
            newClassExpression.add(1, null);
            GNode qualifiedIdentifier = GNode.create("QualifiedIdentifier");
            qualifiedIdentifier.add(0, "__String");
            newClassExpression.add(2, qualifiedIdentifier);
            GNode arguments = GNode.create("Arguments");
            GNode stringLiteral = GNode.create("StringLiteral");
            stringLiteral.add(0, value);
            arguments.add(0, stringLiteral);
            newClassExpression.add(3, arguments);
            newClassExpression.add(4, null);

            n.set(0, newClassExpression);
        }
    }

    public void visitNewClassExpression(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                if (((Node) o).getName().equals("QualifiedIdentifier")) {
                    Node qualifiedIdentifier = (Node) o;
                    String dynamicType = qualifiedIdentifier.getString(0);
                    qualifiedIdentifier.set(0, "__" + dynamicType);
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
            AstMutator visitor = new AstMutator(ImplementationUtil.newRuntime());
            visitor.mutate(node);
            ImplementationUtil.prettyPrintToFile(new File(String.format("testOutputs/mutatedAstOutputs/test%03d.txt", i)), node);
        }
    }


    public AstMutator(Runtime runtime) {
        this.runtime = runtime;
    }

    public void mutate(GNode n) {
        super.dispatch(n);
    }

    public AstMutator.AstMutatorSummary getMutator(Node n) {


        if (debug) {
            out.println("\n");
            out.println("DEBUGGING IS ON");

        }

        return summary;
    }

    static class AstMutatorSummary {
        String currentClass;
        HashMap<String, String> objects = new HashMap<>();

    }
}