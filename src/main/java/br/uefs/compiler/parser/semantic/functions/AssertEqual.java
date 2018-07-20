package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.function.BiConsumer;

public class AssertEqual implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter first = params.get(0);
        Parameter second = params.get(1);

        if (!first.read().equals(second.read())) {
            c.addError(new SemanticError(String.format("%s não é igual a %s.", first.read(), second.read()), c.getCurrentToken().getLine()));
        }
    }
}
