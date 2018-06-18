package br.uefs.compiler.parser;

import br.uefs.compiler.lexer.token.Token;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class PredictiveParser {

    private Map<Symbol, Map<Symbol, Rule>> table;
    private Grammar grammar;

    public PredictiveParser(Grammar grammar) {
        this.grammar = grammar;
        buildTable(grammar);
    }

    public void buildTable(Grammar grammar) {
        table = new Hashtable<>();

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
        }
    }

    private Symbol tokenToSymbol(Token token) {
        if (token.getTokenClass().getName().equals("PALAVRARESERVADA")
                || token.getTokenClass().getName().equals("DELIMITADOR")
                || token.getTokenClass().getName().equals("OPERADORLOGICO")
                || token.getTokenClass().getName().equals("OPERADORARITMETICO")
                || token.getTokenClass().getName().equals("OPERADORRELACIONAL")) {
            return new Symbol("'" + token.getLexeme() + "'");
        } else if (token.getTokenClass().getName().equals("END"))
            return new Symbol(token.getLexeme());
        else return new Symbol(token.getTokenClass().getName());
    }

    public void parse(List<Token> tokens) throws Exception {
        Stack<Symbol> s = new Stack<>();
        s.push(Symbol.INPUT_END);
        s.push(grammar.getStartSymbol());

        int ip = 0;
        while (!s.peek().equals(Symbol.INPUT_END)) {
            Symbol cur = tokenToSymbol(tokens.get(ip));
//            System.out.println(cur +": " +s);
            if (cur.equals(s.peek())) {
                s.pop();
                ip++;
            } else if (s.peek().isTerminal()) {
                throw new Exception(String.format("%s unrecognized terminal", tokens.get(ip)));
            } else if (table.get(s.peek()).get(cur) == null) {
                throw new Exception("Invalid transition on " + tokens.get(ip));
            } else {
                Rule rule = table.get(s.peek()).get(cur);
                System.out.println(s.peek() + " -> " + rule);

                s.pop();

                for (int i = rule.getSymbols().size() - 1; i >= 0; i--) {
                    if (!rule.getSymbols().get(i).isEmptyString())
                        s.push(rule.getSymbols().get(i));
                }
            }
        }
    }

    public Map<Symbol, Map<Symbol, Rule>> getTable() {
        return table;
    }

    public void printTable() {
        System.out.println(table);
    }
}
