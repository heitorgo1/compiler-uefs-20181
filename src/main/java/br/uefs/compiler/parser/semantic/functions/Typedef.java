package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class Typedef implements BiConsumer<Context, Parameter.Array> {

    List<String> primitives = Arrays.asList("int", "float", "string", "bool", "struct");

    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 3;

        Parameter id = params.get(0); // identifier
        Parameter line = params.get(1); // line
        Parameter targetType = params.get(2); // type


        Map<String, String> typeMap = c.getTypeMap();

        if (typeMap.containsKey(id.read())) {
            c.addError(new SemanticError(String.format("Tipo '%s' já existe.", id.read()), c.getCurrentToken().getLine()));
            return;
        }

        if (primitives.contains(targetType.read())) {
            typeMap.putIfAbsent(id.read(), targetType.read());
        }
        else {
            if (typeMap.containsKey(targetType.read())) {
                typeMap.putIfAbsent(id.read(), typeMap.get(targetType.read()));
            }
            else {
                c.addError(new SemanticError(String.format("Tipo '%s' não existe.", targetType.read()), c.getCurrentToken().getLine()));
            }
        }
    }
}
