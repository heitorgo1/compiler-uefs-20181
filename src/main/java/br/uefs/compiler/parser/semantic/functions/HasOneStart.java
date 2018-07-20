package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.function.BiConsumer;

public class HasOneStart implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 0;

        if (c.getStartCounter() == 0) {
            c.addError(new SemanticError("Função start não encontrada", c.getCurrentToken().getLine()));
        }
    }
}
