package imports.example;

import java.util.Stack;

public class maths {
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        if (n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    public static double power(double base, int exponent) {
        if (exponent < 0) {
            throw new IllegalArgumentException("Exponent must be a non-negative integer");
        }
        if (exponent == 0) {
            return 1;
        }
        if (exponent % 2 == 0) {
            double result = power(base, exponent / 2);
            return result * result;
        } else {
            return base * power(base, exponent - 1);
        }
    }

    public static double sqrt(double x) {
        if (x < 0) {
            throw new IllegalArgumentException("Square root is not defined for negative numbers");
        }
        double result = x;
        double epsilon = 1e-15;
        while (Math.abs(result * result - x) > epsilon * x) {
            result = (result + x / result) / 2;
        }
        return result;
    }

    public static double root(double base, int exponent) {
        if (exponent < 0) {
            throw new IllegalArgumentException("Exponent must be a non-negative integer");
        }
        if (exponent == 0) {
            return 1;
        }
        if (exponent == 1) {
            return base;
        }
        double result = base;
        double epsilon = 1e-15;
        while (Math.abs(Math.pow(result, exponent) - base) > epsilon * base) {
            result = ((exponent - 1) * result + base / Math.pow(result, exponent - 1)) / exponent;
        }
        return result;
    }

    //     private boolean isNumber(String token) {
    //     try {
    //         Double.parseDouble(token);
    //         return true;
    //     } catch (NumberFormatException e) {
    //         return false;
    //     }
    // }

    // 判断优先级
    private static   int precedence(String op) {
        if (op.equals("+") || op.equals("-")) return 1;
        if (op.equals("*") || op.equals("/")) return 2;
        return 0;
    }

    // 计算两个数
    private static double applyOp(double b, double a, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": 
                if (b == 0) throw new ArithmeticException("除数不能为零");
                return a / b;
        }
        return 0;
    }

    // 求表达式的值
    public static double evaluate(String tokens) {
        Stack<Double> values = new Stack<>();
        Stack<String> ops = new Stack<>();

        for (int i = 0; i < tokens.length(); i++) {
            char c = tokens.charAt(i);

            // 跳过空格
            if (c == ' ') continue;

            // 如果是数字，读取完整的数字
            if (Character.isDigit(c) || c == '.') {
                StringBuilder sbuf = new StringBuilder();
                while (i < tokens.length() && (Character.isDigit(tokens.charAt(i)) || tokens.charAt(i) == '.'))
                    sbuf.append(tokens.charAt(i++));
                i--;
                values.push(Double.parseDouble(sbuf.toString()));
            }

            // 如果是左括号，直接入栈
            else if (c == '(') ops.push("(");

            // 如果是右括号，计算到左括号
            else if (c == ')') {
                while (ops.peek() != "(")
                    values.push(applyOp(values.pop(), values.pop(), ops.pop()));
                ops.pop();
            }

            // 如果是运算符
            else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(String.valueOf(c)))
                    values.push(applyOp(values.pop(), values.pop(), ops.pop()));
                ops.push(String.valueOf(c));
            }
        }

        // 计算剩余的表达式
        while (!ops.isEmpty())
            values.push(applyOp(values.pop(), values.pop(), ops.pop()));

        // 栈顶为表达式的值
        return values.pop();
    }
}
