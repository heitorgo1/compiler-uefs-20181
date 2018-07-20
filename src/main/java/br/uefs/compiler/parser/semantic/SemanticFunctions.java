package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.lexer.token.ReservedWords;
import br.uefs.compiler.lexer.token.Token;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class SemanticFunctions {
/*

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
        } else if (SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE).containsKey(first.getAttribute())) {
            System.out.println(token.getLexeme()+" "+token.getLine());
            System.out.println("Variable already declared in scope " + SemanticAnalyser.CUR_SCOPE + ": " + first.getAttribute());
        } else {
            SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE).putIfAbsent(first.getAttribute(), new Hashtable<>());
            if (second instanceof ConstantParam) {
                SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE).get(first.getAttribute()).put("type", second.getValue());
            }
            else {
                SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE).get(first.getAttribute()).put("type", second.getAttribute());
            }
            System.out.println(SemanticAnalyser.SYMBOL_TABLE);
        }
    }

    public static void insertCategory(List<Parameter> params, Token token) {
        assert params.size() == 2;

        Parameter first = params.get(0);
        Parameter second = params.get(1);

        SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE).putIfAbsent(first.getAttribute(), new Hashtable<>());

        if (second instanceof VariableParam) {
            SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE).get(first.getAttribute()).put("category", second.getAttribute());
        } else if (second instanceof ConstantParam) {
            SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE).get(first.getAttribute()).put("category", second.getValue());
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

    public static void hasOneStart(List<Parameter> params, Token token) {

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

        String curVal = receiver.getAttribute().trim();

        if (second instanceof VariableParam) {
            if (second.getAttribute().trim().startsWith("[") && second.getAttribute().trim().endsWith("]")) {
                String data = second.getAttribute().trim().substring(1, second.getAttribute().trim().length()-1);
                System.out.println("CONCAT ARRAY VARI DATA");
                System.out.println(data);
                for (String str : data.split(",")) {
                    curVal += " " + str.trim();
                }
            } else
                curVal += " " + second.getAttribute().trim();

            receiver.setAttribute(curVal.trim());
        } else if (second instanceof ConstantParam) {
            if (second.getValue().trim().startsWith("[") && second.getValue().trim().endsWith("]")) {
                String data = second.getValue().trim().substring(1, second.getValue().trim().length()-1);
                System.out.println("CONCAT ARRAY CONST DATA");
                System.out.println(data);
                for (String str : data.split(",")) {
                    curVal += " " + str.trim();
                }
            } else
                curVal += " " + second.getValue().trim();
            receiver.setAttribute(curVal.trim());
        }
    }

    public static void getIdAndInsertParams(List<Parameter> params, Token token) {
        assert params.size() == 2;

        Parameter first = params.get(0);
        Parameter second = params.get(1);

        SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE-1).putIfAbsent(first.getAttribute(), new Hashtable<>());

        if (second instanceof VariableParam) {
            SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE-1).get(first.getAttribute()).put("params", Arrays.asList(second.getAttribute().trim().split("\\s+")));
        } else if (second instanceof ConstantParam) {
            SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE-1).get(first.getAttribute()).put("params", Arrays.asList(second.getValue().trim().split("\\s+")));
        }

    }

    public static void setScope(List<Parameter> params, Token token) {
        assert params.size() == 1;

        Parameter param = params.get(0);

        SemanticAnalyser.CUR_SCOPE = Integer.parseInt(param.getValue());

        while (SemanticAnalyser.SYMBOL_TABLE.size() < SemanticAnalyser.CUR_SCOPE + 1) {
            SemanticAnalyser.SYMBOL_TABLE.add(new Hashtable<>());
        }
    }

    public static void incScope(List<Parameter> params, Token token) {
        assert params.size() == 0;

        SemanticAnalyser.CUR_SCOPE++;

        if (SemanticAnalyser.SYMBOL_TABLE.size() < SemanticAnalyser.CUR_SCOPE + 1) {
            SemanticAnalyser.SYMBOL_TABLE.add(new Hashtable<>());
        }
    }

    public static void decScope(List<Parameter> params, Token token) {
        assert params.size() == 0;

        SemanticAnalyser.SYMBOL_TABLE.remove(SemanticAnalyser.CUR_SCOPE);

        SemanticAnalyser.CUR_SCOPE--;
    }

    public static void getNumType(List<Parameter> params, Token token) {
        assert params.size() == 2;

        params.get(0).setAttribute("NUM");
    }

    public static void getIdType(List<Parameter> params, Token token) {
        assert params.size() == 2;

        Parameter first = params.get(0);
        Parameter second = params.get(1);

        if (SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE).containsKey(second.getAttribute())) {
            first.setAttribute(SemanticAnalyser.SYMBOL_TABLE.get(SemanticAnalyser.CUR_SCOPE).get(second.getAttribute()).get("type").toString());
        }
        else
            first.setAttribute("GID");
    }

    public static void typeMatch(List<Parameter> params, Token token) {
        assert params.size() == 2;

        System.out.println("TYPE MATCH: ");
        System.out.println(params);

    }
*/
}
