package com.calculator.arithmetic;

import com.google.common.base.Preconditions;

/**
 * Basic arithmetic operations with input validation
 */
public class BasicArithmetic {
    
    /**
     * Adds two integers with overflow checking
     */
    public int add(int a, int b) {
        // Use Guava's Preconditions for better error messages
        long result = (long) a + b;
        Preconditions.checkArgument(result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE, 
                "Addition would cause integer overflow: %s + %s", a, b);
        return a + b;
    }
    
    /**
     * Subtracts two integers with overflow checking
     */
    public int subtract(int a, int b) {
        long result = (long) a - b;
        Preconditions.checkArgument(result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE, 
                "Subtraction would cause integer overflow: %s - %s", a, b);
        return a - b;
    }
    
    /**
     * Multiplies two integers with overflow checking
     */
    public int multiply(int a, int b) {
        long result = (long) a * b;
        Preconditions.checkArgument(result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE, 
                "Multiplication would cause integer overflow: %s * %s", a, b);
        return (int) result;
    }
    
    /**
     * Divides two integers with division by zero check
     */
    public double divide(int a, int b) {
        Preconditions.checkArgument(b != 0, "Division by zero is not allowed");
        return (double) a / b;
    }
    
    /**
     * Calculates remainder (modulo operation)
     */
    public int modulo(int a, int b) {
        Preconditions.checkArgument(b != 0, "Modulo by zero is not allowed");
        return a % b;
    }
} 