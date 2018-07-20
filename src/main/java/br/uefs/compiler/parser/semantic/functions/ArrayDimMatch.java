package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.function.BiConsumer;

public class ArrayDimMatch implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter first = params.get(0);
        Parameter second = params.get(1);

        int v1 = Integer.parseInt(first.read());
        int v2 = Integer.parseInt(second.read());

        if (v1 != v2) {
            c.addError(new SemanticError(String.format("Dimensões não batem %s %s", first.read(), second.read()), c.getCurrentToken().getLine()));
        }

    }
}
