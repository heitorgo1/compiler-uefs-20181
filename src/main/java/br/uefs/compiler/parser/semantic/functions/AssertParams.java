package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.List;
import java.util.function.BiConsumer;

public class AssertParams implements BiConsumer<Context, Parameter.Array> {

    private static boolean match(List<String> v1, List<String> v2) {
        return v1.equals(v2);
    }

    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter paramsList1 = params.get(0);
        Parameter paramsList2 = params.get(1);

        if (paramsList1.read() != null && paramsList2.read() != null) {
            List<List<String>> possibleParams = List.class.cast(paramsList1.read());
            for (List<String> v1 : possibleParams) {
                List<String> v2 = List.class.cast(paramsList2.read());

                if (match(v1, v2)) return;
            }
            c.addError(new SemanticError(
                    String.format("Esperava alguma das listas de parâmetros '%s' e recebeu '%s' em Função/Procedimento", possibleParams, paramsList2.read()),
                    c.getCurrentToken().getLine()));
        }
    }
}
