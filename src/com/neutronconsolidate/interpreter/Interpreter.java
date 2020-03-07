package com.neutronconsolidate.interpreter;

import com.neutronconsolidate.interpreter.nodes.*;
import com.neutronconsolidate.interpreter.nodes.Number;

public class Interpreter extends Constant.NodeVisitor {
    Parser parser;
    VariableResolver variableResolver = new VariableResolver("core.patients");

    public Interpreter(Parser parser) {
        this.parser = parser;
    }

    public double visitBinaryArithmeticOperator(BinaryArithmeticOperator binaryArithmeticOperator) {
        if (binaryArithmeticOperator.operator.type == TokenType.ADD) {
            System.out.println(this.visit(binaryArithmeticOperator.left).getClass());
            return (double) this.visit(binaryArithmeticOperator.left) + (double) this.visit(binaryArithmeticOperator.right);
        }
        else if (binaryArithmeticOperator.operator.type == TokenType.SUB) {
            return (double) this.visit(binaryArithmeticOperator.left) - (double) this.visit(binaryArithmeticOperator.right);
        }
        else if (binaryArithmeticOperator.operator.type == TokenType.MUL) {
            return (double) this.visit(binaryArithmeticOperator.left) * (double) this.visit(binaryArithmeticOperator.right);
        }
        else if (binaryArithmeticOperator.operator.type == TokenType.DIV) {
            return (double) this.visit(binaryArithmeticOperator.left) / (double) this.visit(binaryArithmeticOperator.right);
        }
        return 0;
    }

    public double visitNumber(Number number) {
        return number.value;
    }

    public double visitUnaryArithmeticOperator(UnaryArithmeticOperator unaryArithmeticOperator) {
        if (unaryArithmeticOperator.operator.type == TokenType.ADD) {
            return (double) this.visit(unaryArithmeticOperator.right);
        }
        else if (unaryArithmeticOperator.operator.type == TokenType.SUB) {
            return -((double) this.visit(unaryArithmeticOperator.right));
        }
        return 0;
    }

    public Object visitConstantNode(ConstantNode constantNode) {
        return (new ConstantTable()).get(constantNode.name).value;
    }

    public Object visitFunction(Function function) {
        Double[] args = new Double[function.args.size()];
        for (int i = 0; i< function.args.size(); i++) {
            args[i] = (Double) this.visit(function.args.get(i));
        }
        return (new FunctionStore()).invoke(function.name, args);
    }

    public Object visitVariable(Variable variable) {
        return this.variableResolver.resolve(variable.name);
    }

    public boolean visitComparisonOperator(ComparisonOperator comparisonOperator) {
        if (comparisonOperator.operator.type == TokenType.EQ) {
            return (double) this.visit(comparisonOperator.left) == (double) this.visit(comparisonOperator.right);
        }
        else if (comparisonOperator.operator.type == TokenType.NEQ) {
            return (double) this.visit(comparisonOperator.left) != (double) this.visit(comparisonOperator.right);
        }
        else if (comparisonOperator.operator.type == TokenType.LT) {
            return (double) this.visit(comparisonOperator.left) < (double) this.visit(comparisonOperator.right);
        }
        else if (comparisonOperator.operator.type == TokenType.GT) {
            return (double) this.visit(comparisonOperator.left) > (double) this.visit(comparisonOperator.right);
        }
        else if (comparisonOperator.operator.type == TokenType.GTE) {
            return (double) this.visit(comparisonOperator.left) >= (double) this.visit(comparisonOperator.right);
        }
        else if (comparisonOperator.operator.type == TokenType.LTE) {
            return (double) this.visit(comparisonOperator.left) <= (double) this.visit(comparisonOperator.right);
        }
        return false;
    }

    public boolean visitUnaryLogicalOperator(UnaryLogicalOperator unaryLogicalOperator) {
        if (unaryLogicalOperator.operator.type == TokenType.NOT) {
            return !(boolean) this.visit(unaryLogicalOperator.right);
        }
        return false;
    }

    public boolean visitBinaryLogicalOperator(BinaryLogicalOperator binaryLogicalOperator) {
        if (binaryLogicalOperator.operator.type == TokenType.AND) {
            return (boolean) this.visit(binaryLogicalOperator.left) && (boolean) this.visit(binaryLogicalOperator.right);
        }
        else if (binaryLogicalOperator.operator.type == TokenType.OR) {
            return (boolean) this.visit(binaryLogicalOperator.left) || (boolean) this.visit(binaryLogicalOperator.right);
        }
        return false;
    }
    public Object interpret() throws Exception {
        Node tree = this.parser.parse();
        return this.visit(tree);
    }

}
