package br.uefs.compiler.parser;

import java.util.Hashtable;
import java.util.Map;

public class ParsingTable extends Hashtable<Symbol, Map<Symbol, Rule>> {

    private Grammar grammar;

    public ParsingTable(Grammar grammar) {
        this.grammar = grammar;
    }

    public Symbol getStartSymbol() {
        return grammar.getStartSymbol();
    }

    public Rule getRule(Symbol nonTerminal, Symbol input) {
        assert !isMiss(nonTerminal, input) && !isSyncRule(nonTerminal, input);

        return this.get(nonTerminal).get(input);
    }

    public boolean isMiss(Symbol nonTerminal, Symbol input) {
        return !this.get(nonTerminal).containsKey(input);
    }

    public boolean isSyncRule(Symbol nonTerminal, Symbol input) {
        assert !isMiss(nonTerminal, input);

        return this.get(nonTerminal).get(input).isSyncRule();
    }

    /**
     * Build Predictive Parsing Table based on a grammar
     *
     * @param grammar The grammar of the table
     * @return Parsing Table
     */
    public static ParsingTable build(Grammar grammar) {

        ParsingTable table = new ParsingTable(grammar);

        for (Map.Entry<Symbol, Rule.Array> entry : grammar.entrySet()) {
            Symbol X = entry.getKey();
            Rule.Array rules = entry.getValue();

            table.put(X, new Hashtable<>());

            for (Rule rule : rules) {
                for (Symbol terminal : grammar.first(rule.getSymbols())) {
                    if (terminal.isEmptyString()) continue;

                    table.get(X).putIfAbsent(terminal, rule);
                }

                if (grammar.first(rule.getSymbols()).containsEmptyString()) {
                    for (Symbol terminal : grammar.follow(X)) {
                        table.get(X).putIfAbsent(terminal, rule);
                    }
                }
            }

            for (Symbol terminal : grammar.getSynchronizingSet(X)) {
                table.get(X).putIfAbsent(terminal, new Rule(true));
            }
        }

        return table;
    }

}
