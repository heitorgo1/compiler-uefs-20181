package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;

import java.util.function.BiConsumer;

public class Or implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 3;

        Parameter receiver = params.get(0); // symbol
        Parameter arg1 = params.get(1); // const or symbol
        Parameter arg2 = params.get(2); // const or symbol

        String res = arg1.read().equals("true") || arg2.read().equals("true") ? "true" : "false";
        try {
            receiver.write(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
