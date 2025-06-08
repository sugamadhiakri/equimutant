package com.calculator.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.calculator.utils.ExpressionParser;

public class ExpressionParserTest {

    private ExpressionParser parser;
    private static final double EPSILON = 1e-10;
    
    @BeforeEach
    void setUp() {
        parser = new ExpressionParser();
    }
    
    @Nested
    @DisplayName("Basic Expression Tests")
    class BasicExpressionTests {
        
        @DisplayName("Simple arithmetic expressions should be evaluated correctly")
        @ParameterizedTest(name = "{0} = {1}")
        @CsvSource({
            "'2 + 3', 5.0",
            "'10 - 4', 6.0",
            "'5 * 6', 30.0",
            "'20 / 4', 5.0",
            "'2 ^ 3', 8.0",
            "'(7 + 3) * 2', 20.0",
            "'10 / (2 + 3)', 2.0",
            "'2 + 3 * 4', 14.0",
            "'(2 + 3) * 4', 20.0"
        })
        void basicArithmeticExpressions(String expression, double expected) {
            double result = parser.evaluate(expression);
            assertThat(result).isCloseTo(expected, within(EPSILON));
        }
        
        @Test
        @DisplayName("Expressions with whitespace should be handled correctly")
        void expressionsWithWhitespace() {
            double result = parser.evaluate("  2  +  3  *  4  ");
            assertThat(result).isCloseTo(14.0, within(EPSILON));
        }
        
        @Test
        @DisplayName("Expression with division by zero should throw exception")
        void divisionByZeroShouldThrowException() {
            assertThatThrownBy(() -> parser.evaluate("5 / 0"))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("Division by zero");
        }
        
        @Test
        @DisplayName("Empty expression should throw exception")
        void emptyExpressionShouldThrowException() {
            assertThatThrownBy(() -> parser.evaluate(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expression cannot be null or empty");
        }
        
        @Test
        @DisplayName("Invalid expression should throw exception")
        void invalidExpressionShouldThrowException() {
            assertThatThrownBy(() -> parser.evaluate("2 + + 3"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid expression");
        }
        
        @Test
        @DisplayName("Unbalanced parentheses should throw exception")
        void unbalancedParenthesesShouldThrowException() {
            assertThatThrownBy(() -> parser.evaluate("(2 + 3"))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
    
    @Nested
    @DisplayName("Advanced Expression Tests")
    class AdvancedExpressionTests {
        
        @DisplayName("Complex expressions should be evaluated correctly")
        @ParameterizedTest(name = "{0} = {1}")
        @CsvSource({
            "'2 + 3 * 4 ^ 2 / (1 + 5)', 10.0",
            "'3 * (4 + 2) - 7 / (3 - 1)', 14.5",
            "'(2 + 3) * (4 - 1) ^ 2', 45.0",
            "'10 - 2 * 3 + 4 / 2', 6.0"
        })
        void complexExpressions(String expression, double expected) {
            double result = parser.evaluate(expression);
            assertThat(result).isCloseTo(expected, within(EPSILON));
        }
        
        @Test
        @DisplayName("Function expressions should be evaluated correctly")
        void functionExpressions() {
            // For sin, cos, etc. in the expression parser
            double result1 = parser.evaluate("sin(0.5) + cos(0.5)");
            assertThat(result1).isCloseTo(Math.sin(0.5) + Math.cos(0.5), within(EPSILON));
            
            double result2 = parser.evaluate("sin(0) ^ 2 + cos(0) ^ 2");
            assertThat(result2).isCloseTo(1.0, within(EPSILON));
            
            double result3 = parser.evaluate("2 * sin(0.25) * cos(0.25)");
            assertThat(result3).isCloseTo(Math.sin(0.5), within(EPSILON));
        }
    }
    
    @Nested
    @DisplayName("Variable Tests")
    class VariableTests {
        
        @BeforeEach
        void setUpVariables() {
            parser.setVariable("x", 10.0);
            parser.setVariable("y", 5.0);
        }
        
        @Test
        @DisplayName("Expressions with variables should be evaluated correctly")
        void expressionsWithVariables() {
            double result1 = parser.evaluate("x + y");
            assertThat(result1).isCloseTo(15.0, within(EPSILON));
            
            double result2 = parser.evaluate("x * y");
            assertThat(result2).isCloseTo(50.0, within(EPSILON));
            
            double result3 = parser.evaluate("(x + y) * (x - y)");
            assertThat(result3).isCloseTo(75.0, within(EPSILON));
        }
        
        @Test
        @DisplayName("Variables can be updated and retrieved")
        void variablesCanBeUpdatedAndRetrieved() {
            parser.setVariable("z", 20.0);
            assertThat(parser.getVariable("z")).isCloseTo(20.0, within(EPSILON));
            
            parser.setVariable("x", 15.0);
            assertThat(parser.getVariable("x")).isCloseTo(15.0, within(EPSILON));
            
            double result = parser.evaluate("x + y + z");
            assertThat(result).isCloseTo(40.0, within(EPSILON));
        }
        
        @Test
        @DisplayName("Non-existent variable should throw exception")
        void nonExistentVariableShouldThrowException() {
            assertThatThrownBy(() -> parser.getVariable("z"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Variable not defined");
                
            assertThatThrownBy(() -> parser.evaluate("x + z"))
                .isInstanceOf(IllegalArgumentException.class);
        }
        
        @Test
        @DisplayName("Variables can be cleared")
        void variablesCanBeCleared() {
            parser.clearVariables();
            
            assertThatThrownBy(() -> parser.getVariable("x"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Variable not defined");
                
            assertThatThrownBy(() -> parser.evaluate("x + y"))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
    
    @Test
    @DisplayName("Invalid variable name should throw exception")
    void invalidVariableNameShouldThrowException() {
        assertThatThrownBy(() -> parser.setVariable("", 10.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Variable name cannot be null or empty");
            
        assertThatThrownBy(() -> parser.setVariable(null, 10.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Variable name cannot be null or empty");
    }
} 