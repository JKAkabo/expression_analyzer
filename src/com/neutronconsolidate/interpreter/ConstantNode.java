package com.neutronconsolidate.interpreter;

public class ConstantNode extends Node {
    public ConstantNode(Token constant) {
        this.name = String.valueOf(constant.value);
    }

    String name;
}
