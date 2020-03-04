package com.neutronconsolidate.interpreter;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    Lexer lexer;
    Token<Object> currentToken;

    public Parser(Lexer lexer) throws Exception {
        this.lexer = lexer;
        this.currentToken = this.lexer.getNextToken();
    }

    public void throwParseError() throws Exception {
        throw new Exception("Unexpected token at" + this.lexer.position);
    }

    public void eat(TokenType tokenType) throws Exception {
        if (this.currentToken.type.equals(tokenType.toString())) {
            this.currentToken = this.lexer.getNextToken();
        } else {
            this.throwParseError();
        }
    }

    public Node function() throws Exception {
        Token token = this.currentToken;

        Token<String> function = new Token<>(TokenType.FUNCTION, String.valueOf(this.currentToken.value));

        this.eat(TokenType.FUNCTION);
        this.eat(TokenType.L_PARENTHESIS);

        List<Node> args = new ArrayList<>();

        if (!this.currentToken.type.equals(TokenType.R_PARENTHESIS.getValue())) {
            Node node = this.compare();
            args.add(node);
        }

        while (this.currentToken.type.equals(TokenType.COMMA.getValue())) {
            this.eat(TokenType.COMMA);
            Node node = this.compare();
            args.add(node);
        }

        this.eat(TokenType.R_PARENTHESIS);

        return new Function(function, args);
    }

    public Node factor() throws Exception {
        Token token = this.currentToken;
        if (token.type.equals(TokenType.ADD.getValue())) {
            this.eat(TokenType.ADD);
            return new UnaryArithmeticOperator(token, this.factor());
        } else if (token.type.equals(TokenType.SUB.getValue())) {
            this.eat(TokenType.SUB);
            return new UnaryArithmeticOperator(token, this.factor());
        } else if (token.type.equals(TokenType.INTEGER.getValue())) {
            this.eat(TokenType.INTEGER);
            return new Number(token);
        } else if (token.type.equals(TokenType.L_PARENTHESIS.getValue())) {
            this.eat(TokenType.L_PARENTHESIS);
            Node node = this.compare();
            this.eat(TokenType.R_PARENTHESIS);
            return node;
        } else if (token.type.equals(TokenType.FUNCTION.getValue())) {
//            this.eat(TokenType.FUNCTION);
            return this.function();
        } else if (token.type.equals(TokenType.CONSTANT.getValue())) {
            this.eat(TokenType.CONSTANT);
            return new ConstantNode(token);
        }
        throwParseError();
        return null;
    }

    public Node term() throws Exception {
        Node node = this.factor();

        while (this.currentToken.type.equals(TokenType.MUL.getValue()) || this.currentToken.type.equals(TokenType.DIV.getValue())) {
            Token token = this.currentToken;
            if (token.type.equals(TokenType.MUL.getValue())) {
                this.eat(TokenType.MUL);
            }
            node = new BinaryArithmeticOperator(node, token, this.factor());
        }
        return node;
    }

    public Node compare() throws Exception {
        Node node = this.term();
        while (this.currentToken.type == TokenType.ADD || this.currentToken.type == TokenType.SUB) {
            Token token = this.currentToken;
            if (this.currentToken.type == TokenType.ADD.getValue()) {
                this.eat(TokenType.ADD);
            } else {
                this.eat(TokenType.SUB);
            }
            node = new BinaryArithmeticOperator(node, token, this.factor());
        }
        return node;
    }

    public Node parse() throws Exception {
        Node node = this.compare();
        while (this.currentToken.type == TokenType.LT || this.currentToken.type == TokenType.GT ||
                this.currentToken.type == TokenType.LTE || this.currentToken.type == TokenType.GTE ||
                this.currentToken.type == TokenType.EQ || this.currentToken.type == TokenType.NEQ) {

            Token token = this.currentToken;
            if (this.currentToken.type.equals(TokenType.LT.getValue())) ;
        }
    }
}
