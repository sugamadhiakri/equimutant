package com.equivalentmutant.dependency;

import com.equivalentmutant.model.DependencyGraph;
import com.equivalentmutant.model.JavaMethod;
import com.equivalentmutant.model.MethodDependency;

import java.util.*;

/**
 * Resolves method dependencies recursively.
 */
public class RecursiveDependencyResolver {
    
    private final MethodDependencyTracker dependencyTracker;
    private final MethodResolver methodResolver;
    
    public RecursiveDependencyResolver(MethodDependencyTracker dependencyTracker, MethodResolver methodResolver) {
        this.dependencyTracker = dependencyTracker;
        this.methodResolver = methodResolver;
    }
    
    /**
     * Builds a dependency graph for the given root method by recursively resolving all method calls.
     * 
     * @param rootMethod The root method to analyze
     * @param maxDepth The maximum recursion depth (use -1 for unlimited)
     * @return A dependency graph
     */
    public DependencyGraph buildDependencyGraph(JavaMethod rootMethod, int maxDepth) {
        DependencyGraph graph = new DependencyGraph(rootMethod);
        Set<JavaMethod> visitedMethods = new HashSet<>();
        
        buildDependencyGraphRecursive(rootMethod, graph, visitedMethods, 0, maxDepth);
        
        return graph;
    }
    
    private void buildDependencyGraphRecursive(JavaMethod method, DependencyGraph graph,
                                              Set<JavaMethod> visitedMethods, int currentDepth, int maxDepth) {
        // Stop if we've already processed this method or exceeded max depth
        if (visitedMethods.contains(method) || (maxDepth >= 0 && currentDepth > maxDepth)) {
            return;
        }
        
        visitedMethods.add(method);
        
        // Find all method calls within this method
        List<MethodDependencyTracker.MethodCallInfo> methodCalls = dependencyTracker.findMethodCalls(method);
        
        for (MethodDependencyTracker.MethodCallInfo callInfo : methodCalls) {
            // Resolve the called method
            Optional<JavaMethod> calleeOpt = methodResolver.resolveMethod(callInfo.getMethodName(), callInfo.getArgumentTypes());
            
            if (calleeOpt.isPresent()) {
                JavaMethod callee = calleeOpt.get();
                
                // Create and add the dependency
                MethodDependency dependency = dependencyTracker.createDependency(method, callee, callInfo.getLocation());
                graph.addDependency(dependency);
                
                // Recursively process the callee
                buildDependencyGraphRecursive(callee, graph, visitedMethods, currentDepth + 1, maxDepth);
            }
        }
    }
} 