package edu.nyu.oop;

import java.util.ArrayList;

import static java.lang.System.out;

/**
 * Created by susan on 10/19/16.
 */
public class FieldDeclaration extends MethodStatement {


    String modifiers;
    String staticType;
    String variableName;

    /** For primitive types, below is null **/
    String dynamicType;

    String primaryIdentifier;

    ArrayList<ExpressionStatement> arguments;


    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        if(modifiers != null){
            s.append(modifiers + " ");
        }

        if(staticType != null){
            s.append(staticType + " ");
        }

        if(variableName != null){
            s.append(variableName + " ");
        }

        if (dynamicType != null) {
            s .append(" = new "+dynamicType+"(");
            if (arguments != null && !arguments.isEmpty()) {
                for (int i = 0; i < arguments.size(); i++) {
                    s.append(arguments.get(i));
                    if (i < arguments.size() - 1)
                        s.append(", ");
                }
            }
            s.append(")");
        } else if(primaryIdentifier != null){
            s.append(" = " + primaryIdentifier);
        }

        return s.toString();
    }
}
