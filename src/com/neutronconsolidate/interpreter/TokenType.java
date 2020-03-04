package com.neutronconsolidate.interpreter;

public enum TokenType {
    INTEGER("INTEGER"),
    ADD("ADD"),
    SUB("SUB"),
    MUL("MUL"),
    DIV("DIV"),
    L_PARENTHESIS("L_PARENTHESIS"),
    R_PARENTHESIS("R_PARENTHESIS"),
    EOF("EOF"),
    COMMA("COMMA"),
    CONSTANT("CONSTANT"),
    FUNCTION("FUNCTION"),
    LT("LT"),
    GT("GT"),
    LTE("LTE"),
    GTE("GTE"),
    EQ("EQ"),
    NEQ("NEQ");


    private final String value;
    TokenType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }
}
