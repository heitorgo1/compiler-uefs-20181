package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;

import java.util.function.BiConsumer;

public class Concat implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() > 1;

        for (int i = params.size() - 1; i > 0; i--) {
            Parameter second = params.get(i);
            Parameter first = params.get(i - 1);

            try {
                first.concat(second);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
