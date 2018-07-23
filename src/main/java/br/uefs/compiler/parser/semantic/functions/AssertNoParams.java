package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SemanticHelperFunctions;

import java.util.List;
import java.util.function.BiConsumer;

public class AssertNoParams implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 1;

        List<List<Object>> paramsList = List.class.cast(params.get(0).readOnlyAttribute());

        for (List<Object> list : paramsList) {
            if (list.isEmpty()) {
                return;
            }
        }
        c.addError(new SemanticError(String.format("Função/Procedimento deveria ser chamada sem parâmetros."), c.getCurrentToken().getLine()));
    }
}
