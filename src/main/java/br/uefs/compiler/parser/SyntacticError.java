package br.uefs.compiler.parser;

import br.uefs.compiler.util.errors.CompilerError;

public class SyntacticError implements CompilerError {
    @Override
    public long getLine() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
