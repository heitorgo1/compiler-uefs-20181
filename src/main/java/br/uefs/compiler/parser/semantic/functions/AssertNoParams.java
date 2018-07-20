package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SemanticHelperFunctions;

import java.util.function.BiConsumer;

public class AssertNoParams implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 1;

        Parameter paramsList = params.get(0);

        if (paramsList.read() != null) {
            if (!paramsList.read().isEmpty()) {
                c.addError(new SemanticError(String.format("Função ou Procedimento que deveria ser chamada sem parâmetros"), c.getCurrentToken().getLine()));
            }
        }
    }
}
