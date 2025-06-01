package com.calculator.advanced;

import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.analysis.function.Log;
import org.apache.commons.math3.analysis.function.Power;
import org.apache.commons.math3.analysis.function.Sin;
import org.apache.commons.math3.analysis.function.Cos;
import org.apache.commons.math3.analysis.function.Tan;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

import com.google.common.base.Preconditions;

/**
 * Scientific calculator with advanced mathematical operations
 * using Apache Commons Math library
 */
public class ScientificCalculator {
    
    private final Power power = new Power();
    private final Exp exp = new Exp();
    private final Log log = new Log();
    private final Sin sin = new Sin();
    private final Cos cos = new Cos();
    private final Tan tan = new Tan();
    
    /**
     * Calculates the power of a number
     * @param base the base
     * @param exponent the exponent
     * @return base raised to the power of exponent
     */
    public double power(double base, double exponent) {
        return power.value(base, exponent);
    }
    
    /**
     * Calculates the square root of a number
     * @param value the input value
     * @return the square root of the value
     */
    public double sqrt(double value) {
        Preconditions.checkArgument(value >= 0, "Cannot calculate square root of negative number: %s", value);
        return FastMath.sqrt(value);
    }
    
    /**
     * Calculates the exponential function (e^x)
     * @param value the exponent
     * @return e raised to the power of value
     */
    public double exp(double value) {
        return exp.value(value);
    }
    
    /**
     * Calculates the natural logarithm
     * @param value the input value
     * @return natural logarithm of value
     */
    public double ln(double value) {
        Preconditions.checkArgument(value > 0, "Cannot calculate logarithm of non-positive number: %s", value);
        return log.value(value);
    }
    
    /**
     * Calculates the logarithm with a specified base
     * @param value the input value
     * @param base the logarithm base
     * @return logarithm of value with specified base
     */
    public double log(double value, double base) {
        Preconditions.checkArgument(value > 0, "Cannot calculate logarithm of non-positive number: %s", value);
        Preconditions.checkArgument(base > 0 && base != 1, "Logarithm base must be positive and not equal to 1: %s", base);
        return FastMath.log(value, base);
    }
    
    /**
     * Calculates the sine of an angle
     * @param angleRadians the angle in radians
     * @return sine of the angle
     */
    public double sin(double angleRadians) {
        return sin.value(angleRadians);
    }
    
    /**
     * Calculates the cosine of an angle
     * @param angleRadians the angle in radians
     * @return cosine of the angle
     */
    public double cos(double angleRadians) {
        return cos.value(angleRadians);
    }
    
    /**
     * Calculates the tangent of an angle
     * @param angleRadians the angle in radians
     * @return tangent of the angle
     */
    public double tan(double angleRadians) {
        // Check for undefined values (π/2 + nπ)
        double normalizedAngle = angleRadians % FastMath.PI;
        Preconditions.checkArgument(Precision.compareTo(FastMath.abs(normalizedAngle), FastMath.PI/2, 1e-10) != 0, 
                "Tangent is undefined at π/2 + nπ");
        return tan.value(angleRadians);
    }
    
    /**
     * Converts degrees to radians
     * @param angleDegrees the angle in degrees
     * @return the angle in radians
     */
    public double toRadians(double angleDegrees) {
        return FastMath.toRadians(angleDegrees);
    }
    
    /**
     * Converts radians to degrees
     * @param angleRadians the angle in radians
     * @return the angle in degrees
     */
    public double toDegrees(double angleRadians) {
        return FastMath.toDegrees(angleRadians);
    }
    
    /**
     * Calculates basic statistics on a dataset
     * @param data the input data array
     * @return a string containing mean, median, min, max, and standard deviation
     */
    public String calculateStatistics(double[] data) {
        Preconditions.checkArgument(data != null && data.length > 0, "Data array cannot be null or empty");
        
        DescriptiveStatistics stats = new DescriptiveStatistics(data);
        return String.format(
                "Mean: %.4f\nMedian: %.4f\nMin: %.4f\nMax: %.4f\nStd Dev: %.4f",
                stats.getMean(),
                stats.getPercentile(50),
                stats.getMin(),
                stats.getMax(),
                stats.getStandardDeviation()
        );
    }
} 