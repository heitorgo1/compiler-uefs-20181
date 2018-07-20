package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.function.BiConsumer;

public class InvalidConst implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 0;

        c.addError(new SemanticError(String.format("Constantes não podem ser declaradas neste escopo."), c.getCurrentToken().getLine()));
    }
}
