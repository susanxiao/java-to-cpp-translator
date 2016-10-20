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
        return "return "+ literalValue;
    }
}
