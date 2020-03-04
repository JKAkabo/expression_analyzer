package com.neutronconsolidate.interpreter;

public class Number extends Node {
    public Number(Token<Double> token) {
        this.value = token.value;
    }

    Double value;
}
