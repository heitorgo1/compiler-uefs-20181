package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

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

        String extendedStr = beingExtended.read().toString();
        String extensionStr = extension.read().toString();

        Map<String, Map<String, Object>> typeMap = c.getTypeMap();

        if (typeMap.containsKey(extensionStr)) {
            List<Map.Entry<String, Map<String, Object>>> extensionVars = List.class.cast(typeMap.get(extensionStr).get("struct_vars"));
            List<Map.Entry<String, Map<String, Object>>> extendedVars = List.class.cast(typeMap.get(extendedStr).get("struct_vars"));

            for (Map.Entry<String, Map<String, Object>> extensionVar : extensionVars) {
                if (inEntryList(extensionVar.getKey(), extendedVars))
                    c.addError(new SemanticError(String.format("Atributo '%s' já foi declarado no pai da struct.", extensionVar.getKey()), c.getCurrentToken().getLine()));
                else
                    extendedVars.add(extensionVar);
            }
        }
    }
}
