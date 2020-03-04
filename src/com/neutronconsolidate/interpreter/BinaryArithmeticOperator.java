package com.neutronconsolidate.interpreter;

public class BinaryArithmeticOperator extends Node {
    public BinaryArithmeticOperator(Node left, Token<Character> operator, Node right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    Node left;
    Token<Character> operator;
    Node right;
}
