package br.uefs.compiler.parser.semantic;

public class Context {

    private static int SCOPE = 0;
    private static int START_COUNTER = 0;
    private static long START_LINE = 0;
    private SymbolTable symbolTable;

    public Context(SymbolTable table) {
        this.symbolTable = table;
    }
}
