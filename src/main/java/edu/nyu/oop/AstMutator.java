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
    private AstMutator.AstMutatorSummary summary = new AstMutator.AstMutatorSummary();

    public void visitClassDeclaration(GNode n) {
        summary.currentClass = n.getString(1);
        for (Object o : n)
            if (o instanceof Node) {
                if (((Node) o).getName().equals("ClassBody"))
                    visitClassBody(n, GNode.cast(o));
                else
                   dispatch((Node) o);
            }
    }

    public void visitMethodDeclaration(GNode n) {
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
                        for (int j = formalParameters.size() - 2; j > 0 ; j--) {
                            formalParameters.set(j, formalParameters.get(j - 1));
                        }
                        formalParameters.set(0, formalParameter);
                    }
                    else
                        formalParameters.add(formalParameter);
                }
                else
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
                                GNode castExpression = GNode.create("CastExpression");
                                    GNode type1 = GNode.create("Type");
                                        GNode qualifiedIdentifier1 = GNode.create("QualifiedIdentifier");
                                        qualifiedIdentifier1.add(staticType);
                                    type1.add(qualifiedIdentifier1);
                                    type1.add(null);
                                castExpression.add(type1);
                                    GNode primaryIdentifier1 = GNode.create("PrimaryIdentifier");
                                    primaryIdentifier1.add(primaryIdentifierString);
                                castExpression.add(primaryIdentifier1);
                                declarator.set(2, castExpression);
                                //primaryIdentifier.set(0, "(" + staticType + ") " + primaryIdentifierString);
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
                            GNode stringLiteral = GNode.create("StringLiteral");
                            stringLiteral.add(0, value);
                        arguments.add(0, stringLiteral);
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

                boolean cout = false;
                if ((primaryIdentifierString != null && primaryIdentifierString.equals("System"))
                        && (selectionExpressionString != null && selectionExpressionString.equals("out"))
                        && (callExpressionString != null && callExpressionString.equals("println"))) {
                    primaryIdentifier.set(0, "cout");
                    selectionExpression.set(1, null);
                    n.set(2, "endl");
                    cout = true;
                }

                for (int i = 3; i < n.size(); i++) {
                    Object o2 = n.get(i);
                    if (o2 instanceof Node) {
                        if (((Node) o2).getName().equals("Arguments")) {
                            if (((Node) o2).size() > 0) {
                                GNode argument = ((Node) o2).getGeneric(0);
                                if (argument.getName().equals("CallExpression")) {
                                    //TODO: check if argument is ultimately String type
                                    Node callExpressionChild = argument.getNode(0);
                                    if (callExpressionChild != null) {
                                        if (callExpressionChild.getName().equals("SelectionExpression")) {
                                            Node primaryIdentifier1 = callExpressionChild.getNode(0);

                                            GNode arguments = argument.getGeneric(3);
                                            if (!arguments.hasVariable()) {
                                                arguments = GNode.ensureVariable(arguments);
                                            }

                                            arguments.add(0, primaryIdentifier1.getString(0));

                                            argument.set(3, arguments);
                                            visitArguments(arguments);
                                        } else if (callExpressionChild.getName().equals("PrimaryIdentifier")) {
                                            String primaryIdentifier1 = callExpressionChild.getString(0);
                                            GNode arguments = argument.getGeneric(3);
                                            if (!arguments.hasVariable()) {
                                                arguments = GNode.ensureVariable(arguments);
                                            }

                                            arguments.add(0, primaryIdentifier1);

                                            ArrayList<GNode> currentClassFields = summary.fieldDeclarations.get(summary.currentClass);
                                            if (currentClassFields != null) {
                                                for (int j = 0; j < currentClassFields.size(); j++) {
                                                    if (currentClassFields.get(j).getNode(2).getNode(0).getString(0).equals(primaryIdentifier1)) { //Node represents the primary identifier name
                                                        if (currentClassFields.get(j).getNode(1).getNode(0).getString(0).equals("String")) {//field type is String
                                                            arguments.add("data");
                                                        }
                                                    }
                                                }
                                            }
                                            argument.set(3, arguments);
                                            visitArguments(arguments);
                                        }
                                    }
                                }
                                else if (argument.getName().equals("SelectionExpression")) {
                                    String primaryIdentifier1 = argument.getNode(0).getString(0);

                                    String currentObjectClass = summary.objects.get(primaryIdentifier1);
                                    ArrayList<GNode> currentClassFields = summary.fieldDeclarations.get(currentObjectClass);
                                    if (currentClassFields != null) {
                                        for (int j = 0; j < currentClassFields.size(); j++) {
                                            if (currentClassFields.get(j).getNode(2).getNode(0).getString(0).equals(primaryIdentifier1)) { //Node represents the primary identifier name
                                                if (currentClassFields.get(j).getNode(1).getNode(0).getString(0).equals("String")) {//field type is String

                                                    if (!argument.hasVariable()) {
                                                        argument = GNode.ensureVariable(argument);
                                                        ((Node) o2).set(0, argument);
                                                    }
                                                    argument.add("data");
                                                }
                                            }
                                        }
                                    }
                                }
                                else if (argument.getName().equals("PrimaryIdentifier")) {
                                    String primaryIdentifier1 = argument.getString(0);

                                    String currentObjectClass = summary.objects.get(primaryIdentifier1);
                                    if (currentObjectClass != null && currentObjectClass.equals("String")) {
                                        GNode argumentsNode = GNode.cast(o2);
                                        if (!argumentsNode.hasVariable()) {
                                            argumentsNode = GNode.ensureVariable(argumentsNode);
                                            n.set(i, argumentsNode);
                                        }
                                        if (argumentsNode.size() == 1 || !argumentsNode.getString(argumentsNode.size() - 1).equals("data"))
                                            argumentsNode.add("data");
                                    }
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
                            GNode stringLiteral = GNode.create("StringLiteral");
                            stringLiteral.add(0, value);
                        arguments.add(0, stringLiteral);
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
                                GNode stringLiteral = GNode.create("StringLiteral");
                                stringLiteral.add(0, value);
                            arguments.add(0, stringLiteral);
                        newClassExpression.add(3, arguments);
                        newClassExpression.add(4, null);

                        expression.set(i, newClassExpression);
                    }
                    else if (((Node) o).getName().equals("NewClassExpression") && ((Node) o).getNode(2).getString(0).equals("__String")) {
                        //skip these
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

    public void visitConstructorDeclaration(GNode n) {
        for (Object o : n) {
            if (o instanceof Node) {
                if (((Node) o).getName().equals("Block"))
                    visitConstructorBlock(n, GNode.cast(o));
                else
                    visit((Node) o);
            }
        }
    }

    public void visitConstructorBlock(GNode parent, GNode n) {
        if (summary.currentClassExtends != null && summary.constructorBodies.get(summary.currentClassExtends) != null) {
            if (!n.hasVariable()) {
                n = GNode.ensureVariable(n);
                parent.set(5, n); //is block always index 5
            }

            //add the gnodes from constructorBodies
            n.addAll(0, summary.constructorBodies.get(summary.currentClassExtends));
        }


        for (int i = 0; i < n.size(); i++) {
            Object o = n.get(i);
            if (o instanceof Node) {
                if (((Node) o).getName().equals("FieldDeclaration")) {
                    if (!summary.constructorBodies.containsKey(summary.currentClass))
                        summary.constructorBodies.put(summary.currentClass, new ArrayList<GNode>());
                    summary.constructorBodies.get(summary.currentClass).add(GNode.cast(o));

                    visitFieldDeclaration(GNode.cast(o));
                }
                else if (((Node) o).getName().equals("ExpressionStatement")) {
                    if (!summary.constructorBodies.containsKey(summary.currentClass))
                        summary.constructorBodies.put(summary.currentClass, new ArrayList<GNode>());
                    summary.constructorBodies.get(summary.currentClass).add(GNode.cast(o));

                    visitExpressionStatement(GNode.cast(o));
                }
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
                    qualifiedIdentifier.set(0, "__"+dynamicType);
                }
            }
        }
    }

    public void visitExtension(GNode n) {
        summary.currentClassExtends = n.getNode(0).getNode(0).getString(0);
    }

    public void visitClassBody(GNode parent, GNode n) {
        if (summary.currentClassExtends != null) {
            ArrayList<GNode> superClassFields = summary.fieldDeclarations.get(summary.currentClassExtends);

            if (superClassFields != null) {
                //add superClass fields to the arraylist
                if (summary.fieldDeclarations.get(summary.currentClass) == null)
                    summary.fieldDeclarations.put(summary.currentClass, new ArrayList<GNode>());

                summary.fieldDeclarations.get(summary.currentClass).addAll(superClassFields);

                //add the GNodes to the classBody
                if (!n.hasVariable()) {
                    n = GNode.ensureVariable(n);
                    parent.set(5, n); //is class body always index 5
                }
                n.addAll(0, summary.fieldDeclarations.get(summary.currentClassExtends));
            }
        }

        for (Object o : n) {
            if (o instanceof Node) {
                if (((Node) o).getName().equals("FieldDeclaration")) {
                    if (summary.fieldDeclarations.get(summary.currentClass) == null)
                        summary.fieldDeclarations.put(summary.currentClass, new ArrayList<GNode>());
                    summary.fieldDeclarations.get(summary.currentClass).add(GNode.cast(o));
                }

                dispatch((Node) o);
            }
        }
        summary.currentClassExtends = null; //reset for next class
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

    public void mutate(Node n) {
        super.dispatch(n);
    }

    private static class AstMutatorSummary {
        String currentClassExtends;
        String currentClass;
        HashMap<String, ArrayList<GNode>> constructorBodies = new HashMap<>();
        HashMap<String, ArrayList<GNode>> fieldDeclarations = new HashMap<>();
        HashMap<String, String> objects = new HashMap<>();

    }
}
