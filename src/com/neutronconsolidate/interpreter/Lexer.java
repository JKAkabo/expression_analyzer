package com.neutronconsolidate.interpreter;

public class Lexer {
    public Lexer(String expression) {
        this.chars = expression.toCharArray();
        this.position = 0;
        this.currentChar = this.chars[this.position];
    }

    int position;
    Character currentChar;
    char[] chars;

    public void throwParseError() throws Exception {
        throw new Exception("Unexpected character " + this.currentChar + " at " + this.position);
    }

    public void pop() {
        this.position++;
        if (this.position > this.chars.length - 1) {
            this.currentChar = null;
        }
        else {
            this.currentChar = this.chars[this.position];
        }
    }

    public Character peek() {
        int nextPosition = this.position + 1;
        if (nextPosition > this.chars.length - 1) {
            return null;
        }
        else {
            return this.chars[nextPosition];
        }
    }

    public void skipWhitespace() {
        while (this.currentChar != null && this.currentChar == ' ') {
            this.pop();
        }
    }

    public double getNumberLiteral() {
        StringBuilder literalBuilder = new StringBuilder();
        while (this.currentChar != null && (isDigit(this.currentChar) || this.currentChar == '.')) {
            literalBuilder.append(this.currentChar);
            this.pop();
        }
        return Double.parseDouble(literalBuilder.toString());
    }

    public String getIdentifier() {
        StringBuilder identifierBuilder = new StringBuilder();
        identifierBuilder.append(this.currentChar);
        this.pop();
        while (this.currentChar != null && isAlphaNum(this.currentChar)) {
            identifierBuilder.append(this.currentChar);
            this.pop();
        }
        return identifierBuilder.toString();
    }

    public Token<Object> getNextToken() throws Exception {
        while (this.currentChar != null) {
            // Skip Whitespaces
            if (this.currentChar == ' ') {
                this.skipWhitespace();
                continue;
            }

            if (isAlpha(this.currentChar)) {
                String identifier = getIdentifier();
                if (this.currentChar != null && this.currentChar == '(')
                    return new Token<>(TokenType.FUNCTION, identifier);
                return new Token<>(TokenType.CONSTANT, identifier);
            }

            if (isDigit(this.currentChar)) {
                return new Token<>(TokenType.INTEGER, this.getNumberLiteral());
            }

            if (this.currentChar == ArithmeticOperator.ADD.getValue()) {
                this.pop();
                return new Token<>(TokenType.ADD, ArithmeticOperator.ADD.getValue());
            }

            if (this.currentChar == ArithmeticOperator.SUBTRACT.getValue()) {
                this.pop();
                return new Token<>(TokenType.SUB, ArithmeticOperator.SUBTRACT.getValue());
            }

            if (this.currentChar == ArithmeticOperator.MULTIPLY.getValue()) {
                this.pop();
                return new Token<>(TokenType.MUL, ArithmeticOperator.MULTIPLY.getValue());
            }

            if (this.currentChar == ArithmeticOperator.DIVIDE.getValue()) {
                this.pop();
                return new Token<>(TokenType.DIV, ArithmeticOperator.DIVIDE.getValue());
            }

            if (this.currentChar == Parenthesis.LEFT.getValue()) {
                this.pop();
                return new Token<>(TokenType.L_PARENTHESIS, Parenthesis.LEFT.getValue());
            }

            if (this.currentChar == Parenthesis.RIGHT.getValue()) {
                this.pop();
                return new Token<>(TokenType.R_PARENTHESIS, Parenthesis.RIGHT.getValue());
            }

            this.throwParseError();
        }
        return new Token<>(TokenType.EOF, TokenType.EOF.getValue());
    }



    // Private Stuff
    private static boolean isDigit(char character) {
        return character >= '0' && character <= '9';
    }

    private static boolean isAlpha(char character) {
        return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
    }

    private static boolean isAlphaNum(char character) {
        return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || isDigit(character);
    }
}
