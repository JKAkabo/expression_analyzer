package com.neutronconsolidate;


import com.neutronconsolidate.utils.NDate;

import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

public class Main {
    private static final char[] ARITHMETIC_OPERATORS = {'*', '/', '+', '-'};
    private static final char[] UNARY_ARITHMETIC_OPERATORS = {'+', '-'};
    private static final char[] LOGICAL_OPERATORS = {'&', '|'};
    private static final char[] COMPARISON_OPERATORS = {'=', '<', '>'};
    private static final String[] EXTENDED_COMPARISON_OPERATORS = {">=", "<=", "!="};

    public static void main(String[] args) {
        String expression = "!(1 != (day(current_date)) - 28)";
        try {
            String parsedExpression = parseExpression(expression);
            System.out.println(parsedExpression);
            System.out.println(evaluateLogicalExpression(parsedExpression));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String parseExpression(String expression) {
        expression = expression.replaceAll("\\{\\s+", "{").replaceAll("\\s+}", "}");
        char[] tokens = expression.toCharArray();
        HashMap<String, String> variables = new HashMap<>();
        HashMap<String, String> constants = new HashMap<>();
        Stack<Function> functions = new Stack<>();
        Stack<String> arguments = new Stack<>();

        int argumentCount = 0;

        StringBuilder variableBuilder = new StringBuilder();
        StringBuilder constantBuilder = new StringBuilder();
        StringBuilder functionBuilder = new StringBuilder();

        boolean variableBuildInProgress = false;
        boolean constantBuildInProgress = false;
        boolean argumentBuildInProgress = false;

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;

            if (variableBuildInProgress) {
                if (tokens[i] == '}') {
                    variableBuildInProgress = false;
                    variableBuilder.append(tokens[i]);
                    String cleanedVariable = variableBuilder.toString().substring(1, variableBuilder.toString().length() - 1);
                    variables.put(cleanedVariable, getDBVariable(cleanedVariable));
                    if (argumentBuildInProgress) {
                        arguments.add(variableBuilder.toString());
                        argumentCount++;
                    }
                    variableBuilder = new StringBuilder();

                    continue;
                } else if (argumentBuildInProgress && tokens[i] == ',') {
                    variableBuildInProgress = false;
                } else {
                    variableBuilder.append(tokens[i]);
                }
            } else if (constantBuildInProgress) {
                if (tokens[i] == ' ') {
                    constantBuildInProgress = false;
                    constants.put(constantBuilder.toString(), getConstantValue(constantBuilder.toString()));
                    if (argumentBuildInProgress) {
                        arguments.add(constantBuilder.toString());
                        argumentCount++;
                    }
                    constantBuilder = new StringBuilder();
                    continue;
                } else if (argumentBuildInProgress && tokens[i] == ',') {
                    constantBuildInProgress = false;
                    arguments.push(constantBuilder.toString());
                    argumentCount++;
                    constantBuilder = new StringBuilder();
                } else if (tokens[i] == '(') {
                    constantBuildInProgress = false;
                    argumentBuildInProgress = true;
                    functionBuilder.append(constantBuilder.toString());
                    constantBuilder = new StringBuilder();
                    continue;
                } else if (argumentBuildInProgress && tokens[i] == ')') {
                    constantBuildInProgress = false;
                    argumentBuildInProgress = false;

                    constants.put(constantBuilder.toString(), getConstantValue(constantBuilder.toString()));
                    arguments.add(constantBuilder.toString());
                    argumentCount++;

                    constantBuilder = new StringBuilder();

                    functions.add(new Function(functionBuilder.toString(), argumentCount));
                    argumentCount = 0;
                    functionBuilder = new StringBuilder();
                } else {
                    constantBuilder.append(tokens[i]);
                }
            } else if (argumentBuildInProgress) {
                if (tokens[i] == ' ')
                    continue;

                if (tokens[i] == '{') {
                    variableBuildInProgress = true;
                    variableBuilder.append(tokens[i]);
                } else if (tokens[i] == ')') {
                    argumentBuildInProgress = false;
                    functions.add(new Function(functionBuilder.toString(), argumentCount));
                    argumentCount = 0;
                    functionBuilder = new StringBuilder();
                } else {
                    constantBuilder.append(tokens[i]);
                    constantBuildInProgress = true;
                }
            } else {
                if (tokens[i] == '{') {
                    variableBuildInProgress = true;
                    variableBuilder.append(tokens[i]);
                } else if ((tokens[i] >= 'a' && tokens[i] <= 'z') || (tokens[i] >= 'A' && tokens[i] <= 'Z') || tokens[i] == '_') {
                    constantBuilder.append(tokens[i]);
                    constantBuildInProgress = true;
                }
            }
            if (tokens[i] == ' ') System.out.println("Unexpected token");
        }

        for (String key : constants.keySet()) {
            expression = expression.replaceAll(NeutronHelper.stringToRegex(key), constants.get(key));
        }

        for (String key : variables.keySet()) {
            expression = expression.replaceAll(NeutronHelper.stringToRegex("{" + key + "}"), variables.get(key));
        }

        while (!functions.empty()) {
            Function function = functions.pop();
            String[] args = new String[function.getArgumentCount()];
            StringBuilder functionExpressionBuilder = new StringBuilder();
            functionExpressionBuilder.append(function.getName());
            functionExpressionBuilder.append("(");
            for (int i = 0; i < args.length; i++) {
                String argKey = arguments.pop();
                String arg;
                if (argKey.indexOf("{") == 0)
                    arg = variables.get(argKey.substring(1, argKey.length() - 1));
                else
                    arg = constants.get(argKey);
                if (arg == null)
                    System.out.println("Unexpected token"); // exception
                args[i] = arg;

                functionExpressionBuilder.append(arg).append(",");
            }
            functionExpressionBuilder.deleteCharAt(functionExpressionBuilder.toString().length() - 1);
            functionExpressionBuilder.append(")");

            expression = expression.replaceAll(NeutronHelper.stringToRegex(functionExpressionBuilder.toString()), getFunctionReturnValue(function.getName(), args));
//            getFunctionReturnValue(function.getName(), args));
        }

        return expression;
    }

    private static boolean evaluateLogicalExpression(String expression) {
        Stack<String> comparisonExpressions = new Stack<>();
        Stack<Character> operators = new Stack<>();
        char[] tokens = expression.toCharArray();

        StringBuilder comparisonExpressionBuilder = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            if (inArray(tokens[i], LOGICAL_OPERATORS)) {
                operators.push(tokens[i]);
                comparisonExpressions.push(comparisonExpressionBuilder.toString());
                comparisonExpressionBuilder = new StringBuilder();
            } else if (tokens[i] == '!' && tokens[i + 1] != '=') {
                operators.push('!');
            } else if (tokens[i] == '(') {
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (operators.peek() != '(') {
                    comparisonExpressions.push(String.valueOf(evaluateLogicalExpression(operators.pop(), evaluateComparisonExpression(comparisonExpressions.pop()), evaluateLogicalExpression(comparisonExpressions.pop()))));
                }
                operators.pop();
            } else {
                comparisonExpressionBuilder.append(tokens[i]);
            }
        }
        if (comparisonExpressions.size() == 0)
            comparisonExpressions.push(String.valueOf(evaluateComparisonExpression(comparisonExpressionBuilder.toString())));
        else
            comparisonExpressions.push((comparisonExpressionBuilder.toString()));

        while (!operators.empty()) {
            Character operator = operators.pop();
            if (operator == '!')
                comparisonExpressions.push(String.valueOf(evaluateLogicalExpression(operator, evaluateComparisonExpression(comparisonExpressions.pop()))));
            else
                comparisonExpressions.push(String.valueOf(evaluateLogicalExpression(operator, evaluateComparisonExpression(comparisonExpressions.pop()), evaluateComparisonExpression(comparisonExpressions.pop()))));
        }

        return Boolean.parseBoolean(comparisonExpressions.pop());
    }

    private static boolean evaluateLogicalExpression(char operator, boolean right, boolean left) {
        switch (operator) {
            case '&':
                return left && right;
            case '|':
                return left || right;
        }
        return false;
    }

    private static boolean evaluateLogicalExpression(char operator, boolean right) {
        switch (operator) {
            case '!':
                return !right;
        }
        return false;
    }

    private static boolean evaluateComparisonExpression(String expression) {
        Stack<String> arithmeticExpressions = new Stack<>();
        Stack<String> operators = new Stack<>();
        char[] tokens = expression.toCharArray();

        StringBuilder arithmeticExpressionBuilder = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
//            String extendedToken = String.valueOf(tokens[i]) + tokens[i + 1];

            if (i < tokens.length - 1 && inArray(String.valueOf(tokens[i]) + tokens[i + 1], EXTENDED_COMPARISON_OPERATORS)) {
                operators.push(String.valueOf(tokens[i]) + tokens[i + 1]);
                arithmeticExpressions.push(arithmeticExpressionBuilder.toString());
                arithmeticExpressionBuilder = new StringBuilder();
                i++;
            }
            else if (inArray(tokens[i], COMPARISON_OPERATORS)) {
                operators.push(String.valueOf(tokens[i]));
                arithmeticExpressions.push(arithmeticExpressionBuilder.toString());
                arithmeticExpressionBuilder = new StringBuilder();
            }
            else if (tokens[i] == '(') {
                operators.push(String.valueOf(tokens[i]));
            } else if (tokens[i] == ')') {
                while (!operators.peek().equals("(")) {
                    arithmeticExpressions.push(String.valueOf(evaluateComparisonExpression(operators.pop(), evaluateArithmeticExpression(arithmeticExpressions.pop()), evaluateArithmeticExpression(arithmeticExpressions.pop()))));
                }
                operators.pop();
            } else {
                arithmeticExpressionBuilder.append(tokens[i]);
            }
        }
        arithmeticExpressions.push(arithmeticExpressionBuilder.toString());


        while (!operators.empty()) {
            arithmeticExpressions.push(String.valueOf(evaluateComparisonExpression(operators.pop(), evaluateArithmeticExpression(arithmeticExpressions.pop()), evaluateArithmeticExpression(arithmeticExpressions.pop()))));
        }

        return arithmeticExpressions.pop().equals("true");

    }

    private static boolean evaluateComparisonExpression(String operator, int right, int left) {
        switch (operator) {
            case "=":
                return left == right;
            case ">=":
                return left >= right;
            case "<=":
                return left <= right;
            case "<":
                return left < right;
            case ">":
                return left > right;
            case "!=":
                return left != right;
        }
        return false;
    }

    private static int evaluateArithmeticExpression(String expression) {
        char[] tokens = expression.toCharArray();

        Stack<Integer> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;

            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuilder stringBuilder = new StringBuilder();

                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    stringBuilder.append(tokens[i++]);
                }
                i--; // May require optimization

                values.push(Integer.parseInt(stringBuilder.toString()));
            } else if (tokens[i] == '(') {
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (operators.peek() != '(') {
                    values.push(evaluateBasicArithmeticExpression(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop();
            } else if (inArray(tokens[i], ARITHMETIC_OPERATORS)) {
//                if (tokens[i - 1] )
                int j = i;
                boolean isUnary = false;
                if (inArray(tokens[i], UNARY_ARITHMETIC_OPERATORS)) {
                    while (j > 0) {
                        if (!(tokens[j] >= '0' && tokens[j] <= '9') || tokens[j] != ' ') {
                            isUnary = true;
                            break;
                        }
                        j--;
                    }
                }

                if (isUnary) {
                    operators.push(tokens[i]);
                    i++;
                    if (tokens[i] >= '0' && tokens[i] <= '9') {
                        StringBuilder stringBuilder = new StringBuilder();

                        while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                            stringBuilder.append(tokens[i++]);
                        }
                        i--; // May require optimization

                        values.push(evaluateBasicArithmeticExpression(operators.pop(), Integer.parseInt(stringBuilder.toString())));
                    }
                } else {
                    while (!operators.empty() && arithmeticOperatorHasPrecedence(tokens[i], operators.peek())) {
                        values.push(evaluateBasicArithmeticExpression(operators.pop(), values.pop(), values.pop()));
                    }
                    operators.push(tokens[i]);
                }
            }
        }

        while (!operators.empty()) {
            values.push(evaluateBasicArithmeticExpression(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private static int evaluateBasicArithmeticExpression(char operator, int b, int a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by ZERO");
                return a / b;
        }
        return 0;
    }

    private static int evaluateBasicArithmeticExpression(char operator, int b) {
        switch (operator) {
            case '+':
                return b;
            case '-':
                return -b;
        }
        return 0;
    }

    private static boolean inArray(char key, char[] array) {
        for (char member : array) {
            if (member == key) {
                return true;
            }
        }
        return false;
    }

    private static boolean inArray(String key, String[] array) {
        for (String member : array) {
            if (member.equals(key)) {
                return true;
            }
        }
        return false;
    }

    private static boolean arithmeticOperatorHasPrecedence(char operator1, char operator2) {
        if (operator2 == '(' || operator2 == ')')
            return false;
        else return (operator1 == '*' || operator1 == '/') && (operator2 == '+' || operator2 == '-');
    }

    private static String getConstantValue(String constant) {
        switch (constant) {
            case "current_date":
                NDate date = new NDate(new Date());
                return date.toString();
            default:
                return "";
        }
    }

    private static String getDBVariable(String variable) {
        String[] parts = variable.split("\\.");
        return "2001/02/17";
    }

    private static String getFunctionReturnValue(String function, String... args) {
        switch (function) {
            case "month":
                if (args.length != 1) System.out.println("Invalid number of arguments");
                return new NDate(args[0]).getMonth();
            case "day":
                if (args.length != 1) System.out.println("Invalid number of arguments");
                return new NDate(args[0]).getDay();
            case "year":
                if (args.length != 1) System.out.println("Invalid number of arguments");
                return new NDate(args[0]).getYear();
        }
        return "";
    }
}
