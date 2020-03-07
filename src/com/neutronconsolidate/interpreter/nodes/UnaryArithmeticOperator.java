package com.neutronconsolidate.interpreter.nodes;

import com.neutronconsolidate.interpreter.Token;

public class UnaryArithmeticOperator extends Node {
    public UnaryArithmeticOperator(Token operator, Node right) {
        this.operator = operator;
        this.right = right;
    }

    public Token operator;
    public Node right;
}
