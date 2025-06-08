package com.calculator.financial;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.util.FastMath;

/**
 * Financial calculator that handles common financial calculations
 * such as compound interest, loan payments, and depreciation
 */
public class FinancialCalculator {

    /**
     * Calculates future value with compound interest
     * 
     * @param principal initial investment amount
     * @param rate annual interest rate (as a decimal, e.g., 0.05 for 5%)
     * @param time time period in years
     * @param compoundingPerYear number of times interest is compounded per year
     * @return the future value of the investment
     */
    public double calculateCompoundInterest(double principal, double rate, double time, int compoundingPerYear) {
        Preconditions.checkArgument(principal >= 0, "Principal cannot be negative: %s", principal);
        Preconditions.checkArgument(rate >= 0, "Interest rate cannot be negative: %s", rate);
        Preconditions.checkArgument(time >= 0, "Time cannot be negative: %s", time);
        Preconditions.checkArgument(compoundingPerYear > 0, "Compounding frequency must be positive: %s", compoundingPerYear);
        
        return principal * FastMath.pow(1 + rate / compoundingPerYear, compoundingPerYear * time);
    }
    
    /**
     * Calculates monthly payment for a fixed-rate loan
     * 
     * @param principal loan amount
     * @param annualInterestRate annual interest rate (as a decimal)
     * @param loanTermYears loan term in years
     * @return monthly payment amount
     */
    public double calculateLoanPayment(double principal, double annualInterestRate, int loanTermYears) {
        Preconditions.checkArgument(principal > 0, "Principal must be positive: %s", principal);
        Preconditions.checkArgument(annualInterestRate >= 0, "Interest rate cannot be negative: %s", annualInterestRate);
        Preconditions.checkArgument(loanTermYears > 0, "Loan term must be positive: %s", loanTermYears);
        
        double monthlyRate = annualInterestRate / 12;
        int totalPayments = loanTermYears * 12;
        
        if (annualInterestRate == 0) {
            return principal / totalPayments;
        }
        
        double factor = FastMath.pow(1 + monthlyRate, totalPayments);
        return principal * monthlyRate * factor / (factor - 1);
    }
    
    /**
     * Calculates straight-line depreciation
     * 
     * @param cost initial cost of the asset
     * @param salvageValue estimated salvage value at the end of useful life
     * @param usefulLifeYears useful life of the asset in years
     * @return annual depreciation amount
     */
    public double calculateStraightLineDepreciation(double cost, double salvageValue, int usefulLifeYears) {
        Preconditions.checkArgument(cost >= 0, "Asset cost cannot be negative: %s", cost);
        Preconditions.checkArgument(salvageValue >= 0, "Salvage value cannot be negative: %s", salvageValue);
        Preconditions.checkArgument(salvageValue <= cost, "Salvage value cannot exceed cost: %s > %s", salvageValue, cost);
        Preconditions.checkArgument(usefulLifeYears > 0, "Useful life must be positive: %s", usefulLifeYears);
        
        return (cost - salvageValue) / usefulLifeYears;
    }
    
    /**
     * Calculates the present value of a future amount
     * 
     * @param futureValue the future value
     * @param rate discount rate (as a decimal)
     * @param years number of years
     * @return the present value
     */
    public double calculatePresentValue(double futureValue, double rate, int years) {
        Preconditions.checkArgument(futureValue >= 0, "Future value cannot be negative: %s", futureValue);
        Preconditions.checkArgument(rate >= 0, "Discount rate cannot be negative: %s", rate);
        Preconditions.checkArgument(years >= 0, "Years cannot be negative: %s", years);
        
        return futureValue / FastMath.pow(1 + rate, years);
    }
    
    /**
     * Calculates the internal rate of return (IRR) for a series of cash flows
     * 
     * @param initialInvestment the initial investment (negative value)
     * @param cashFlows array of cash flows (positive values)
     * @param guess initial guess for IRR
     * @param maxIterations maximum number of iterations for convergence
     * @param tolerance tolerance for convergence
     * @return the internal rate of return as a decimal
     */
    public double calculateIRR(double initialInvestment, double[] cashFlows, double guess, 
                              int maxIterations, double tolerance) {
        Preconditions.checkArgument(initialInvestment < 0, "Initial investment must be negative: %s", initialInvestment);
        Preconditions.checkArgument(cashFlows != null && cashFlows.length > 0, "Cash flows array cannot be null or empty");
        
        double rate = guess;
        double[] allCashFlows = new double[cashFlows.length + 1];
        allCashFlows[0] = initialInvestment;
        System.arraycopy(cashFlows, 0, allCashFlows, 1, cashFlows.length);
        
        for (int i = 0; i < maxIterations; i++) {
            double npv = calculateNPV(rate, allCashFlows);
            
            if (FastMath.abs(npv) < tolerance) {
                return rate;
            }
            
            double derivativeNpv = calculateNPVDerivative(rate, allCashFlows);
            double newRate = rate - npv / derivativeNpv;
            
            if (FastMath.abs(newRate - rate) < tolerance) {
                return newRate;
            }
            
            rate = newRate;
        }
        
        throw new IllegalStateException("IRR calculation did not converge after " + maxIterations + " iterations");
    }
    
    /**
     * Calculates net present value for a series of cash flows
     */
    private double calculateNPV(double rate, double[] cashFlows) {
        double npv = 0;
        for (int i = 0; i < cashFlows.length; i++) {
            npv += cashFlows[i] / FastMath.pow(1 + rate, i);
        }
        return npv;
    }
    
    /**
     * Calculates derivative of NPV with respect to rate
     */
    private double calculateNPVDerivative(double rate, double[] cashFlows) {
        double derivative = 0;
        for (int i = 1; i < cashFlows.length; i++) {
            derivative -= i * cashFlows[i] / FastMath.pow(1 + rate, i + 1);
        }
        return derivative;
    }
} 