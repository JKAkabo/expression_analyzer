package com.neutronconsolidate.interpreter;

public class Token {
    public Token(TokenType tokenType, Object value) {
        this.type = tokenType;
        this.value = value;
    }

    public TokenType type;
    public Object value;
}
