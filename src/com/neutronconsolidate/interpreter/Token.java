package com.neutronconsolidate.interpreter;

public class Token<T> {
    public Token(TokenType tokenType, T value) {
        this.type = tokenType;
        this.value = value;
    }

    TokenType type;
    T value;
}
