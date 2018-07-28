package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class InitializeParams implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 1;

        Parameter id = params.get(0); // function identifier

        if (c.getSymbolTable(c.getScope() - 1).containsKey(id.read().toString())) {
            Map<String, Object> symbol = c.getSymbolTable(c.getScope() - 1).get(id.read().toString());
            Object category = symbol.get("cat");

            if (category != null && (category.toString().equals("func") || category.toString().equals("proc"))) {
                if (Boolean.class.cast(symbol.get("addparams"))) {
                    symbol.put("addparams", false);
                    if (!List.class.cast(symbol.get("paramtypes")).contains(new ArrayList<>())) {
                        List.class.cast(symbol.get("paramtypes")).add(new ArrayList<>());
                    }
                    else {
                        String type = category.toString().equals("proc") ? "" : symbol.get("type").toString();
                        c.addError(new SemanticError(String.format("Função/Procedimento com assinatura '%s %s(%s)' já existe.",
                                type,
                                id.read(),
                                new ArrayList<>().toString().replaceAll("^\\[|\\]$", "")),
                                c.getCurrentToken().getLine()));
                    }
                }
            }
            else {
                c.addError(new SemanticError(String.format("Variável '%s' não é uma função.", id.read()), c.getCurrentToken().getLine()));
            }
        }
        else {
            c.addError(new SemanticError(String.format("Função/Procedimento '%s' inexistente.", id.read()), c.getCurrentToken().getLine()));
        }
    }
}
