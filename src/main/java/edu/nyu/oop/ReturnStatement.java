package edu.nyu.oop;

/**
 * Created by susan on 10/19/16.
 */
public class ReturnStatement extends MethodStatement {

    String literalValue;

    //TODO: nonliteral value
    ExpressionStatement nonLiteralValue;

    @Override
    public String toString() {
        //Ordering of the return values?

        StringBuilder s = new StringBuilder();

        s.append("return ");

        if (nonLiteralValue != null) {
            s.append(nonLiteralValue);
        }

        if(literalValue != null){
            s.append(literalValue);
        }

        return s.toString();

    }

    @Override
    public String toCpp(){
        StringBuilder s = new StringBuilder();
        return s.toString();
    }
}
