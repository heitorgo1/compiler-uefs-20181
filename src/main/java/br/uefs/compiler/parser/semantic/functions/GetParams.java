package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SemanticHelperFunctions;

import java.util.function.BiConsumer;

public class GetParams implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter receiver = params.get(0);
        Parameter data = params.get(1);
        Parameter testCategory = params.get(2);

        // default
        try {
            receiver.write("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!testCategory.read().equals("func") && !testCategory.read().equals("proc")) {
            c.addError(new SemanticError(String.format("Tentativa de chamar função inexistente."), c.getCurrentToken().getLine()));
            return;
        }

        try {
            receiver.write(data.read());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
