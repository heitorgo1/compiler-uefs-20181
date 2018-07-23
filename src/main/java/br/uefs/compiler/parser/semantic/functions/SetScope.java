package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;

import java.util.function.BiConsumer;

public class SetScope implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 1;

        Parameter first = params.get(0); // Int

        c.setCurrentScope(Integer.parseInt(first.read().toString()));
    }
}
