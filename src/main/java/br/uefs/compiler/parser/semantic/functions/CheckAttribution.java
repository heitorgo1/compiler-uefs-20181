package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.Map;
import java.util.function.BiConsumer;

public class CheckAttribution implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 1;

        Parameter category = params.get(0); // struct Id

        String categoryStr = category.read().toString();

        if (categoryStr.equals("expr")) {
            c.addError(new SemanticError(String.format("Não se pode atribuir valor a uma expressão."), c.getCurrentToken().getLine()));
        }
        if (categoryStr.equals("const")) {
            c.addError(new SemanticError(String.format("Não se pode atribuir valor a uma constante."), c.getCurrentToken().getLine()));
        }
        if (categoryStr.equals("func") || categoryStr.equals("proc")) {
            c.addError(new SemanticError(String.format("Não se pode atribuir valor a uma Função/Procedimento."), c.getCurrentToken().getLine()));
        }
    }
}
