package edu.nyu.oop;

import java.util.ArrayList;

/**
 * Created by susan on 10/19/16.
 */
public class FieldDeclaration extends MethodStatement {
    String staticType;
    String variableName;

    /** For primitive types, below is null **/
    String dynamicType;
    ArrayList<ExpressionStatement> arguments;


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(staticType+" "+variableName+" =");
        if (dynamicType != null) {
            s .append(" new "+dynamicType+"(");
            if (arguments != null && !arguments.isEmpty()) {
                for (int i = 0; i < arguments.size(); i++) {
                    s.append(arguments.get(i));
                    if (i < arguments.size() - 1)
                        s.append(", ");
                }
            }
            s.append(")");
        }
        return s.toString();
    }
}
