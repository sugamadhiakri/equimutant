package com.equivalentmutant.dependency;

import com.equivalentmutant.model.JavaMethod;

import java.util.*;

/**
 * A simple implementation of MethodResolver that uses a collection of known methods.
 */
public class SimpleMethodResolver implements MethodResolver {
    
    private final Map<String, List<JavaMethod>> methodsByName = new HashMap<>();
    
    /**
     * Adds a method to the resolver.
     * 
     * @param method The method to add
     */
    public void addMethod(JavaMethod method) {
        methodsByName.computeIfAbsent(method.getMethodName(), k -> new ArrayList<>()).add(method);
    }
    
    /**
     * Adds multiple methods to the resolver.
     * 
     * @param methods The methods to add
     */
    public void addMethods(Collection<JavaMethod> methods) {
        for (JavaMethod method : methods) {
            addMethod(method);
        }
    }
    
    @Override
    public Optional<JavaMethod> resolveMethod(String methodName, List<String> argumentTypes) {
        List<JavaMethod> candidates = methodsByName.getOrDefault(methodName, Collections.emptyList());
        
        // In a real implementation, we'd match based on argument types
        // This is a simplified version that just returns the first matching method by name
        return candidates.stream().findFirst();
    }
} 