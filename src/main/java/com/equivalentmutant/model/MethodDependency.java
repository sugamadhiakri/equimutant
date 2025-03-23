package com.equivalentmutant.model;

import java.util.Objects;

/**
 * Represents a dependency between a caller method and a callee method.
 */
public class MethodDependency {
    private final JavaMethod caller;
    private final JavaMethod callee;
    private final SourceLocation callSite;

    public MethodDependency(JavaMethod caller, JavaMethod callee, SourceLocation callSite) {
        this.caller = caller;
        this.callee = callee;
        this.callSite = callSite;
    }

    public JavaMethod getCaller() {
        return caller;
    }

    public JavaMethod getCallee() {
        return callee;
    }

    public SourceLocation getCallSite() {
        return callSite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodDependency that = (MethodDependency) o;
        return Objects.equals(caller, that.caller) &&
               Objects.equals(callee, that.callee) &&
               Objects.equals(callSite, that.callSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caller, callee, callSite);
    }

    @Override
    public String toString() {
        return caller.getMethodName() + " calls " + callee.getMethodName() + " at " + callSite;
    }
} 