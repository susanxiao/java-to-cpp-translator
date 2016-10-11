package edu.nyu.oop;

import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Garrett on 10/10/16.
 */
public class VisitMethods extends Visitor {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private Runtime runtime;

    private VisitMethods.VisitMethodsSummary summary = new VisitMethods.VisitMethodsSummary();

    public void visitCompilationUnit(GNode n) {
        visit(n);
        runtime.console().flush();
    }

    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) {
                dispatch((Node) o);
            }
        }
    }

    public void visitCallExpression(GNode n){
        if(n.getNode(0).getName() == "SelectionExpression"){
            if(n.getNode(0).getNode(0).getName() == "PrimaryIdentifier"){
                summary.tempMethod.add(n.getNode(0).getNode(0).getString(0));
                summary.tempMethod.add(n.getNode(0).getString(1));
                summary.tempMethod.add(n.getString(2));
            }
        }
        if(n.getNode(3).getName() == "Arguments"){
            if(n.getNode(3).getNode(0).getName() == "StringLiteral"){
                summary.tempMethod.add(n.getNode(3).getNode(0).getString(0));
            }
        }
    }

    public void visitMethodDeclaration(GNode n) {
        summary.tempMethod = new ArrayList<String>();
        summary.tempMethod.add(n.getString(3));
        summary.MethodsCount++;
        scope(n,n);
    }

    public VisitMethods(Runtime runtime) {
        this.runtime = runtime;
    }

    private void scope(Node curr, Node next) {

        runtime.console().p("Entering scope at ").loc(curr).pln();
        if (null != next) visit(next);
        runtime.console().p("Exiting scope at ").loc(curr).pln();
        summary.Methods.add(summary.tempMethod);

    }

    public VisitMethods.VisitMethodsSummary getSummary(Node n) {
        super.dispatch(n);
        return summary;
    }

    // An instance of this class will be mutated as the Ast is traversed.
    static class VisitMethodsSummary {
        int MethodsCount = 0;
        int CurrentMethod = 0;
        ArrayList<String> tempMethod;
        List<ArrayList<String>> Methods = new ArrayList<ArrayList<String>>();
    }
}