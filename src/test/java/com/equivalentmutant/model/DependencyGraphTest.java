package com.equivalentmutant.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Set;

public class DependencyGraphTest {

    @Test
    public void testAddDependency() {
        // Create a root method
        JavaMethod rootMethod = new JavaMethod(
            "com.example", "TestClass", "testMethod",
            "void testMethod()", "void testMethod() {}", 
            new SourceLocation("TestClass.java", 1, 1, 3, 3),
            false, Collections.emptyList()
        );
        
        // Create a dependency graph
        DependencyGraph graph = new DependencyGraph(rootMethod);
        
        // Create a callee method
        JavaMethod calleeMethod = new JavaMethod(
            "com.example", "TestClass", "calleeMethod",
            "void calleeMethod()", "void calleeMethod() {}", 
            new SourceLocation("TestClass.java", 5, 1, 7, 3),
            false, Collections.emptyList()
        );
        
        // Create a dependency
        MethodDependency dependency = new MethodDependency(
            rootMethod, calleeMethod,
            new SourceLocation("TestClass.java", 2, 5, 2, 20)
        );
        
        // Add the dependency to the graph
        graph.addDependency(dependency);
        
        // Test that the dependency is correctly added
        Set<MethodDependency> dependencies = graph.getDependenciesForMethod(rootMethod);
        assertEquals(1, dependencies.size());
        assertTrue(dependencies.contains(dependency));
        
        // Test getAllMethods
        Set<JavaMethod> allMethods = graph.getAllMethods();
        assertEquals(2, allMethods.size());
        assertTrue(allMethods.contains(rootMethod));
        assertTrue(allMethods.contains(calleeMethod));
    }
} 