package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class AttachStructVars implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 1;

        Parameter structId = params.get(0); // struct Id

        SymbolTable table = c.getCurrentScopeSymbolTable();

        List<Map.Entry<String, Map<String, Object>>> varList = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> entry :  table.entrySet()) {

            if (entry.getValue().containsKey("cat")) {
                if (entry.getValue().get("cat").toString().equals("struct_var")) {
                    varList.add(entry);
                }
            }
        }

        c.getSymbolTable(c.getScope() - 1).get(structId.read()).putIfAbsent("struct_vars", varList);
    }
}
