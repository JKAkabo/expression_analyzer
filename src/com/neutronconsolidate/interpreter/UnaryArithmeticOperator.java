package com.neutronconsolidate.interpreter;

public class UnaryArithmeticOperator extends Node {
    public UnaryArithmeticOperator(Token<Character> operator, Node right) {
        this.operator = operator;
        this.right = right;
    }

    Token<Character> operator;
    Node right;
}
