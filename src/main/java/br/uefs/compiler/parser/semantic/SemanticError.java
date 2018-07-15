package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.util.errors.CompilerError;

public class SemanticError implements CompilerError {

    private long line;
    private String message;

    public SemanticError(String message, long line) {
        this.message = message;
        this.line = line;
    }
    @Override
    public long getLine() {
        return line;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
