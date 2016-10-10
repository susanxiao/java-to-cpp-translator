package edu.nyu.oop;

import org.slf4j.Logger;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import java.util.List;
import java.util.ArrayList;

/**
 * This class demostrates a trivial usage of Xtc's Visitor class.
 * You may use this as a base for your LinkedListVisitor.
 */
public class implementation extends Visitor {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private ClassSummary summary = new ClassSummary();

    public void visitCompilationUnit(GNode n) {
        visit(n);
    }

    public void visitMethodDeclaration(GNode n) {
        summary.nodes += n.getName() + " ";
        summary.names += n.getString(3) + ",";
        summary.count++;
        visit(n);
    }

    public void visit(Node n) {
        for (Object o : n) if (o instanceof Node) dispatch((Node) o);
    }

    public void visitClassDeclaration(GNode n) {
        if(summary.outerClass == true) {
            summary.outerClass = false;
            System.out.println(n.getString(1));
            scope(n, n.getNode(5));
        }
    }
    /**
    public void visitCallExpression(GNode n){
        String one = n.getString(0);
        String two = n.getString(1);
        String three = n.getString(2);
        if(one == "System" && two == "out" && three == "println"){

        }
    }**/

    private void scope(Node curr, Node next) {
        if (null != next) visit(next);
        //runtime.console().p("Exiting scope at ").loc(curr).pln();
    }


    public ClassSummary getSummary(Node n) {
        super.dispatch(n);
        return summary;
    }

    // An instance of this class will be mutated as the Ast is traversed.
    static class ClassSummary {
        int count = 0;
        Boolean outerClass = true;
        String names = "MethodNames,";
        String nodes = "";
        String classNames = "ClassName,";

        public String toString() {
            return "Method count: " + count + System.lineSeparator() +
                    "Method names: " + names + System.lineSeparator() +
                    "Node names: "  + nodes + System.lineSeparator();
        }
    }
}