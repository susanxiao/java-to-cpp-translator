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
        summary.classes.add(n.getString(1));

        for (Object o : n)
            if (o instanceof Node)
                visit((Node) o);
    }

    //TODO: rewrite traversal to use visitFieldDeclaration instead of Declaration
    //we need to check that the staticType == dynamicType
    //if not, then add a cast
    
    public void visitDeclarator(GNode n) {
        String varName = n.getString(0);

        for (Object o : n) {
            if (o instanceof Node) {
                Node node = (Node) o;
                if (node.getName().equals("NewClassExpression")) {
                    String type = node.getNode(2).getString(0);
                    visitNewClassExpression(GNode.cast(node));
                    if (summary.classes.contains(type))
                        summary.objects.put(varName, type);
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

                for (int i =3; i < n.size(); i++) {
                    Object o2 = n.get(i);
                    if (o2 instanceof Node)
                        visit((Node) o2);
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
                    System.out.println("test");
                    GNode arguments = n.getGeneric(3);

                    if (!arguments.hasVariable()) {
                        arguments = GNode.ensureVariable(arguments);
                        arguments.add(primaryIdentifier.getString(0));
                        n.set(3, arguments);
                    }
                }

            }

        }

    }

    public void visitBlock(GNode n) {
        for (int i = 0; i < n.size(); i++) {
            Object o = n.get(i);
            if (o instanceof Node) {
                if (((Node) o).getName().equals("ReturnStatement"))
                    visitReturnStatement(n, i);
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

            GNode newClassExpression = GNode.create("NewClassExpression");
            newClassExpression.add(0, null);
            newClassExpression.add(1, null);
                GNode qualifiedIdentifier = GNode.create("QualifiedIdentifier");
                qualifiedIdentifier.add(0, "__String");
            newClassExpression.add(2, qualifiedIdentifier);
                GNode arguments = GNode.create("Arguments");
                    GNode argument = GNode.create("Argument");
                        GNode stringLiteral = GNode.create("StringLiteral");
                        stringLiteral.add(0, "\"A\"");
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

        HashSet<String> classes = new HashSet<>();
        HashMap<String, String> objects = new HashMap<>();

    }
}
