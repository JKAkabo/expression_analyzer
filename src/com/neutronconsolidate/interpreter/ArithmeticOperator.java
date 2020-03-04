package com.neutronconsolidate.interpreter;

public enum ArithmeticOperator {
    ADD('+'),
    SUBTRACT('-'),
    DIVIDE('/'),
    MULTIPLY('*');

    private final char value;
    ArithmeticOperator(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}
