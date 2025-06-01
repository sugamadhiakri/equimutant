package com.calculator;

import com.calculator.arithmetic.BasicArithmetic;
import com.calculator.advanced.ScientificCalculator;
import com.calculator.financial.FinancialCalculator;
import com.calculator.utils.ExpressionParser;

import com.google.common.base.Preconditions;

/**
 * Main calculator class that serves as a facade for all calculator functionality
 */
public class Calculator {
    private final BasicArithmetic basicArithmetic;
    private final ScientificCalculator scientificCalculator;
    private final FinancialCalculator financialCalculator;
    private final ExpressionParser expressionParser;
    
    /**
     * Creates a new calculator with all components
     */
    public Calculator() {
        this.basicArithmetic = new BasicArithmetic();
        this.scientificCalculator = new ScientificCalculator();
        this.financialCalculator = new FinancialCalculator();
        this.expressionParser = new ExpressionParser();
    }
    
    // Basic arithmetic operations delegated to BasicArithmetic

    /**
     * Adds two integers
     */
    public int add(int a, int b) {
        return basicArithmetic.add(a, b);
    }
    
    /**
     * Subtracts two integers
     */
    public int subtract(int a, int b) {
        return basicArithmetic.subtract(a, b);
    }
    
    /**
     * Multiplies two integers
     */
    public int multiply(int a, int b) {
        return basicArithmetic.multiply(a, b);
    }
    
    /**
     * Divides two integers
     */
    public double divide(int a, int b) {
        return basicArithmetic.divide(a, b);
    }
    
    /**
     * Calculates modulo
     */
    public int modulo(int a, int b) {
        return basicArithmetic.modulo(a, b);
    }
    
    // Scientific calculator operations

    /**
     * Calculates power
     */
    public double power(double base, double exponent) {
        return scientificCalculator.power(base, exponent);
    }
    
    /**
     * Calculates square root
     */
    public double sqrt(double value) {
        return scientificCalculator.sqrt(value);
    }
    
    /**
     * Calculates sine of an angle in radians
     */
    public double sin(double angleRadians) {
        return scientificCalculator.sin(angleRadians);
    }
    
    /**
     * Calculates cosine of an angle in radians
     */
    public double cos(double angleRadians) {
        return scientificCalculator.cos(angleRadians);
    }
    
    /**
     * Calculates tangent of an angle in radians
     */
    public double tan(double angleRadians) {
        return scientificCalculator.tan(angleRadians);
    }
    
    /**
     * Calculates natural logarithm
     */
    public double ln(double value) {
        return scientificCalculator.ln(value);
    }
    
    /**
     * Calculates logarithm with specified base
     */
    public double log(double value, double base) {
        return scientificCalculator.log(value, base);
    }
    
    /**
     * Converts degrees to radians
     */
    public double toRadians(double angleDegrees) {
        return scientificCalculator.toRadians(angleDegrees);
    }
    
    /**
     * Converts radians to degrees
     */
    public double toDegrees(double angleRadians) {
        return scientificCalculator.toDegrees(angleRadians);
    }
    
    /**
     * Calculates statistics for a dataset
     */
    public String calculateStatistics(double[] data) {
        return scientificCalculator.calculateStatistics(data);
    }
    
    // Financial calculator operations
    
    /**
     * Calculates compound interest
     */
    public double calculateCompoundInterest(double principal, double rate, double time, int compoundingPerYear) {
        return financialCalculator.calculateCompoundInterest(principal, rate, time, compoundingPerYear);
    }
    
    /**
     * Calculates loan payment
     */
    public double calculateLoanPayment(double principal, double annualInterestRate, int loanTermYears) {
        return financialCalculator.calculateLoanPayment(principal, annualInterestRate, loanTermYears);
    }
    
    /**
     * Calculates straight-line depreciation
     */
    public double calculateDepreciation(double cost, double salvageValue, int usefulLifeYears) {
        return financialCalculator.calculateStraightLineDepreciation(cost, salvageValue, usefulLifeYears);
    }
    
    /**
     * Calculates present value
     */
    public double calculatePresentValue(double futureValue, double rate, int years) {
        return financialCalculator.calculatePresentValue(futureValue, rate, years);
    }
    
    // Expression parser operations
    
    /**
     * Evaluates a mathematical expression
     */
    public double evaluate(String expression) {
        return expressionParser.evaluate(expression);
    }
    
    /**
     * Sets a variable for use in expressions
     */
    public void setVariable(String name, double value) {
        expressionParser.setVariable(name, value);
    }
    
    /**
     * Gets a variable value
     */
    public double getVariable(String name) {
        return expressionParser.getVariable(name);
    }
    
    /**
     * Clears all variables
     */
    public void clearVariables() {
        expressionParser.clearVariables();
    }
} 