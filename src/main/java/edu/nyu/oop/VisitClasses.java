package edu.nyu.oop;

import org.slf4j.Logger;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;

import java.util.List;
import java.util.ArrayList;


/**
 * Created by Garrett on 10/10/16.
 */
public class VisitClasses extends Visitor {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private Runtime runtime;

    private VisitClasses.VisitClassesSummary summary = new VisitClasses.VisitClassesSummary();

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

    public void visitClassDeclaration(GNode n) {
        summary.tempClass = new ArrayList<String>();
        summary.tempClass.add(n.getString(1));
        summary.ClassNames.add(summary.tempClass);
        summary.ClassesCount++;
        scope(n, n.getNode(5));
    }

    public VisitClasses(Runtime runtime) {
        this.runtime = runtime;
    }

    private void scope(Node curr, Node next) {
        runtime.console().p("Entering scope at ").loc(curr).pln();
        System.out.println(curr.getLocation());
        if (null != next) visit(next);
        runtime.console().p("Exiting scope at ").loc(curr).pln();
    }

    public VisitClasses.VisitClassesSummary getSummary(Node n) {
        super.dispatch(n);
        return summary;
    }

    // An instance of this class will be mutated as the Ast is traversed.
    static class VisitClassesSummary {
        int ClassesCount = 0;
        ArrayList<String> tempClass;
        List<ArrayList<String>> ClassNames = new ArrayList<ArrayList<String>>();
    }
}