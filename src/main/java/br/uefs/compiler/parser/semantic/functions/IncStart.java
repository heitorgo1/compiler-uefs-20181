package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.function.BiConsumer;

public class IncStart implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 0;

        c.incrementStartCounter();
        if (c.getStartCounter() == 1) {
            c.setStartLine(c.getCurrentToken().getLine());
        } else {
            String message = String.format("start já foi declarado na linha %d", c.getStartLine());
            c.addError(new SemanticError(message, c.getCurrentToken().getLine()));
        }
    }
}
