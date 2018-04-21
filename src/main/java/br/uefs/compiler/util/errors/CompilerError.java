package br.uefs.compiler.util.errors;

public interface CompilerError {

    long getLine();
    String getMessage();
}
