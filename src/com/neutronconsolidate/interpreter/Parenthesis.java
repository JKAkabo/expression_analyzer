package com.neutronconsolidate.interpreter;

public enum Parenthesis {
    LEFT('('),
    RIGHT(')');
    private final char value;

    Parenthesis(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}
