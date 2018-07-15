package br.uefs.compiler.parser;

import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.lexer.token.TokenClass;
import br.uefs.compiler.parser.semantic.Action;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticAnalyser;
import br.uefs.compiler.parser.semantic.VariableParam;

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

    /**
     * Build Predictive Parsing Table based on a grammar
     */
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

    /**
     * Do parsing of token list based on the grammar.
     * Errors are stored in the errors array.
     */
    public void parse(List<Token> tokens) throws Exception {
        Stack<Symbol> s = new Stack<>();
        Stack<Symbol> aux = new Stack<>();
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
                s.peek().addProperty("lex", token.getLexeme());
                aux.push(s.peek());
                s.pop();
                ip++;
            } else if (s.peek().isTerminal()) {
                // pop stack peek
                String foundStr = token.getLexeme().equals("$") ? "fim de arquivo" : "'" + token.getLexeme() + "'";
                String message = String.format("Erro sintático. Esperava %s mas encontrou %s.", s.peek(), foundStr);
                errors.add(new SyntacticError(message, token.getLine()));
                s.pop();

            } else if (s.peek().isAction()) {

                for (String step : SemanticAnalyser.parseSteps(s.peek())) {
                    Action action = SemanticAnalyser.extractAction(step);

                    if (action.isNop()) continue;
                    else if (action.isPop()) {
                        int n = Integer.parseInt(action.getParams().get(0).getValue());
                        while (!aux.empty() && n-- > 0) aux.pop();
                    } else {
                        for (Parameter param : action.getParams()) {
                            if (param instanceof VariableParam) {
                                VariableParam vp = VariableParam.class.cast(param);
                                if (vp.isInAux()) {
                                    vp.setSymbol(aux.get(aux.size() - 1 + vp.getOffset()));
                                } else {
                                    vp.setSymbol(s.get(s.size() - 1 + vp.getOffset()));
                                }
                            }
                        }

                        SemanticAnalyser.runAction(action, token);
                    }
                }
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
                        && (s.peek().isNonTerminal() || s.peek().isAction())
                        && table.get(s.peek()).get(cur) != null
                        && table.get(s.peek()).get(cur).isSyncRule())
                    s.pop();
            } else if (s.peek().isNonTerminal()) {
                Rule rule = table.get(s.peek()).get(cur);

                // add pop action
                int counter = 0;
                for (Symbol sy : rule.getSymbols()) {
                    if (!sy.isEmptyString() && !sy.isAction()) counter++;
                }
                Symbol.Array arr = new Symbol.Array(rule.getSymbols());
                arr.add(new Symbol(String.format("{pop(%d)}", counter)));

                aux.push(s.peek());
                s.pop();

                for (int i = arr.size() - 1; i >= 0; i--) {
                    if (!arr.get(i).isEmptyString())
                        s.push(arr.get(i));
                }
            } else {
                throw new Exception(String.format("Fatal Error. Problem in grammar definition. Peek value: %s", s.peek()));
            }
        }
    }

    public List<SyntacticError> getErrors() {
        return errors;
    }

    public void clearErrors() {
        errors.clear();
    }

    public Map<Symbol, Map<Symbol, Rule>> getTable() {
        return table;
    }

    public void printTable() {
        System.out.println(table);
    }
}
