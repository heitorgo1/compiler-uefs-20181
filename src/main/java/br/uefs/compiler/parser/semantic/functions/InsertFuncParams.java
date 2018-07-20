package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.lexer.token.ReservedWords;
import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.function.BiConsumer;

public class InsertFuncParams implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 3;

        Parameter id = params.get(0); // function identifier
        Parameter funcParamsTypes = params.get(1); // params types
        Parameter funcParamsDims = params.get(2); // params dimentions


        if (c.getSymbolTable(c.getScope() - 1).containsKey(id.read())) {
            Object category = c.getSymbolTable(c.getScope() - 1).get(id.read()).get("cat");

            if (category != null && category.toString().equals("func") || category.toString().equals("proc")) {
                c.getSymbolTable(c.getScope() - 1).get(id.read()).put("params", funcParamsTypes.read());
                c.getSymbolTable(c.getScope() - 1).get(id.read()).put("paramsdims", funcParamsDims.read());
                System.out.println(c.getSymbolTableList());
            }
            else {
                c.addError(new SemanticError(String.format("Variável '%s' não é uma função.", id.read()), c.getCurrentToken().getLine()));
            }
        }
        else {
            c.addError(new SemanticError(String.format("Função '%s' inexistente.", id.read()), c.getCurrentToken().getLine()));
        }
    }
}
