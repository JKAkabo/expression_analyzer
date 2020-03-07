package com.neutronconsolidate.interpreter.nodes;

import com.neutronconsolidate.interpreter.Token;

import java.util.List;

public class Function extends Node {
    public Function(Token identifier, List<Node> args) {
        this.name = String.valueOf(identifier.value);
        this.args = args;
    }

    public String name;

    public List<Node> args;

    public List<Double> actualArgs;
}
