package com.neutronconsolidate.interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FunctionStore {
    public Object invoke(String functionName, Double[] args) {
        try {
            Method method = this.getClass().getMethod(functionName, Double[].class);
            return method.invoke(this, (Object) args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Object abs(Double[] args) {
        Double num = args[0];
        if (num < 0) return -num;
        return num;
    }

}
