package com.calculator;

public class Main {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        
        // Demo calculations
        System.out.println("Addition: 5 + 3 = " + calc.add(5, 3));
        System.out.println("Subtraction: 10 - 4 = " + calc.subtract(10, 4));
        System.out.println("Multiplication: 6 * 7 = " + calc.multiply(6, 7));
        System.out.println("Division: 15 / 3 = " + calc.divide(15, 3));
        System.out.println("Power: 2^3 = " + calc.power(2, 3));
    }
}
