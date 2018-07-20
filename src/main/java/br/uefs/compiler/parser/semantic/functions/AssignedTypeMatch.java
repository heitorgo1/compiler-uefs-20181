package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.function.BiConsumer;

public class AssignedTypeMatch implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter target = params.get(0);
        Parameter receiving = params.get(1);

        if (!target.read().equals(receiving.read())) {
            if (target.read().equals("null"))
                c.addError(new SemanticError(String.format("Variável de tipo desconhecido recebendo '%s'", receiving.read()), c.getCurrentToken().getLine()));
            else
                c.addError(new SemanticError(String.format("Esperava receber tipo '%s' e recebeu '%s'", target.read(), receiving.read()), c.getCurrentToken().getLine()));
        }
    }
}
