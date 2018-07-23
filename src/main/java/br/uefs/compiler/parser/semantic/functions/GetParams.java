package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;

public class GetParams implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter receiver = params.get(0);
        Parameter id = params.get(1);

        String idStr = id.read().toString();

        SymbolTable globalTable = c.getSymbolTable(0);


        // default
        try {
            receiver.write(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!globalTable.containsKey(idStr)) {
            c.addError(new SemanticError(String.format("Tentativa de usar '%s' como se fosse Função/Procedimento.", idStr), c.getCurrentToken().getLine()));
            return;
        }

        Map<String, Object> idData = globalTable.get(idStr);

        if (!idData.get("cat").equals("func") && !idData.get("cat").equals("proc")) {
            c.addError(new SemanticError(String.format("Tentativa de chamar função inexistente."), c.getCurrentToken().getLine()));
            return;
        }

        try {
            receiver.write(idData.get("paramtypes"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
