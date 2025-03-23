package com.equivalentmutant.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Java method with its source code and location information.
 */
public class JavaMethod {
    private final String packageName;
    private final String className;
    private final String methodName;
    private final String signature;
    private final String sourceCode;
    private final SourceLocation location;
    private final boolean isStatic;
    private final List<String> parameterTypes;
    
    public JavaMethod(String packageName, String className, String methodName, 
                     String signature, String sourceCode, SourceLocation location, 
                     boolean isStatic, List<String> parameterTypes) {
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.signature = signature;
        this.sourceCode = sourceCode;
        this.location = location;
        this.isStatic = isStatic;
        this.parameterTypes = new ArrayList<>(parameterTypes);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getSignature() {
        return signature;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public SourceLocation getLocation() {
        return location;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public List<String> getParameterTypes() {
        return new ArrayList<>(parameterTypes);
    }

    public String getFullyQualifiedName() {
        return packageName + "." + className + "." + methodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaMethod that = (JavaMethod) o;
        return Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signature);
    }

    @Override
    public String toString() {
        return signature;
    }
} 