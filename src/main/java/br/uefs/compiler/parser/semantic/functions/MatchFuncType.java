package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SemanticHelperFunctions;

import java.util.function.BiConsumer;

public class MatchFuncType implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter expected = params.get(0); // symbol
        Parameter result = params.get(1); // symbol

        if (expected.read() == null) {
            return;
        }

        if (expected.read().equals("null")) {
            String message = String.format("Start ou Procedimento não deve ter retorno.");
            c.addError(new SemanticError(message, c.getCurrentToken().getLine()));
            return;
        }

        if (!expected.read().equals(result.read())) {
            String message = String.format("Função esperava retorno do tipo '%s' e encontrou '%s'.",expected.read(), result.read());
            c.addError(new SemanticError(message, c.getCurrentToken().getLine()));
        }
    }
}
