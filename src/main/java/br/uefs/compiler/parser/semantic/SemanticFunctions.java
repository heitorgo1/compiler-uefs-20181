package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.lexer.token.ReservedWords;
import br.uefs.compiler.lexer.token.Token;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class SemanticFunctions {

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
            SemanticAnalyser.SYMBOL_TABLE.putIfAbsent(first.getAttribute(), new Hashtable<>());
            SemanticAnalyser.SYMBOL_TABLE.get(first.getAttribute()).put("type", second.getAttribute());
            System.out.println("TYPE");
            System.out.println(SemanticAnalyser.SYMBOL_TABLE);
        }
    }

    public static void insertCategory(List<Parameter> params, Token token) {
        assert params.size() == 2;

        Parameter first = params.get(0);
        Parameter second = params.get(1);

        SemanticAnalyser.SYMBOL_TABLE.putIfAbsent(first.getAttribute(), new Hashtable<>());

        if (second instanceof VariableParam) {
            SemanticAnalyser.SYMBOL_TABLE.get(first.getAttribute()).put("category", second.getAttribute());
        } else if (second instanceof ConstantParam) {
            SemanticAnalyser.SYMBOL_TABLE.get(first.getAttribute()).put("category", second.getValue());
        }
        System.out.println("CATEGORY");
        System.out.println(SemanticAnalyser.SYMBOL_TABLE);
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

    public static void markAsArray(List<Parameter> parameters, Token token) {

        System.out.println("MARKING AS ARRAY");
        System.out.println(parameters);
    }

    public static void concat(List<Parameter> params, Token token) {
        assert params.size() == 2;

        Parameter first = params.get(0);
        Parameter second = params.get(1);

        VariableParam receiver = VariableParam.class.cast(first);

        if (receiver.getAttribute() == null) {
            receiver.setAttribute("");
        }

        String curVal = receiver.getAttribute();

        if (second instanceof VariableParam) {
            receiver.setAttribute(curVal + " "+second.getAttribute().trim());
        } else if (second instanceof ConstantParam) {
            receiver.setAttribute(curVal + " "+second.getValue().trim());
        }
    }

    public static void insertParams(List<Parameter> params, Token token) {
        assert params.size() == 2;

        Parameter first = params.get(0);
        Parameter second = params.get(1);

        SemanticAnalyser.SYMBOL_TABLE.putIfAbsent(first.getAttribute(), new Hashtable<>());

        if (second instanceof VariableParam) {
            SemanticAnalyser.SYMBOL_TABLE.get(first.getAttribute()).put("params", Arrays.asList(second.getAttribute().trim().split("\\s+")));
        } else if (second instanceof ConstantParam) {
            SemanticAnalyser.SYMBOL_TABLE.get(first.getAttribute()).put("params", Arrays.asList(second.getValue().trim().split("\\s+")));
        }
        System.out.println("PARAMS");
        System.out.println(SemanticAnalyser.SYMBOL_TABLE);
    }
}
