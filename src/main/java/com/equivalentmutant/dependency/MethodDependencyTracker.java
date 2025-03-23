package com.equivalentmutant.dependency;

import com.equivalentmutant.model.JavaMethod;
import com.equivalentmutant.model.MethodDependency;
import com.equivalentmutant.model.SourceLocation;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

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
        String sourceCode = method.getSourceCode();
        List<MethodCallInfo> methodCalls = new ArrayList<>();
        
        // Parse the method source code
        Optional<CompilationUnit> cuOpt = parser.parse(sourceCode).getResult();
        if (!cuOpt.isPresent()) {
            throw new RuntimeException("Failed to parse method: " + method.getSignature());
        }
        
        // Visit all method calls
        cuOpt.get().accept(new MethodCallVisitor(method.getLocation().getFilePath(), methodCalls), null);
        
        return methodCalls;
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