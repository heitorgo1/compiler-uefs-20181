package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.function.BiConsumer;

public class AssertInitConst implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter init = params.get(0);
        Parameter category = params.get(1);

        if (init.read().equals("false") && category.read().equals("const")) {
            c.addError(new SemanticError(String.format("Constante não inicializada."), c.getCurrentToken().getLine()));
        }
    }
}
