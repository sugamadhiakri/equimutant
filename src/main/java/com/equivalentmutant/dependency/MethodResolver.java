package com.equivalentmutant.dependency;

import com.equivalentmutant.model.JavaMethod;

import java.util.List;
import java.util.Optional;

/**
 * Interface for resolving method calls to their implementations.
 */
public interface MethodResolver {
    
    /**
     * Resolves a method call to its implementation.
     * 
     * @param methodName The name of the called method
     * @param argumentTypes The types of the arguments
     * @return The resolved method, if found
     */
    Optional<JavaMethod> resolveMethod(String methodName, List<String> argumentTypes);
} 