package com.neutronconsolidate.interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NodeVisitor {
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
