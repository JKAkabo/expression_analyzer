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
    VARIABLE("VARIABLE"),
    LT("LT"),
    GT("GT"),
    LTE("LTE"),
    GTE("GTE"),
    EQ("EQ"),
    NEQ("NEQ"),
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    L_BRACES("L_BRACES"),
    R_BRACES("R_BRACES");


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
