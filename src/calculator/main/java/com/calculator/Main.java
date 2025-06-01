package com.calculator;

public class Main {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        
        System.out.println("===== ENHANCED CALCULATOR DEMO =====\n");
        
        // Basic arithmetic operations
        System.out.println("===== Basic Arithmetic =====");
        System.out.println("Addition: 128 + 256 = " + calc.add(128, 256));
        System.out.println("Subtraction: 512 - 64 = " + calc.subtract(512, 64));
        System.out.println("Multiplication: 25 * 4 = " + calc.multiply(25, 4));
        System.out.println("Division: 100 / 8 = " + calc.divide(100, 8));
        System.out.println("Modulo: 17 % 5 = " + calc.modulo(17, 5));
        
        // Scientific calculator operations
        System.out.println("\n===== Scientific Calculator =====");
        System.out.println("Power: 2.5^3.5 = " + calc.power(2.5, 3.5));
        System.out.println("Square Root: √144 = " + calc.sqrt(144));
        System.out.println("Natural Log: ln(10) = " + calc.ln(10));
        System.out.println("Log base 10: log10(1000) = " + calc.log(1000, 10));
        System.out.println("Exponential: e^2 = " + calc.power(Math.E, 2));
        
        // Trigonometric functions
        double angleInDegrees = 45;
        double angleInRadians = calc.toRadians(angleInDegrees);
        System.out.println("\n===== Trigonometry =====");
        System.out.println("Sin(45°) = " + calc.sin(angleInRadians));
        System.out.println("Cos(45°) = " + calc.cos(angleInRadians));
        System.out.println("Tan(45°) = " + calc.tan(angleInRadians));
        System.out.println("Converting 2 radians to degrees: " + calc.toDegrees(2));
        
        // Statistical calculations
        double[] data = {12.5, 9.8, 15.2, 7.4, 11.3, 8.6, 14.7};
        System.out.println("\n===== Statistics =====");
        System.out.println("Dataset statistics:\n" + calc.calculateStatistics(data));
        
        // Financial calculations
        System.out.println("\n===== Financial Calculations =====");
        double principal = 10000;
        double interestRate = 0.05; // 5%
        double years = 10;
        int compoundingFrequency = 12; // monthly
        System.out.println("Future value of $" + principal + " at " + (interestRate * 100) 
                + "% interest over " + years + " years (compounded monthly): $" 
                + String.format("%.2f", calc.calculateCompoundInterest(principal, interestRate, years, compoundingFrequency)));
        
        double loanAmount = 200000;
        double annualRate = 0.04; // 4%
        int loanTerm = 30; // years
        System.out.println("Monthly payment for a $" + loanAmount + " mortgage at " 
                + (annualRate * 100) + "% for " + loanTerm + " years: $" 
                + String.format("%.2f", calc.calculateLoanPayment(loanAmount, annualRate, loanTerm)));
        
        double assetCost = 50000;
        double salvageValue = 5000;
        int usefulLife = 7;
        System.out.println("Annual depreciation of $" + assetCost + " asset over " 
                + usefulLife + " years: $" 
                + String.format("%.2f", calc.calculateDepreciation(assetCost, salvageValue, usefulLife)));
        
        // Expression evaluation
        System.out.println("\n===== Expression Evaluation =====");
        
        String expr1 = "3 + 4 * 2 / (1 - 5)^2";
        System.out.println("Evaluating expression: " + expr1 + " = " + calc.evaluate(expr1));
        
        String expr2 = "sin(0.5) + cos(0.5)";
        System.out.println("Evaluating expression: " + expr2 + " = " + calc.evaluate(expr2));
        
        // Using variables
        System.out.println("\n===== Using Variables =====");
        calc.setVariable("x", 10);
        calc.setVariable("y", 5);
        String expr3 = "x^2 + 2*x*y + y^2";
        System.out.println("With x = 10 and y = 5, " + expr3 + " = " + calc.evaluate(expr3));
    }
}
