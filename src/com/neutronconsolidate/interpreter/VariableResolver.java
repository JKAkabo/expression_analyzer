package com.neutronconsolidate.interpreter;

import java.util.HashMap;

public class VariableResolver {
    public VariableResolver(String tableName) {
        this.tableName = tableName;
    }

    private String tableName;
    private HashMap<String, Object> cache = new HashMap<>();

    private void addToCache(String name, Object value) {
        this.cache.put(name, value);
    }

    private Object retrieveFromDB(String fieldName) {
        return (double) 2001;
    }

    public Object resolve(String variableName) {
        if (!this.cache.containsKey(variableName))
            this.cache.put(variableName, this.retrieveFromDB(variableName));
        return this.cache.get(variableName);
    }
}
