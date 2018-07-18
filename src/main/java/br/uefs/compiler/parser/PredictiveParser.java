package br.uefs.compiler.parser;

import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.lexer.token.TokenClass;
import br.uefs.compiler.parser.semantic.Action;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticAnalyser;
import br.uefs.compiler.parser.semantic.VariableParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Parser responsible for deciding if source code is syntactically valid
 */
public class PredictiveParser {

    private ParsingTable table;
    private List<SyntacticError> errors;
    private Stack<Symbol> stack;
    private Stack<Symbol> aux;

    public PredictiveParser(ParsingTable table) {
        this.table = table;
        reset();
    }

    public void reset() {
        this.errors = new ArrayList<>();
        this.stack = new Stack<>();
        this.aux = new Stack<>();

        stack.push(Symbol.INPUT_END);
        stack.push(table.getStartSymbol());
    }

    private void handleTerminal(Token token) {
        stack.peek().addProperty("lex", token.getLexeme());
        aux.push(stack.peek());
        stack.pop();
    }

    private void handleTerminalMissError(Token token) {
        String foundStr = token.getLexeme().equals("$") ? "fim de arquivo" : "'" + token.getLexeme() + "'";
        String message = String.format("Erro sintático. Esperava %s mas encontrou %s.", stack.peek(), foundStr);
        errors.add(new SyntacticError(message, token.getLine()));
        aux.push(stack.peek());
        stack.pop();
    }


    private void handleSemanticAction(Token token) throws Exception {
        for (String step : SemanticAnalyser.parseSteps(stack.peek())) {
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
                            vp.setSymbol(stack.get(stack.size() - 1 + vp.getOffset()));
                        }
                    }
                }
                SemanticAnalyser.runAction(action, token);
            }
        }
        stack.pop();
    }

    private void handleTableMissError(Token token) {
        String foundStr = token.getLexeme().equals("$") ? "fim de arquivo" : "'" + token.getLexeme() + "'";
        String message = String.format("Erro sintático. Token inesperado: %s.", foundStr);
        errors.add(new SyntacticError(message, token.getLine()));
    }

    private void handleSync(Token token, Symbol cur) {
        String message = token.getLexeme().equals(Symbol.INPUT_END.getName()) ?
                "Erro sintático próximo ao fim do arquivo."
                : String.format("Erro sintático próximo ao token '%s'.", token.getLexeme());

        errors.add(new SyntacticError(message, token.getLine()));

        while (!stack.empty()
                && stack.peek().isNonTerminal()
                && !table.isMiss(stack.peek(), cur)
                && table.isSyncRule(stack.peek(), cur)) {
            aux.push(stack.peek());
            stack.pop();
        }
    }

    private void handleNonTerminal(Symbol cur) {
        Rule rule = table.getRule(stack.peek(), cur);

        aux.push(stack.peek());
        stack.pop();

        for (int i = rule.getSymbols().size() - 1; i >= 0; i--) {
            if (!rule.getSymbols().get(i).isEmptyString())
                stack.push(rule.getSymbols().get(i).cloneWithoutProperties());
        }
    }

    /**
     * Do parsing of token list based on the parsing table.
     * Errors are stored in the errors array.
     *
     * @param tokens List of tokens to be parsed
     */
    public void parse(List<Token> tokens) throws Exception {
        long lastLine = tokens.get(tokens.size() - 1).getLine();
        // Insert input end symbol
        tokens.add(new Token(TokenClass.END_CLASS, Symbol.INPUT_END.getName(), lastLine));

        reset();

        int ip = 0;
        while (!stack.peek().equals(Symbol.INPUT_END)) {
            Token token = tokens.get(ip);
            Symbol cur = token.toSymbol();

            if (stack.peek().isTerminal()) {
                if (cur.equals(stack.peek())) {
                    handleTerminal(token);
                    ip++;
                } else handleTerminalMissError(token);

            } else if (stack.peek().isAction()) {
                handleSemanticAction(token);
            } else if (stack.peek().isNonTerminal()) {
                if (table.isMiss(stack.peek(), cur)) {
                    handleTableMissError(token);
                    ip++;
                } else if (table.isSyncRule(stack.peek(), cur)) handleSync(token, cur);
                else handleNonTerminal(cur);
            } else {
                throw new Exception(String.format("Fatal Error. Unexpected peek value: %s", stack.peek()));
            }
        }
    }

    public List<SyntacticError> getErrors() {
        return errors;
    }

    public void printTable() {
        System.out.println(table);
    }
}
