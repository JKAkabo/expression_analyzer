package com.neutronconsolidate.interpreter;

import java.util.List;

public class Function extends Node {
    public Function(Token<String> identifier, List<Node> args) {
        this.name = String.valueOf(identifier.value);
        this.args = args;
    }

    String name;

    List<Node> args;

    List<Double> actualArgs;
}
