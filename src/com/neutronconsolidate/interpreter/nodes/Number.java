package com.neutronconsolidate.interpreter.nodes;

import com.neutronconsolidate.interpreter.Token;

public class Number extends Node {
    public Number(Token token) {
        this.value = (Double) token.value;
    }

    public Double value;
}
