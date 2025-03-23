package com.equivalentmutant.model;

/**
 * Represents a location in source code.
 */
public class SourceLocation {
    private final String filePath;
    private final int beginLine;
    private final int beginColumn;
    private final int endLine;
    private final int endColumn;

    public SourceLocation(String filePath, int beginLine, int beginColumn, int endLine, int endColumn) {
        this.filePath = filePath;
        this.beginLine = beginLine;
        this.beginColumn = beginColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getBeginLine() {
        return beginLine;
    }

    public int getBeginColumn() {
        return beginColumn;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getEndColumn() {
        return endColumn;
    }

    @Override
    public String toString() {
        return filePath + ":" + beginLine + ":" + beginColumn + " to " + endLine + ":" + endColumn;
    }
} 