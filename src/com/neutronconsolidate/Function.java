package com.neutronconsolidate;

public class Function {
    private String name;

    private int argumentCount = 0;

    public Function(String name, int argumentCount) {
        this.name = name;
        this.argumentCount = argumentCount;
    }

    public String getName() {
        return name;
    }

    public int getArgumentCount() {
        return argumentCount;
    }
}
