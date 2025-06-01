package com.equivalentmutant.dependency;

import com.equivalentmutant.model.JavaMethod;

import java.util.*;

/**
 * A simple implementation of MethodResolver that uses a collection of known methods.
 */
public class SimpleMethodResolver implements MethodResolver {
    
    private final Map<String, List<JavaMethod>> methodsByName = new HashMap<>();
    private final Map<String, List<JavaMethod>> methodsByFullyQualifiedName = new HashMap<>();
    
    /**
     * Adds a method to the resolver.
     * 
     * @param method The method to add
     */
    public void addMethod(JavaMethod method) {
        methodsByName.computeIfAbsent(method.getMethodName(), k -> new ArrayList<>()).add(method);
        methodsByFullyQualifiedName.computeIfAbsent(method.getFullyQualifiedName(), k -> new ArrayList<>()).add(method);
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
        // First, try to find methods with the given name
        List<JavaMethod> candidates = methodsByName.getOrDefault(methodName, Collections.emptyList());
        
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        
        // If there's exactly one method with this name, return it
        if (candidates.size() == 1) {
            return Optional.of(candidates.get(0));
        }
        
        // Try to match by argument count if we know them
        if (!argumentTypes.isEmpty()) {
            List<JavaMethod> matchingArgCount = new ArrayList<>();
            for (JavaMethod candidate : candidates) {
                if (candidate.getParameterTypes().size() == argumentTypes.size()) {
                    matchingArgCount.add(candidate);
                }
            }
            
            if (matchingArgCount.size() == 1) {
                return Optional.of(matchingArgCount.get(0));
            }
            
            if (!matchingArgCount.isEmpty()) {
                // Return the first one with matching arg count
                return Optional.of(matchingArgCount.get(0));
            }
        }
        
        // If we can't match by arguments, just return the first candidate
        return Optional.of(candidates.get(0));
    }
    
    /**
     * Resolves a method by its fully qualified name.
     * 
     * @param fullyQualifiedName The fully qualified method name (packageName.className.methodName)
     * @return The resolved method, if found
     */
    public Optional<JavaMethod> resolveMethodByFullyQualifiedName(String fullyQualifiedName) {
        List<JavaMethod> candidates = methodsByFullyQualifiedName.getOrDefault(fullyQualifiedName, Collections.emptyList());
        return candidates.stream().findFirst();
    }
} 