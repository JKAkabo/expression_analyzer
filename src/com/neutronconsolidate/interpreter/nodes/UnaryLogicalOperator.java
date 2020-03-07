package com.neutronconsolidate.interpreter.nodes;

import com.neutronconsolidate.interpreter.Token;

public class UnaryLogicalOperator extends Node {
    public UnaryLogicalOperator(Token operator, Node right) {
        this.operator = operator;
        this.right = right;
    }

    public Token operator;
    public Node right;
}
