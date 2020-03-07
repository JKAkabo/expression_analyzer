package com.neutronconsolidate.interpreter;

import com.neutronconsolidate.interpreter.nodes.*;
import com.neutronconsolidate.interpreter.nodes.Number;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    Lexer lexer;
    Token currentToken;

    public Parser(Lexer lexer) throws Exception {
        this.lexer = lexer;
        this.currentToken = this.lexer.getNextToken();
    }

    public void throwParseError() throws Exception {
        throw new Exception("Unexpected token at" + this.lexer.position);
    }

    public void eat(TokenType tokenType) throws Exception {
        if (this.currentToken.type == tokenType) {
            this.currentToken = this.lexer.getNextToken();
        } else {
            this.throwParseError();
        }
    }

    public Node function() throws Exception {
//        Token token = this.currentToken;

        Token function = new Token(TokenType.FUNCTION, String.valueOf(this.currentToken.value));

        this.eat(TokenType.FUNCTION);
        this.eat(TokenType.L_PARENTHESIS);

        List<Node> args = new ArrayList<>();

        if (!(this.currentToken.type == TokenType.R_PARENTHESIS)) {
            Node node = this.parse();
            args.add(node);
        }

        while (this.currentToken.type == TokenType.COMMA) {
            this.eat(TokenType.COMMA);
            Node node = this.parse();
            args.add(node);
        }

        this.eat(TokenType.R_PARENTHESIS);

        return new Function(function, args);
    }

    public Node factor() throws Exception {
        Token token = this.currentToken;
        if (token.type == TokenType.ADD) {
            this.eat(TokenType.ADD);
            return new UnaryArithmeticOperator(token, this.factor());
        } else if (token.type == TokenType.SUB) {
            this.eat(TokenType.SUB);
            return new UnaryArithmeticOperator(token, this.factor());
        } else if (token.type == TokenType.INTEGER) {
            this.eat(TokenType.INTEGER);
            return new Number(token);
        } else if (token.type == TokenType.L_PARENTHESIS) {
            this.eat(TokenType.L_PARENTHESIS);
            Node node = this.parse();
            this.eat(TokenType.R_PARENTHESIS);
            return node;
        } else if (token.type == TokenType.FUNCTION) {
//            this.eat(TokenType.FUNCTION);
            return this.function();
        } else if (token.type == TokenType.CONSTANT) {
            this.eat(TokenType.CONSTANT);
            return new ConstantNode(token);
        } else if (token.type == TokenType.VARIABLE) {
            this.eat(TokenType.VARIABLE);
            this.eat(TokenType.R_BRACES);
            return new Variable(token);
        }
        throwParseError();
        return null;
    }

    public Node term() throws Exception {
        Node node = this.factor();

        while (this.currentToken.type == TokenType.MUL || this.currentToken.type == TokenType.DIV) {
            Token token = this.currentToken;
            if (token.type == TokenType.MUL) {
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
            if (this.currentToken.type == TokenType.ADD) {
                this.eat(TokenType.ADD);
            } else {
                this.eat(TokenType.SUB);
            }
            node = new BinaryArithmeticOperator(node, token, this.factor());
        }
        return node;
    }

    public Node logic() throws Exception {
        Node node = this.compare();
        while (this.currentToken.type == TokenType.LT || this.currentToken.type == TokenType.GT ||
                this.currentToken.type == TokenType.LTE || this.currentToken.type == TokenType.GTE ||
                this.currentToken.type == TokenType.EQ || this.currentToken.type == TokenType.NEQ) {
            Token token = this.currentToken;
            this.eat(token.type);
            return new ComparisonOperator(node, token, this.compare());
        }
        return node;
    }

    public Node unaryParse() throws Exception {
//        Node node = this.logic();
        while (this.currentToken.type == TokenType.NOT) {
            Token token = this.currentToken;
            this.eat(token.type);
            return new UnaryLogicalOperator(token, this.logic());
        }
        return this.logic();
    }

    public Node parse() throws Exception {
        Node node = this.unaryParse();
        while (this.currentToken.type == TokenType.AND || this.currentToken.type == TokenType.OR) {
            Token token = this.currentToken;
            this.eat(token.type);
            return new BinaryLogicalOperator(node, token, this.unaryParse());
        }
        return node;
    }
}
