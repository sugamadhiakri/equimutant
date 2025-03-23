package com.equivalentmutant.model;

import java.util.*;

/**
 * Represents a graph of method dependencies.
 */
public class DependencyGraph {
    private final Map<JavaMethod, Set<MethodDependency>> dependencies = new HashMap<>();
    private final JavaMethod rootMethod;

    public DependencyGraph(JavaMethod rootMethod) {
        this.rootMethod = rootMethod;
    }

    public void addDependency(MethodDependency dependency) {
        JavaMethod caller = dependency.getCaller();
        dependencies.computeIfAbsent(caller, k -> new HashSet<>()).add(dependency);
    }

    public Set<MethodDependency> getDependenciesForMethod(JavaMethod method) {
        return dependencies.getOrDefault(method, Collections.emptySet());
    }

    public Set<JavaMethod> getAllMethods() {
        Set<JavaMethod> allMethods = new HashSet<>(dependencies.keySet());
        for (Set<MethodDependency> deps : dependencies.values()) {
            for (MethodDependency dep : deps) {
                allMethods.add(dep.getCallee());
            }
        }
        return allMethods;
    }

    public JavaMethod getRootMethod() {
        return rootMethod;
    }

    public Map<JavaMethod, Set<MethodDependency>> getAllDependencies() {
        return new HashMap<>(dependencies);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Dependency Graph for ").append(rootMethod.getSignature()).append(":\n");
        
        for (Map.Entry<JavaMethod, Set<MethodDependency>> entry : dependencies.entrySet()) {
            builder.append("  ").append(entry.getKey().getSignature()).append(" calls:\n");
            for (MethodDependency dependency : entry.getValue()) {
                builder.append("    ").append(dependency.getCallee().getSignature())
                       .append(" at ").append(dependency.getCallSite()).append("\n");
            }
        }
        
        return builder.toString();
    }
} 