package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;

import java.util.function.BiConsumer;

public class GetNumType implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter first = params.get(0); // symbol
        Parameter second = params.get(1); // symbol

        String number = second.read().toString().replaceAll("\\s+", "");
        String type = number.contains(".") ? "float": "int";

        try {
            first.write(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
