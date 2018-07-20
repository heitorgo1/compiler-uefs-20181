package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SymbolTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class CheckDeclaration implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 1;

        Parameter id = params.get(0); // struct Id

        SymbolTable globalScope = c.getSymbolTable(0);

        for (int i = c.getScope(); i >= 0; i--) {
            SymbolTable table = c.getSymbolTable(i);

            if (table.containsKey(id.read())) return;
        }

        c.addError(new SemanticError(String.format("Identificador '%s' não declarado.", id.read()), c.getCurrentToken().getLine()));

    }
}
