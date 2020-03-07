package com.neutronconsolidate.interpreter;

public class TestRunner {
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer("!({age} - 1999 = 2 | 2 < 3)");
        Parser parser = new Parser(lexer);
        Interpreter interpreter = new Interpreter(parser);
        System.out.println(interpreter.interpret());
//        System.out.println(Boolean.parseBoolean("true"));
    }
}
