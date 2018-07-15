package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.lexer.token.ReservedWords;
import br.uefs.compiler.lexer.token.Token;

import java.util.List;

public class SemanticFunctions {

    static int starts = 0;

    public static void assign(List<Parameter> params, Token token) {
        assert params.size() == 2;

        Parameter first = params.get(0);
        Parameter second = params.get(1);

        VariableParam receiver = VariableParam.class.cast(first);

        if (second instanceof VariableParam) {
            receiver.setAttribute(second.getAttribute());
        } else if (second instanceof ConstantParam) {
            receiver.setAttribute(second.getValue());
        }
    }

    public static void insertSymbolType(List<Parameter> params, Token token) {
        assert params.size() == 2;

        Parameter first = params.get(0);
        Parameter second = params.get(1);

        if (ReservedWords.isReserved(first.getAttribute())) {
            System.out.println(first.getAttribute() + " is a reserved word.");
        } else if (SemanticAnalyser.SYMBOL_TABLE.containsKey(first.getAttribute())) {
            System.out.println("Variable already declared: " + first.getAttribute());
        } else {
            SemanticAnalyser.SYMBOL_TABLE.put(first.getAttribute(), second.getAttribute());
        }
    }

    public static void incStart(List<Parameter> params, Token token) {
        SemanticAnalyser.START_COUNTER++;

        if (SemanticAnalyser.START_COUNTER == 1) {
            SemanticAnalyser.START_LINE = token.getLine();
        } else {
            SemanticError err = new SemanticError("start já foi definido na linha " + SemanticAnalyser.START_LINE, token.getLine());
            SemanticAnalyser.ERRORS.add(err);
        }
    }

    public static void hasOneStart (List<Parameter> params, Token token) {

        if (SemanticAnalyser.START_COUNTER == 0) {
            SemanticError err = new SemanticError("Função start não encontrada.", token.getLine());
            SemanticAnalyser.ERRORS.add(err);
        }
    }

}
