package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SymbolTable;

import java.util.function.BiConsumer;

public class CheckHasReturn implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter hasreturn = params.get(0); // struct Id
        Parameter lineParam = params.get(1); // struct Id

        long line = Long.valueOf(lineParam.read().toString());

        if (!hasreturn.read().equals("true")) {
            c.addError(new SemanticError(String.format("Falta retorno na Função."), line));
        }
    }
}
