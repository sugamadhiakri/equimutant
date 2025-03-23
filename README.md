# Equivalent Mutant Analyzer Tool

A Java-based tool for analyzing method dependencies to identify equivalent mutants in mutation testing.

## Introduction

The Equivalent Mutant Analyzer Tool parses Java source code, identifies method dependencies, and builds a comprehensive dependency graph. This tool serves as a foundation for analyzing mutation equivalence by capturing the complete context needed for further analysis.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Building the Project

To build the project, run the following command:

```bash
mvn clean package
```

### Running the Tool

To run the tool, use the following command:

```bash
java -jar target/equivalent-mutant-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar -s <source_path> -c <class_name> -m <method_name>
```

#### Command-line Options

- `-s, --source <PATH>`: Path to the source directory or file (required)
- `-c, --class <CLASS>`: Fully qualified name of the class (required)
- `-m, --method <METHOD>`: Name of the method to analyze (required)
- `-d, --depth <DEPTH>`: Maximum recursion depth (-1 for unlimited)
- `-h, --help`: Print help information

### Example

```bash
java -jar target/equivalent-mutant-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar -s src/main/java -c com.example.MyClass -m calculateTotal
```
