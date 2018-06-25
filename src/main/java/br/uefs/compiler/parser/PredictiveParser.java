package br.uefs.compiler.parser;

import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.lexer.token.TokenClass;

import java.util.*;

/**
 * Parser responsible for deciding if source code is syntactically valid
 */
public class PredictiveParser {

    private Map<Symbol, Map<Symbol, Rule>> table;
    private Grammar grammar;
    private List<SyntacticError> errors;

    public PredictiveParser(Grammar grammar) {
        this.grammar = grammar;
        this.errors = new ArrayList<>();
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


            for (Symbol terminal : grammar.getSynchronizingSet(X)) {
                table.get(X).putIfAbsent(terminal, new Rule(true));
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

        long lastLine = tokens.get(tokens.size() - 1).getLine();
        // Insert input end symbol
        tokens.add(new Token(new TokenClass(-1, "", "END"), Symbol.INPUT_END.getName(), lastLine));

        int ip = 0;
        while (!s.peek().equals(Symbol.INPUT_END)) {
            Token token = tokens.get(ip);
            Symbol cur = tokenToSymbol(token);

            if (cur.equals(s.peek())) {
                s.pop();
                ip++;
            } else if (s.peek().isTerminal()) {
                // pop stack peek
                String foundStr = token.getLexeme().equals("$") ? "fim de arquivo" : "'" + token.getLexeme() + "'";
                String message = String.format("Erro sintático. Esperava %s mas encontrou %s.", s.peek(), foundStr);
                errors.add(new SyntacticError(message, token.getLine()));
                s.pop();
            } else if (table.get(s.peek()).get(cur) == null) {
                // if table miss, read next token
                String foundStr = token.getLexeme().equals("$") ? "fim de arquivo" : "'" + token.getLexeme() + "'";
                String message = String.format("Erro sintático. Token inesperado: %s.", foundStr);
                errors.add(new SyntacticError(message, token.getLine()));
                ip++;
            } else if (table.get(s.peek()).get(cur).isSyncRule()) {
                // if table sync hit, pop non terminal
                String message;
                if (token.getLexeme().equals("$")) {
                    message = "Erro sintático próximo ao fim do arquivo.";
                } else {
                    message = String.format("Erro sintático próximo ao token '%s'.", token.getLexeme());
                }
                errors.add(new SyntacticError(message, token.getLine()));
                while (!s.empty()
                        && s.peek().isNonTerminal()
                        && table.get(s.peek()).get(cur) != null
                        && table.get(s.peek()).get(cur).isSyncRule())
                    s.pop();
            } else {
                Rule rule = table.get(s.peek()).get(cur);

                s.pop();

                for (int i = rule.getSymbols().size() - 1; i >= 0; i--) {
                    if (!rule.getSymbols().get(i).isEmptyString())
                        s.push(rule.getSymbols().get(i));
                }
            }
        }
    }

    public List<SyntacticError> getErrors() {
        return errors;
    }

    public Map<Symbol, Map<Symbol, Rule>> getTable() {
        return table;
    }

    public void printTable() {
        System.out.println(table);
    }
}
