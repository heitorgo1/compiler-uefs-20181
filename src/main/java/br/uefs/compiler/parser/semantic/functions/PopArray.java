package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.function.BiConsumer;

public class PopArray implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 1;

        Parameter type = params.get(0); // symbol

        String typeStr = type.read().toString();

        try {

            if (typeStr.endsWith("[]")) {
                typeStr = typeStr.substring(0, typeStr.length() - 2);
                type.write(typeStr);
            } else {
                c.addError(new SemanticError(String.format("Tentativa de acessar elemento de tipo '%s' como array.", typeStr), c.getCurrentToken().getLine()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
