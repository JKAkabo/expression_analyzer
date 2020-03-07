package com.neutronconsolidate.interpreter.nodes;

import com.neutronconsolidate.interpreter.Token;

public class Variable extends Node {
    public Variable(Token variable) {
        this.name = (String) variable.value;
    }

    public String name;
}
