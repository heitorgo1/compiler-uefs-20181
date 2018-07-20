package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticHelperFunctions;

import java.util.function.BiConsumer;

public class Concat implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {

        if (params.size() == 2 && !params.get(1).isWritable()) {
            Parameter second = params.get(1);
            Parameter first = params.get(0);

            try {
                first.concat(second.read());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
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
}
