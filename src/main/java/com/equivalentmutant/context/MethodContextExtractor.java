package com.equivalentmutant.context;

import com.equivalentmutant.model.DependencyGraph;
import com.equivalentmutant.model.JavaMethod;
import com.equivalentmutant.model.MethodDependency;

import java.util.*;

/**
 * Extracts and formats method context for further analysis.
 */
public class MethodContextExtractor {
    
    /**
     * Extracts the complete context of a method and its dependencies.
     * 
     * @param graph The dependency graph
     * @return A formatted string with the complete context
     */
    public String extractMethodContext(DependencyGraph graph) {
        StringBuilder context = new StringBuilder();
        Set<JavaMethod> processedMethods = new HashSet<>();
        
        // Start with the root method
        JavaMethod rootMethod = graph.getRootMethod();
        context.append("ROOT METHOD:\n");
        appendMethodContext(context, rootMethod);
        processedMethods.add(rootMethod);
        
        // Process all dependencies
        context.append("\nDEPENDENCIES:\n");
        for (Map.Entry<JavaMethod, Set<MethodDependency>> entry : graph.getAllDependencies().entrySet()) {
            JavaMethod caller = entry.getKey();
            
            for (MethodDependency dependency : entry.getValue()) {
                JavaMethod callee = dependency.getCallee();
                
                if (!processedMethods.contains(callee)) {
                    context.append("Method called from: ")
                          .append(caller.getFullyQualifiedName())
                          .append("\n");
                    appendMethodContext(context, callee);
                    context.append("\n");
                    processedMethods.add(callee);
                }
            }
        }
        
        return context.toString();
    }
    
    private void appendMethodContext(StringBuilder context, JavaMethod method) {
        context.append("Package: ").append(method.getPackageName()).append("\n");
        context.append("Class: ").append(method.getClassName()).append("\n");
        context.append("Method: ").append(method.getSignature()).append("\n");
        context.append("Source: ").append(method.getLocation()).append("\n");
        context.append("Code:\n").append(method.getSourceCode()).append("\n");
    }
    
    /**
     * Extracts a dependency structure in a format suitable for visualization.
     * 
     * @param graph The dependency graph
     * @return A map representation of the dependency structure
     */
    public Map<String, Object> extractDependencyStructure(DependencyGraph graph) {
        Map<String, Object> structure = new HashMap<>();
        structure.put("rootMethod", formatMethod(graph.getRootMethod()));
        
        List<Map<String, Object>> dependencies = new ArrayList<>();
        for (Map.Entry<JavaMethod, Set<MethodDependency>> entry : graph.getAllDependencies().entrySet()) {
            for (MethodDependency dependency : entry.getValue()) {
                Map<String, Object> dep = new HashMap<>();
                dep.put("caller", formatMethod(dependency.getCaller()));
                dep.put("callee", formatMethod(dependency.getCallee()));
                dep.put("callSite", dependency.getCallSite().toString());
                dependencies.add(dep);
            }
        }
        
        structure.put("dependencies", dependencies);
        return structure;
    }
    
    private Map<String, String> formatMethod(JavaMethod method) {
        Map<String, String> formatted = new HashMap<>();
        formatted.put("name", method.getMethodName());
        formatted.put("class", method.getClassName());
        formatted.put("package", method.getPackageName());
        formatted.put("signature", method.getSignature());
        return formatted;
    }
} 