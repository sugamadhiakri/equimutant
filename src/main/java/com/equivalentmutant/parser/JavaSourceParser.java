package com.equivalentmutant.parser;

import com.equivalentmutant.model.JavaMethod;
import com.equivalentmutant.model.SourceLocation;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Parser for Java source files.
 */
public class JavaSourceParser {
    
    private final JavaParser parser;
    
    public JavaSourceParser() {
        this.parser = new JavaParser();
    }
    
    /**
     * Parse a Java source file and extract all methods.
     * 
     * @param sourceFile The Java source file to parse
     * @return A list of JavaMethod objects
     * @throws FileNotFoundException If the source file cannot be found
     */
    public List<JavaMethod> parseFile(File sourceFile) throws FileNotFoundException {
        ParseResult<CompilationUnit> parseResult = parser.parse(sourceFile);
        
        if (!parseResult.isSuccessful() || !parseResult.getResult().isPresent()) {
            throw new RuntimeException("Failed to parse file: " + sourceFile.getPath());
        }
        
        CompilationUnit cu = parseResult.getResult().get();
        List<JavaMethod> methods = new ArrayList<>();
        
        // Extract the package name
        String packageName = cu.getPackageDeclaration()
                              .map(pd -> pd.getName().asString())
                              .orElse("");
        
        // Visit all method declarations
        cu.accept(new MethodVisitor(sourceFile.getPath(), packageName, methods), null);
        
        return methods;
    }
    
    /**
     * Visitor to extract method information from a compilation unit.
     */
    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        private final String filePath;
        private final String packageName;
        private final List<JavaMethod> methods;
        private String currentClassName = "";
        
        public MethodVisitor(String filePath, String packageName, List<JavaMethod> methods) {
            this.filePath = filePath;
            this.packageName = packageName;
            this.methods = methods;
        }
        
        @Override
        public void visit(ClassOrInterfaceDeclaration classDecl, Void arg) {
            String previousClassName = currentClassName;
            currentClassName = classDecl.getNameAsString();
            
            super.visit(classDecl, arg);
            
            // Restore the previous class name for nested classes
            currentClassName = previousClassName;
        }
        
        @Override
        public void visit(MethodDeclaration methodDecl, Void arg) {
            String methodName = methodDecl.getNameAsString();
            boolean isStatic = methodDecl.isStatic();
            
            // Get parameter types
            List<String> parameterTypes = new ArrayList<>();
            for (Parameter param : methodDecl.getParameters()) {
                parameterTypes.add(param.getType().asString());
            }
            
            // Create method signature
            String returnType = methodDecl.getType().asString();
            StringBuilder signatureBuilder = new StringBuilder();
            signatureBuilder.append(returnType).append(" ").append(methodName).append("(");
            
            for (int i = 0; i < methodDecl.getParameters().size(); i++) {
                Parameter param = methodDecl.getParameters().get(i);
                signatureBuilder.append(param.getType().asString()).append(" ").append(param.getNameAsString());
                if (i < methodDecl.getParameters().size() - 1) {
                    signatureBuilder.append(", ");
                }
            }
            signatureBuilder.append(")");
            String signature = signatureBuilder.toString();
            
            // Get source code (preserving original formatting and comments)
            String sourceCode = methodDecl.toString();
            
            // Create source location
            SourceLocation location = new SourceLocation(
                filePath,
                methodDecl.getBegin().map(pos -> pos.line).orElse(0),
                methodDecl.getBegin().map(pos -> pos.column).orElse(0),
                methodDecl.getEnd().map(pos -> pos.line).orElse(0),
                methodDecl.getEnd().map(pos -> pos.column).orElse(0)
            );
            
            // Create and add the JavaMethod object
            JavaMethod method = new JavaMethod(
                packageName,
                currentClassName,
                methodName,
                signature,
                sourceCode,
                location,
                isStatic,
                parameterTypes
            );
            
            methods.add(method);
            
            super.visit(methodDecl, arg);
        }
    }
} 