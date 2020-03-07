package com.neutronconsolidate.interpreter.nodes;

import com.neutronconsolidate.interpreter.Token;

public class ComparisonOperator extends Node {
    public ComparisonOperator(Node left, Token operator, Node right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Node left;
    public Token operator;
    public Node right;
}
