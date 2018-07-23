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

public class CheckStructDefined implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 1;

        Parameter id = params.get(0); // struct Id

        String idStr = id.read().toString();

        Map<String, Map<String, Object>> typeMap = c.getTypeMap();

        if (typeMap.containsKey(idStr) && typeMap.get(idStr).get("ref").equals(idStr) && typeMap.get(idStr).containsKey("struct_vars"))
            return;

        c.addError(new SemanticError(String.format("Struct '%s' não foi declarada.", id.read()), c.getCurrentToken().getLine()));
    }
}
