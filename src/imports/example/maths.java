package imports.example;

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
}
