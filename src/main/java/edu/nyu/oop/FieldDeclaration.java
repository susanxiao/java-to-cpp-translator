package edu.nyu.oop;

import java.util.ArrayList;

/**
 * Created by susan on 10/19/16.
 */
public class FieldDeclaration extends MethodStatement {


    String modifiers;
    String staticType;
    String variableName;
    String literalValue;

    /**
     * For primitive types, below is null
     **/

    /**
     * Declaring a new object
     */
    String dynamicType;
    String primaryIdentifier;

    ArrayList<ExpressionStatement> arguments;

    /**
     * Other declarations
     */
    ExpressionStatement assignment;

    boolean isStatic = false;

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        if (modifiers != null) {
            s.append(modifiers + " ");
        }

        if (staticType != null) {
            s.append(staticType + " ");
        }

        if (variableName != null) {
            s.append(variableName + " ");
        }

        if (dynamicType != null) {
            s.append(" = new " + dynamicType + "(");
            if (arguments != null && !arguments.isEmpty()) {
                for (int i = 0; i < arguments.size(); i++) {
                    s.append(arguments.get(i));
                    if (i < arguments.size() - 1)
                        s.append(", ");
                }
            }
            s.append(")");
        } else if (primaryIdentifier != null) {
            s.append(" = " + primaryIdentifier);
        } else if (literalValue != null) {
            s.append(" = " + literalValue);
        } else if (assignment != null) {
            s.append(" = "+assignment.toString());
        }

        return s.toString();
    }

    @Override
    //TODO: Modify this
    public String toCpp() {
        StringBuilder s = new StringBuilder();


        if (modifiers != null) {
            s.append(modifiers + " ");
        }

        if (staticType != null) {
            s.append(staticType + " ");
        }

        if (variableName != null) {
            s.append(variableName + " ");
        }

        if (dynamicType != null) {
            s.append(" = new " + dynamicType + "(");
            if (arguments != null && !arguments.isEmpty()) {
                for (int i = 0; i < arguments.size(); i++) {
                    s.append(arguments.get(i));
                    if (i < arguments.size() - 1)
                        s.append(", ");
                }
            }
            s.append(")");
        } else if (primaryIdentifier != null) {
            s.append(" = " + primaryIdentifier);
        } else if (literalValue != null) {
            s.append(" = " + literalValue);
        }

        return s.toString();
    }
}
