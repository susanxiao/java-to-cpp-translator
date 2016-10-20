package edu.nyu.oop;

import java.util.ArrayList;

import static java.lang.System.out;

/**
 * Created by susan on 10/19/16.
 */
public class ExpressionStatement extends MethodStatement {

    String primaryIdentifier; /** Object we are making the call on **/
    ArrayList<String> fields; /** Fields are nested within each other
                                e.g. primaryIdentifier contains fields(0),
                                     fields.get(0) contains fields.get(1), etc **/
    String method; /** Method we are calling if we call a method which is contained by fields.get(fields.size() - 1) **/
    ArrayList<ExpressionStatement> arguments; /** Arguments for method if it is not null */

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(primaryIdentifier);

        if (fields != null) {
            for (int i = 0; i < fields.size(); i++) {
                s.append("." + fields.get(i));
            }
        }

        if (method != null) {
            s.append("."+method+"(");
            if (arguments != null) {
                for (int i = 0; i < arguments.size(); i++) {
                    s.append(arguments.get(i).toString());
                    if (i < arguments.size() - 1)
                        s.append(", ");
                }
            }
            s.append(")");
        }


        return s.toString();
    }
}
