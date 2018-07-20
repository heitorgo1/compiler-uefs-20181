package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SymbolTable;

import java.util.Map;
import java.util.function.BiConsumer;

public class GetIdentifier implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter receiver = params.get(0);
        Parameter id = params.get(1);

        for (int i = c.getScope(); i >= 0; i--) {
            SymbolTable table = c.getSymbolTable(i);

            if (table.containsKey(id.read())) {
                Map<String, Object> content = table.get(id.read());
                receiver.writeToAttribute("lex", id.read());
                receiver.writeToAttribute("line", content.get("line").toString());
                receiver.writeToAttribute("cat", content.get("cat").toString());
                receiver.writeToAttribute("dim", content.get("dim").toString());
                receiver.writeToAttribute("type", content.get("type").toString());

                if (content.get("cat").toString().equals("func") || content.get("cat").toString().equals("proc")) {
                    receiver.writeToAttribute("params", content.get("params").toString());
                    receiver.writeToAttribute("paramsdims", content.get("paramsdims").toString());
                }
                if (content.get("type").toString().equals("struct")) {
                    // @TODO fix this
                    receiver.writeToAttribute("struct_vars", "");
                }
                return;
            }
        }

        receiver.writeToAttribute("lex", id.read());
        receiver.writeToAttribute("line", "-1");
        receiver.writeToAttribute("cat", "null");
        receiver.writeToAttribute("dim", "0");
        receiver.writeToAttribute("type", "null");
        receiver.writeToAttribute("params", "");
        receiver.writeToAttribute("paramsdims", "");
        receiver.writeToAttribute("struct_vars", "");
        c.addError(new SemanticError(String.format("Variável não declarada '%s'", id.read()), c.getCurrentToken().getLine()));
    }
}
