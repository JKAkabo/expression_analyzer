package com.neutronconsolidate.interpreter;

public class TestRunner {
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer("abs(-abs(-3) + 2)");
        Parser parser = new Parser(lexer);
        Interpreter interpreter = new Interpreter(parser);
        System.out.println(interpreter.interpret());
    }
}
