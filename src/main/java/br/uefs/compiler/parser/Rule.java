package br.uefs.compiler.parser;

public class Rule {

    public String nonTerminal;
    public SymbolArray symbols;

    public Rule(String nonTerminal, SymbolArray symbols) {
        this.nonTerminal = nonTerminal;
        this.symbols = symbols;
    }

    public String getNonTerminal() {
        return nonTerminal;
    }

    public SymbolArray getSymbols() {
        return symbols;
    }
}
