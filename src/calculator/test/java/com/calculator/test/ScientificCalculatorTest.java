package com.calculator.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.calculator.advanced.ScientificCalculator;

public class ScientificCalculatorTest {

    private ScientificCalculator calculator;
    private static final double EPSILON = 1e-10;
    
    @BeforeEach
    void setUp() {
        calculator = new ScientificCalculator();
    }
    
    @DisplayName("Power function should calculate correctly")
    @ParameterizedTest(name = "{0}^{1} = {2}")
    @CsvSource({
        "2.0, 3.0, 8.0",
        "3.0, 2.0, 9.0",
        "2.0, 0.5, 1.4142135623730951",
        "4.0, -0.5, 0.5",
        "1.0, 10.0, 1.0",
        "0.0, 0.0, 1.0",
        "10.0, -1.0, 0.1"
    })
    void powerShouldCalculateCorrectly(double base, double exponent, double expected) {
        double result = calculator.power(base, exponent);
        assertThat(result).isCloseTo(expected, within(EPSILON));
    }
    
    @DisplayName("Square root should calculate correctly")
    @ParameterizedTest(name = "sqrt({0}) = {1}")
    @CsvSource({
        "4.0, 2.0",
        "9.0, 3.0",
        "2.0, 1.4142135623730951",
        "0.0, 0.0",
        "1.0, 1.0",
        "0.25, 0.5"
    })
    void sqrtShouldCalculateCorrectly(double value, double expected) {
        double result = calculator.sqrt(value);
        assertThat(result).isCloseTo(expected, within(EPSILON));
    }
    
    @Test
    @DisplayName("Square root should throw exception for negative inputs")
    void sqrtShouldThrowExceptionForNegativeInputs() {
        assertThatThrownBy(() -> calculator.sqrt(-1.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cannot calculate square root of negative number");
    }
    
    @DisplayName("Exponential function should calculate correctly")
    @ParameterizedTest(name = "exp({0}) = {1}")
    @CsvSource({
        "0.0, 1.0",
        "1.0, 2.718281828459045",
        "-1.0, 0.36787944117144233",
        "2.0, 7.38905609893065"
    })
    void expShouldCalculateCorrectly(double value, double expected) {
        double result = calculator.exp(value);
        assertThat(result).isCloseTo(expected, within(EPSILON));
    }
    
    @DisplayName("Natural logarithm should calculate correctly")
    @ParameterizedTest(name = "ln({0}) = {1}")
    @CsvSource({
        "1.0, 0.0",
        "2.718281828459045, 1.0",
        "0.36787944117144233, -1.0",
        "7.38905609893065, 2.0"
    })
    void lnShouldCalculateCorrectly(double value, double expected) {
        double result = calculator.ln(value);
        assertThat(result).isCloseTo(expected, within(EPSILON));
    }
    
    @Test
    @DisplayName("Natural logarithm should throw exception for non-positive inputs")
    void lnShouldThrowExceptionForNonPositiveInputs() {
        assertThatThrownBy(() -> calculator.ln(0.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cannot calculate logarithm of non-positive number");
            
        assertThatThrownBy(() -> calculator.ln(-1.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cannot calculate logarithm of non-positive number");
    }
    
    @DisplayName("Logarithm with base should calculate correctly")
    @ParameterizedTest(name = "log({0}, {1}) = {2}")
    @CsvSource({
        "100.0, 10.0, 2.0",
        "8.0, 2.0, 3.0",
        "0.01, 10.0, -2.0",
        "10000.0, 10.0, 4.0"
    })
    void logShouldCalculateCorrectly(double value, double base, double expected) {
        double result = calculator.log(value, base);
        assertThat(result).isCloseTo(expected, within(EPSILON));
    }
    
    @Test
    @DisplayName("Logarithm should throw exception for invalid inputs")
    void logShouldThrowExceptionForInvalidInputs() {
        assertThatThrownBy(() -> calculator.log(0.0, 10.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cannot calculate logarithm of non-positive number");
            
        assertThatThrownBy(() -> calculator.log(10.0, 0.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Logarithm base must be positive and not equal to 1");
            
        assertThatThrownBy(() -> calculator.log(10.0, 1.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Logarithm base must be positive and not equal to 1");
    }
    
    @DisplayName("Sine should calculate correctly")
    @ParameterizedTest(name = "sin({0}) = {1}")
    @CsvSource({
        "0.0, 0.0",
        "1.5707963267948966, 1.0", // π/2
        "3.141592653589793, 0.0",  // π
        "4.71238898038469, -1.0"   // 3π/2
    })
    void sinShouldCalculateCorrectly(double angleRadians, double expected) {
        double result = calculator.sin(angleRadians);
        assertThat(result).isCloseTo(expected, within(EPSILON));
    }
    
    @DisplayName("Cosine should calculate correctly")
    @ParameterizedTest(name = "cos({0}) = {1}")
    @CsvSource({
        "0.0, 1.0",
        "1.5707963267948966, 0.0", // π/2
        "3.141592653589793, -1.0", // π
        "4.71238898038469, 0.0"    // 3π/2
    })
    void cosShouldCalculateCorrectly(double angleRadians, double expected) {
        double result = calculator.cos(angleRadians);
        assertThat(result).isCloseTo(expected, within(EPSILON));
    }
    
    @DisplayName("Tangent should calculate correctly")
    @ParameterizedTest(name = "tan({0}) = {1}")
    @CsvSource({
        "0.0, 0.0",
        "0.7853981633974483, 1.0", // π/4
        "3.141592653589793, 0.0",  // π
        "2.356194490192345, -1.0"  // 3π/4
    })
    void tanShouldCalculateCorrectly(double angleRadians, double expected) {
        double result = calculator.tan(angleRadians);
        assertThat(result).isCloseTo(expected, within(EPSILON));
    }
    
    @Test
    @DisplayName("Tangent should throw exception for undefined values")
    void tanShouldThrowExceptionForUndefinedValues() {
        double halfPi = Math.PI / 2;
        
        assertThatThrownBy(() -> calculator.tan(halfPi))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Tangent is undefined");
    }
    
    @DisplayName("Degrees to radians conversion should be correct")
    @ParameterizedTest(name = "toRadians({0}) = {1}")
    @CsvSource({
        "0.0, 0.0",
        "90.0, 1.5707963267948966",
        "180.0, 3.141592653589793",
        "270.0, 4.71238898038469",
        "360.0, 6.283185307179586"
    })
    void toRadiansShouldConvertCorrectly(double degrees, double expected) {
        double result = calculator.toRadians(degrees);
        assertThat(result).isCloseTo(expected, within(EPSILON));
    }
    
    @DisplayName("Radians to degrees conversion should be correct")
    @ParameterizedTest(name = "toDegrees({0}) = {1}")
    @CsvSource({
        "0.0, 0.0",
        "1.5707963267948966, 90.0",
        "3.141592653589793, 180.0",
        "4.71238898038469, 270.0",
        "6.283185307179586, 360.0"
    })
    void toDegreesShouldConvertCorrectly(double radians, double expected) {
        double result = calculator.toDegrees(radians);
        assertThat(result).isCloseTo(expected, within(EPSILON));
    }
    
    @Test
    @DisplayName("Calculate statistics should work correctly")
    void calculateStatisticsShouldWorkCorrectly() {
        double[] data = {1.0, 2.0, 3.0, 4.0, 5.0};
        String result = calculator.calculateStatistics(data);
        
        assertThat(result).contains("Mean: 3.0000");
        assertThat(result).contains("Median: 3.0000");
        assertThat(result).contains("Min: 1.0000");
        assertThat(result).contains("Max: 5.0000");
        assertThat(result).contains("Std Dev: 1.5811");
    }
    
    @Test
    @DisplayName("Calculate statistics should throw exception for null or empty data")
    void calculateStatisticsShouldThrowExceptionForInvalidData() {
        assertThatThrownBy(() -> calculator.calculateStatistics(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Data array cannot be null or empty");
            
        assertThatThrownBy(() -> calculator.calculateStatistics(new double[0]))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Data array cannot be null or empty");
    }
} 