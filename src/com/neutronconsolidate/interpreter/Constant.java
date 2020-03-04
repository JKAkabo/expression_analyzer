package com.neutronconsolidate.interpreter;

public class Constant<T> {
    public Constant(String name, T value) {
        this.name = name;
        this.value = value;
    }

    String name;
    T value;
}
