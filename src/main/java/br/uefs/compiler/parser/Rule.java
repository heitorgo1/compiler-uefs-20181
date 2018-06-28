package br.uefs.compiler.parser;

import java.util.ArrayList;

/**
 * Rule applied to a non terminal symbol of a grammar.
 * May be seen as a production.
 * */
public class Rule {

    static class Array extends ArrayList<Rule> {

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Rule r : this) {
                sb.append(r + "\n");
            }
            return sb.toString();
        }
    }

    private Symbol nonTerminal;
    private Symbol.Array symbols;
    private boolean syncRule;

    public Rule(boolean syncRule) {
        this.syncRule = syncRule;
    }

    public Rule(String nonTerminal, Symbol.Array symbols) {
        this.nonTerminal = new Symbol(nonTerminal);
        this.symbols = symbols;
        syncRule = false;
    }

    public Symbol getNonTerminal() {
        return nonTerminal;
    }

    public Symbol.Array getSymbols() {
        return symbols;
    }

    public boolean isSyncRule() {
        return syncRule;
    }

    @Override
    public String toString() {
        if (isSyncRule()) return "Sync";
        return String.format("%s ::= %s", nonTerminal, symbols);
    }
}
