package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SymbolTable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class GetStructVarType implements BiConsumer<Context, Parameter.Array> {

    private boolean inEntryList(String val, List<Map.Entry<String, Map<String, Object>>> list) {
        for (Map.Entry<String, Map<String, Object>> item : list) {
            if (item.getKey().equals(val)) return true;
        }
        return false;
    }

    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 3;

        Parameter receiver = params.get(0);
        Parameter struct = params.get(1);
        Parameter var = params.get(2);

        SymbolTable globalTable = c.getSymbolTable(0);

        // Default value
        try {
            receiver.write("null");
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (globalTable.containsKey(struct.read())) {

            if (!globalTable.get(struct.read()).get("type").toString().equals("struct")) {
                c.addError(new SemanticError(String.format("'%s' não é uma struct.", struct.read()), c.getCurrentToken().getLine()));
                return;
            }

            List<Map.Entry<String, Map<String, Object>>> structVars = List.class.cast(globalTable.get(struct.read()).get("struct_vars"));

            try {
                if (!inEntryList(var.read(), structVars)) {
                    c.addError(new SemanticError(String.format("Struct '%s' não possui atributo '%s'", struct.read(), var.read()), c.getCurrentToken().getLine()));
                } else {
                    for (Map.Entry<String, Map<String, Object>> structVar : structVars)
                        if (structVar.getKey().equals(var.read())) {
                            receiver.write(structVar.getValue().get("type").toString());
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            c.addError(new SemanticError(String.format("Variável '%s' não declarada", struct.read()), c.getCurrentToken().getLine()));
        }
    }
}
