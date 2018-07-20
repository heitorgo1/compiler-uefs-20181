package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SymbolTable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ExtendStruct implements BiConsumer<Context, Parameter.Array> {

    private boolean inEntryList(String val, List<Map.Entry<String, Map<String, Object>>> list) {
        for (Map.Entry<String, Map<String, Object>> item : list) {
            if (item.getKey().equals(val)) return true;
        }
        return false;
    }

    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 2;

        Parameter beingExtended = params.get(0); // struct Id
        Parameter extension = params.get(1); // struct Id

        SymbolTable globalTable = c.getSymbolTable(0);

        if (globalTable.containsKey(extension.read())) {
            List<Map.Entry<String, Map<String, Object>>> extensionVars = List.class.cast(globalTable.get(extension.read()).get("struct_vars"));
            List<Map.Entry<String, Map<String, Object>>> extendedVars = List.class.cast(globalTable.get(beingExtended.read()).get("struct_vars"));

            for (Map.Entry<String, Map<String, Object>> extensionVar : extensionVars) {
                if (inEntryList(extensionVar.getKey(), extendedVars)) {
                    c.addError(new SemanticError(String.format("Atributo '%s' já foi declarado na extensão da struct.", extensionVar.getKey()), c.getCurrentToken().getLine()));
                }
                else {
                    extendedVars.add(extensionVar);
                }
            }
        }
    }
}
