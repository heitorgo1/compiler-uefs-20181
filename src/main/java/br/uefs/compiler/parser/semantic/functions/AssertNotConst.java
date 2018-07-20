package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.function.BiConsumer;

public class AssertNotConst implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 1;

        Parameter category = params.get(0);

        if (category.read().equals("const")) {
            c.addError(new SemanticError(String.format("Valor constante não pode ser alterado."), c.getCurrentToken().getLine()));
        }
    }
}
