package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.lexer.token.ReservedWords;
import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;

import java.util.function.BiConsumer;

public class InsertSymbol implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 5;

        Parameter id = params.get(0); // identifier
        Parameter line = params.get(1); // line
        Parameter type = params.get(2); // type
        Parameter category = params.get(3); // category
        Parameter dimentions = params.get(4); // dimentions

        if (ReservedWords.isReserved(id.read())) {
            String message = String.format("'%s' não pode ser usado como nome de variável", id.read());
            c.addError(new SemanticError(message, c.getCurrentToken().getLine()));
        } else if (c.isDeclaredInCurrentScope(id.read())) {
            String message = String.format("Variável '%s' já foi declarada neste escopo.", id.read());
            c.addError(new SemanticError(message, c.getCurrentToken().getLine()));
        } else {
            c.getInCurrentScope(id.read()).put("line", line.read());
            c.getInCurrentScope(id.read()).put("type", type.read());
            c.getInCurrentScope(id.read()).put("cat", category.read());
            c.getInCurrentScope(id.read()).put("dim", dimentions.read());

            if (category.read().equals("func") || category.read().equals("proc")) {
                c.getInCurrentScope(id.read()).put("params", "");
            }
            System.out.println(c.getSymbolTableList());
        }
    }
}
