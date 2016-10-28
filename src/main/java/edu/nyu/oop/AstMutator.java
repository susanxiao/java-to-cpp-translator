package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import org.slf4j.Logger;
import xtc.parser.StringLiteral;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

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
        //summary.classes.add(n.getString(1));

        for (Object o : n)
            if (o instanceof Node)
                visit((Node) o);
    }

    //TODO: rewrite traversal to use visitFieldDeclaration instead of Declaration
    //we need to check that the staticType == dynamicType
    //if not, then add a cast

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
                                primaryIdentifier.set(0, "(" + staticType + ") " + primaryIdentifierString);
                            }
                        } else visitDeclarators(n.getGeneric(2));
                    }
                    else visitDeclarators(n.getGeneric(2));
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
                }
                else if (node.getName().equals("StringLiteral")) {
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
                            GNode argument = GNode.create("Argument");
                                GNode stringLiteral = GNode.create("StringLiteral");
                                stringLiteral.add(0, value);
                            argument.add(0, stringLiteral);
                        arguments.add(0, argument);
                    newClassExpression.add(3, arguments);
                    newClassExpression.add(4, null);

                    declarator.set(2, newClassExpression);
                }
                else {
                    visit((Node) o);
                }
            }
        }
    }

    public void visitCallExpression(GNode n) {

        Object o = n.get(0);
        if (o instanceof Node) {
            //check if call expression is 'system out print'
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
                            Node argument = ((Node) o2).getNode(0);
                            if (argument.getName().equals("CallExpression")) {
                                Node selectionExpression1 = argument.getNode(0);
                                if (selectionExpression1.getName().equals("SelectionExpression")) {
                                    Node primaryIdentifier1 = selectionExpression1.getNode(0);

                                    GNode arguments = argument.getGeneric(3);
                                    if (!arguments.hasVariable()) {
                                        arguments = GNode.ensureVariable(arguments);
                                    }

                                    if (arguments.size() > 0) {
                                        arguments.add(arguments.get(arguments.size() - 1));
                                        for (int j = arguments.size() - 2; j > 0; j--) {
                                            arguments.set(j, arguments.get(j - 1));
                                        }
                                        arguments.set(0, primaryIdentifier1.getString(0));
                                    } else
                                        arguments.add(primaryIdentifier1.getString(0));

                                    argument.set(3, arguments);
                                    visitArguments(arguments);
                                }
                                else if (selectionExpression1.getName().equals("PrimaryIdentifier")) {
                                    String primaryIdentifier1 = selectionExpression1.getString(0);
                                    GNode arguments = argument.getGeneric(3);
                                    if (!arguments.hasVariable()) {
                                        arguments = GNode.ensureVariable(arguments);
                                    }

                                    if (arguments.size() > 0) {
                                        arguments.add(arguments.get(arguments.size() - 1));
                                        for (int j = arguments.size() - 2; j > 0; j--) {
                                            arguments.set(j, arguments.get(j - 1));
                                        }
                                        arguments.set(0, primaryIdentifier1);
                                    } else
                                        arguments.add(primaryIdentifier1);

                                    argument.set(3, arguments);
                                    visitArguments(arguments);
                                }
                            }
                        }
                        else
                            visit((Node) o2);
                    }

                }

            }
            else if (((Node) o).getName().equals("PrimaryIdentifier")) {
                Node primaryIdentifier = (Node) o;
                Object fieldsObj = n.get(1);
                if (fieldsObj == null) {
                    GNode fields = GNode.create("Fields");
                    fields.add(0, "__vptr");
                    n.set(1, fields);
                }
                else {
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
                    }
                    else
                        arguments.add(primaryIdentifier.getString(0));

                    n.set(3, arguments);
                    visitArguments(arguments);
                }
            }
            else if (((Node) o).getName().equals("Arguments")) {
                visitArguments(n.getGeneric(3));
            }
            else visit((Node) o);
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
                            GNode argument1 = GNode.create("Argument");
                                GNode stringLiteral = GNode.create("StringLiteral");
                                stringLiteral.add(0, value);
                            argument1.add(0, stringLiteral);
                        arguments.add(0, argument1);
                    newClassExpression.add(3, arguments);
                    newClassExpression.add(4, null);

                    parent.set(i, newClassExpression);
                }
                else if (argument.getName().equals("CallExpression"))
                    visitCallExpression(argument);
                else {
                    visit((Node) o);
                }
            }
        }
    }

    public void visitExpressionStatement(GNode n) {
        GNode expression = n.getGeneric(0);
        if (expression.getName().equals("Expression")) {
            for (int i =0; i < expression.size(); i++) {
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
                                GNode argument1 = GNode.create("Argument");
                                    GNode stringLiteral = GNode.create("StringLiteral");
                                    stringLiteral.add(0, value);
                                argument1.add(0, stringLiteral);
                            arguments.add(0, argument1);
                        newClassExpression.add(3, arguments);
                        newClassExpression.add(4, null);

                        expression.set(i, newClassExpression);
                    }
                    else visit((Node) o);
                }
            }
        }
        else if (expression.getName().equals("CallExpression")) visitCallExpression(expression);
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
                    GNode argument = GNode.create("Argument");
                        GNode stringLiteral = GNode.create("StringLiteral");
                        stringLiteral.add(0, value);
                    argument.add(0, stringLiteral);
                arguments.add(0, argument);
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
                    qualifiedIdentifier.set(0, "__"+dynamicType);
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

    public AstMutator(Runtime runtime) {
        this.runtime = runtime;
    }

    public AstMutator.AstMutatorSummary getMutator(Node n) {

        super.dispatch(n);

        if (debug) {
            out.println("\n");
            out.println("DEBUGGING IS ON");

        }

        return summary;
    }

    static class AstMutatorSummary {

        HashMap<String, String> objects = new HashMap<>();

    }
}
