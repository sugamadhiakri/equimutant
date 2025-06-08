package com.calculator.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.google.common.base.Preconditions;
import com.calculator.advanced.ScientificCalculator;

/**
 * Expression parser for evaluating mathematical expressions
 * Supports basic arithmetic operations, functions and variables
 */
public class ExpressionParser {
    private final Map<String, Double> variables = new HashMap<>();
    private final ScientificCalculator scientificCalculator = new ScientificCalculator();
    
    /**
     * Parses and evaluates a mathematical expression
     * @param expression the expression to evaluate
     * @return the result of the evaluation
     */
    public double evaluate(String expression) {
        Preconditions.checkArgument(expression != null && !expression.isEmpty(), 
                "Expression cannot be null or empty");
        
        try {
            // Convert infix to postfix notation
            String postfix = convertToPostfix(expression);
            
            // Evaluate postfix expression
            return evaluatePostfix(postfix);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression: " + expression, e);
        }
    }
    
    /**
     * Sets a variable value for use in expressions
     * @param name variable name
     * @param value variable value
     */
    public void setVariable(String name, double value) {
        Preconditions.checkArgument(name != null && !name.isEmpty(), "Variable name cannot be null or empty");
        variables.put(name, value);
    }
    
    /**
     * Gets a variable value
     * @param name variable name
     * @return the variable value
     */
    public double getVariable(String name) {
        Double value = variables.get(name);
        Preconditions.checkArgument(value != null, "Variable not defined: %s", name);
        return value;
    }
    
    /**
     * Clears all variables
     */
    public void clearVariables() {
        variables.clear();
    }
    
    /**
     * Converts infix expression to postfix notation
     */
    private String convertToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Deque<Character> operatorStack = new ArrayDeque<>();
        
        // Remove all spaces
        infix = infix.replaceAll("\\s+", "");
        
        // Flag to track if we're parsing a number
        boolean parsingNumber = false;
        
        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            
            // Handle numbers (including decimals)
            if (Character.isDigit(c) || c == '.') {
                if (!parsingNumber && postfix.length() > 0 && !Character.isWhitespace(postfix.charAt(postfix.length() - 1))) {
                    postfix.append(' ');
                }
                postfix.append(c);
                parsingNumber = true;
            } 
            // Handle variables and functions
            else if (Character.isLetter(c)) {
                if (parsingNumber) {
                    postfix.append(' ');
                    parsingNumber = false;
                }
                
                StringBuilder identifier = new StringBuilder();
                identifier.append(c);
                
                // Parse the full identifier
                while (i + 1 < infix.length() && (Character.isLetterOrDigit(infix.charAt(i + 1)) || infix.charAt(i + 1) == '_')) {
                    identifier.append(infix.charAt(++i));
                }
                
                String id = identifier.toString();
                
                // Check if it's a function (followed by a parenthesis)
                if (i + 1 < infix.length() && infix.charAt(i + 1) == '(') {
                    operatorStack.push('f'); // Function marker
                    operatorStack.push(id.charAt(0)); // Store the first character as identifier
                } else {
                    // It's a variable
                    if (postfix.length() > 0 && !Character.isWhitespace(postfix.charAt(postfix.length() - 1))) {
                        postfix.append(' ');
                    }
                    postfix.append(id);
                    postfix.append(' ');
                }
            }
            // Handle operators and parentheses
            else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '(' || c == ')') {
                if (parsingNumber) {
                    postfix.append(' ');
                    parsingNumber = false;
                }
                
                if (c == '(') {
                    operatorStack.push(c);
                } else if (c == ')') {
                    while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                        postfix.append(operatorStack.pop()).append(' ');
                    }
                    
                    // Pop the '('
                    if (!operatorStack.isEmpty()) {
                        operatorStack.pop();
                    }
                    
                    // Check for function
                    if (!operatorStack.isEmpty() && operatorStack.peek() == 'f') {
                        operatorStack.pop(); // Remove function marker
                        char functionId = operatorStack.pop(); // Get function identifier
                        postfix.append(functionId).append(' ');
                    }
                } else {
                    // Operator precedence
                    while (!operatorStack.isEmpty() && getPrecedence(operatorStack.peek()) >= getPrecedence(c)) {
                        postfix.append(operatorStack.pop()).append(' ');
                    }
                    operatorStack.push(c);
                }
            }
        }
        
        // Handle remaining number
        if (parsingNumber) {
            postfix.append(' ');
        }
        
        // Pop remaining operators
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop()).append(' ');
        }
        
        return postfix.toString().trim();
    }
    
    /**
     * Evaluates a postfix expression
     */
    private double evaluatePostfix(String postfix) {
        Deque<Double> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(postfix);
        
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            
            // If token is a number
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            }
            // If token is a variable
            else if (variables.containsKey(token)) {
                stack.push(variables.get(token));
            }
            // If token is a function
            else if (token.length() == 1 && "s".equals(token)) {
                double arg = stack.pop();
                stack.push(scientificCalculator.sin(arg));
            }
            else if (token.length() == 1 && "c".equals(token)) {
                double arg = stack.pop();
                stack.push(scientificCalculator.cos(arg));
            }
            else if (token.length() == 1 && "t".equals(token)) {
                double arg = stack.pop();
                stack.push(scientificCalculator.tan(arg));
            }
            else if (token.length() == 1 && "l".equals(token)) {
                double arg = stack.pop();
                stack.push(scientificCalculator.ln(arg));
            }
            else if (token.length() == 1 && "e".equals(token)) {
                double arg = stack.pop();
                stack.push(scientificCalculator.exp(arg));
            }
            else if (token.length() == 1 && "r".equals(token)) {
                double arg = stack.pop();
                stack.push(scientificCalculator.sqrt(arg));
            }
            // If token is an operator
            else {
                double b = stack.pop();
                double a = stack.pop();
                
                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        if (b == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        stack.push(a / b);
                        break;
                    case "^":
                        stack.push(scientificCalculator.power(a, b));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown operator: " + token);
                }
            }
        }
        
        // Result should be the only value left on the stack
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        
        return stack.pop();
    }
    
    /**
     * Gets operator precedence
     */
    private int getPrecedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return 0;
        }
    }
    
    /**
     * Checks if a string is a numeric value
     */
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
} 