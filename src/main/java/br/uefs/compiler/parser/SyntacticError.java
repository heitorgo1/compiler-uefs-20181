package br.uefs.compiler.parser;

import br.uefs.compiler.util.errors.CompilerError;

public class SyntacticError implements CompilerError {

    private long line;
    private String message;

    public SyntacticError(String message, long line) {
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
