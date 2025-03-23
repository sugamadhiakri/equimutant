package com.equivalentmutant;

import com.equivalentmutant.cli.CommandLineInterface;

/**
 * Main entry point for the Equivalent Mutant Analyzer Tool.
 */
public class Main {
    
    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        cli.run(args);
    }
} 