package com.calculator.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.calculator.arithmetic.BasicArithmetic;

public class BasicArithmeticTest {

    private BasicArithmetic arithmetic;
    
    @BeforeEach
    void setUp() {
        arithmetic = new BasicArithmetic();
    }
    
    @DisplayName("Addition should work correctly with valid inputs")
    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
        "1, 1, 2",
        "0, 0, 0",
        "5, -3, 2",
        "-5, -3, -8",
        "2147483646, 1, 2147483647" // MAX_INT - 1 + 1 = MAX_INT
    })
    void addShouldWorkWithValidInputs(int a, int b, int expected) {
        int result = arithmetic.add(a, b);
        assertEquals(expected, result);
    }
    
    @Test
    @DisplayName("Addition should throw exception on overflow")
    void addShouldThrowExceptionOnOverflow() {
        assertThatThrownBy(() -> arithmetic.add(Integer.MAX_VALUE, 1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Addition would cause integer overflow");
    }
    
    @DisplayName("Subtraction should work correctly with valid inputs")
    @ParameterizedTest(name = "{0} - {1} = {2}")
    @CsvSource({
        "5, 3, 2",
        "0, 0, 0",
        "3, 5, -2",
        "-5, -3, -2",
        "-2147483647, 1, -2147483648" // MIN_INT + 1 - 1 = MIN_INT
    })
    void subtractShouldWorkWithValidInputs(int a, int b, int expected) {
        int result = arithmetic.subtract(a, b);
        assertEquals(expected, result);
    }
    
    @Test
    @DisplayName("Subtraction should throw exception on overflow")
    void subtractShouldThrowExceptionOnOverflow() {
        assertThatThrownBy(() -> arithmetic.subtract(Integer.MIN_VALUE, 1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Subtraction would cause integer overflow");
    }
    
    @DisplayName("Multiplication should work correctly with valid inputs")
    @ParameterizedTest(name = "{0} * {1} = {2}")
    @CsvSource({
        "5, 3, 15",
        "0, 0, 0",
        "3, -5, -15",
        "-5, -3, 15",
        "10, 0, 0"
    })
    void multiplyShouldWorkWithValidInputs(int a, int b, int expected) {
        int result = arithmetic.multiply(a, b);
        assertEquals(expected, result);
    }
    
    @Test
    @DisplayName("Multiplication should throw exception on overflow")
    void multiplyShouldThrowExceptionOnOverflow() {
        assertThatThrownBy(() -> arithmetic.multiply(Integer.MAX_VALUE, 2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Multiplication would cause integer overflow");
    }
    
    @DisplayName("Division should work correctly with valid inputs")
    @ParameterizedTest(name = "{0} / {1} = {2}")
    @CsvSource({
        "6, 3, 2.0",
        "5, 2, 2.5",
        "-15, 3, -5.0",
        "-10, -2, 5.0",
        "0, 5, 0.0"
    })
    void divideShouldWorkWithValidInputs(int a, int b, double expected) {
        double result = arithmetic.divide(a, b);
        // Use AssertJ for floating-point comparison
        assertThat(result).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("Division should throw exception when dividing by zero")
    void divideShouldThrowExceptionOnDivideByZero() {
        assertThatThrownBy(() -> arithmetic.divide(10, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Division by zero");
    }
    
    @DisplayName("Modulo should work correctly with valid inputs")
    @ParameterizedTest(name = "{0} % {1} = {2}")
    @CsvSource({
        "7, 3, 1",
        "8, 2, 0",
        "5, 10, 5",
        "-7, 3, -1",
        "7, -3, 1"
    })
    void moduloShouldWorkWithValidInputs(int a, int b, int expected) {
        int result = arithmetic.modulo(a, b);
        assertEquals(expected, result);
    }
    
    @Test
    @DisplayName("Modulo should throw exception when dividing by zero")
    void moduloShouldThrowExceptionOnDivideByZero() {
        assertThatThrownBy(() -> arithmetic.modulo(10, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Modulo by zero");
    }
} 