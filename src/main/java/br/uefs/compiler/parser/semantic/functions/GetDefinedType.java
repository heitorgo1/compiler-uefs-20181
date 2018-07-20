package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.Map;
import java.util.function.BiConsumer;

public class GetDefinedType implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter receiver = params.get(0);
        Parameter id = params.get(1);

        Map<String, String> typeMap = c.getTypeMap();

        try {
            if (!typeMap.containsKey(id.read())) {
                c.addError(new SemanticError(String.format("Não existe um tipo chamado '%s'",id.read()), c.getCurrentToken().getLine()));
                receiver.write("null");
            }
            else {
                receiver.write(typeMap.get(id.read()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
