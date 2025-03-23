package com.equivalentmutant.cli;

import com.equivalentmutant.dependency.MethodDependencyTracker;
import com.equivalentmutant.dependency.RecursiveDependencyResolver;
import com.equivalentmutant.dependency.SimpleMethodResolver;
import com.equivalentmutant.model.DependencyGraph;
import com.equivalentmutant.model.JavaMethod;
import com.equivalentmutant.parser.JavaSourceParser;
import com.equivalentmutant.context.MethodContextExtractor;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Command-line interface for the equivalent mutant analyzer tool.
 */
public class CommandLineInterface {
    
    private final JavaSourceParser parser;
    private final MethodDependencyTracker dependencyTracker;
    private final SimpleMethodResolver methodResolver;
    private final RecursiveDependencyResolver dependencyResolver;
    private final MethodContextExtractor contextExtractor;
    
    public CommandLineInterface() {
        this.parser = new JavaSourceParser();
        this.dependencyTracker = new MethodDependencyTracker();
        this.methodResolver = new SimpleMethodResolver();
        this.dependencyResolver = new RecursiveDependencyResolver(dependencyTracker, methodResolver);
        this.contextExtractor = new MethodContextExtractor();
    }
    
    /**
     * Parses command-line arguments and executes the appropriate action.
     * 
     * @param args Command-line arguments
     */
    public void run(String[] args) {
        Options options = createOptions();
        CommandLineParser cmdParser = new DefaultParser();
        
        try {
            CommandLine cmd = cmdParser.parse(options, args);
            
            if (cmd.hasOption("help")) {
                printHelp(options);
                return;
            }
            
            String sourcePath = cmd.getOptionValue("source");
            String className = cmd.getOptionValue("class");
            String methodName = cmd.getOptionValue("method");
            int maxDepth = Integer.parseInt(cmd.getOptionValue("depth", "-1"));
            
            if (sourcePath == null || className == null || methodName == null) {
                System.err.println("Error: source, class, and method options are required.");
                printHelp(options);
                return;
            }
            
            analyze(sourcePath, className, methodName, maxDepth);
            
        } catch (ParseException e) {
            System.err.println("Error parsing command-line arguments: " + e.getMessage());
            printHelp(options);
        } catch (Exception e) {
            System.err.println("Error during analysis: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Options createOptions() {
        Options options = new Options();
        
        Option sourceOption = Option.builder("s")
            .longOpt("source")
            .hasArg()
            .argName("PATH")
            .desc("Path to the source directory or file")
            .required()
            .build();
        
        Option classOption = Option.builder("c")
            .longOpt("class")
            .hasArg()
            .argName("CLASS")
            .desc("Fully qualified name of the class")
            .required()
            .build();
        
        Option methodOption = Option.builder("m")
            .longOpt("method")
            .hasArg()
            .argName("METHOD")
            .desc("Name of the method to analyze")
            .required()
            .build();
        
        Option depthOption = Option.builder("d")
            .longOpt("depth")
            .hasArg()
            .argName("DEPTH")
            .desc("Maximum recursion depth (-1 for unlimited)")
            .build();
        
        Option helpOption = Option.builder("h")
            .longOpt("help")
            .desc("Print help information")
            .build();
        
        options.addOption(sourceOption);
        options.addOption(classOption);
        options.addOption(methodOption);
        options.addOption(depthOption);
        options.addOption(helpOption);
        
        return options;
    }
    
    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar equivalent-mutant-analyzer.jar", 
                           "Equivalent Mutant Analyzer Tool", options, 
                           "Example: java -jar equivalent-mutant-analyzer.jar -s src/main/java -c com.example.MyClass -m myMethod", 
                           true);
    }
    
    private void analyze(String sourcePath, String className, String methodName, int maxDepth) throws FileNotFoundException {
        System.out.println("Analyzing method: " + className + "." + methodName);
        System.out.println("Source path: " + sourcePath);
        System.out.println("Max depth: " + (maxDepth < 0 ? "unlimited" : maxDepth));
        
        // Parse all Java files in the source directory
        List<JavaMethod> allMethods = new ArrayList<>();
        File sourceDir = new File(sourcePath);
        
        if (sourceDir.isDirectory()) {
            parseDirectory(sourceDir, allMethods);
        } else if (sourceDir.isFile() && sourceDir.getName().endsWith(".java")) {
            allMethods.addAll(parser.parseFile(sourceDir));
        } else {
            throw new IllegalArgumentException("Source path must be a directory or a Java file");
        }
        
        // Add all methods to the resolver
        methodResolver.addMethods(allMethods);
        
        // Find the target method
        Optional<JavaMethod> targetMethodOpt = allMethods.stream()
            .filter(m -> m.getFullyQualifiedName().equals(className + "." + methodName))
            .findFirst();
        
        if (!targetMethodOpt.isPresent()) {
            throw new IllegalArgumentException("Target method not found: " + className + "." + methodName);
        }
        
        JavaMethod targetMethod = targetMethodOpt.get();
        
        // Build the dependency graph
        DependencyGraph graph = dependencyResolver.buildDependencyGraph(targetMethod, maxDepth);
        
        // Extract and print the context
        String context = contextExtractor.extractMethodContext(graph);
        System.out.println("\nMethod Context:");
        System.out.println(context);
        
        // Print summary
        System.out.println("\nDependency Summary:");
        System.out.println("Total methods in dependency graph: " + graph.getAllMethods().size());
    }
    
    private void parseDirectory(File directory, List<JavaMethod> allMethods) throws FileNotFoundException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    parseDirectory(file, allMethods);
                } else if (file.getName().endsWith(".java")) {
                    allMethods.addAll(parser.parseFile(file));
                }
            }
        }
    }
} 