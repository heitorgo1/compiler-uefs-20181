package br.uefs.compiler.util.parser;

import br.uefs.compiler.lexer.token.Token;

import java.util.*;

public class PredictiveParser {

    private Map<String, Map<String, List<List<String>>>> table;

    public PredictiveParser() {

    }

    public void buildTable(Map<String, Set<String>> first, Map<String, Set<String>> follow) {
        table = new Hashtable<>();
        for (Production p : Grammar.PRODUCTIONS) {
            String X = p.getName();
            table.put(X, new Hashtable<>());
            for (List<String> symbols : p.getProductions()) {
                for (String terminal : Grammar.first(symbols, first)) {
                    if (terminal.isEmpty()) continue;

                    table.get(X).putIfAbsent(terminal, new ArrayList<>());

                    table.get(X).get(terminal).add(symbols);
                }

                if (Grammar.first(symbols, first).contains("")) {
                    for (String terminal : follow.get(X)) {
                        table.get(X).putIfAbsent(terminal, new ArrayList<>());

                        table.get(X).get(terminal).add(symbols);
                    }
                }
            }
        }
    }

    private String getTokenName(Token token) {
        if (token.getTokenClass().getName().equals("PALAVRARESERVADA")
                || token.getTokenClass().getName().equals("DELIMITADOR")
                || token.getTokenClass().getName().equals("OPERADORLOGICO")
                || token.getTokenClass().getName().equals("OPERADORARITMETICO")
                || token.getTokenClass().getName().equals("OPERADORRELACIONAL")) {
            return "'"+token.getLexeme()+"'";
        }
        else if (token.getTokenClass().getName().equals("END"))
            return token.getLexeme();
        else return token.getTokenClass().getName();
    }


    public void parse(List<Token> tokens) throws Exception {
        Stack<String> s = new Stack<>();
        s.push("$");
        s.push(Grammar.START_SYMBOL.getName());

        int ip = 0;
        while (s.peek() != "$") {
            String cur = getTokenName(tokens.get(ip));
//            System.out.println(cur +": " +s);
            if (cur.equals(s.peek())) {
                s.pop();
                ip++;
            }
            else if (Grammar.isTerminal(s.peek())) {
                throw new Exception(String.format("%s unrecognized terminal", tokens.get(ip)));
            }
            else if (table.get(s.peek()).get(cur) == null) {
                throw new Exception("Invalid transition on "+tokens.get(ip));
            }
            else {
                List<String> symbols = table.get(s.peek()).get(cur).get(0);
                System.out.println(s.peek() + " -> "+symbols);

                s.pop();

                for (int i = symbols.size()-1; i >= 0; i--) {
                    if (!symbols.get(i).isEmpty())
                        s.push(symbols.get(i));
                }
            }
        }
    }

    public Map<String, Map<String, List<List<String>>>> getTable() {
        return table;
    }
}
