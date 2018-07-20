package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;

import java.util.function.BiConsumer;

public class Assign implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter receiver = params.get(0); // symbol
        Parameter data = params.get(1); // const or symbol

        try {
            receiver.assign(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
