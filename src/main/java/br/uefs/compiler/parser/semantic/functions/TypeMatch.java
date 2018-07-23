package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SemanticHelperFunctions;

import java.util.function.BiConsumer;

public class TypeMatch implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter first = params.get(0); // const or symbol
        Parameter second = params.get(1); // const or symbol

        for (String input : SemanticHelperFunctions.splitParam(second.read().toString())) {
            for (String target : SemanticHelperFunctions.splitParam(first.read().toString())) {
                if (input.equals(target)) {
                    return;
                }
            }
        }
        String message = String.format("Operação esperava tipo '%s' e encontrou '%s' .",first.read(), second.read());
        c.addError(new SemanticError(message, c.getCurrentToken().getLine()));
    }
}