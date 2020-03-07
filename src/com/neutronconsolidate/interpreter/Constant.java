package com.neutronconsolidate.interpreter;

import com.neutronconsolidate.interpreter.nodes.Node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Constant<T> {
    public Constant(String name, T value) {
        this.name = name;
        this.value = value;
    }

    String name;
    T value;

    public static class NodeVisitor {
        public Object visit(Node node) {
            String methodName = "visit" + node.getClass().getSimpleName();
            try {
                Method method = this.getClass().getMethod(methodName, node.getClass());
                return method.invoke(this, node);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }
}
