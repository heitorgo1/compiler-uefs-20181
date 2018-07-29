package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.lexer.token.ReservedWords;
import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;

public class InsertSymbol implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 4;

        Parameter id = params.get(0); // identifier
        Parameter line = params.get(1); // line
        Parameter type = params.get(2); // type
        Parameter category = params.get(3); // category

        if (id.read() == null) {
            return;
        }

        if (ReservedWords.isReserved(id.read().toString())) {
            String message = String.format("'%s' não pode ser usado como nome de variável", id.read());
            c.addError(new SemanticError(message, c.getCurrentToken().getLine()));
        } else if (c.isDeclaredInCurrentScope(id.read().toString())) {
            Map<String, Object> symbol = c.getInCurrentScope(id.read().toString());
            if (type.read().equals(symbol.get("type")) && (symbol.get("cat").equals("func") || symbol.get("cat").equals("proc"))) {
                symbol.put("addparams", true);
            }
            else {
                String message = String.format("Identificador '%s' já foi declarado neste escopo.", id.read());
                c.addError(new SemanticError(message, c.getCurrentToken().getLine()));
            }
        } else if (c.getTypeMap().containsKey(id.read().toString())) {
            String message = String.format("Identificador '%s' já foi declarado como um tipo.", id.read());
            c.addError(new SemanticError(message, c.getCurrentToken().getLine()));
        } else {
            c.getInCurrentScope(id.read().toString()).put("line", line.read());
            c.getInCurrentScope(id.read().toString()).put("type", type.read());
            c.getInCurrentScope(id.read().toString()).put("cat", category.read());

            if (category.read().equals("func") || category.read().equals("proc")) {
                c.getInCurrentScope(id.read().toString()).put("paramtypes", new ArrayList<>());
                c.getInCurrentScope(id.read().toString()).put("addparams", true);
            }
        }
    }
}
