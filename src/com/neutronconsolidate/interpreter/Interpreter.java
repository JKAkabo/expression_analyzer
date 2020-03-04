package com.neutronconsolidate.interpreter;

public class Interpreter extends NodeVisitor {
    Parser parser;

    public Interpreter(Parser parser) {
        this.parser = parser;
    }

    public double visitBinaryArithmeticOperator(BinaryArithmeticOperator binaryArithmeticOperator) {
        if (binaryArithmeticOperator.operator.type.equals(TokenType.ADD.getValue())) {
            return (double) this.visit(binaryArithmeticOperator.left) + (double) this.visit(binaryArithmeticOperator.right);
        }
        else if (binaryArithmeticOperator.operator.type.equals(TokenType.SUB.getValue())) {
            return (double) this.visit(binaryArithmeticOperator.left) - (double) this.visit(binaryArithmeticOperator.right);
        }
        else if (binaryArithmeticOperator.operator.type.equals(TokenType.MUL.getValue())) {
            return (double) this.visit(binaryArithmeticOperator.left) * (double) this.visit(binaryArithmeticOperator.right);
        }
        else if (binaryArithmeticOperator.operator.type.equals(TokenType.DIV.getValue())) {
            return (double) this.visit(binaryArithmeticOperator.left) / (double) this.visit(binaryArithmeticOperator.right);
        }
        return 0;
    }

    public double visitNumber(Number number) {
        return number.value;
    }

    public double visitUnaryArithmeticOperator(UnaryArithmeticOperator unaryArithmeticOperator) {
        if (unaryArithmeticOperator.operator.type.equals(TokenType.ADD.getValue())) {
            return (double) this.visit(unaryArithmeticOperator.right);
        }
        else if (unaryArithmeticOperator.operator.type.equals(TokenType.SUB.getValue())) {
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

    public Object interpret() throws Exception {
        Node tree = this.parser.compare();
        return this.visit(tree);
    }

}
