package com.neutronconsolidate.interpreter.nodes;

import com.neutronconsolidate.interpreter.Token;

public class ConstantNode extends Node {
    public ConstantNode(Token constant) {
        this.name = String.valueOf(constant.value);
    }

    public String name;
}
