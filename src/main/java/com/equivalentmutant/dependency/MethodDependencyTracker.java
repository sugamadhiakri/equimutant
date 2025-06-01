package com.equivalentmutant.dependency;

import com.equivalentmutant.model.JavaMethod;
import com.equivalentmutant.model.MethodDependency;
import com.equivalentmutant.model.SourceLocation;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tracks method dependencies by analyzing method calls within a method.
 */
public class MethodDependencyTracker {
    
    private final JavaParser parser;
    
    public MethodDependencyTracker() {
        this.parser = new JavaParser();
    }
    
    /**
     * Identifies direct method calls within the given method.
     * 
     * @param method The method to analyze
     * @return A list of method call information
     */
    public List<MethodCallInfo> findMethodCalls(JavaMethod method) {
        List<MethodCallInfo> methodCalls = new ArrayList<>();
        
        try {
            // Instead of parsing just the method source code, we need to parse the entire file
            // to get proper context for method calls
            File sourceFile = new File(method.getLocation().getFilePath());
            if (!sourceFile.exists()) {
                throw new RuntimeException("Source file not found: " + method.getLocation().getFilePath());
            }
            
            ParseResult<CompilationUnit> parseResult = parser.parse(sourceFile);
            if (!parseResult.isSuccessful() || !parseResult.getResult().isPresent()) {
                throw new RuntimeException("Failed to parse file: " + sourceFile.getPath());
            }
            
            CompilationUnit cu = parseResult.getResult().get();
            
            // Find the specific method in the compilation unit and analyze its calls
            cu.findAll(MethodDeclaration.class).stream()
                .filter(m -> m.getNameAsString().equals(method.getMethodName()))
                .filter(m -> {
                    // Try to match the method by signature as well if possible
                    ClassOrInterfaceDeclaration parent = m.findAncestor(ClassOrInterfaceDeclaration.class).orElse(null);
                    if (parent == null) {
                        return true; // If we can't find the parent class, just match by name
                    }
                    return parent.getNameAsString().equals(method.getClassName());
                })
                .findFirst()
                .ifPresent(methodDecl -> {
                    // Create a visitor to extract all method calls within this method
                    methodDecl.accept(new MethodCallVisitor(sourceFile.getPath(), methodCalls), null);
                });
            
            return methodCalls;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to parse source file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates a method dependency between caller and callee.
     * 
     * @param caller The caller method
     * @param callee The callee method
     * @param callSite The location of the call
     * @return A MethodDependency object
     */
    public MethodDependency createDependency(JavaMethod caller, JavaMethod callee, SourceLocation callSite) {
        return new MethodDependency(caller, callee, callSite);
    }
    
    /**
     * Information about a method call.
     */
    public static class MethodCallInfo {
        private final String methodName;
        private final List<String> argumentTypes;
        private final SourceLocation location;
        
        public MethodCallInfo(String methodName, List<String> argumentTypes, SourceLocation location) {
            this.methodName = methodName;
            this.argumentTypes = argumentTypes;
            this.location = location;
        }
        
        public String getMethodName() {
            return methodName;
        }
        
        public List<String> getArgumentTypes() {
            return argumentTypes;
        }
        
        public SourceLocation getLocation() {
            return location;
        }
    }
    
    /**
     * Visitor to extract method call information from a method body.
     */
    private static class MethodCallVisitor extends VoidVisitorAdapter<Void> {
        private final String filePath;
        private final List<MethodCallInfo> methodCalls;
        
        public MethodCallVisitor(String filePath, List<MethodCallInfo> methodCalls) {
            this.filePath = filePath;
            this.methodCalls = methodCalls;
        }
        
        @Override
        public void visit(MethodCallExpr methodCallExpr, Void arg) {
            // Get method name
            String methodName = methodCallExpr.getNameAsString();
            
            // Get argument types (this is a simplification, in a real implementation we'd need to resolve types)
            List<String> argumentTypes = new ArrayList<>();
            methodCallExpr.getArguments().forEach(argument -> {
                // In a real implementation, we'd resolve the actual type
                argumentTypes.add("Unknown");
            });
            
            // Create source location
            SourceLocation location = new SourceLocation(
                filePath,
                methodCallExpr.getBegin().map(pos -> pos.line).orElse(0),
                methodCallExpr.getBegin().map(pos -> pos.column).orElse(0),
                methodCallExpr.getEnd().map(pos -> pos.line).orElse(0),
                methodCallExpr.getEnd().map(pos -> pos.column).orElse(0)
            );
            
            // Create and add the MethodCallInfo object
            MethodCallInfo methodCallInfo = new MethodCallInfo(methodName, argumentTypes, location);
            methodCalls.add(methodCallInfo);
            
            super.visit(methodCallExpr, arg);
        }
    }
} 