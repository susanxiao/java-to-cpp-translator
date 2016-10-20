package edu.nyu.oop;

import java.util.ArrayList;

/**
 * Created by Garrett on 10/20/16.
 */
public class ExpressionStatementConstructor extends ConstructorStatement {
    String stringLiteral;
    String primaryIdentifier; /** Object we are making the call on **/
    ArrayList<String> fields; /** Fields are nested within each other
     e.g. primaryIdentifier contains fields(0),
     fields.get(0) contains fields.get(1), etc **/
    String method; /** Method we are calling if we call a method which is contained by fields.get(fields.size() - 1) **/
    ArrayList<ExpressionStatement> arguments; /** Arguments for method if it is not null */

    String assignment;


    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();
        if(primaryIdentifier != null){
            s.append(primaryIdentifier + " ");
        }
        if(assignment != null){
            s.append(assignment);
        }
        return s.toString();
    }
}

