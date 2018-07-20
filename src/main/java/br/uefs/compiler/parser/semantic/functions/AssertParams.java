package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SemanticHelperFunctions;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class AssertParams implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter paramsList1 = params.get(0);
        Parameter paramsList2 = params.get(1);

        if (paramsList1.read() != null && paramsList2.read() != null) {
            String[] v1 = SemanticHelperFunctions.splitParam(paramsList1.read());
            String[] v2 = SemanticHelperFunctions.splitParam(paramsList2.read());

            if (v1.length != v2.length) {
                c.addError(new SemanticError("Cheque a quantidade de parâmetros passados à função/procedimento", c.getCurrentToken().getLine()));
                return;
            } else {
                for (int i = 0; i < v1.length; i++) {
                    if (!v1[i].equals(v2[i])) {
                        c.addError(new SemanticError(
                                String.format("Esperava parâmetros '%s' e recebeu '%s' em função/procedimento", Arrays.asList(v1), Arrays.asList(v2)),
                                c.getCurrentToken().getLine()));
                        return;
                    }
                }
            }
        }
    }
}
