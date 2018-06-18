package br.uefs.compiler.parser;

import java.util.ArrayList;

public class Rule {

    static class Array extends ArrayList<Rule> {

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Rule r : this) {
                sb.append(r+"\n");
            }
            return sb.toString();
        }
    }

    private Symbol nonTerminal;
    private Symbol.Array symbols;

    public Rule(String nonTerminal, Symbol.Array symbols) {
        this.nonTerminal = new Symbol(nonTerminal);
        this.symbols = symbols;
    }

    public Symbol getNonTerminal() {
        return nonTerminal;
    }

    public Symbol.Array getSymbols() {
        return symbols;
    }

    @Override
    public String toString() {
        return String.format("%s ::= %s", nonTerminal, symbols);
    }
}
